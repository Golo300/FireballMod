package com.timl.fireballmod;

import com.timl.fireballmod.gui.GuiZoomSettings;
import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import com.timl.fireballmod.keybinding.ConfigMenuKeybind;
import com.timl.fireballmod.keybinding.ZoomKeybind;
import com.timl.fireballmod.scope.ScopeManager;
import net.minecraftforge.common.MinecraftForge;

import static com.timl.fireballmod.FireballMod.settings;

public class ClientProxy extends CommonProxy {

    public static ScopeManager scopeManager;

    @Override
    public void preInit() {

        scopeManager = new ScopeManager();
        scopeManager.reload();

        ZoomKeybind.register();
        ConfigMenuKeybind.register(settings, scopeManager);

        MinecraftForge.EVENT_BUS.register(new ZoomHandler(settings, scopeManager));
        MinecraftForge.EVENT_BUS.register(new CameraShakeHandler(settings));
        MinecraftForge.EVENT_BUS.register(new ConfigMenuKeybind());
    }

    @Override
    public void init() {
        
    }
}
