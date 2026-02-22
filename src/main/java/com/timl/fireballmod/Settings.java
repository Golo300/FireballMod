package com.timl.fireballmod;

import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;

public class Settings {

    // SSOT
    private float smoothing = ZoomHandler.DEFAULT_ZOOM_SMOOTHING;
    private float zoomStep = ZoomHandler.DEFAULT_ZOOM_STEP;
    private float maxShake = CameraShakeHandler.DEFAULT_SHAKE;

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
}
