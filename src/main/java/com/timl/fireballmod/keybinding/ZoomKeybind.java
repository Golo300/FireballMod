package com.timl.fireballmod.keybinding;

import com.timl.fireballmod.FireballMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ZoomKeybind {
    public static KeyBinding zoomKey;

    public static void register() {
        zoomKey = new KeyBinding("Zoom", Keyboard.KEY_C, FireballMod.NAME);
        ClientRegistry.registerKeyBinding(zoomKey);
    }
}
