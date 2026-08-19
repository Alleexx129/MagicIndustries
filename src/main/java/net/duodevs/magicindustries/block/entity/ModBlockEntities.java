package net.duodevs.magicindustries.block.entity;

import net.duodevs.magicindustries.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.Set;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "magicindustries");
   public static final RegistryObject<BlockEntityType<ManaExtractorBlockEntity>> MANA_EXTRACTOR = BLOCK_ENTITIES.register(
      "mana_extractor", () -> new BlockEntityType<>(ManaExtractorBlockEntity::new, Set.of(ModBlocks.MANA_EXTRACTOR.get()))
   );
   public static final RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = BLOCK_ENTITIES.register(
      "coal_generator", () -> new BlockEntityType<>(CoalGeneratorBlockEntity::new, Set.of(ModBlocks.COAL_GENERATOR.get()))
   );

   public static void register(BusGroup eventBus) {
      BLOCK_ENTITIES.register(eventBus);
   }
}
