package net.duodevs.magicindustries.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.duodevs.magicindustries.worldgen.ModBiomeModifiers;
import net.duodevs.magicindustries.worldgen.ModConfiguredFeatures;
import net.duodevs.magicindustries.worldgen.ModPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries.Keys;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
   public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
      .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
      .add(Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

   public ModWorldGenProvider(PackOutput output, CompletableFuture<Provider> registries) {
      super(output, registries, BUILDER, Set.of("magicindustries"));
   }
}
