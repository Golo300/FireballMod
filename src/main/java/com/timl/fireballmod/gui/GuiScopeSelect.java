package com.timl.fireballmod.gui;

import com.timl.fireballmod.Settings;
import com.timl.fireballmod.scope.ScopeManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class GuiScopeSelect extends GuiScreen {

    private static final int MARGIN = 8;
    private static final int TITLE_H = 22;
    private static final int BOTTOM_H = 28;

    private static final int THUMB_SIZE = 44;
    private static final int THUMB_GAP = 5;
    private static final int THUMB_LABEL_H = 10;
    private static final int THUMB_CELL_H = THUMB_SIZE + THUMB_GAP + THUMB_LABEL_H;
    private static final int THUMBS_PER_ROW = 4;

    private static final int SCROLL_W = 12;
    private static final int BTN_W = 80;
    private static final int BTN_H = 20;
    private static final int PREVIEW_SZ = 80;

    private final GuiScreen parent;
    private final Settings settings;
    private final ScopeManager scopeManager;

    private List<ScopeManager.ScopeEntry> scopes;
    private int selectedIndex = 0;
    private int scrollOffset = 0;

    private int guiX, guiY, guiW, guiH;
    private int listX, listY, listW, listH, visibleRows;
    private int previewX, previewY;

    private GuiButton btnSelect, btnReload, btnBack, btnScrollUp, btnScrollDown;

    public GuiScopeSelect(GuiScreen parent, Settings settings, ScopeManager scopeManager) {
        this.parent = parent;
        this.settings = settings;
        this.scopeManager = scopeManager;
    }

    @Override
    public void initGui() {
        scopeManager.reload();
        scopes = scopeManager.getScopes();

        String saved = settings.getSelectedScope();
        for (int i = 0; i < scopes.size(); i++) {
            if (scopes.get(i).name.equals(saved)) {
                selectedIndex = i;
                break;
            }
        }

        
        guiW = Math.min(360, (int) (width * 0.9));
        guiH = Math.min(260, (int) (height * 0.9)); 
        guiX = (width - guiW) / 2;
        guiY = (height - guiH) / 2;

        int innerY = guiY + TITLE_H;
        int innerH = guiH - TITLE_H - BOTTOM_H;

        int pSz = Math.min(PREVIEW_SZ, innerH);
        previewX = guiX + guiW - MARGIN - pSz;
        previewY = innerY + MARGIN;

        listX = guiX + MARGIN;
        listY = innerY + MARGIN;

        int colW = THUMB_SIZE + THUMB_GAP;
        listW = THUMBS_PER_ROW * colW - THUMB_GAP;
        listH = innerH - MARGIN * 2;

        
        visibleRows = 3;
        clampScroll();

        int scrollX = listX + listW + MARGIN;
        int scrollMid = listY + listH / 2;

        btnScrollUp = new GuiButton(3, scrollX, scrollMid - BTN_H - 2, SCROLL_W, BTN_H, "^");
        btnScrollDown = new GuiButton(4, scrollX, scrollMid + 2, SCROLL_W, BTN_H, "v");

        int totalBtnW = BTN_W * 3 + MARGIN * 2;
        int btnStartX = guiX + (guiW - totalBtnW) / 2;
        int btnY = guiY + guiH - BOTTOM_H + (BOTTOM_H - BTN_H) / 2;

        btnSelect = new GuiButton(0, btnStartX, btnY, BTN_W, BTN_H, "Select");
        btnReload = new GuiButton(1, btnStartX + BTN_W + MARGIN, btnY, BTN_W, BTN_H, "Reload");
        btnBack   = new GuiButton(2, btnStartX + (BTN_W + MARGIN) * 2, btnY, BTN_W, BTN_H, "Back");

        buttonList.clear();
        buttonList.add(btnSelect);
        buttonList.add(btnReload);
        buttonList.add(btnBack);
        buttonList.add(btnScrollUp);
        buttonList.add(btnScrollDown);
    }

    private void clampScroll() {
        if (scopes == null || scopes.isEmpty()) {
            scrollOffset = 0;
            return;
        }
        int totalRows = (scopes.size() + THUMBS_PER_ROW - 1) / THUMBS_PER_ROW;
        scrollOffset = Math.max(0, Math.min(scrollOffset, Math.max(0, totalRows - visibleRows)));
    }

    private void enableScissor(int x, int y, int w, int h) {
        int s = new ScaledResolution(mc).getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x * s, mc.displayHeight - (y + h) * s, w * s, h * s);
    }

    private void drawScaledTexture(int x, int y, int w, int h) {
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x,     y + h, 0).tex(0, 1).endVertex();
        wr.pos(x + w, y + h, 0).tex(1, 1).endVertex();
        wr.pos(x + w, y,     0).tex(1, 0).endVertex();
        wr.pos(x,     y,     0).tex(0, 0).endVertex();
        t.draw();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        drawCenteredString(fontRendererObj, "Select Scope",
                guiX + guiW / 2, guiY + 5, Color.COLOR_TITLE);
        
        String pathText = ScopeManager.SCOPE_DIR.getAbsolutePath();
        drawCenteredString(fontRendererObj, pathText,
                guiX + guiW / 2, guiY + 13, Color.COLOR_PATH);

        if (scopes == null || scopes.isEmpty()) {
            drawCenteredString(fontRendererObj, "No scopes found.",
                    guiX + guiW / 2, guiY + guiH / 2, Color.COLOR_NO_SCOPES);
            super.drawScreen(mouseX, mouseY, partialTicks);
            return;
        }

        drawRect(listX - 2, listY - 2, listX + listW + 2, listY + listH + 2, Color.COLOR_LIST_BORDER);
        drawRect(listX, listY, listX + listW, listY + listH, Color.COLOR_LIST_BG);

        enableScissor(listX - 2, listY - 2, listW + 4, listH + 4);

        for (int i = 0; i < scopes.size(); i++) {
            int row = i / THUMBS_PER_ROW;
            int col = i % THUMBS_PER_ROW;
            int visRow = row - scrollOffset;
            if (visRow < 0 || visRow >= visibleRows) continue;

            int tx = listX + col * (THUMB_SIZE + THUMB_GAP);
            int ty = listY + visRow * THUMB_CELL_H;

            drawRect(tx - 2, ty - 2, tx + THUMB_SIZE + 2, ty + THUMB_SIZE + 2,
                    i == selectedIndex ? Color.COLOR_THUMB_SELECTED : Color.COLOR_THUMB_UNSELECTED);
            drawRect(tx, ty, tx + THUMB_SIZE, ty + THUMB_SIZE, Color.COLOR_THUMB_BG);

            mc.getTextureManager().bindTexture(scopes.get(i).location);
            drawScaledTexture(tx, ty, THUMB_SIZE, THUMB_SIZE);

            String label = scopes.get(i).name;
            if (label.length() > 8) label = label.substring(0, 7) + "..";
            drawCenteredString(fontRendererObj, label,
                    tx + THUMB_SIZE / 2, ty + THUMB_SIZE + 2, Color.COLOR_LABEL);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        int pSz = Math.min(PREVIEW_SZ, listH);
        drawRect(previewX - 1, previewY - 1, previewX + pSz + 1, previewY + pSz + 1, Color.COLOR_PREVIEW_BORDER);
        drawRect(previewX, previewY, previewX + pSz, previewY + pSz, Color.COLOR_PREVIEW_BG);

        ScopeManager.ScopeEntry sel = scopes.get(selectedIndex);
        mc.getTextureManager().bindTexture(sel.location);
        drawScaledTexture(previewX, previewY, pSz, pSz);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (scopes == null) return;

        for (int i = 0; i < scopes.size(); i++) {
            int row = i / THUMBS_PER_ROW;
            int col = i % THUMBS_PER_ROW;
            int visRow = row - scrollOffset;
            if (visRow < 0 || visRow >= visibleRows) continue;

            int tx = listX + col * (THUMB_SIZE + THUMB_GAP);
            int ty = listY + visRow * THUMB_CELL_H;

            if (mouseX >= tx && mouseX <= tx + THUMB_SIZE &&
                    mouseY >= ty && mouseY <= ty + THUMB_SIZE &&
                    mouseX >= listX && mouseX <= listX + listW &&
                    mouseY >= listY && mouseY <= listY + listH) {
                selectedIndex = i;
                break;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel < 0) scrollOffset++;
        else if (dWheel > 0) scrollOffset--;
        clampScroll();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == btnSelect) {
            settings.setSelectedScope(scopes.get(selectedIndex).name);
            settings.save();
            mc.displayGuiScreen(parent);
        } else if (button == btnReload) {
            scopeManager.reload();
            scopes = scopeManager.getScopes();
            selectedIndex = 0;
            scrollOffset = 0;
            clampScroll();
        } else if (button == btnBack) {
            mc.displayGuiScreen(parent);
        } else if (button == btnScrollUp) {
            scrollOffset--;
            clampScroll();
        } else if (button == btnScrollDown) {
            scrollOffset++;
            clampScroll();
        }
    }

    @Override
    protected void keyTyped(char c, int key) {
        if (key == 1) mc.displayGuiScreen(parent);
    }
}
