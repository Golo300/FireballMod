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
    public static final String VERSION = "1.6.0";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    private Configuration config;

    @Mod.Instance
    public static FireballMod instance;

    @SidedProxy(
            clientSide = "com.timl.fireballmod.ClientProxy",
            serverSide = "com.timl.fireballmod.CommonProxy"
    )
    public static CommonProxy proxy;

    public Configuration getConfig() { return config; }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        float smoothing = config.getFloat("zoomSmoothing", "zoom", ZoomHandler.DEFAULT_ZOOM_SMOOTHING, ZoomHandler.MIN_ZOOM_SMOOTHING, ZoomHandler.MAX_ZOOM_SMOOTHING, "zoom smoothness");
        float zoomStep = config.getFloat("zoomStep", "zoom", ZoomHandler.DEFAULT_ZOOM_STEP, ZoomHandler.MIN_ZOOM_STEP, ZoomHandler.MAX_ZOOM_STEP, "zoom");
        float maxShake = config.getFloat("maxShake", "camera", CameraShakeHandler.DEFAULT_SHAKE, CameraShakeHandler.MIN_SHAKE, CameraShakeHandler.MIN_SHAKE, "camera shake intensity");

        proxy.preInit();
        LOGGER.info("Config loaded: smoothing={}, zoomStep={}, maxShake={}", smoothing, zoomStep, maxShake);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        System.out.println(NAME + " initialized!");
    }
}
