package net.duodevs.magicindustries.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;

public class GemSawItem extends Item {
   public GemSawItem(Properties properties) {
      super(properties);
   }

   public boolean hasCraftingRemainingItem(ItemStack stack) {
      return true;
   }

   public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
      ItemStack damagedStack = itemStack.copy();
      damagedStack.setDamageValue(damagedStack.getDamageValue() + 1);
      return damagedStack.getDamageValue() >= damagedStack.getMaxDamage() ? ItemStack.EMPTY : damagedStack;
   }

   public boolean isDamageable(ItemStack stack) {
      return true;
   }
}
