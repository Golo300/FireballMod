package com.timl.fireballmod.handler;

import com.timl.fireballmod.FireballMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.timl.fireballmod.keybinding.ZoomKeybind.zoomKey;

public class ZoomHandler {

    public static final float MIN_ZOOM = 2.0F;
    public static final float MAX_ZOOM = 60.0F;
    public static final float ZOOM_STEP = 4.0F;
    public static float currentZoom = 20.0F;

    public static final int YOFFSET = 3;

    private int savedHotbarSlot = -1;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (zoomCondition()) {
            if (savedHotbarSlot == -1) {
                savedHotbarSlot = mc.thePlayer.inventory.currentItem;
            }

            mc.thePlayer.inventory.currentItem = savedHotbarSlot;
        } else {
            savedHotbarSlot = -1;
        }
    }

    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseInputEvent event) {
        if (!zoomCondition()) return;

        Minecraft mc = Minecraft.getMinecraft();
        int scroll = org.lwjgl.input.Mouse.getEventDWheel();
        if (scroll != 0) {

            if (scroll > 0) {
                currentZoom = Math.max(MIN_ZOOM, currentZoom - ZOOM_STEP);
                mc.thePlayer.playSound("random.click", 0.3F, 1.5F);
            } else {
                currentZoom = Math.min(MAX_ZOOM, currentZoom + ZOOM_STEP);
                mc.thePlayer.playSound("random.click", 0.3F, 1.2F);
            }
        }
    }

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        if (!zoomCondition()) return;
        event.setFOV(currentZoom);
    }

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        if (zoomCondition()) {
            if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
                event.setCanceled(true);
            }
            if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        if (!zoomCondition()) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        int screenWidth = sr.getScaledWidth();
        int imageSize = sr.getScaledHeight();
        int xPos = (screenWidth - imageSize) / 2;

        Gui.drawRect(0, 0, xPos, imageSize, 0xFF000000);
        Gui.drawRect(xPos + imageSize, 0, screenWidth, imageSize, 0xFF000000);

        mc.getTextureManager().bindTexture(new ResourceLocation(FireballMod.MODID, "textures/gui/scope.png"));
        Gui.drawModalRectWithCustomSizedTexture(
                xPos, YOFFSET,
                0, 0,
                imageSize, imageSize,
                imageSize, imageSize
        );
    }

    public static boolean zoomCondition() {
        return zoomKey.isKeyDown();
    }

}