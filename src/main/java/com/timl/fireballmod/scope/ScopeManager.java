package com.timl.fireballmod.scope;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ScopeManager {

    public static final File SCOPE_DIR = new File(Minecraft.getMinecraft().mcDataDir, "fireballmod/scopes");
    public static final String DEFAULT_SCOPE = "default";

    private final List<ScopeEntry> scopes = new ArrayList();

    public static class ScopeEntry {
        public final String name;
        public final ResourceLocation location;
        public final boolean isDefault;

        public ScopeEntry(String name, ResourceLocation location, boolean isDefault) {
            this.name = name;
            this.location = location;
            this.isDefault = isDefault;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public void reload() {
        
        for (ScopeEntry entry : scopes) {
            if (!entry.isDefault) {
                Minecraft.getMinecraft().getTextureManager().deleteTexture(entry.location);
            }
        }
        scopes.clear();

        
        scopes.add(new ScopeEntry(
                DEFAULT_SCOPE,
                new ResourceLocation("fireballmod", "textures/gui/scope.png"),
                true
        ));

        
        SCOPE_DIR.mkdirs();

        System.out.println("[FireballMod] Scope-Ordner: " + SCOPE_DIR.getAbsolutePath());
        System.out.println("[FireballMod] Ordner existiert: " + SCOPE_DIR.exists());
        System.out.println("[FireballMod] Ordner lesbar: " + SCOPE_DIR.canRead());

        File[] files = SCOPE_DIR.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".png");
            }
        });
        if (files == null) return;

        for (File file : files) {
            String name = file.getName().replace(".png", "");
            ResourceLocation loc = new ResourceLocation("fireballmod_dynamic", "scope_" + name);

            try {
                final BufferedImage img = ImageIO.read(file);
                if (img == null) continue;

                Minecraft.getMinecraft().getTextureManager().loadTexture(loc, new AbstractTexture() {
                    @Override
                    public void loadTexture(IResourceManager manager) throws IOException {
                        
                        this.glTextureId = GL11.glGenTextures();
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glTextureId);

                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

                        int width = img.getWidth();
                        int height = img.getHeight();
                        int[] pixels = new int[width * height];
                        img.getRGB(0, 0, width, height, pixels, 0, width);

                        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
                        for (int pixel : pixels) {
                            buffer.put((byte) ((pixel >> 16) & 0xFF)); 
                            buffer.put((byte) ((pixel >> 8) & 0xFF));  
                            buffer.put((byte) (pixel & 0xFF));         
                            buffer.put((byte) ((pixel >> 24) & 0xFF)); 
                        }
                        buffer.flip();

                        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                                width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                    }
                });

                scopes.add(new ScopeEntry(name, loc, false));

            } catch (Exception e) {
                System.err.println("[FireballMod] Konnte Scope nicht laden: " + file.getName() + " - " + e.getMessage());
            }
        }

        System.out.println("[FireballMod] " + scopes.size() + " Scope(s) geladen.");
    }

    public List<ScopeEntry> getScopes() {
        return scopes;
    }

    public ScopeEntry findByName(String name) {
        for (ScopeEntry entry : scopes) {
            if (entry.name.equals(name)) return entry;
        }
        return scopes.isEmpty() ? null : scopes.get(0);
    }
}