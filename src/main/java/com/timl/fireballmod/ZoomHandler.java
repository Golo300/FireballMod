package com.timl.fireballmod;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import net.minecraft.init.Items;

public class ZoomHandler {

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack held = mc.thePlayer.getCurrentEquippedItem();

        if (held != null && held.getItem() == Items.fire_charge) {
            if (ZoomKeybind.zoomKey.isKeyDown()) {
                event.setFOV(20.0F); // Zoom aktivieren
            }
        }
    }

}
