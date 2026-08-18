package net.duodevs.magicindustries.tile;

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.duodevs.magicindustries.block.BlockMachine;
import net.duodevs.magicindustries.fluid.CombinedFluidHandler;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.init.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileManaExtractor extends TileEntity implements ITickable {
    public static final int WATER_CAPACITY = 64000;
    public static final int MANA_CAPACITY = 100000;
    public static final int WATER_PER_OPERATION = 500;
    public static final int MANA_PER_OPERATION = 250;
    public static final int MAX_PROGRESS = 100;

    private int progress;
    private final ValidatingItemStackHandler items = new ValidatingItemStackHandler(3) {
        @Override protected void onContentsChanged(int slot) { markAndSync(); }
        @Override public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0) return isManaFilter(stack);
            if (slot == 1) return stack.getItem() == Items.WATER_BUCKET || stack.getItem() == Items.BUCKET;
            return true;
        }
    };
    private final FluidTank waterTank = new FluidTank(WATER_CAPACITY) {
        @Override public boolean canFillFluidType(FluidStack fluid) { return fluid != null && fluid.getFluid() == FluidRegistry.WATER; }
        @Override protected void onContentsChanged() { markAndSync(); }
    };
    private final FluidTank manaTank = new FluidTank(MANA_CAPACITY) {
        @Override public boolean canFillFluidType(FluidStack fluid) { return fluid != null && fluid.getFluid() == ModFluids.MANA_WATER; }
        @Override protected void onContentsChanged() { markAndSync(); }
    };
    private final IFluidHandler combined = new CombinedFluidHandler(waterTank, manaTank);
    private final Map<EnumFacing, IItemHandler> directionWrappedHandlerMap = new EnumMap<>(EnumFacing.class);

    public TileManaExtractor() {
        directionWrappedHandlerMap.put(EnumFacing.DOWN, new SidedItemHandler(items, s -> s == 2, (s, st) -> false));
        directionWrappedHandlerMap.put(EnumFacing.NORTH, new SidedItemHandler(items, s -> s == 1, (s, st) -> s == 1 && items.isItemValid(1, st)));
        directionWrappedHandlerMap.put(EnumFacing.SOUTH, new SidedItemHandler(items, s -> s == 2, (s, st) -> false));
        directionWrappedHandlerMap.put(EnumFacing.EAST, new SidedItemHandler(items, s -> s == 1, (s, st) -> s == 1 && items.isItemValid(1, st)));
        directionWrappedHandlerMap.put(EnumFacing.WEST, new SidedItemHandler(items, s -> s == 0 || s == 1,
            (s, st) -> (s == 0 || s == 1) && (items.isItemValid(0, st) || items.isItemValid(1, st))));
    }

    @Override public void update() {
        if (world == null || world.isRemote) return;
        ItemStack filter = items.getStackInSlot(0);
        if (!filter.isEmpty() && hasAdjacentManaFlower() && waterTank.getFluidAmount() >= WATER_PER_OPERATION) {
            progress++;
            if (progress >= MAX_PROGRESS) {
                waterTank.drain(WATER_PER_OPERATION, true);
                progress = 0;
                filter.setItemDamage(filter.getItemDamage() + 1);
                if (filter.getItemDamage() >= filter.getMaxDamage()) items.setStackInSlot(0, ItemStack.EMPTY);
                manaTank.fill(new FluidStack(ModFluids.MANA_WATER, MANA_PER_OPERATION), true);
            }
            markAndSync();
        } else if (progress != 0) {
            progress = 0;
            markAndSync();
        }
        emptyFluidContainerIntoWaterTank();
    }

    private void emptyFluidContainerIntoWaterTank() {
        ItemStack stack = items.getStackInSlot(1);
        if (stack.isEmpty()) return;
        FluidActionResult result = FluidUtil.tryEmptyContainer(stack, waterTank, Integer.MAX_VALUE, null, true);
        if (result.isSuccess()) items.setStackInSlot(1, result.getResult());
    }

    private boolean hasAdjacentManaFlower() {
        for (EnumFacing side : EnumFacing.HORIZONTALS) if (world.getBlockState(pos.offset(side)).getBlock() == ModContent.MANA_FLOWER) return true;
        return false;
    }

    public static boolean isManaFilter(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (int id : OreDictionary.getOreIDs(stack)) if ("manaFilters".equals(OreDictionary.getOreName(id))) return true;
        return stack.getItem() == ModContent.GOLD_FILTER || stack.getItem() == ModContent.COPPER_FILTER || stack.getItem() == ModContent.NETHERITE_FILTER;
    }

    private EnumFacing mappedSide(@Nullable EnumFacing side) {
        if (side == null) return null;
        if (side == EnumFacing.UP || side == EnumFacing.DOWN) return side;
        if (world == null) return side;
        IBlockState state = world.getBlockState(pos);
        if (!state.getPropertyKeys().contains(BlockMachine.FACING)) return side;
        EnumFacing facing = state.getValue(BlockMachine.FACING);
        if (facing == EnumFacing.EAST) return side.rotateY();
        if (facing == EnumFacing.SOUTH) return side;
        if (facing == EnumFacing.WEST) return side.rotateYCCW();
        return side.getOpposite(); // NORTH/default
    }

    public ItemStackHandler getItems() { return items; }
    public FluidTank getWaterTank() { return waterTank; }
    public FluidTank getManaTank() { return manaTank; }
    public int getProgress() { return progress; }
    public int getMaxProgress() { return MAX_PROGRESS; }
    public FluidStack getFluidStack() { return new FluidStack(FluidRegistry.WATER, waterTank.getFluidAmount()); }
    public FluidStack getFluidStackMana() { return new FluidStack(ModFluids.MANA_WATER, manaTank.getFluidAmount()); }
    public void setFluid(FluidStack fluid) { waterTank.setFluid(fluid); }
    public void setFluidMana(FluidStack fluid) { manaTank.setFluid(fluid); }

    @Override public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null) return true;
            EnumFacing mapped = mappedSide(side);
            return directionWrappedHandlerMap.containsKey(mapped);
        }
        return super.hasCapability(capability, side);
    }

    @SuppressWarnings("unchecked")
    @Override public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T) combined;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null) return (T) items;
            return (T) directionWrappedHandlerMap.get(mappedSide(side));
        }
        return super.getCapability(capability, side);
    }

    @Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("inventory", items.serializeNBT());
        tag.setTag("fluid_tank", waterTank.writeToNBT(new NBTTagCompound()));
        tag.setTag("mana_tank", manaTank.writeToNBT(new NBTTagCompound()));
        tag.setInteger("mana_extractor.progress", progress);
        return tag;
    }
    @Override public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("inventory")) items.deserializeNBT(tag.getCompoundTag("inventory"));
        if (tag.hasKey("fluid_tank")) waterTank.readFromNBT(tag.getCompoundTag("fluid_tank"));
        else if (tag.hasKey("waterTank")) waterTank.readFromNBT(tag.getCompoundTag("waterTank"));
        if (tag.hasKey("mana_tank")) manaTank.readFromNBT(tag.getCompoundTag("mana_tank"));
        else if (tag.hasKey("manaTank")) manaTank.readFromNBT(tag.getCompoundTag("manaTank"));
        progress = tag.hasKey("mana_extractor.progress") ? tag.getInteger("mana_extractor.progress") : tag.getInteger("progress");
    }
    public void markAndSync() {
        markDirty();
        if (world != null && !world.isRemote) world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }
    @Override public NBTTagCompound getUpdateTag() { return writeToNBT(new NBTTagCompound()); }
    @Override public SPacketUpdateTileEntity getUpdatePacket() { return new SPacketUpdateTileEntity(pos, 1, getUpdateTag()); }
    @Override public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) { readFromNBT(pkt.getNbtCompound()); }
}
