package net.duodevs.magicindustries.datagen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "magicindustries",
   bus = Bus.MOD
)
public class DataGenerators {
   @SubscribeEvent
   public static void gatherData(GatherDataEvent event) {
      DataGenerator generator = event.getGenerator();
      PackOutput packOutput = generator.getPackOutput();
      CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
      generator.addProvider(event.includeServer(), new ModWorldGenProvider(packOutput, lookupProvider));
   }
}
