package net.duodevs.magicindustries.gui;

import net.duodevs.magicindustries.tile.TileManaExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerManaExtractor extends Container {
    public final TileManaExtractor tile;
    public ContainerManaExtractor(InventoryPlayer inv, TileManaExtractor tile) {
        this.tile=tile;
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addSlotToContainer(new SlotItemHandler(tile.getItems(),0,25,31)); // index 36
        addSlotToContainer(new SlotItemHandler(tile.getItems(),1,130,31)); // index 37
    }
    private void addPlayerInventory(InventoryPlayer inv) { for(int row=0;row<3;row++) for(int col=0;col<9;col++) addSlotToContainer(new Slot(inv,col+row*9+9,8+col*18,79+row*18)); }
    private void addPlayerHotbar(InventoryPlayer inv) { for(int col=0;col<9;col++) addSlotToContainer(new Slot(inv,col,8+col*18,137)); }
    @Override public boolean canInteractWith(EntityPlayer player) { return tile.getWorld()!=null && tile.getWorld().getTileEntity(tile.getPos())==tile && player.getDistanceSq(tile.getPos())<=64D; }
    @Override public ItemStack transferStackInSlot(EntityPlayer player,int index) {
        if(index<0||index>=inventorySlots.size()) return ItemStack.EMPTY;
        Slot slot=inventorySlots.get(index); if(slot==null||!slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack stack=slot.getStack(),copy=stack.copy();
        if(index<36) { if(!mergeItemStack(stack,36,38,false)) return ItemStack.EMPTY; }
        else if(index<38) { if(!mergeItemStack(stack,0,36,false)) return ItemStack.EMPTY; }
        else return ItemStack.EMPTY;
        if(stack.isEmpty()) slot.putStack(ItemStack.EMPTY); else slot.onSlotChanged();
        slot.onTake(player,stack); return copy;
    }
}
