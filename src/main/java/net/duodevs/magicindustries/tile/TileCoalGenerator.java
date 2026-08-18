package net.duodevs.magicindustries.tile;

import javax.annotation.Nullable;
import net.duodevs.magicindustries.util.ModEnergyStorage;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCoalGenerator extends TileEntity implements ITickable {
    public static final int CAPACITY = 69000;
    public static final int MAX_EXTRACT = 256;
    public static final int MAX_PROGRESS = 100;

    private int progress;
    private final ValidatingItemStackHandler items = new ValidatingItemStackHandler(3) {
        @Override protected void onContentsChanged(int slot) { markAndSync(); }
        @Override public boolean isItemValid(int slot, ItemStack stack) { return slot != 0 || TileEntityFurnace.getItemBurnTime(stack) > 0; }
    };
    private final ModEnergyStorage energy = new ModEnergyStorage(CAPACITY, 0, MAX_EXTRACT, this::markAndSync);
    private final IItemHandler fuelInput = new SlotViewItemHandler(items, 0, true, false, false);
    private final IItemHandler bucketOutput = new SlotViewItemHandler(items, 0, false, true, true);

    @Override public void update() {
        if (world == null || world.isRemote || energy.getEnergyStored() >= CAPACITY) return;
        ItemStack fuel = items.getStackInSlot(0);
        if (fuel.isEmpty()) return;

        int burnTime = TileEntityFurnace.getItemBurnTime(fuel);
        if (fuel.getItem() == Items.LAVA_BUCKET) burnTime = 20000;
        if (burnTime <= 0) return;

        double scalingFactor = Math.log(burnTime + 1.0D) / Math.log(1601.0D);
        progress += scalingFactor * (100.0D / MAX_PROGRESS);
        markAndSync();

        if (progress >= MAX_PROGRESS) {
            if (fuel.getItem() == Items.LAVA_BUCKET) {
                items.setStackInSlot(0, new ItemStack(Items.BUCKET));
                energy.addEnergy(12500);
            } else {
                // Preserve the shipped 1.20.1 operation order exactly: shrink first, then query the remaining stack's burn time.
                fuel.shrink(1);
                int remainingBurnTime = TileEntityFurnace.getItemBurnTime(items.getStackInSlot(0));
                energy.addEnergy(1000 * (remainingBurnTime / 1600));
            }
            progress = 0;
            markAndSync();
        }
    }

    public ItemStackHandler getItems() { return items; }
    public IEnergyStorage getEnergyStorage() { return energy; }
    public int getProgress() { return progress; }
    public int getMaxProgress() { return MAX_PROGRESS; }
    public void setEnergyLevel(int value) { energy.setEnergy(value); }

    @Override public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return true;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return (T) energy;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) return (T) items;
            if (facing == EnumFacing.DOWN) return (T) bucketOutput;
            return (T) fuelInput; // UP and all four horizontal sides
        }
        return super.getCapability(capability, facing);
    }

    @Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("inventory", items.serializeNBT());
        tag.setInteger("coal_generator.energy", energy.getEnergyStored());
        tag.setInteger("coal_generator.progress", progress);
        return tag;
    }
    @Override public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("inventory")) items.deserializeNBT(tag.getCompoundTag("inventory"));
        energy.setEnergySilently(tag.hasKey("coal_generator.energy") ? tag.getInteger("coal_generator.energy") : tag.getInteger("energy"));
        progress = tag.hasKey("coal_generator.progress") ? tag.getInteger("coal_generator.progress") : tag.getInteger("progress");
    }
    public void markAndSync() {
        markDirty();
        if (world != null && !world.isRemote) world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }
    @Override public NBTTagCompound getUpdateTag() { return writeToNBT(new NBTTagCompound()); }
    @Override public SPacketUpdateTileEntity getUpdatePacket() { return new SPacketUpdateTileEntity(pos, 1, getUpdateTag()); }
    @Override public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) { readFromNBT(pkt.getNbtCompound()); }
}
