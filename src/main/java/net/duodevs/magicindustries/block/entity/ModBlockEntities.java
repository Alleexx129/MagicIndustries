package net.duodevs.magicindustries.block.entity;

import net.duodevs.magicindustries.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "magicindustries");
   public static final RegistryObject<BlockEntityType<ManaExtractorBlockEntity>> MANA_EXTRACTOR = BLOCK_ENTITIES.register(
      "mana_extractor", () -> Builder.of(ManaExtractorBlockEntity::new, new Block[]{(Block)ModBlocks.MANA_EXTRACTOR.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = BLOCK_ENTITIES.register(
      "coal_generator", () -> Builder.of(CoalGeneratorBlockEntity::new, new Block[]{(Block)ModBlocks.COAL_GENERATOR.get()}).build(null)
   );

   public static void register(IEventBus eventBus) {
      BLOCK_ENTITIES.register(eventBus);
   }
}
