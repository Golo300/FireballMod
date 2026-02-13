package com.timl.fireballmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = FireballMod.MODID,
        name = FireballMod.NAME,
        version = FireballMod.VERSION,
        clientSideOnly = true
)
public class FireballMod {

    public static final String MODID = "fireballmod";
    public static final String NAME = "Fireball Tank Mod";
    public static final String VERSION = "0.2.0";

    @Mod.Instance
    public static FireballMod instance;

    @SidedProxy(
            clientSide = "com.timl.fireballmod.ClientProxy",
            serverSide = "com.timl.fireballmod.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        System.out.println(NAME + " initialized!");
    }
}
