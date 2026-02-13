package com.timl.fireballmod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ZoomKeybind {
    public static KeyBinding zoomKey;

    public static void register() {
        zoomKey = new KeyBinding("key.fireballmod.zoom", Keyboard.KEY_C, "Fireball Mod");
        ClientRegistry.registerKeyBinding(zoomKey);
    }
}
