package com.timl.fireballmod.handler;

import com.timl.fireballmod.FireballMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.timl.fireballmod.keybinding.ZoomKeybind.zoomKey;

public class ZoomHandler {

    public static final float MIN_ZOOM = 1.0F;
    public static final float MAX_ZOOM = 60.0F;
    public static final float ZOOM_STEP = 10.0F;
    public static float currentZoom = 20.0F;

    public static int BLACK = 0xFF000000;
    public static int GRAY = 0x88000000;
    public static int GREEN = 0x00FF00;

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
                if (currentZoom > MIN_ZOOM) mc.thePlayer.playSound("random.click", 0.3F, 1.5F);
                currentZoom = Math.max(MIN_ZOOM, currentZoom - ZOOM_STEP);
            } else {
                if (currentZoom < MAX_ZOOM) mc.thePlayer.playSound("random.click", 0.3F, 1.2F);
                currentZoom = Math.min(MAX_ZOOM, currentZoom + ZOOM_STEP);
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
        int screenHeight = sr.getScaledHeight();
        int imageSize = sr.getScaledHeight();
        int xPos = (screenWidth - imageSize) / 2;

        Gui.drawRect(0, 0, screenWidth, YOFFSET, BLACK);

        Gui.drawRect(0, 0, xPos, imageSize, BLACK);
        Gui.drawRect(xPos + imageSize, 0, screenWidth, imageSize, BLACK);

        mc.getTextureManager().bindTexture(new ResourceLocation(FireballMod.MODID, "textures/gui/scope.png"));
        Gui.drawModalRectWithCustomSizedTexture(
                xPos, YOFFSET,
                0, 0,
                imageSize, imageSize,
                imageSize, imageSize
        );

        drawDistanceInfo(mc, screenWidth, screenHeight);
    }

    private void drawDistanceInfo(Minecraft mc, int screenWidth, int screenHeight) {
        String distanceText;
        MovingObjectPosition rayTrace = mc.thePlayer.rayTrace(200.0D, 1.0F);

        if (rayTrace != null && rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            double dx = mc.thePlayer.posX - (rayTrace.getBlockPos().getX() + 0.5);
            double dy = mc.thePlayer.posY - (rayTrace.getBlockPos().getY() + 0.5);
            double dz = mc.thePlayer.posZ - (rayTrace.getBlockPos().getZ() + 0.5);
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            distanceText = String.format("%.1f m", distance);
        } else {
            distanceText = "No Target";
        }

        int textWidth = mc.fontRendererObj.getStringWidth(distanceText);
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight - 30;

        Gui.drawRect(x - 4, y - 2, x + textWidth + 4, y + 10, GRAY);

        mc.fontRendererObj.drawString(distanceText, x, y, GREEN);
    }

    public static boolean zoomCondition() {
        return zoomKey.isKeyDown();
    }

}