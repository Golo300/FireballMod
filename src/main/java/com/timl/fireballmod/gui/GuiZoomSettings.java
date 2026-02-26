package com.timl.fireballmod.gui;

import com.timl.fireballmod.Settings;
import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import com.timl.fireballmod.scope.ScopeManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiZoomSettings extends GuiScreen {

    
    private static final int GUI_W    = 220;
    private static final int GUI_H    = 230;
    private static final int MARGIN   = 10;
    private static final int ELEM_W   = GUI_W - MARGIN * 2;
    private static final int ELEM_H   = 20;
    private static final int ROW_GAP  = 26;
    private static final int TITLE_H  = 20;
    private static final int BOTTOM_H = 56;

    
    private static final String LABEL_SMOOTHING = "Zoom smoothness: ";
    private static final String LABEL_ZOOM_STEP = "Zoom step: ";
    private static final String LABEL_SHAKE     = "Shake intensity: ";

    private GuiSlider smoothingSlider;
    private GuiSlider zoomStepSlider;
    private GuiSlider maxShakeSlider;
    private GuiButton btnShowDistance;
    private GuiButton btnShowFireballCount;
    private GuiButton btnSelectScope;
    private GuiButton btnSave;
    private GuiButton btnReset;

    private final Settings settings;
    private final ScopeManager scopeManager;

    private boolean showDistance;
    private boolean showFireballCount;

    private int guiX, guiY;

    public GuiZoomSettings(Settings settings, ScopeManager scopeManager) {
        this.settings     = settings;
        this.scopeManager = scopeManager;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        
        guiX = (width  - GUI_W) / 2;
        guiY = (height - GUI_H) / 2;
        
        int ex = guiX + MARGIN;
        int ey = guiY + TITLE_H + MARGIN;

        smoothingSlider = new GuiSlider(
                0, ex, ey, ELEM_W, ELEM_H,
                LABEL_SMOOTHING, "",
                ZoomHandler.MIN_ZOOM_SMOOTHING, ZoomHandler.MAX_ZOOM_SMOOTHING,
                settings.getSmoothing(), false, true,
                new GuiSlider.ISlider() {
                    @Override public void onChangeSliderValue(GuiSlider s) {
                        s.displayString = formatSmoothing(s.getValue());
                    }
                }
        );
        smoothingSlider.precision = 2;
        smoothingSlider.displayString = formatSmoothing(smoothingSlider.getValue());

        zoomStepSlider = new GuiSlider(
                1, ex, ey + ROW_GAP, ELEM_W, ELEM_H,
                LABEL_ZOOM_STEP, "",
                ZoomHandler.MIN_ZOOM_STEP, ZoomHandler.MAX_ZOOM_STEP,
                settings.getZoomStep(), false, true,
                new GuiSlider.ISlider() {
                    @Override public void onChangeSliderValue(GuiSlider s) {
                        s.displayString = formatZoomStep(s.getValue());
                    }
                }
        );
        zoomStepSlider.displayString = formatZoomStep(zoomStepSlider.getValue());

        maxShakeSlider = new GuiSlider(
                2, ex, ey + ROW_GAP * 2, ELEM_W, ELEM_H,
                LABEL_SHAKE, "",
                CameraShakeHandler.MIN_SHAKE, CameraShakeHandler.MAX_SHAKE,
                settings.getMaxShake(), false, true,
                new GuiSlider.ISlider() {
                    @Override public void onChangeSliderValue(GuiSlider s) {
                        s.displayString = formatShake(s.getValue());
                    }
                }
        );
        maxShakeSlider.precision = 1;
        maxShakeSlider.displayString = formatShake(maxShakeSlider.getValue());

        showDistance      = settings.getShowDistance();
        showFireballCount = settings.getShowFireballCount();

        btnShowDistance = new GuiButton(
                3, ex, ey + ROW_GAP * 3, ELEM_W, ELEM_H,
                formatShowDistance(showDistance));

        btnShowFireballCount = new GuiButton(
                4, ex, ey + ROW_GAP * 4, ELEM_W, ELEM_H,
                formatShowFireballCount(showFireballCount));

        btnSelectScope = new GuiButton(
                5, ex, ey + ROW_GAP * 5, ELEM_W, ELEM_H,
                "Select Scope: " + settings.getSelectedScope());

        
        int halfW = (ELEM_W - MARGIN) / 2;
        int btnY  = guiY + GUI_H - BOTTOM_H + MARGIN;
        btnSave  = new GuiButton(6, ex, btnY, halfW, ELEM_H, "Save");
        btnReset = new GuiButton(7, ex + halfW + MARGIN, btnY, halfW, ELEM_H, "Reset");

        buttonList.add(smoothingSlider);
        buttonList.add(zoomStepSlider);
        buttonList.add(maxShakeSlider);
        buttonList.add(btnShowDistance);
        buttonList.add(btnShowFireballCount);
        buttonList.add(btnSelectScope);
        buttonList.add(btnSave);
        buttonList.add(btnReset);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        
        drawCenteredString(fontRendererObj, "Fireball Tank Mod Settings",
                guiX + GUI_W / 2, guiY + 6, 0xFFFFFF);

        String scope = settings.getSelectedScope();
        if (scope.length() > 15) scope = scope.substring(0, 12) + "..";
        btnSelectScope.displayString = "Select Scope: " + scope;

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == btnSave) {
            saveSettings();
        } else if (button == btnReset) {
            resetSettings();
        } else if (button == btnShowDistance) {
            showDistance = !showDistance;
            btnShowDistance.displayString = formatShowDistance(showDistance);
        } else if (button == btnShowFireballCount) {
            showFireballCount = !showFireballCount;
            btnShowFireballCount.displayString = formatShowFireballCount(showFireballCount);
        } else if (button == btnSelectScope) {
            mc.displayGuiScreen(new GuiScopeSelect(this, settings, scopeManager));
        }
    }

    private void saveSettings() {
        settings.setSmoothing((float) smoothingSlider.getValue());
        settings.setZoomStep((float) zoomStepSlider.getValue());
        settings.setMaxShake((float) maxShakeSlider.getValue());
        settings.setShowDistance(showDistance);
        settings.setShowFireballCount(showFireballCount);
        settings.save();
        mc.displayGuiScreen(null);
    }

    private void resetSettings() {
        smoothingSlider.setValue(ZoomHandler.DEFAULT_ZOOM_SMOOTHING);
        zoomStepSlider.setValue(ZoomHandler.DEFAULT_ZOOM_STEP);
        maxShakeSlider.setValue(CameraShakeHandler.DEFAULT_SHAKE);
        showDistance      = true;
        showFireballCount = true;

        smoothingSlider.displayString        = formatSmoothing(ZoomHandler.DEFAULT_ZOOM_SMOOTHING);
        zoomStepSlider.displayString         = formatZoomStep(ZoomHandler.DEFAULT_ZOOM_STEP);
        maxShakeSlider.displayString         = formatShake(CameraShakeHandler.DEFAULT_SHAKE);
        btnShowDistance.displayString        = formatShowDistance(true);
        btnShowFireballCount.displayString   = formatShowFireballCount(true);
        settings.setSelectedScope(ScopeManager.DEFAULT_SCOPE);
        btnSelectScope.displayString         = "Select Scope: " + ScopeManager.DEFAULT_SCOPE;
    }

    private static String formatSmoothing(double v)          { return LABEL_SMOOTHING + String.format("%.2f", v); }
    private static String formatZoomStep(double v)           { return LABEL_ZOOM_STEP + String.format("%.0f", v); }
    private static String formatShake(double v)              { return LABEL_SHAKE     + String.format("%.1f", v); }
    private static String formatShowDistance(boolean b)      { return "Show distance: "      + (b ? "ON" : "OFF"); }
    private static String formatShowFireballCount(boolean b) { return "Show fireball count: " + (b ? "ON" : "OFF"); }
}
