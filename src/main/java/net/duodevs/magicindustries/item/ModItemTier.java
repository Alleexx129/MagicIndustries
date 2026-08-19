package net.duodevs.magicindustries.item;

import java.util.function.Supplier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public enum ModItemTier implements Tier {
   COPPER(BlockTags.INCORRECT_FOR_IRON_TOOL, 200, 5.0F, 5.0F, 10, () -> Ingredient.of(ModItems.COPPER_INGOT.get()));

   private final TagKey<Block> incorrectBlocksForDrops;
   private final int uses;
   private final float speed;
   private final float damage;
   private final int enchantmentValue;
   private final Supplier<Ingredient> repairIngredient;

   private ModItemTier(
      TagKey<Block> incorrectBlocksForDrops,
      int uses,
      float speed,
      float damage,
      int enchantmentValue,
      Supplier<Ingredient> repairIngredient
   ) {
      this.incorrectBlocksForDrops = incorrectBlocksForDrops;
      this.uses = uses;
      this.speed = speed;
      this.damage = damage;
      this.enchantmentValue = enchantmentValue;
      this.repairIngredient = repairIngredient;
   }

   public int getUses() {
      return this.uses;
   }

   public float getSpeed() {
      return this.speed;
   }

   public float getAttackDamageBonus() {
      return this.damage;
   }

   public TagKey<Block> getIncorrectBlocksForDrops() {
      return this.incorrectBlocksForDrops;
   }

   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }
}
