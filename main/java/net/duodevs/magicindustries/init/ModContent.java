package net.duodevs.magicindustries.init;

import java.util.Random;
import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.block.*;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.item.*;
import net.duodevs.magicindustries.tile.TileCoalGenerator;
import net.duodevs.magicindustries.tile.TileManaExtractor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = MagicIndustries.MODID)
public final class ModContent {
    private ModContent() {}

    public static final Item COPPER_INGOT = basic("copper_ingot");
    public static final Item RAW_TUNGSTEN = basic("raw_tungsten");
    public static final Item TUNGSTEN_INGOT = basic("tungsten_ingot");
    public static final Item MITHRIL_INGOT = basic("mithril_ingot");
    public static final Item NETHERITE_NUGGET = basic("netherite_nugget");
    /** Compatibility stand-in for the vanilla 1.20 netherite ingot used by the source recipe. */
    public static final Item NETHERITE_INGOT = basic("netherite_ingot");
    public static final Item SAPPHIRE = basic("sapphire");
    public static final Item CUT_SAPPHIRE = basic("cut_sapphire");

    public static final Item GEM_SAW = named(new ItemGemSaw(100), "gem_saw");
    public static final Item NETHERITE_GEMSTONE_SAW = named(new ItemGemSaw(500), "netherite_gemstone_saw");

    public static final Item COPPER_HELMET = named(new ItemCopperArmor(EntityEquipmentSlot.HEAD), "copper_helmet");
    public static final Item COPPER_CHESTPLATE = named(new ItemCopperArmor(EntityEquipmentSlot.CHEST), "copper_chestplate");
    public static final Item COPPER_LEGGINGS = named(new ItemCopperArmor(EntityEquipmentSlot.LEGS), "copper_leggings");
    public static final Item COPPER_BOOTS = named(new ItemCopperArmor(EntityEquipmentSlot.FEET), "copper_boots");
    public static final Item COPPER_PICKAXE = named(new ItemCopperPickaxe(), "copper_pickaxe");
    public static final Item COPPER_AXE = named(new ItemCopperAxe(), "copper_axe");
    public static final Item COPPER_SHOVEL = named(new ItemCopperShovel(), "copper_shovel");
    public static final Item COPPER_SWORD = named(new ItemCopperSword(), "copper_sword");
    public static final Item COPPER_HOE = named(new ItemCopperHoe(), "copper_hoe");

    public static final Item SAPPHIRE_CHARM = named(new ItemSapphireCharm(), "sapphire_charm");
    public static final Item GOLD_FILTER = filter("gold_filter", 30);
    public static final Item COPPER_FILTER = filter("copper_filter", 12);
    public static final Item NETHERITE_FILTER = filter("netherite_filter", 167);
    public static final Item MANA_FLASK = named(new ItemManaFlask(), "mana_flask");
    public static final Item EMPTY_FLASK = basic("empty_flask").setMaxStackSize(64);

    public static final Block TUNGSTEN_BLOCK = harvested(block("tungsten_block", Material.IRON, 3F, 6F), 1);
    public static final Block SAPPHIRE_ORE = harvested(ore("sapphire_ore", 2F, SAPPHIRE, 1, 1, false), 2);
    public static final Block SAPPHIRE_BLOCK = harvested(block("sapphire_block", Material.ROCK, 2F, 6F), 2);
    public static final Block MITHRIL_ORE = harvested(block("mithril_ore", Material.ROCK, 2F, 5F), 1);
    public static final Block TUNGSTEN_ORE = harvested(ore("tungsten_ore", 2F, RAW_TUNGSTEN, 2, 4, true), 1);
    public static final Block MITHRIL_BLOCK = block("mithril_block", Material.IRON, 2F, 6F);
    public static final Block MANA_FLOWER = namedBlock(new BlockManaFlower(), "mana_flower");
    public static final Block COPPER_BLOCK = harvested(block("copper_block", Material.IRON, 3F, 6F), 1);
    /** 1.12 compatibility source for copper, because vanilla 1.12 has no copper ore. */
    public static final Block COPPER_ORE = harvested(ore("copper_ore", 3F, COPPER_INGOT, 1, 1, false), 1);
    public static final Block COAL_GENERATOR = harvested(namedBlock(new BlockCoalGenerator(), "coal_generator").setHardness(3F).setResistance(6F), 1);
    public static final Block MANA_EXTRACTOR = harvested(namedBlock(new BlockManaExtractor(), "mana_extractor").setHardness(3F).setResistance(6F), 1);

    public static BlockManaWater MANA_WATER_BLOCK;
    public static Item MANA_WATER_BUCKET;

    public static void preInit() {
        ensureManaWaterBlock();
        GameRegistry.registerTileEntity(TileCoalGenerator.class, id("coal_generator"));
        GameRegistry.registerTileEntity(TileManaExtractor.class, id("mana_extractor"));
    }

    private static synchronized void ensureManaWaterBlock() {
        if (MANA_WATER_BLOCK == null) {
            MANA_WATER_BLOCK = new BlockManaWater(ModFluids.MANA_WATER);

            MANA_WATER_BLOCK.setRegistryName(id("mana_water_block"));
            MANA_WATER_BLOCK.setTranslationKey(key("mana_water_block"));
        }
    }

    @SubscribeEvent public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ensureManaWaterBlock();
        event.getRegistry().registerAll(TUNGSTEN_BLOCK, SAPPHIRE_ORE, SAPPHIRE_BLOCK,
            MITHRIL_ORE, TUNGSTEN_ORE, MITHRIL_BLOCK,
            MANA_FLOWER, COPPER_BLOCK, COPPER_ORE, COAL_GENERATOR, MANA_EXTRACTOR, MANA_WATER_BLOCK);
    }

    @SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
        ensureManaWaterBlock();
        event.getRegistry().registerAll(COPPER_INGOT,RAW_TUNGSTEN,TUNGSTEN_INGOT,MITHRIL_INGOT,NETHERITE_NUGGET,NETHERITE_INGOT,
            SAPPHIRE,CUT_SAPPHIRE,GEM_SAW,NETHERITE_GEMSTONE_SAW,COPPER_HELMET,COPPER_CHESTPLATE,COPPER_LEGGINGS,COPPER_BOOTS,
            COPPER_PICKAXE,COPPER_AXE,COPPER_SHOVEL,COPPER_SWORD,COPPER_HOE,SAPPHIRE_CHARM,GOLD_FILTER,COPPER_FILTER,NETHERITE_FILTER,
            MANA_FLASK,EMPTY_FLASK);

        Block[] itemBlocks = {TUNGSTEN_BLOCK, SAPPHIRE_ORE, SAPPHIRE_BLOCK, MITHRIL_ORE,
            TUNGSTEN_ORE, MITHRIL_BLOCK, MANA_FLOWER, COPPER_BLOCK, COPPER_ORE, COAL_GENERATOR, MANA_EXTRACTOR};
        for(Block block:itemBlocks) event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));

        MANA_WATER_BUCKET = new ItemBucket(MANA_WATER_BLOCK).setContainerItem(Items.BUCKET).setMaxStackSize(1)
            .setRegistryName(id("mana_water_bucket")).setTranslationKey(key("mana_water_bucket")).setCreativeTab(MagicIndustries.TAB);
        event.getRegistry().register(MANA_WATER_BUCKET);

        registerOreDictionary(); // must exist before 1.12 JSON recipes are parsed
    }

    private static void registerOreDictionary() {
        OreDictionary.registerOre("ingotCopper",COPPER_INGOT); OreDictionary.registerOre("blockCopper",COPPER_BLOCK); OreDictionary.registerOre("oreCopper",COPPER_ORE);
        OreDictionary.registerOre("ingotTungsten",TUNGSTEN_INGOT); OreDictionary.registerOre("blockTungsten",TUNGSTEN_BLOCK); OreDictionary.registerOre("oreTungsten",TUNGSTEN_ORE);
        OreDictionary.registerOre("ingotMithril",MITHRIL_INGOT); OreDictionary.registerOre("blockMithril",MITHRIL_BLOCK); OreDictionary.registerOre("oreMithril",MITHRIL_ORE);
        OreDictionary.registerOre("ingotNetherite",NETHERITE_INGOT); OreDictionary.registerOre("nuggetNetherite",NETHERITE_NUGGET);
        OreDictionary.registerOre("gemSapphire",SAPPHIRE); OreDictionary.registerOre("gemCutSapphire",CUT_SAPPHIRE);
        // Damageable crafting tools must be registered with wildcard metadata.
        // Otherwise the ore-dict recipe only accepts damage 0 and stops matching after one craft.
        OreDictionary.registerOre("gemstoneSaws", new ItemStack(GEM_SAW, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("gemstoneSaws", new ItemStack(NETHERITE_GEMSTONE_SAW, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("manaFilters",GOLD_FILTER); OreDictionary.registerOre("manaFilters",COPPER_FILTER); OreDictionary.registerOre("manaFilters",NETHERITE_FILTER);
    }

    /** Accepts this mod's copper plus any 1.12 mod that correctly registers ingotCopper. */
    public static boolean isCopperIngot(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.getItem() == COPPER_INGOT) return true;
        for (int id : OreDictionary.getOreIDs(stack)) {
            if ("ingotCopper".equals(OreDictionary.getOreName(id))) return true;
        }
        return false;
    }

    public static void initRecipesAndTrades() {
        // Modern source recipes: mithril/raw tungsten award 0.2 XP. Copper is a 1.12 compatibility recipe.
        GameRegistry.addSmelting(COPPER_ORE,new ItemStack(COPPER_INGOT),0.7F);
        GameRegistry.addSmelting(MITHRIL_ORE,new ItemStack(MITHRIL_INGOT),0.2F);
        GameRegistry.addSmelting(RAW_TUNGSTEN,new ItemStack(TUNGSTEN_INGOT),0.2F);

        // Original: Farmer, level 4, 32 emeralds -> Mana Flower, max one use.
        VillagerRegistry.VillagerProfession farmer=VillagerRegistry.getById(0);
        if(farmer!=null && farmer.getCareer(0)!=null) farmer.getCareer(0).addTrade(4,new ManaFlowerTrade(32));

        // 1.12 has no Wandering Trader. Use the vanilla nitwit career as a stationary analogue for the generic 16-emerald offer.
        VillagerRegistry.VillagerProfession nitwit=VillagerRegistry.getById(5);
        if(nitwit!=null && nitwit.getCareer(0)!=null) nitwit.getCareer(0).addTrade(1,new ManaFlowerTrade(16));
    }

    private static Item basic(String name) { return named(new Item(),name); }
    private static <T extends Item> T named(T item,String name) { return (T)item.setRegistryName(id(name)).setTranslationKey(key(name)).setCreativeTab(MagicIndustries.TAB); }
    private static Item filter(String name,int durability) { return named(new Item().setMaxStackSize(1).setMaxDamage(durability),name); }
    private static Block block(String name,Material material,float hardness,float resistance) { return namedBlock(new Block(material),name).setHardness(hardness).setResistance(resistance); }
    private static <T extends Block> T namedBlock(T block,String name) { return (T)block.setRegistryName(id(name)).setTranslationKey(key(name)).setCreativeTab(MagicIndustries.TAB); }
    private static <T extends Block> T harvested(T block, int level) { block.setHarvestLevel("pickaxe", level); return block; }
    private static Block ore(String name,float hardness,Item drop,int min,int max,boolean bonusPerRoll) {
        return namedBlock(new BlockDropOre(drop,min,max,bonusPerRoll),name).setHardness(hardness).setResistance(5F);
    }
    private static ResourceLocation id(String name){return new ResourceLocation(MagicIndustries.MODID,name);}
    private static String key(String name){return MagicIndustries.MODID+"."+name;}

    private static final class ManaFlowerTrade implements net.minecraft.entity.passive.EntityVillager.ITradeList {
        private final int emeralds; private ManaFlowerTrade(int emeralds){this.emeralds=emeralds;}
        @Override public void addMerchantRecipe(net.minecraft.entity.IMerchant merchant,net.minecraft.village.MerchantRecipeList list,Random random){
            list.add(new net.minecraft.village.MerchantRecipe(new ItemStack(Items.EMERALD,emeralds),ItemStack.EMPTY,new ItemStack(MANA_FLOWER),0,1));
        }
    }
}
