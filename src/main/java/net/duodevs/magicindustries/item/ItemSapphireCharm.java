package net.duodevs.magicindustries.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/** Curios charm -> Baubles CHARM, preserving the original Speed II behavior. */
public class ItemSapphireCharm extends Item implements IBauble {
    public ItemSapphireCharm() { setMaxStackSize(1); }
    @Override public BaubleType getBaubleType(ItemStack itemstack) { return BaubleType.CHARM; }
    @Override public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20, 1, true, false));
    }
    @Override public void onUnequipped(ItemStack itemstack, EntityLivingBase player) { player.removePotionEffect(MobEffects.SPEED); }
    @Override public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) { return true; }
}
