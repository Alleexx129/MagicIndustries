package net.duodevs.magicindustries.worldgen;

import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {
    private ModBiomeModifiers() {}

    public static final ResourceKey<BiomeModifier> ADD_SAPPHIRE_ORE = registerKey("add_sapphire_ore");
    public static final ResourceKey<BiomeModifier> ADD_TUNGSTEN_ORE = registerKey("add_tungsten_ore");
    public static final ResourceKey<BiomeModifier> ADD_MITHRIL_ORE = registerKey("add_mithril_ore");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderSet<Biome> overworld = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);

        context.register(ADD_SAPPHIRE_ORE, new AddFeaturesBiomeModifier(
                overworld, HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SAPPHIRE_ORE_PLACED_KEY)), Decoration.UNDERGROUND_ORES));
        context.register(ADD_TUNGSTEN_ORE, new AddFeaturesBiomeModifier(
                overworld, HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.TUNGSTEN_ORE_PLACED_KEY)), Decoration.UNDERGROUND_ORES));
        context.register(ADD_MITHRIL_ORE, new AddFeaturesBiomeModifier(
                overworld, HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MITHRIL_ORE_PLACED_KEY)), Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, name)
        );
    }
}
