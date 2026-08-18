package net.duodevs.magicindustries.tile;

import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/** 1.12 ItemStackHandler does not call isItemValid from insertItem; modern Forge does. */
public abstract class ValidatingItemStackHandler extends ItemStackHandler {
    public ValidatingItemStackHandler(int size) { super(size); }
    @Nonnull @Override public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
    }
}
