package net.duodevs.magicindustries.item;

import java.util.function.Supplier;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.item.custom.GemSawItem;
import net.duodevs.magicindustries.item.custom.ManaBottle;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "magicindustries");
   public static final RegistryObject<Item> MANA_WATER_BUCKET = ITEMS.register(
      "mana_water_bucket", () -> new BucketItem(ModFluids.SOURCE_MANA_WATER, new Properties().stacksTo(1).craftRemainder(Items.BUCKET))
   );
   public static final RegistryObject<Item> COPPER_INGOT = registerItem("copper_ingot", () -> new Item(new Properties()));
   public static final RegistryObject<Item> RAW_TUNGSTEN = registerItem("raw_tungsten", () -> new Item(new Properties()));
   public static final RegistryObject<Item> TUNGSTEN_INGOT = registerItem("tungsten_ingot", () -> new Item(new Properties()));
   public static final RegistryObject<Item> MITHRIL_INGOT = registerItem("mithril_ingot", () -> new Item(new Properties()));
   public static final RegistryObject<Item> NETHERITE_NUGGET = registerItem("netherite_nugget", () -> new Item(new Properties()));
   public static final RegistryObject<Item> SAPPHIRE = registerItem("sapphire", () -> new Item(new Properties()));
   public static final RegistryObject<Item> CUT_SAPPHIRE = registerItem("cut_sapphire", () -> new Item(new Properties()));
   public static final RegistryObject<Item> GEM_SAW = registerItem("gem_saw", () -> new GemSawItem(new Properties().durability(100)));
   public static final RegistryObject<Item> NETHERITE_GEMSTONE_SAW = registerItem(
      "netherite_gemstone_saw", () -> new GemSawItem(new Properties().durability(500))
   );
   public static final RegistryObject<Item> COPPER_HELMET = registerItem(
      "copper_helmet", () -> new ArmorItem(ModArmorMaterial.COPPER.getHolder().orElseThrow(), Type.HELMET, new Properties().durability(Type.HELMET.getDurability(10)))
   );
   public static final RegistryObject<Item> COPPER_CHESTPLATE = registerItem(
      "copper_chestplate", () -> new ArmorItem(ModArmorMaterial.COPPER.getHolder().orElseThrow(), Type.CHESTPLATE, new Properties().durability(Type.CHESTPLATE.getDurability(10)))
   );
   public static final RegistryObject<Item> COPPER_LEGGINGS = registerItem(
      "copper_leggings", () -> new ArmorItem(ModArmorMaterial.COPPER.getHolder().orElseThrow(), Type.LEGGINGS, new Properties().durability(Type.LEGGINGS.getDurability(10)))
   );
   public static final RegistryObject<Item> COPPER_BOOTS = registerItem(
      "copper_boots", () -> new ArmorItem(ModArmorMaterial.COPPER.getHolder().orElseThrow(), Type.BOOTS, new Properties().durability(Type.BOOTS.getDurability(10)))
   );
   public static final RegistryObject<Item> COPPER_SWORD = registerItem("copper_sword", () -> new SwordItem(ModItemTier.COPPER, new Properties().attributes(SwordItem.createAttributes(ModItemTier.COPPER, 3, -2.4F))));
   public static final RegistryObject<Item> COPPER_PICKAXE = registerItem(
      "copper_pickaxe", () -> new PickaxeItem(ModItemTier.COPPER, new Properties().attributes(PickaxeItem.createAttributes(ModItemTier.COPPER, 1, -2.8F)))
   );
   public static final RegistryObject<Item> COPPER_SHOVEL = registerItem(
      "copper_shovel", () -> new ShovelItem(ModItemTier.COPPER, new Properties().attributes(ShovelItem.createAttributes(ModItemTier.COPPER, 1.5F, -3.0F)))
   );
   public static final RegistryObject<Item> COPPER_HOE = registerItem("copper_hoe", () -> new HoeItem(ModItemTier.COPPER, new Properties().attributes(HoeItem.createAttributes(ModItemTier.COPPER, -2, -1.0F))));
   public static final RegistryObject<Item> COPPER_AXE = registerItem("copper_axe", () -> new AxeItem(ModItemTier.COPPER, new Properties().attributes(AxeItem.createAttributes(ModItemTier.COPPER, 6.0F, -3.1F))));
   public static final RegistryObject<Item> NETHERITE_FILTER = registerItem("netherite_filter", () -> new Item(new Properties().stacksTo(1).durability(167)));
   public static final RegistryObject<Item> GOLDEN_FILTER = registerItem("gold_filter", () -> new Item(new Properties().stacksTo(1).durability(30)));
   public static final RegistryObject<Item> COPPER_FILTER = registerItem("copper_filter", () -> new Item(new Properties().stacksTo(1).durability(12)));

   public static final RegistryObject<Item> MANA_FLASK = registerItem("mana_flask", () -> new ManaBottle(new Properties().stacksTo(16)));
   public static final RegistryObject<Item> EMPTY_FLASK = registerItem("empty_flask", () -> new Item(new Properties().stacksTo(64)));

   private static RegistryObject<Item> registerItem(String name, Supplier<Item> item) {
      return ITEMS.register(name, item);
   }

   public static void register(IEventBus eventBus) {
      ITEMS.register(eventBus);
   }
}
