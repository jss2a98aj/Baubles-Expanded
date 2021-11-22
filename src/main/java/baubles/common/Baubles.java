package baubles.common;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import baubles.common.event.EventHandlerEntity;
import baubles.common.event.EventHandlerNetwork;
import baubles.common.network.PacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = Baubles.MODID, name = Baubles.MODNAME, version = Baubles.VERSION)

public class Baubles {

    public static final String MODID = "Baubles";
    public static final String MODNAME = "Baubles";
    public static final String VERSION = "2.0.0";

    @SidedProxy(clientSide = "baubles.client.ClientProxy", serverSide = "baubles.common.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value=Baubles.MODID)
    public static Baubles instance;

    public EventHandlerEntity entityEventHandler;
    public EventHandlerNetwork entityEventNetwork;

    public static final Logger log = LogManager.getLogger("Baubles");
    public static final int GUI = 0;

    public static final Item itemDebugger = new ItemDebugger().setUnlocalizedName("baubleSlotDebugTool");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        event.getModMetadata().version = Baubles.VERSION;

        PacketHandler.init();

        entityEventHandler = new EventHandlerEntity();
        entityEventNetwork = new EventHandlerNetwork();

        MinecraftForge.EVENT_BUS.register(entityEventHandler);
        FMLCommonHandler.instance().bus().register(entityEventNetwork);
        proxy.registerHandlers();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //This config is intentionally loaded later than normal.
        BaublesConfig.loadConfig(new Configuration(new File(Launch.minecraftHome, "config" + File.separator + "baubles.cfg")));

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
          proxy.registerKeyBindings();
          GameRegistry.registerItem(itemDebugger, "bauble_slot_debug_tool", Baubles.MODID);
    }

}
