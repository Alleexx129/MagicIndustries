package net.duodevs.magicindustries;

import net.duodevs.magicindustries.capability.ManaCapability;
import net.duodevs.magicindustries.event.CommonEvents;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.gui.GuiHandler;
import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.network.ModNetwork;
import net.duodevs.magicindustries.proxy.CommonProxy;
import net.duodevs.magicindustries.world.ModWorldGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(
    modid = MagicIndustries.MODID,
    name = MagicIndustries.NAME,
    version = MagicIndustries.VERSION,
    acceptedMinecraftVersions = "[1.12.2]",
    dependencies = "required-after:forge@[14.23.5.2859,);required-after:baubles@[1.5.2,);required-after:jei@[4.16.1.302,)"
)
public class MagicIndustries {
    public static final String MODID = "magicindustries";
    public static final String NAME = "Magic Industries";
    public static final String VERSION = "1.5.2-1.12.2";


    @Mod.Instance(MODID)
    public static MagicIndustries INSTANCE;

    @SidedProxy(
        clientSide = "net.duodevs.magicindustries.proxy.ClientProxy",
        serverSide = "net.duodevs.magicindustries.proxy.CommonProxy"
    )
    public static CommonProxy PROXY;

    public static final CreativeTabs TAB = new CreativeTabs("magic_industries_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModContent.SAPPHIRE);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModFluids.preInit();
        ManaCapability.register();
        ModNetwork.init();
        ModContent.preInit();
        PROXY.preInit();
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 3);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModContent.initRecipesAndTrades();
        PROXY.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit();
    }
}
