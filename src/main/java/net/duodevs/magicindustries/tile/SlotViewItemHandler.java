package net.duodevs.magicindustries.tile;

import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/** Maps a one-slot sided capability view to one backing inventory slot. */
public final class SlotViewItemHandler implements IItemHandler {
    private final IItemHandler delegate;
    private final int backingSlot;
    private final boolean allowInsert;
    private final boolean allowExtract;
    private final boolean bucketOnlyExtract;

    public SlotViewItemHandler(IItemHandler delegate, int backingSlot, boolean allowInsert, boolean allowExtract, boolean bucketOnlyExtract) {
        this.delegate=delegate; this.backingSlot=backingSlot; this.allowInsert=allowInsert; this.allowExtract=allowExtract; this.bucketOnlyExtract=bucketOnlyExtract;
    }
    @Override public int getSlots(){return 1;}
    @Nonnull @Override public ItemStack getStackInSlot(int slot){return slot==0?delegate.getStackInSlot(backingSlot):ItemStack.EMPTY;}
    @Nonnull @Override public ItemStack insertItem(int slot,@Nonnull ItemStack stack,boolean simulate){return slot==0&&allowInsert?delegate.insertItem(backingSlot,stack,simulate):stack;}
    @Nonnull @Override public ItemStack extractItem(int slot,int amount,boolean simulate){
        if(slot!=0||!allowExtract) return ItemStack.EMPTY;
        if(bucketOnlyExtract && delegate.getStackInSlot(backingSlot).getItem()!=net.minecraft.init.Items.BUCKET) return ItemStack.EMPTY;
        return delegate.extractItem(backingSlot,amount,simulate);
    }
    @Override public int getSlotLimit(int slot){return slot==0?delegate.getSlotLimit(backingSlot):0;}
    @Override public boolean isItemValid(int slot, @Nonnull ItemStack stack){
        return slot==0 && allowInsert && delegate.isItemValid(backingSlot, stack);
    }
}
