package com.timl.fireballmod.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

public class RenderHandler {

    public static int BLACK = 0xFF000000;
    public static int GRAY = 0x88000000;
    public static int GREEN = 0x00FF00;

    // Shot Counter Settings
    public static final int COUNTER_Y_POSITION = 10;
    public static final int COUNTER_PADDING = 0;
    public static final int COUNTER_HEIGHT = 16;
    public static final int COUNTER_ICON_SIZE = 16;
    public static final int COUNTER_TEXT_OFFSET = 20;
    public static final int COUNTER_BG_COLOR = 0xAA000000;
    public static final int COUNTER_TEXT_COLOR = 0xFFFFFF;

    public void drawDistanceInfo(Minecraft mc, int screenWidth, int screenHeight) {
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


    public void drawShotCounter(Minecraft mc, int screenWidth) {

        int padding = COUNTER_PADDING;
        int textOffset = COUNTER_TEXT_OFFSET;

        String shotText = String.valueOf(countFireballs(mc.thePlayer));
        int textWidth = mc.fontRendererObj.getStringWidth(shotText);
        int totalWidth = COUNTER_ICON_SIZE + (textOffset / 2) + textWidth;

        int x = (screenWidth - totalWidth) / 2;
        int y = COUNTER_Y_POSITION;

        Gui.drawRect(
                x - padding,
                y - padding,
                x + totalWidth + padding,
                y + COUNTER_HEIGHT,
                COUNTER_BG_COLOR
        );

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        ItemStack fireCharge = new ItemStack(Items.fire_charge);
        mc.getRenderItem().renderItemAndEffectIntoGUI(fireCharge, x, y);

        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        mc.fontRendererObj.drawString(shotText, x + textOffset, y + 4, COUNTER_TEXT_COLOR);
    }

    private int countFireballs(EntityPlayer player) {
        ItemStack heldItem = player.getHeldItem();

        if (heldItem != null && heldItem.getItem() == Items.fire_charge) {
            return heldItem.stackSize;
        }

        return 0;
    }

}
