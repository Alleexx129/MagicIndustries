package net.duodevs.magicindustries;

import java.util.stream.Collectors;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.block.entity.ModBlockEntities;
import net.duodevs.magicindustries.fluid.ModFluidTypes;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.item.ModCreativeModTabs;
import net.duodevs.magicindustries.item.ModItems;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.recipe.ModRecipes;
import net.duodevs.magicindustries.screen.ModMenuTypes;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MagicIndustries.MOD_ID)
public class MagicIndustries {
   public static final String MOD_ID = "magicindustries";
   private static final Logger LOGGER = LogManager.getLogger();

   public MagicIndustries(FMLJavaModLoadingContext context) {
      var eventBus = context.getModBusGroup();

      ModItems.register(eventBus);
      ModBlocks.register(eventBus);
      ModCreativeModTabs.register(eventBus);
      ModFluids.register(eventBus);
      ModFluidTypes.register(eventBus);
      ModMessages.register();
      ModBlockEntities.register(eventBus);
      ModMenuTypes.register(eventBus);
      ModRecipes.register(eventBus);
      FMLCommonSetupEvent.getBus(eventBus).addListener(this::setup);
      InterModEnqueueEvent.getBus(eventBus).addListener(this::enqueueIMC);
      InterModProcessEvent.getBus(eventBus).addListener(this::processIMC);
      //FMLClientSetupEvent.getBus(eventBus).addListener(this::doClientStuff);
      FMLCommonSetupEvent.getBus(eventBus).addListener(this::commonSetup);
      //eventBus.addListener(MagicIndustries::registerGuiOverlays);
      ServerStartingEvent.BUS.addListener(this::onServerStarting);
   }

   private void setup(FMLCommonSetupEvent event) {
      LOGGER.info("MAGIC INDUSTRIES: TUNG TUNG TUNG SAHUR 67");
   }

   private void commonSetup(final FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {

      });
   }

   /*
   @SubscribeEvent
   public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
      event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "mana_hud",ManaHudOverlay.HUD_MANA);
   }

   private void doClientStuff(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_MANA_WATER.get(), RenderType.translucent());
         ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_MANA_WATER.get(), RenderType.translucent());
         MenuScreens.register(ModMenuTypes.MANA_EXTRACTOR_MENU.get(), ManaExtractorScreen::new);
         MenuScreens.register(ModMenuTypes.COAL_GENERATOR_MENU.get(), CoalGeneratorScreen::new);
      });
   }
    */

   private void enqueueIMC(InterModEnqueueEvent event) {

   }

   private void processIMC(InterModProcessEvent event) {
      LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.messageSupplier().get()).collect(Collectors.toList()));
   }

   public void onServerStarting(ServerStartingEvent event) {
      LOGGER.info("HELLO from server starting");
   }

   /// private void addCreative(BuildCreativeModeTabContentsEvent event) {
   /// }
}
