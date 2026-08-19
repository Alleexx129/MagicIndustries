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
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
   public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "magicindustries");
   public static final RegistryObject<Block> TUNGSTEN_BLOCK = registerBlock(
      "tungsten_block", () -> new Block(Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(BLOCKS.key("tungsten_block")).strength(3.0F))
   );
   public static final RegistryObject<Block> SAPPHIRE_ORE = registerBlock(
      "sapphire_ore", () -> new Block(Properties.ofFullCopy(Blocks.EMERALD_ORE).setId(BLOCKS.key("sapphire_ore")).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final RegistryObject<LiquidBlock> MANA_WATER_BLOCK = BLOCKS.register(
      "mana_water_block", () -> new LiquidBlock(ModFluids.SOURCE_MANA_WATER, Properties.ofFullCopy(Blocks.WATER).setId(BLOCKS.key("mana_water_block")))
   );
   public static final RegistryObject<Block> DEEPSLATE_SAPPHIRE_ORE = registerBlock(
      "deepslate_sapphire_ore", () -> new Block(Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE).setId(BLOCKS.key("deepslate_sapphire_ore")).requiresCorrectToolForDrops().strength(3.0F))
   );
   public static final RegistryObject<Block> SAPPHIRE_BLOCK = registerBlock(
      "sapphire_block", () -> new Block(Properties.ofFullCopy(Blocks.EMERALD_ORE).setId(BLOCKS.key("sapphire_block")).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final RegistryObject<Block> MITHRIL_ORE = registerBlock(
      "mithril_ore", () -> new Block(Properties.ofFullCopy(Blocks.IRON_ORE).setId(BLOCKS.key("mithril_ore")).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final RegistryObject<Block> DEEPSLATE_MITHRIL_ORE = registerBlock(
      "deepslate_mithril_ore", () -> new Block(Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE).setId(BLOCKS.key("deepslate_mithril_ore")).requiresCorrectToolForDrops().strength(3.0F))
   );
   public static final RegistryObject<Block> TUNGSTEN_ORE = registerBlock(
      "tungsten_ore", () -> new Block(Properties.ofFullCopy(Blocks.IRON_ORE).setId(BLOCKS.key("tungsten_ore")).requiresCorrectToolForDrops().strength(2.0F))
   );
   public static final RegistryObject<Block> DEEPSLATE_TUNGSTEN_ORE = registerBlock(
      "deepslate_tungsten_ore", () -> new Block(Properties.ofFullCopy(Blocks.DEEPSLATE).setId(BLOCKS.key("deepslate_tungsten_ore")).strength(3.0F))
   );
   public static final RegistryObject<Block> MITHRIL_BLOCK = registerBlock("mithril_block", () -> new Block(Properties.of().setId(BLOCKS.key("mithril_block")).strength(2.0F)));
   public static final RegistryObject<Block> MANA_FLOWER = registerBlock(
      "mana_flower", () -> new ManaFlowerBlock(MobEffects.SPEED, 2.0F, Properties.of().setId(BLOCKS.key("mana_flower")).noCollision().instabreak().sound(SoundType.GRASS))
   );
   public static final RegistryObject<Block> COPPER_BLOCK = registerBlock(
      "copper_block", () -> new Block(Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(BLOCKS.key("copper_block")).requiresCorrectToolForDrops().strength(3.0F))
   );
   public static final RegistryObject<Block> COAL_GENERATOR = registerBlock(
      "coal_generator", () -> new CoalGeneratorBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(BLOCKS.key("coal_generator")).strength(3.0F).requiresCorrectToolForDrops().noOcclusion())
   );
   public static final RegistryObject<Block> MANA_EXTRACTOR = registerBlock(
      "mana_extractor", () -> new ManaExtractorBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(BLOCKS.key("mana_extractor")).strength(3.0F).requiresCorrectToolForDrops().noOcclusion())
   );

   private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
      RegistryObject<T> toReturn = BLOCKS.register(name, block);
      registerBlockItem(name, toReturn);
      return toReturn;
   }

   private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
      return ModItems.ITEMS.register(name, () -> new BlockItem((Block)block.get(), new net.minecraft.world.item.Item.Properties().setId(ModItems.ITEMS.key(name)).useBlockDescriptionPrefix()));
   }

   public static void register(BusGroup eventBus) {
      BLOCKS.register(eventBus);
   }
}
