package net.duodevs.magicindustries.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemGemSaw extends Item {
    public ItemGemSaw(int durability) {
        setMaxStackSize(1);
        setMaxDamage(durability);
        setNoRepair();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        int nextDamage = copy.getItemDamage() + 1;
        if (nextDamage >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        copy.setItemDamage(nextDamage);
        return copy;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }
}
