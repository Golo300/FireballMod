package com.timl.fireballmod.gui;

import com.timl.fireballmod.Settings;
import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiZoomSettings extends GuiScreen {
    private static final String zoomSmoothnessLabel = "Zoom smoothness: ";
    private static final String zoomStepLabel = "Zoom step: ";
    private static final String shakeIntensityLabel = "Shake intensity: ";

    private GuiSlider smoothingSlider;
    private GuiSlider zoomStepSlider;
    private GuiSlider maxShakeSlider;
    private GuiButton showDistanceToggle;
    private GuiButton showFireballCountToggle;
    private GuiButton saveButton;
    private GuiButton resetButton;

    private final Settings settings;

    private boolean showDistance;
    private boolean showFireballCount;

    public GuiZoomSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void initGui() {
        int centerX = width / 2, centerY = height / 2;
        buttonList.clear();

        smoothingSlider = new GuiSlider(
                0, centerX - 70, centerY - 90, 140, 20,
                zoomSmoothnessLabel, "",
                ZoomHandler.MIN_ZOOM_SMOOTHING, ZoomHandler.MAX_ZOOM_SMOOTHING, settings.getSmoothing(),
                false,
                true,
                new GuiSlider.ISlider() {
                    @Override
                    public void onChangeSliderValue(GuiSlider slider) {
                        slider.displayString = getSmoothingSliderDisplayString(slider.getValue());
                    }
                }
        );
        smoothingSlider.precision = 2;
        smoothingSlider.displayString = getSmoothingSliderDisplayString(smoothingSlider.getValue());

        zoomStepSlider = new GuiSlider(
                1, centerX - 70, centerY - 60, 140, 20,
                zoomStepLabel, "",
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
                2, centerX - 70, centerY - 30, 140, 20,
                shakeIntensityLabel, "",
                CameraShakeHandler.MIN_SHAKE, CameraShakeHandler.MAX_SHAKE, settings.getMaxShake(),
                false,
                true,
                new GuiSlider.ISlider() {
                    @Override
                    public void onChangeSliderValue(GuiSlider guiSlider) {
                        guiSlider.displayString = getMaxShakeDisplayString(guiSlider.getValue());
                    }
                }
        );

        maxShakeSlider.precision = 1;
        maxShakeSlider.displayString = getMaxShakeDisplayString(maxShakeSlider.getValue());

        showDistance = settings.getShowDistance();
        showFireballCount = settings.getShowFireballCount();

        showDistanceToggle = new GuiButton(
                3,
                centerX - 70, centerY, 140, 20,
                getShowDistanceDisplayString(showDistance)
        );

        showFireballCountToggle = new GuiButton(
                4, centerX - 70, centerY + 30, 140, 20,
                getShowFireballCountDisplayString(showFireballCount)
        );

        saveButton = new GuiButton(1, centerX - 40, centerY + 60, 80, 20, "Save");
        resetButton = new GuiButton(1, centerX - 40, centerY + 90, 80, 20, "Reset");

        buttonList.add(smoothingSlider);
        buttonList.add(zoomStepSlider);
        buttonList.add(maxShakeSlider);
        buttonList.add(showDistanceToggle);
        buttonList.add(showFireballCountToggle);
        buttonList.add(saveButton);
        buttonList.add(resetButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == saveButton) {
            saveSettings();
        } else if (button == resetButton) {
            resetSettings();
        } else if (button == showDistanceToggle) {
            toggleDistance();
        } else if (button == showFireballCountToggle) {
            toggleFireballCount();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void saveSettings() {
        settings.setSmoothing((float) smoothingSlider.getValue());
        settings.setZoomStep((float) zoomStepSlider.getValue());
        settings.setMaxShake((float) maxShakeSlider.getValue());
        settings.setShowDistance(showDistance);
        settings.setShowFireballCount(showFireballCount);
        mc.displayGuiScreen(null);

        settings.save();
    }

    private void resetSettings() {
        smoothingSlider.setValue(ZoomHandler.DEFAULT_ZOOM_SMOOTHING);
        zoomStepSlider.setValue(ZoomHandler.DEFAULT_ZOOM_STEP);
        maxShakeSlider.setValue(CameraShakeHandler.DEFAULT_SHAKE);

        showDistance = true;
        showFireballCount = true;

        smoothingSlider.displayString = getSmoothingSliderDisplayString(ZoomHandler.DEFAULT_ZOOM_SMOOTHING);
        zoomStepSlider.displayString = getZoomingStepDisplayString(ZoomHandler.DEFAULT_ZOOM_STEP);
        maxShakeSlider.displayString = getMaxShakeDisplayString(CameraShakeHandler.DEFAULT_SHAKE);
        showDistanceToggle.displayString = getShowDistanceDisplayString(true);
        showFireballCountToggle.displayString = getShowFireballCountDisplayString(true);
    }

    private void toggleDistance() {
        showDistance = !showDistance;
        showDistanceToggle.displayString = getShowDistanceDisplayString(showDistance);
    }

    private void toggleFireballCount() {
        showFireballCount = !showFireballCount;
        showFireballCountToggle.displayString = getShowFireballCountDisplayString(showDistance);
    }

    private static String getSmoothingSliderDisplayString(double smoothness) {
        return zoomSmoothnessLabel + String.format("%.2f", smoothness);
    }

    private static String getZoomingStepDisplayString(double zoomStep) {
        return zoomStepLabel + String.format("%.0f", zoomStep);
    }

    private static String getMaxShakeDisplayString(double maxShake) {
        return shakeIntensityLabel + String.format("%.1f", maxShake);
    }

    private static String getShowDistanceDisplayString(boolean showDistance) {
        return "Show distance: " + (showDistance ? "ON" : "OFF");
    }

    private static String getShowFireballCountDisplayString(boolean showFireballCount) {
        return "Show fireball count: " + (showFireballCount ? "ON" : "OFF");
    }
}