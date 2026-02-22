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

    public static final float DEFAULT_SHAKE = 2.0f;
    public static final float MIN_SHAKE = 0.0f;
    public static final float MAX_SHAKE = 5.0f;
    private static float maxShake = DEFAULT_SHAKE;

    private final Random random = new Random();
    private float currentShakeIntensity = 0f;
    private long lastShakeTime = 0;

    public static float getMaxShake() { return maxShake; }
    public static void setMaxShake(float value) { maxShake = value; }

    public void triggerCameraShake() {
        currentShakeIntensity = maxShake;
        lastShakeTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (currentShakeIntensity <= 0) return;

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastShakeTime;

        currentShakeIntensity = Math.max(0, maxShake - (elapsed / 500f));

        if (currentShakeIntensity <= 0) return;

        float shakeYaw = (random.nextFloat() - 0.5f) * 15f * currentShakeIntensity;
        float shakePitch = (random.nextFloat() - 0.5f) * 15f * currentShakeIntensity;
        float shakeRoll = (random.nextFloat() - 0.5f) * 8f * currentShakeIntensity;

        event.yaw += shakeYaw;
        event.pitch += shakePitch;
        event.roll += shakeRoll;
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
