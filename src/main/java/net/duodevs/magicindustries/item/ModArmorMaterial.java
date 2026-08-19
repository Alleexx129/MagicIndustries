package net.duodevs.magicindustries.item;

import java.util.Map;
import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class ModArmorMaterial {
   public static final TagKey<Item> COPPER_REPAIR = TagKey.create(
      Registries.ITEM, Identifier.fromNamespaceAndPath(MagicIndustries.MOD_ID, "copper_repair")
   );
   public static final ResourceKey<EquipmentAsset> COPPER_ASSET = ResourceKey.create(
      EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MagicIndustries.MOD_ID, "copper")
   );

   public static final ArmorMaterial COPPER = new ArmorMaterial(
      10,
      Map.of(
         ArmorType.BOOTS, 1,
         ArmorType.LEGGINGS, 3,
         ArmorType.CHESTPLATE, 4,
         ArmorType.HELMET, 2,
         ArmorType.BODY, 4
      ),
      12,
      SoundEvents.ARMOR_EQUIP_IRON,
      0.0F,
      0.0F,
      COPPER_REPAIR,
      COPPER_ASSET
   );
}
