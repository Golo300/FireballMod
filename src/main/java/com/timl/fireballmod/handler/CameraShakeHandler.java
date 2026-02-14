package com.timl.fireballmod.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

import static com.timl.fireballmod.handler.ZoomHandler.zoomCondition;

public class CameraShakeHandler {

    private final Random random = new Random();
    private float shakeIntensity = 0f;
    private long lastShakeTime = 0;

    public void triggerCameraShake() {
        shakeIntensity = 1.0f;
        lastShakeTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (shakeIntensity > 0) {
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - lastShakeTime;

            shakeIntensity = Math.max(0, 1.0f - (elapsed / 500f));

            if (shakeIntensity > 0) {
                float shakeYaw = (random.nextFloat() - 0.5f) * 15f * shakeIntensity;
                float shakePitch = (random.nextFloat() - 0.5f) * 15f * shakeIntensity;
                float shakeRoll = (random.nextFloat() - 0.5f) * 8f * shakeIntensity;

                event.yaw += shakeYaw;
                event.pitch += shakePitch;
                event.roll += shakeRoll;
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.world.isRemote) return;
        EntityPlayer player = event.entityPlayer;
        ItemStack heldItem = player.getCurrentEquippedItem();

        if (heldItem != null && heldItem.getItem() == Items.fire_charge && zoomCondition()) {
            triggerCameraShake();

            player.playSound("random.explode", 3.0F, 0.4F);
            player.playSound("mob.zombie.metal", 2.0F, 0.3F);
        }
    }
}
