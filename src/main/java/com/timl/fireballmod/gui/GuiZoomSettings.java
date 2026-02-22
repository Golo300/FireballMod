package com.timl.fireballmod.gui;

import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiZoomSettings extends GuiScreen {

    private float smoothing = ZoomHandler.getZoomSmoothing();
    private float zoomStep = ZoomHandler.getZoomStep();
    private float maxShake = CameraShakeHandler.getMaxShake();
    private GuiSlider smoothingSlider;
    private GuiSlider zoomStepSlider;
    private GuiSlider maxShakeSlider;
    private GuiButton saveButton;
    private GuiButton resetButton;
    private final Configuration config = com.timl.fireballmod.FireballMod.instance.getConfig();

    @Override
    public void initGui() {
        int centerX = width / 2, centerY = height / 2;
        buttonList.clear();

        smoothingSlider = new GuiSlider(
                0, centerX - 70, centerY - 60, 140, 20,
                "Zoom smoothness: ", "",
                ZoomHandler.MIN_ZOOM_SMOOTHING, ZoomHandler.MAX_ZOOM_SMOOTHING, smoothing,
                false,
                true,
                new GuiSlider.ISlider() {
                    @Override
                    public void onChangeSliderValue(GuiSlider slider) {
                        slider.displayString = "Zoom smoothness: " + String.format("%.2f", slider.getValue());
                    }
                }
        );
        smoothingSlider.precision = 2;
        smoothingSlider.displayString = "Zoom smoothness: " + String.format("%.2f", smoothingSlider.getValue());

        zoomStepSlider = new GuiSlider(
                1, centerX - 70, centerY - 30, 140, 20,
                "Zoom step: ", "",
                ZoomHandler.MIN_ZOOM_STEP, ZoomHandler.MAX_ZOOM_STEP, zoomStep,
                false,
                true,
                new GuiSlider.ISlider() {
                    @Override
                    public void onChangeSliderValue(GuiSlider guiSlider) {
                    }
                }
        );

        maxShakeSlider = new GuiSlider(
                2, centerX - 70, centerY, 140, 20,
                "Shake intensity: ", "",
                CameraShakeHandler.MIN_SHAKE, CameraShakeHandler.MAX_SHAKE, maxShake,
                false,
                true,
                new GuiSlider.ISlider() {
                    @Override
                    public void onChangeSliderValue(GuiSlider guiSlider) {
                        guiSlider.displayString = "Shake intensity: " + String.format("%.1f", guiSlider.getValue());
                    }
                }
        );
        maxShakeSlider.precision = 1;
        maxShakeSlider.displayString = "Shake intensity: " + String.format("%.1f", maxShakeSlider.getValue());

        saveButton = new GuiButton(1, centerX - 40, centerY + 30, 80, 20, "Save");
        resetButton = new GuiButton(1, centerX - 40, centerY + 60, 80, 20, "Reset");

        buttonList.add(smoothingSlider);
        buttonList.add(zoomStepSlider);
        buttonList.add(maxShakeSlider);
        buttonList.add(saveButton);
        buttonList.add(resetButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == saveButton) {
            smoothing = (float) smoothingSlider.getValue();
            zoomStep = (float) zoomStepSlider.getValue();
            maxShake = (float) maxShakeSlider.getValue();
            ZoomHandler.setZoomSmoothing(smoothing);
            ZoomHandler.setZoomStep(zoomStep);
            CameraShakeHandler.setMaxShake(maxShake);
            mc.displayGuiScreen(null);

            final Configuration config = com.timl.fireballmod.FireballMod.instance.getConfig();
            config.get("zoom", "zoomSmoothing", ZoomHandler.DEFAULT_ZOOM_SMOOTHING).set(smoothing);
            config.get("zoom", "zoomStep", ZoomHandler.DEFAULT_ZOOM_STEP).set(zoomStep);
            config.get("camera", "maxShake", CameraShakeHandler.DEFAULT_SHAKE).set(maxShake);
            config.save();
        } else if (button == resetButton) {
            smoothing = ZoomHandler.DEFAULT_ZOOM_SMOOTHING;
            zoomStep = ZoomHandler.DEFAULT_ZOOM_STEP;
            maxShake = CameraShakeHandler.DEFAULT_SHAKE;

            smoothingSlider.setValue(smoothing);
            zoomStepSlider.setValue(zoomStep);
            maxShakeSlider.setValue(maxShake);

            smoothingSlider.displayString = "Smoothness: " + String.format("%.2f", smoothing);
            zoomStepSlider.displayString = "Zoom step: " + String.format("%.0f", zoomStep);
            maxShakeSlider.displayString = "Shake intensity: " + String.format("%.1f", maxShake);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}