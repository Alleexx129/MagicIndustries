package net.duodevs.magicindustries.datagen;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class DataGenerators {
    private DataGenerators() {}

    public static void gatherData(GatherDataEvent event) {
        event.createDatapackRegistryObjects(ModWorldGenProvider.BUILDER);
    }
}
