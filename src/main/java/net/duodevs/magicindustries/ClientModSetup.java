package net.duodevs.magicindustries;

import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.screen.CoalGeneratorScreen;
import net.duodevs.magicindustries.screen.ManaExtractorScreen;
import net.duodevs.magicindustries.screen.ManaHudOverlay;
import net.duodevs.magicindustries.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = MagicIndustries.MOD_ID, value = Dist.CLIENT)
public final class ClientModSetup {
    private ClientModSetup() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_MANA_WATER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_MANA_WATER.get(), RenderType.translucent());
            MenuScreens.register(ModMenuTypes.MANA_EXTRACTOR_MENU.get(), ManaExtractorScreen::new);
            MenuScreens.register(ModMenuTypes.COAL_GENERATOR_MENU.get(), CoalGeneratorScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "mana_hud"),
                ManaHudOverlay.HUD_MANA
        );
    }
}
