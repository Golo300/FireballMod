package com.timl.fireballmod.keybinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import com.timl.fireballmod.gui.GuiZoomSettings;
import net.minecraft.client.Minecraft;
import com.timl.fireballmod.Settings;

public class ConfigMenuKeybind {
    public static final KeyBinding OPEN_CONFIG = new KeyBinding(
            "Settings", Keyboard.KEY_P, "Fireball Tank Mod");

    private static Settings settings = null;

    public static void register(Settings settings) {
        ConfigMenuKeybind.settings = settings;
        ClientRegistry.registerKeyBinding(OPEN_CONFIG);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ConfigMenuKeybind.settings == null) return;

        if (OPEN_CONFIG.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiZoomSettings(settings));
        }
    }
}