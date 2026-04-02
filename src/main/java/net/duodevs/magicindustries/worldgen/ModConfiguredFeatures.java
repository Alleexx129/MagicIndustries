package net.duodevs.magicindustries.worldgen;

import java.util.List;
import net.duodevs.magicindustries.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class ModConfiguredFeatures {
   public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SAPPHIRE_ORE_KEY = registerKey("sapphire_ore");
   public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TUNGSTEN_ORE_KEY = registerKey("tungsten_ore");
   public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_MITHRIL_ORE_KEY = registerKey("mithril_ore");

   public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
      RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
      RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
      List<TargetBlockState> overworldSapphireOres = List.of(
         OreConfiguration.target(stoneReplaceable, ((Block)ModBlocks.SAPPHIRE_ORE.get()).defaultBlockState()),
         OreConfiguration.target(deepslateReplaceables, ((Block)ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get()).defaultBlockState())
      );
      register(context, OVERWORLD_SAPPHIRE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSapphireOres, 2));
      List<TargetBlockState> overworldTungstenOres = List.of(
         OreConfiguration.target(stoneReplaceable, ((Block)ModBlocks.TUNGSTEN_ORE.get()).defaultBlockState()),
         OreConfiguration.target(deepslateReplaceables, ((Block)ModBlocks.DEEPSLATE_TUNGSTEN_ORE.get()).defaultBlockState())
      );
      register(context, OVERWORLD_TUNGSTEN_ORE_KEY, Feature.ORE, new OreConfiguration(overworldTungstenOres, 9));
      List<TargetBlockState> overworldMithrilOres = List.of(
         OreConfiguration.target(stoneReplaceable, ((Block)ModBlocks.MITHRIL_ORE.get()).defaultBlockState()),
         OreConfiguration.target(deepslateReplaceables, ((Block)ModBlocks.DEEPSLATE_MITHRIL_ORE.get()).defaultBlockState())
      );
      register(context, OVERWORLD_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 6));
   }

   public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
      return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("magicindustries", name));
   }

   private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
      BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration
   ) {
      context.register(key, new ConfiguredFeature(feature, configuration));
   }
}
