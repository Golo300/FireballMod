package com.timl.fireballmod;

import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import jdk.jfr.internal.consumer.RecordingInput;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.timl.fireballmod.FireballMod.LOGGER;

public class Settings {

    // SSOT
    private float smoothing = ZoomHandler.DEFAULT_ZOOM_SMOOTHING;
    private float zoomStep = ZoomHandler.DEFAULT_ZOOM_STEP;
    private float maxShake = CameraShakeHandler.DEFAULT_SHAKE;

    private final Configuration config;
    private final Property propSmoothing;
    private final Property propZoomStep;
    private final Property propMaxShake;

    public Settings(Configuration config) {
        this.config = config;
        config.load();

        propSmoothing = config.get("zoom", "zoomSmoothing", ZoomHandler.DEFAULT_ZOOM_SMOOTHING, "zoom smoothness");
        propSmoothing.setMinValue(ZoomHandler.MIN_ZOOM_SMOOTHING);
        propSmoothing.setMaxValue(ZoomHandler.MAX_ZOOM_SMOOTHING);

        propZoomStep = config.get("zoom", "zoomStep", ZoomHandler.DEFAULT_ZOOM_STEP, "zoom step size");
        propZoomStep.setMinValue(ZoomHandler.MIN_ZOOM_STEP);
        propZoomStep.setMaxValue(ZoomHandler.MAX_ZOOM_STEP);

        propMaxShake = config.get("camera", "maxShake", CameraShakeHandler.DEFAULT_SHAKE, "camera shake intensity");
        propMaxShake.setMinValue(CameraShakeHandler.MIN_SHAKE);
        propMaxShake.setMaxValue(CameraShakeHandler.MAX_SHAKE);

        smoothing = (float) propSmoothing.getDouble();
        zoomStep   = (float) propZoomStep.getDouble();
        maxShake   = (float) propMaxShake.getDouble();

        LOGGER.info("Config loaded: smoothing={}, zoomStep={}, maxShake={}", smoothing, zoomStep, maxShake);
    }


    public float getSmoothing() {
        return smoothing;
    }

    public void setSmoothing(float smoothing) {
        this.smoothing = smoothing;
    }

    public float getZoomStep() {
        return zoomStep;
    }

    public void setZoomStep(float zoomStep) {
        this.zoomStep = zoomStep;
    }

    public float getMaxShake() {
        return maxShake;
    }

    public void setMaxShake(float maxShake) {
        this.maxShake = maxShake;
    }

    public void save() {
        propSmoothing.setValue(smoothing);
        propZoomStep.setValue(zoomStep);
        propMaxShake.setValue(maxShake);
        config.save();
        LOGGER.info("Config saved: smoothing={}, zoomStep={}, maxShake={}", smoothing, zoomStep, maxShake);
    }
}
