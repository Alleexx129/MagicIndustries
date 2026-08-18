package net.duodevs.magicindustries.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiPredicate;
import java.util.function.IntPredicate;

/** 1.12 equivalent of the original WrappedHandler sided capability wrapper. */
public final class SidedItemHandler implements IItemHandler {
    private final IItemHandler delegate;
    private final IntPredicate canExtract;
    private final BiPredicate<Integer, ItemStack> canInsert;

    public SidedItemHandler(IItemHandler delegate, IntPredicate canExtract, BiPredicate<Integer, ItemStack> canInsert) {
        this.delegate = delegate;
        this.canExtract = canExtract;
        this.canInsert = canInsert;
    }

    @Override public int getSlots() { return delegate.getSlots(); }
    @Override public ItemStack getStackInSlot(int slot) { return delegate.getStackInSlot(slot); }
    @Override public int getSlotLimit(int slot) { return delegate.getSlotLimit(slot); }
    @Override public boolean isItemValid(int slot, ItemStack stack) {
        return canInsert.test(slot, stack) && delegate.isItemValid(slot, stack);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return canInsert.test(slot, stack) ? delegate.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return canExtract.test(slot) ? delegate.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
}
