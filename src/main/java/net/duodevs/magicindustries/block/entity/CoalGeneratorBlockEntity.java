package net.duodevs.magicindustries.block.entity;

import net.duodevs.magicindustries.item.ModItems;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.networking.packet.EnergySyncS2CPacket;
import net.duodevs.magicindustries.screen.CoalGeneratorMenu;
import net.duodevs.magicindustries.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoalGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            CoalGeneratorBlockEntity.this.setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot == 0 ? stack.getBurnTime(RecipeType.SMELTING) > 0 : super.isItemValid(slot, stack);
        }
    };

    private final IItemHandler fuelInputHandler = new IItemHandler() {
        @Override public int getSlots() { return 1; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) { return itemHandler.getStackInSlot(0); }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack.getBurnTime(RecipeType.SMELTING) > 0 ? itemHandler.insertItem(0, stack, simulate) : stack;
        }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) { return ItemStack.EMPTY; }
        @Override public int getSlotLimit(int slot) { return itemHandler.getSlotLimit(0); }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return stack.getBurnTime(RecipeType.SMELTING) > 0; }
    };

    private final IItemHandler bucketOutputHandler = new IItemHandler() {
        @Override public int getSlots() { return 1; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) { return itemHandler.getStackInSlot(0); }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) { return stack; }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack inSlot = itemHandler.getStackInSlot(0);
            return inSlot.is(Items.BUCKET) ? itemHandler.extractItem(0, Math.min(1, amount), simulate) : ItemStack.EMPTY;
        }
        @Override public int getSlotLimit(int slot) { return itemHandler.getSlotLimit(0); }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return false; }
    };

    private final ModEnergyStorage energyStorage = new ModEnergyStorage(69000, 0, 256) {
        @Override
        public void onEnergyChanged() {
            CoalGeneratorBlockEntity.this.setChanged();
            if (CoalGeneratorBlockEntity.this.level != null && !CoalGeneratorBlockEntity.this.level.isClientSide()) {
                ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, CoalGeneratorBlockEntity.this.getBlockPos()));
            }
        }
    };

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COAL_GENERATOR.get(), pos, state);
        this.data = new ContainerData() {
            @Override public int get(int index) { return index == 0 ? progress : index == 1 ? maxProgress : 0; }
            @Override public void set(int index, int value) { if (index == 0) progress = value; else if (index == 1) maxProgress = value; }
            @Override public int getCount() { return 2; }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Heat Generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            ModMessages.sendToPlayer(new EnergySyncS2CPacket(energyStorage.getEnergyStored(), getBlockPos()), serverPlayer);
        }
        return new CoalGeneratorMenu(id, inventory, this, data);
    }

    public IItemHandler getInternalItemHandler() { return itemHandler; }
    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null) return itemHandler;
        return side == Direction.DOWN ? bucketOutputHandler : fuelInputHandler;
    }
    public IEnergyStorage getEnergyStorage() { return energyStorage; }
    public void setEnergyLevel(int energy) { energyStorage.setEnergy(energy); }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.put("inventory", itemHandler.serializeNBT(registries));
        nbt.putInt("coal_generator.progress", progress);
        nbt.putInt("coal_generator.energy", energyStorage.getEnergyStored());
        super.saveAdditional(nbt, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        itemHandler.deserializeNBT(registries, nbt.getCompound("inventory"));
        progress = nbt.getInt("coal_generator.progress");
        energyStorage.setEnergy(nbt.getInt("coal_generator.energy"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) inventory.setItem(i, itemHandler.getStackInSlot(i));
        Containers.dropContents(level, worldPosition, inventory);
    }

    public static boolean hasFullEnergy(CoalGeneratorBlockEntity entity) {
        return entity.energyStorage.getMaxEnergyStored() <= entity.energyStorage.getEnergyStored();
    }

    public static boolean hasEnoughItems(CoalGeneratorBlockEntity entity) {
        return !entity.itemHandler.getStackInSlot(0).isEmpty();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CoalGeneratorBlockEntity entity) {
        if (level.isClientSide() || hasFullEnergy(entity) || !hasEnoughItems(entity)) return;

        ItemStack fuel = entity.itemHandler.getStackInSlot(0);
        int burnTime = fuel.is(Items.LAVA_BUCKET) ? 20000 : fuel.getBurnTime(RecipeType.SMELTING);
        if (burnTime <= 0) return;

        double scalingFactor = Math.log(burnTime + 1.0D) / Math.log(1601.0D);
        entity.progress = (int) (entity.progress + scalingFactor * (entity.maxProgress / 100.0D));
        setChanged(level, pos, state);

        if (entity.progress >= entity.maxProgress) {
            if (fuel.is(Items.LAVA_BUCKET)) {
                entity.itemHandler.setStackInSlot(0, new ItemStack(Items.BUCKET));
                entity.energyStorage.setEnergy(entity.energyStorage.getEnergyStored() + 12500);
            } else {
                entity.itemHandler.extractItem(0, 1, false);
                int generated = Math.max(1, 1000 * burnTime / 1600);
                entity.energyStorage.setEnergy(entity.energyStorage.getEnergyStored() + generated);
            }
            if (entity.energyStorage.getEnergyStored() > entity.energyStorage.getMaxEnergyStored()) {
                entity.energyStorage.setEnergy(entity.energyStorage.getMaxEnergyStored());
            }
            entity.resetProgress();
        }
    }

    private void resetProgress() {
        setChanged();
        progress = 0;
    }

    // Kept for compatibility with older call sites/debugging.
    private static boolean hasGemInFirstSlot(CoalGeneratorBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(0).is(ModItems.SAPPHIRE.get());
    }
}
