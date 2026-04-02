package net.duodevs.magicindustries.item;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public enum ModArmorMaterial implements ArmorMaterial {
   COPPER(
      "copper",
      10,
      new int[]{1, 3, 4, 2},
      12,
      SoundEvents.ARMOR_EQUIP_IRON,
      0.0F,
      0.0F,
      () -> Ingredient.of(new ItemLike[]{ModItems.COPPER_INGOT.get()})
   );

   private static final int[] DURABILITY_PER_SLOT = new int[]{13, 15, 16, 11};
   private final String name;
   private final int durabilityMultiplier;
   private final int[] slotProtections;
   private final int enchantmentValue;
   private final SoundEvent sound;
   private final float toughness;
   private final float knockbackResistance;
   private final LazyLoadedValue<Ingredient> repairIngredient;

   private ModArmorMaterial(
      String name,
      int durabilityMultiplier,
      int[] slotProtections,
      int enchantmentValue,
      SoundEvent sound,
      float toughness,
      float knockbackResistance,
      Supplier<Ingredient> repairIngredient
   ) {
      this.name = name;
      this.durabilityMultiplier = durabilityMultiplier;
      this.slotProtections = slotProtections;
      this.enchantmentValue = enchantmentValue;
      this.sound = sound;
      this.toughness = toughness;
      this.knockbackResistance = knockbackResistance;
      this.repairIngredient = new LazyLoadedValue(repairIngredient);
   }

   public int getDurabilityForType(Type type) {
      return DURABILITY_PER_SLOT[type.getSlot().getIndex()] * this.durabilityMultiplier;
   }

   public int getDefenseForType(Type type) {
      return this.slotProtections[type.getSlot().getIndex()];
   }

   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   public SoundEvent getEquipSound() {
      return this.sound;
   }

   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }

   public String getName() {
      return "magicindustries:" + this.name;
   }

   public float getToughness() {
      return this.toughness;
   }

   public float getKnockbackResistance() {
      return this.knockbackResistance;
   }
}
