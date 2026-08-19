package net.duodevs.magicindustries;

import net.duodevs.magicindustries.DataContainers.PlayerManaProvider;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.block.entity.ModBlockEntities;
import net.duodevs.magicindustries.datagen.DataGenerators;
import net.duodevs.magicindustries.event.ModEvents;
import net.duodevs.magicindustries.fluid.ModFluidTypes;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.item.ModArmorMaterial;
import net.duodevs.magicindustries.item.ModCreativeModTabs;
import net.duodevs.magicindustries.item.ModItems;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.recipe.ModRecipes;
import net.duodevs.magicindustries.screen.ModMenuTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MagicIndustries.MOD_ID)
public class MagicIndustries {
    public static final String MOD_ID = "magicindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public MagicIndustries(IEventBus modEventBus) {
        ModArmorMaterial.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModTabs.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        PlayerManaProvider.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ModBlockEntities::registerCapabilities);
        modEventBus.addListener(ModMessages::register);
        modEventBus.addListener(DataGenerators::gatherData);

        NeoForge.EVENT_BUS.addListener(ModEvents::addCustomTrades);
        NeoForge.EVENT_BUS.addListener(ModEvents::addCustomWanderingTrades);
        NeoForge.EVENT_BUS.addListener(ModEvents::onPlayerCloned);
        NeoForge.EVENT_BUS.addListener(ModEvents::onPlayerJoin);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Magic Industries NeoForge 1.21.1 initializing");
    }

    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Magic Industries server hooks initialized");
    }
}
