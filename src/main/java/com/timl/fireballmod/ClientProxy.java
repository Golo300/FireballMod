package com.timl.fireballmod;

import com.timl.fireballmod.gui.GuiZoomSettings;
import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import com.timl.fireballmod.keybinding.ConfigMenuKeybind;
import com.timl.fireballmod.keybinding.ZoomKeybind;
import net.minecraftforge.common.MinecraftForge;

import static com.timl.fireballmod.FireballMod.settings;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {

        ZoomKeybind.register();
        ConfigMenuKeybind.register(settings);

        MinecraftForge.EVENT_BUS.register(new ZoomHandler(settings));
        MinecraftForge.EVENT_BUS.register(new CameraShakeHandler(settings));
        MinecraftForge.EVENT_BUS.register(new ConfigMenuKeybind());
    }

    @Override
    public void init() {
        // weitere Client-Initialisierung
    }
}
