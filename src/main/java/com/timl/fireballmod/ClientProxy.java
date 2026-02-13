package com.timl.fireballmod;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ZoomKeybind.register();
        MinecraftForge.EVENT_BUS.register(new ZoomHandler());
    }

    @Override
    public void init() {
        // weitere Client-Initialisierung
    }
}
