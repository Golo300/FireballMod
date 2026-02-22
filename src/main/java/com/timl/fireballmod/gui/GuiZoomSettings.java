package com.timl.fireballmod.gui;

import com.timl.fireballmod.Settings;
import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiZoomSettings extends GuiScreen {

    private GuiSlider smoothingSlider;
    private GuiSlider zoomStepSlider;
    private GuiSlider maxShakeSlider;
    private GuiButton saveButton;
    private GuiButton resetButton;

    private final Settings settings;

    public GuiZoomSettings(Settings settings) {
        this.settings = settings;
    }
    @Override
    public void initGui() {
        int centerX = width / 2, centerY = height / 2;
        buttonList.clear();

        smoothingSlider = new GuiSlider(
                0, centerX - 70, centerY - 60, 140, 20,
                "Zoom smoothness: ", "",
                ZoomHandler.MIN_ZOOM_SMOOTHING, ZoomHandler.MAX_ZOOM_SMOOTHING, settings.getSmoothing(),
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
                ZoomHandler.MIN_ZOOM_STEP, ZoomHandler.MAX_ZOOM_STEP, settings.getZoomStep(),
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
                CameraShakeHandler.MIN_SHAKE, CameraShakeHandler.MAX_SHAKE, settings.getMaxShake(),
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
            settings.setSmoothing((float) smoothingSlider.getValue());
            settings.setZoomStep((float) zoomStepSlider.getValue());
            settings.setMaxShake((float) maxShakeSlider.getValue());
            mc.displayGuiScreen(null);

            settings.save();

        } else if (button == resetButton) {

            settings.setSmoothing(ZoomHandler.DEFAULT_ZOOM_SMOOTHING);
            settings.setZoomStep(ZoomHandler.DEFAULT_ZOOM_STEP);
            settings.setMaxShake(CameraShakeHandler.DEFAULT_SHAKE);

            smoothingSlider.setValue(settings.getSmoothing());
            zoomStepSlider.setValue(settings.getZoomStep());
            maxShakeSlider.setValue(settings.getMaxShake());

            smoothingSlider.displayString = "Smoothness: " + String.format("%.2f", settings.getSmoothing());
            zoomStepSlider.displayString = "Zoom step: " + String.format("%.0f", settings.getZoomStep());
            maxShakeSlider.displayString = "Shake intensity: " + String.format("%.1f", settings.getMaxShake());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}