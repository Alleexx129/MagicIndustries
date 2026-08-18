package net.duodevs.magicindustries.item;

import net.duodevs.magicindustries.capability.ManaCapability;
import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.network.ModNetwork;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemManaFlask extends Item {
    public ItemManaFlask() {
        setMaxStackSize(16);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) {
            return stack;
        }

        EntityPlayer player = (EntityPlayer) entity;
        if (!world.isRemote) {
            ManaCapability.get(player).ifPresent(mana -> {
                mana.addMana(4);
                if (player instanceof EntityPlayerMP) {
                    ModNetwork.syncMana((EntityPlayerMP) player);
                }
            });
        }

        player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
            ItemStack empty = new ItemStack(ModContent.EMPTY_FLASK);
            if (stack.isEmpty()) {
                return empty;
            }
            if (!player.inventory.addItemStackToInventory(empty)) {
                player.dropItem(empty, false);
            }
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 15;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
