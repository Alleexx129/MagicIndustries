package net.duodevs.magicindustries.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class SapphireCharm extends Item implements ICurioItem {
   public SapphireCharm(Properties properties) {
      super(properties.stacksTo(1));
   }

   public void curioTick(SlotContext slotContext, ItemStack stack) {
      slotContext.entity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1, true, false));
   }

   public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
      slotContext.entity().removeEffect(MobEffects.MOVEMENT_SPEED);
   }
}
