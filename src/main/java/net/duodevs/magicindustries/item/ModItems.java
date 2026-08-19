package net.duodevs.magicindustries.item;

import java.util.function.Supplier;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.item.custom.GemSawItem;
import net.duodevs.magicindustries.item.custom.ManaBottle;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "magicindustries");
   public static final RegistryObject<Item> MANA_WATER_BUCKET = ITEMS.register(
      "mana_water_bucket", () -> new BucketItem(ModFluids.SOURCE_MANA_WATER, new Properties().setId(ITEMS.key("mana_water_bucket")).stacksTo(1).craftRemainder(Items.BUCKET))
   );
   public static final RegistryObject<Item> COPPER_INGOT = registerItem("copper_ingot", () -> new Item(new Properties().setId(ITEMS.key("copper_ingot"))));
   public static final RegistryObject<Item> RAW_TUNGSTEN = registerItem("raw_tungsten", () -> new Item(new Properties().setId(ITEMS.key("raw_tungsten"))));
   public static final RegistryObject<Item> TUNGSTEN_INGOT = registerItem("tungsten_ingot", () -> new Item(new Properties().setId(ITEMS.key("tungsten_ingot"))));
   public static final RegistryObject<Item> MITHRIL_INGOT = registerItem("mithril_ingot", () -> new Item(new Properties().setId(ITEMS.key("mithril_ingot"))));
   public static final RegistryObject<Item> NETHERITE_NUGGET = registerItem("netherite_nugget", () -> new Item(new Properties().setId(ITEMS.key("netherite_nugget"))));
   public static final RegistryObject<Item> SAPPHIRE = registerItem("sapphire", () -> new Item(new Properties().setId(ITEMS.key("sapphire"))));
   public static final RegistryObject<Item> CUT_SAPPHIRE = registerItem("cut_sapphire", () -> new Item(new Properties().setId(ITEMS.key("cut_sapphire"))));
   public static final RegistryObject<Item> GEM_SAW = registerItem("gem_saw", () -> new GemSawItem(new Properties().setId(ITEMS.key("gem_saw")).durability(100)));
   public static final RegistryObject<Item> NETHERITE_GEMSTONE_SAW = registerItem(
      "netherite_gemstone_saw", () -> new GemSawItem(new Properties().setId(ITEMS.key("netherite_gemstone_saw")).durability(500))
   );
   public static final RegistryObject<Item> COPPER_HELMET = registerItem(
      "copper_helmet", () -> new Item(new Properties().setId(ITEMS.key("copper_helmet")).humanoidArmor(ModArmorMaterial.COPPER, ArmorType.HELMET))
   );
   public static final RegistryObject<Item> COPPER_CHESTPLATE = registerItem(
      "copper_chestplate", () -> new Item(new Properties().setId(ITEMS.key("copper_chestplate")).humanoidArmor(ModArmorMaterial.COPPER, ArmorType.CHESTPLATE))
   );
   public static final RegistryObject<Item> COPPER_LEGGINGS = registerItem(
      "copper_leggings", () -> new Item(new Properties().setId(ITEMS.key("copper_leggings")).humanoidArmor(ModArmorMaterial.COPPER, ArmorType.LEGGINGS))
   );
   public static final RegistryObject<Item> COPPER_BOOTS = registerItem(
      "copper_boots", () -> new Item(new Properties().setId(ITEMS.key("copper_boots")).humanoidArmor(ModArmorMaterial.COPPER, ArmorType.BOOTS))
   );
   public static final RegistryObject<Item> COPPER_SWORD = registerItem("copper_sword", () -> new Item(new Properties().setId(ITEMS.key("copper_sword")).sword(ModItemTier.COPPER, 3, -2.4F)));
   public static final RegistryObject<Item> COPPER_PICKAXE = registerItem(
      "copper_pickaxe", () -> new Item(new Properties().setId(ITEMS.key("copper_pickaxe")).pickaxe(ModItemTier.COPPER, 1, -2.8F))
   );
   public static final RegistryObject<Item> COPPER_SHOVEL = registerItem(
      "copper_shovel", () -> new ShovelItem(ModItemTier.COPPER, 1.5F, -3.0F, new Properties().setId(ITEMS.key("copper_shovel")))
   );
   public static final RegistryObject<Item> COPPER_HOE = registerItem("copper_hoe", () -> new HoeItem(ModItemTier.COPPER, -2, -1.0F, new Properties().setId(ITEMS.key("copper_hoe"))));
   public static final RegistryObject<Item> COPPER_AXE = registerItem("copper_axe", () -> new AxeItem(ModItemTier.COPPER, 6.0F, -3.1F, new Properties().setId(ITEMS.key("copper_axe"))));
   public static final RegistryObject<Item> NETHERITE_FILTER = registerItem("netherite_filter", () -> new Item(new Properties().setId(ITEMS.key("netherite_filter")).stacksTo(1).durability(167)));
   public static final RegistryObject<Item> GOLDEN_FILTER = registerItem("gold_filter", () -> new Item(new Properties().setId(ITEMS.key("gold_filter")).stacksTo(1).durability(30)));
   public static final RegistryObject<Item> COPPER_FILTER = registerItem("copper_filter", () -> new Item(new Properties().setId(ITEMS.key("copper_filter")).stacksTo(1).durability(12)));

   public static final RegistryObject<Item> MANA_FLASK = registerItem("mana_flask", () -> new ManaBottle(new Properties().setId(ITEMS.key("mana_flask")).stacksTo(16).component(DataComponents.CONSUMABLE, Consumable.builder().consumeSeconds(0.75F).animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).build())));
   public static final RegistryObject<Item> EMPTY_FLASK = registerItem("empty_flask", () -> new Item(new Properties().setId(ITEMS.key("empty_flask")).stacksTo(64)));

   private static RegistryObject<Item> registerItem(String name, Supplier<Item> item) {
      return ITEMS.register(name, item);
   }

   public static void register(BusGroup eventBus) {
      ITEMS.register(eventBus);
   }
}
