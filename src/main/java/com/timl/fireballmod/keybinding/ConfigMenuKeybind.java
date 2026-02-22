package com.timl.fireballmod.keybinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import com.timl.fireballmod.gui.GuiZoomSettings;
import net.minecraft.client.Minecraft;

public class ConfigMenuKeybind {
    public static final KeyBinding OPEN_CONFIG = new KeyBinding(
            "Settings", Keyboard.KEY_P, "Fireball Tank Mod");

    public static void register() {
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(OPEN_CONFIG);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (OPEN_CONFIG.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiZoomSettings());
        }
    }
}