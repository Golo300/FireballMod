package com.timl.fireballmod.handler;

import com.timl.fireballmod.FireballMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.Minecraft;

import static com.timl.fireballmod.keybinding.ZoomKeybind.zoomKey;

public class ZoomHandler {

    public static final float ZOOM = 20.0F;
    public static final int YOFFSET = 3;

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        if (!zoomCondition()) return;
        event.setFOV(ZOOM);
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
