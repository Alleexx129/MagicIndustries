package net.duodevs.magicindustries.worldgen;

import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries.Keys;

public class ModBiomeModifiers {
   public static final ResourceKey<BiomeModifier> ADD_SAPPHIRE_ORE = registerKey("add_sapphire_ore");
   public static final ResourceKey<BiomeModifier> ADD_TUNGSTEN_ORE = registerKey("add_tungsten_ore");
   public static final ResourceKey<BiomeModifier> ADD_MITHRIL_ORE = registerKey("add_mithril_ore");

   public static void bootstrap(BootstapContext<BiomeModifier> context) {
      HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
      HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
      context.register(
         ADD_SAPPHIRE_ORE,
         new AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(new Holder[]{placedFeatures.getOrThrow(ModPlacedFeatures.SAPPHIRE_ORE_PLACED_KEY)}),
            Decoration.UNDERGROUND_ORES
         )
      );
      context.register(
         ADD_TUNGSTEN_ORE,
         new AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(new Holder[]{placedFeatures.getOrThrow(ModPlacedFeatures.TUNGSTEN_ORE_PLACED_KEY)}),
            Decoration.UNDERGROUND_ORES
         )
      );
      context.register(
         ADD_MITHRIL_ORE,
         new AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(new Holder[]{placedFeatures.getOrThrow(ModPlacedFeatures.MITHRIL_ORE_PLACED_KEY)}),
            Decoration.UNDERGROUND_ORES
         )
      );
   }

   private static ResourceKey<BiomeModifier> registerKey(String name) {
      return ResourceKey.create(Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, name));
   }
}
