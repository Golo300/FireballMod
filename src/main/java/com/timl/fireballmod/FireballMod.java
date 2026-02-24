package com.timl.fireballmod;

import com.timl.fireballmod.handler.CameraShakeHandler;
import com.timl.fireballmod.handler.ZoomHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = FireballMod.MODID,
        name = FireballMod.NAME,
        version = FireballMod.VERSION,
        clientSideOnly = true
)
public class FireballMod {

    public static final String MODID = "fireballmod";
    public static final String NAME = "Fireball Tank Mod";
    public static final String VERSION = "1.7.0";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static Settings settings;

    @Mod.Instance
    public static FireballMod instance;

    @SidedProxy(
            clientSide = "com.timl.fireballmod.ClientProxy",
            serverSide = "com.timl.fireballmod.CommonProxy"
    )
    public static CommonProxy proxy;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        FireballMod.settings = new Settings(config);
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        System.out.println(NAME + " initialized!");
    }
}
