package com.timl.fireballmod.handler;

import static com.timl.fireballmod.FireballMod.LOGGER;
import com.timl.fireballmod.FireballMod;
import com.timl.fireballmod.Settings;
import com.timl.fireballmod.scope.ScopeManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.timl.fireballmod.handler.RenderHandler.BLACK;
import static com.timl.fireballmod.keybinding.ZoomKeybind.zoomKey;

public class ZoomHandler {
    private static final int Y_OFFSET = 3;
    public static final float MIN_ZOOM = 1.0F;
    public static final float MAX_ZOOM = 60.0F;
    public static final float MIN_ZOOM_STEP = 1.0F;
    public static final float MAX_ZOOM_STEP = 30.0F;
    public static final float DEFAULT_ZOOM_STEP = 5.0F;
    public static final float MIN_ZOOM_SMOOTHING = 1.0F;
    public static final float MAX_ZOOM_SMOOTHING = 0.05F;
    public static final float DEFAULT_ZOOM_SMOOTHING = 0.25F;

    private final Settings settings;
    private final ScopeManager scopeManager;   

    private float targetZoom = 20.0F;
    private float currentZoom = 20.0F;

    private int savedHotbarSlot = -1;
    private float originalSensitivity = -1.0F;

    public RenderHandler renderHandler;

    public ZoomHandler(Settings settings, ScopeManager scopeManager) {
        this.settings = settings;
        this.scopeManager = scopeManager;
        renderHandler = new RenderHandler(settings);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (Math.abs(currentZoom - targetZoom) > 0.01F) {
            currentZoom += (targetZoom - currentZoom) * settings.getSmoothing();
        } else {
            currentZoom = targetZoom;
        }

        if (zoomCondition()) {
            if (savedHotbarSlot == -1) {
                savedHotbarSlot = mc.thePlayer.inventory.currentItem;
            }
            if (originalSensitivity == -1.0F) {
                originalSensitivity = mc.gameSettings.mouseSensitivity;
            }
            float zoomFactor = currentZoom / MAX_ZOOM;
            float reducedSensitivity = originalSensitivity * zoomFactor;
            mc.gameSettings.mouseSensitivity = Math.max(0.0001F, reducedSensitivity);
            mc.thePlayer.inventory.currentItem = savedHotbarSlot;
        } else {
            savedHotbarSlot = -1;
            if (originalSensitivity != -1.0F) {
                mc.gameSettings.mouseSensitivity = originalSensitivity;
                originalSensitivity = -1.0F;
            }
        }
    }

    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseInputEvent event) {
        if (!zoomCondition()) return;
        Minecraft mc = Minecraft.getMinecraft();
        int scroll = org.lwjgl.input.Mouse.getEventDWheel();
        if (scroll != 0) {
            if (scroll > 0) {
                if (targetZoom > MIN_ZOOM) mc.thePlayer.playSound("random.click", 0.3F, 1.5F);
                targetZoom = Math.max(MIN_ZOOM, targetZoom - settings.getZoomStep());
            } else {
                if (targetZoom < MAX_ZOOM) mc.thePlayer.playSound("random.click", 0.3F, 1.2F);
                targetZoom = Math.min(MAX_ZOOM, targetZoom + settings.getZoomStep());
            }
            LOGGER.info("Zoom changed: {}", targetZoom);
        }
    }

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        if (!zoomCondition()) return;
        float mappedZoom = mapZoom(currentZoom);
        event.setFOV(mappedZoom);
    }

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        if (zoomCondition()) {
            if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) event.setCanceled(true);
            if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR)     event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!zoomCondition()) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        int screenWidth  = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();
        int imageSize    = sr.getScaledHeight();
        int xPos = (screenWidth - imageSize) / 2;

        Gui.drawRect(0, 0, screenWidth, Y_OFFSET, BLACK);
        Gui.drawRect(0, 0, xPos, imageSize, BLACK);
        Gui.drawRect(xPos + imageSize, 0, screenWidth, imageSize, BLACK);

        ResourceLocation scopeTexture = getCurrentScopeTexture();
        mc.getTextureManager().bindTexture(scopeTexture);
        Gui.drawModalRectWithCustomSizedTexture(
                xPos, Y_OFFSET,
                0, 0,
                imageSize, imageSize,
                imageSize, imageSize
        );

        renderHandler.drawDistanceInfo(mc, screenWidth, screenHeight);
        renderHandler.drawShotCounter(mc, screenWidth);
    }

    private ResourceLocation getCurrentScopeTexture() {
        String selected = settings.getSelectedScope();
        ScopeManager.ScopeEntry entry = scopeManager.findByName(selected);
        if (entry != null) {
            return entry.location;
        }
        // Fallback
        return new ResourceLocation(FireballMod.MODID, "textures/gui/scope.png");
    }

    public static boolean zoomCondition() {
        return zoomKey.isKeyDown();
    }

    private float mapZoom(float linearZoom) {
        float min = MIN_ZOOM;
        float max = MAX_ZOOM;
        float t = (linearZoom - min) / (max - min);
        return min * (float) Math.pow(max / min, t);
    }
}