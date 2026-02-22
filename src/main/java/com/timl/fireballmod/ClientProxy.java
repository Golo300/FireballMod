package com.timl.fireballmod;

import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import com.timl.fireballmod.keybinding.ConfigMenuKeybind;
import com.timl.fireballmod.keybinding.ZoomKeybind;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ZoomKeybind.register();
        ConfigMenuKeybind.register();
        MinecraftForge.EVENT_BUS.register(new ZoomHandler());
        MinecraftForge.EVENT_BUS.register(new CameraShakeHandler());
        MinecraftForge.EVENT_BUS.register(new ConfigMenuKeybind());
    }

    @Override
    public void init() {
        // weitere Client-Initialisierung
    }
}
