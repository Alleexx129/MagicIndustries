package net.duodevs.magicindustries.block.entity;

import java.util.function.Supplier;
import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    private ModBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MagicIndustries.MOD_ID);

    public static final Supplier<BlockEntityType<ManaExtractorBlockEntity>> MANA_EXTRACTOR = BLOCK_ENTITIES.register(
            "mana_extractor", () -> BlockEntityType.Builder.of(ManaExtractorBlockEntity::new, ModBlocks.MANA_EXTRACTOR.get()).build(null)
    );
    public static final Supplier<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = BLOCK_ENTITIES.register(
            "coal_generator", () -> BlockEntityType.Builder.of(CoalGeneratorBlockEntity::new, ModBlocks.COAL_GENERATOR.get()).build(null)
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MANA_EXTRACTOR.get(), ManaExtractorBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MANA_EXTRACTOR.get(), ManaExtractorBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COAL_GENERATOR.get(), CoalGeneratorBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, COAL_GENERATOR.get(), (be, side) -> be.getEnergyStorage());
    }
}
