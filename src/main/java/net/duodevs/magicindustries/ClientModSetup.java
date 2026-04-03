package net.duodevs.magicindustries;

import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.screen.CoalGeneratorScreen;
import net.duodevs.magicindustries.screen.ManaExtractorScreen;
import net.duodevs.magicindustries.screen.ManaHudOverlay;
import net.duodevs.magicindustries.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MagicIndustries.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModSetup {

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
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "mana_hud", ManaHudOverlay.HUD_MANA);
    }
}