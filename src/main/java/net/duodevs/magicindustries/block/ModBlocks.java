package net.duodevs.magicindustries.block;

import java.util.function.Supplier;
import net.duodevs.magicindustries.block.custom.CoalGeneratorBlock;
import net.duodevs.magicindustries.block.custom.ManaExtractorBlock;
import net.duodevs.magicindustries.block.custom.ManaFlowerBlock;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.item.ModItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;


public class ModBlocks {
   public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "magicindustries");
   public static final Supplier<Block> TUNGSTEN_BLOCK = registerBlock(
      "tungsten_block", () -> new Block(Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0F))
   );
   public static final Supplier<Block> SAPPHIRE_ORE = registerBlock(
      "sapphire_ore", () -> new Block(Properties.ofFullCopy(Blocks.EMERALD_ORE).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final Supplier<LiquidBlock> MANA_WATER_BLOCK = BLOCKS.register(
      "mana_water_block", () -> new LiquidBlock(ModFluids.SOURCE_MANA_WATER.get(), Properties.ofFullCopy(Blocks.WATER))
   );
   public static final Supplier<Block> DEEPSLATE_SAPPHIRE_ORE = registerBlock(
      "deepslate_sapphire_ore", () -> new Block(Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE).requiresCorrectToolForDrops().strength(3.0F))
   );
   public static final Supplier<Block> SAPPHIRE_BLOCK = registerBlock(
      "sapphire_block", () -> new Block(Properties.ofFullCopy(Blocks.EMERALD_ORE).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final Supplier<Block> MITHRIL_ORE = registerBlock(
      "mithril_ore", () -> new Block(Properties.ofFullCopy(Blocks.IRON_ORE).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final Supplier<Block> DEEPSLATE_MITHRIL_ORE = registerBlock(
      "deepslate_mithril_ore", () -> new Block(Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE).requiresCorrectToolForDrops().strength(3.0F))
   );
   public static final Supplier<Block> TUNGSTEN_ORE = registerBlock(
      "tungsten_ore", () -> new Block(Properties.ofFullCopy(Blocks.IRON_ORE).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final Supplier<Block> DEEPSLATE_TUNGSTEN_ORE = registerBlock(
      "deepslate_tungsten_ore", () -> new Block(Properties.ofFullCopy(Blocks.DEEPSLATE).strength(3.0F))
   );
   public static final Supplier<Block> MITHRIL_BLOCK = registerBlock("mithril_block", () -> new Block(Properties.of().strength(2.0F)));
   public static final Supplier<Block> MANA_FLOWER = registerBlock(
      "mana_flower", () -> new ManaFlowerBlock(MobEffects.MOVEMENT_SPEED, 2.0F, Properties.of().noCollission().instabreak().sound(SoundType.GRASS))
   );
   public static final Supplier<Block> COPPER_BLOCK = registerBlock(
      "copper_block", () -> new Block(Properties.ofFullCopy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(3.0F))
   );
   public static final Supplier<Block> COAL_GENERATOR = registerBlock(
      "coal_generator", () -> new CoalGeneratorBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0F).requiresCorrectToolForDrops().noOcclusion())
   );
   public static final Supplier<Block> MANA_EXTRACTOR = registerBlock(
      "mana_extractor", () -> new ManaExtractorBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0F).requiresCorrectToolForDrops().noOcclusion())
   );

   private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
      Supplier<T> toReturn = BLOCKS.register(name, block);
      registerBlockItem(name, toReturn);
      return toReturn;
   }

   private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
      return ModItems.ITEMS.register(name, () -> new BlockItem((Block)block.get(), new net.minecraft.world.item.Item.Properties()));
   }

   public static void register(IEventBus eventBus) {
      BLOCKS.register(eventBus);
   }
}
