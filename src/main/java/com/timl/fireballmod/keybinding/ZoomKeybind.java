package com.timl.fireballmod.keybinding;

import com.timl.fireballmod.FireballMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ZoomKeybind {
    public static final KeyBinding ZOOM_KEY = new KeyBinding("Zoom", Keyboard.KEY_C, FireballMod.NAME);

    public static void register() {
        ClientRegistry.registerKeyBinding(ZOOM_KEY);
    }
}
