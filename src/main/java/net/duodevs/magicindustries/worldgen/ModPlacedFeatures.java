package net.duodevs.magicindustries.worldgen;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class ModPlacedFeatures {
   public static final ResourceKey<PlacedFeature> SAPPHIRE_ORE_PLACED_KEY = registerKey("sapphire_ore_placed");
   public static final ResourceKey<PlacedFeature> TUNGSTEN_ORE_PLACED_KEY = registerKey("tungsten_ore_placed");
   public static final ResourceKey<PlacedFeature> MITHRIL_ORE_PLACED_KEY = registerKey("mithril_ore_placed");

   public static void bootstrap(BootstapContext<PlacedFeature> context) {
      HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
      register(
         context,
         SAPPHIRE_ORE_PLACED_KEY,
         configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_SAPPHIRE_ORE_KEY),
         ModOrePlacement.rareOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(256)))
      );
      register(
         context,
         TUNGSTEN_ORE_PLACED_KEY,
         configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_TUNGSTEN_ORE_KEY),
         ModOrePlacement.commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(72)))
      );
      register(
         context,
         MITHRIL_ORE_PLACED_KEY,
         configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_MITHRIL_ORE_KEY),
         ModOrePlacement.commonOrePlacement(14, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(64)))
      );
   }

   private static ResourceKey<PlacedFeature> registerKey(String name) {
      return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("magicindustries", name));
   }

   private static void register(
      BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers
   ) {
      context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
   }
}
