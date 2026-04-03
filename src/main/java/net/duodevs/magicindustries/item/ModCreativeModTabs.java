package net.duodevs.magicindustries.item;

import net.duodevs.magicindustries.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
   public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "magicindustries");
   public static final RegistryObject<CreativeModeTab> MAGIC_INDUSTRIES_TAB = CREATIVE_MODE_TABS.register(
      "magic_industries_tab",
      () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.SAPPHIRE.get()))
            .title(Component.translatable("creativetab.magic_industries_tab"))
            .displayItems((pParameters, pOutput) -> {
               pOutput.accept(ModItems.SAPPHIRE.get());
               pOutput.accept(ModItems.CUT_SAPPHIRE.get());
               pOutput.accept(ModItems.COPPER_AXE.get());
               pOutput.accept(ModItems.COPPER_INGOT.get());
               pOutput.accept(ModItems.COPPER_BOOTS.get());
               pOutput.accept(ModItems.COPPER_CHESTPLATE.get());
               pOutput.accept(ModItems.COPPER_HELMET.get());
               pOutput.accept(ModItems.COPPER_HOE.get());
               pOutput.accept(ModItems.COPPER_LEGGINGS.get());
               pOutput.accept(ModItems.COPPER_PICKAXE.get());
               pOutput.accept(ModItems.COPPER_SHOVEL.get());
               pOutput.accept(ModItems.COPPER_SWORD.get());
               pOutput.accept(ModItems.RAW_TUNGSTEN.get());
               pOutput.accept(ModItems.MITHRIL_INGOT.get());
               pOutput.accept(ModItems.TUNGSTEN_INGOT.get());
               pOutput.accept(ModItems.GEM_SAW.get());
               pOutput.accept(ModItems.NETHERITE_GEMSTONE_SAW.get());
               pOutput.accept(ModItems.NETHERITE_NUGGET.get());
               pOutput.accept(ModItems.SAPPHIRE_CHARM.get());
               pOutput.accept(ModItems.MANA_WATER_BUCKET.get());
               pOutput.accept(ModItems.NETHERITE_FILTER.get());
               pOutput.accept(ModItems.GOLDEN_FILTER.get());
               pOutput.accept(ModItems.COPPER_FILTER.get());
               pOutput.accept(ModBlocks.MANA_EXTRACTOR.get());
               pOutput.accept(ModBlocks.COPPER_BLOCK.get());
               pOutput.accept(ModBlocks.SAPPHIRE_ORE.get());
               pOutput.accept(ModBlocks.MITHRIL_BLOCK.get());
               pOutput.accept(ModBlocks.MITHRIL_ORE.get());
               pOutput.accept(ModBlocks.TUNGSTEN_BLOCK.get());
               pOutput.accept(ModBlocks.TUNGSTEN_ORE.get());
               pOutput.accept(ModBlocks.MANA_FLOWER.get());
               pOutput.accept(ModBlocks.SAPPHIRE_BLOCK.get());
               pOutput.accept(ModBlocks.COAL_GENERATOR.get());
                pOutput.accept(ModItems.MANA_FLASK.get());
                pOutput.accept(ModItems.EMPTY_FLASK.get());
            })
            .build()
   );

   public static void register(IEventBus eventBus) {
      CREATIVE_MODE_TABS.register(eventBus);
   }
}
