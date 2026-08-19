package net.duodevs.magicindustries;

import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.screen.CoalGeneratorScreen;
import net.duodevs.magicindustries.screen.ManaExtractorScreen;
import net.duodevs.magicindustries.screen.ManaHudOverlay;
import net.duodevs.magicindustries.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MagicIndustries.MOD_ID, value = Dist.CLIENT)
public class ClientModSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_MANA_WATER.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_MANA_WATER.get(), ChunkSectionLayer.TRANSLUCENT);

            MenuScreens.register(ModMenuTypes.MANA_EXTRACTOR_MENU.get(), ManaExtractorScreen::new);
            MenuScreens.register(ModMenuTypes.COAL_GENERATOR_MENU.get(), CoalGeneratorScreen::new);
        });
    }

    static {
        AddGuiOverlayLayersEvent.BUS.addListener(ClientModSetup::registerGuiOverlays);
    }

    public static void registerGuiOverlays(AddGuiOverlayLayersEvent event) {
        event.getLayeredDraw().addBelow(
                ForgeLayeredDraw.POST_SLEEP_STACK,
                Identifier.fromNamespaceAndPath(MagicIndustries.MOD_ID, "mana_hud"),
                ForgeLayeredDraw.CHAT_OVERLAY,
                ManaHudOverlay.HUD_MANA
        );
    }
}