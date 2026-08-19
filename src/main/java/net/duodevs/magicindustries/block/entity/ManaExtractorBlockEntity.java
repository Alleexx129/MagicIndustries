package net.duodevs.magicindustries.block.entity;

import java.util.Map;
import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.block.custom.ManaExtractorBlock;
import net.duodevs.magicindustries.fluid.CombinedFluidHandler;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.networking.packet.FluidSyncS2CPacket;
import net.duodevs.magicindustries.screen.ManaExtractorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaExtractorBlockEntity extends BlockEntity implements MenuProvider {
    private static final net.minecraft.tags.TagKey<net.minecraft.world.item.Item> MANA_FILTERS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "mana_filters"));

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            ManaExtractorBlockEntity.this.setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.is(MANA_FILTERS);
                case 1 -> stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final FluidTank fluidTank = new FluidTank(64000, stack -> stack.is(Fluids.WATER)) {
        @Override protected void onContentsChanged() { onFluidChanged(); }
    };
    private final FluidTank manaTank = new FluidTank(100000, stack -> stack.is(ModFluids.SOURCE_MANA_WATER.get()) || stack.is(ModFluids.FLOWING_MANA_WATER.get())) {
        @Override protected void onContentsChanged() { onFluidChanged(); }
    };
    private final IFluidHandler combinedFluidHandler = new CombinedFluidHandler(fluidTank, manaTank);

    private final Map<Direction, WrappedHandler> directionWrappedHandlerMap = Map.of(
            Direction.DOWN, new WrappedHandler(itemHandler, i -> i == 2, (i, s) -> false),
            Direction.NORTH, new WrappedHandler(itemHandler, i -> i == 1, (i, s) -> itemHandler.isItemValid(1, s)),
            Direction.SOUTH, new WrappedHandler(itemHandler, i -> i == 2, (i, s) -> false),
            Direction.EAST, new WrappedHandler(itemHandler, i -> i == 1, (i, s) -> itemHandler.isItemValid(1, s)),
            Direction.WEST, new WrappedHandler(itemHandler, i -> i == 0 || i == 1,
                    (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s))
    );

    protected final ContainerData data;
    private int progress;
    private int maxProgress = 100;

    public ManaExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MANA_EXTRACTOR.get(), pos, state);
        this.data = new ContainerData() {
            @Override public int get(int index) { return index == 0 ? progress : index == 1 ? maxProgress : 0; }
            @Override public void set(int index, int value) { if (index == 0) progress = value; else if (index == 1) maxProgress = value; }
            @Override public int getCount() { return 2; }
        };
    }

    private void onFluidChanged() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            ModMessages.sendToClients(new FluidSyncS2CPacket(getFluidStack(), getFluidStackMana(), worldPosition));
        }
    }

    public void setFluid(FluidStack stack) { fluidTank.setFluid(stack); }
    public void setFluidMana(FluidStack stack) { manaTank.setFluid(stack); }
    public FluidStack getFluidStack() { return fluidTank.getFluid().copy(); }
    public FluidStack getFluidStackMana() { return manaTank.getFluid().copy(); }
    public IItemHandler getInternalItemHandler() { return itemHandler; }
    public IFluidHandler getFluidHandler(@Nullable Direction side) { return combinedFluidHandler; }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null) return itemHandler;
        WrappedHandler direct = directionWrappedHandlerMap.get(side);
        if (direct == null) return itemHandler;

        Direction localDir = getBlockState().getValue(ManaExtractorBlock.FACING);
        if (side == Direction.UP || side == Direction.DOWN) return direct;
        return switch (localDir) {
            case EAST -> directionWrappedHandlerMap.get(side.getClockWise());
            case SOUTH -> direct;
            case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise());
            default -> directionWrappedHandlerMap.get(side.getOpposite());
        };
    }

    @Override public Component getDisplayName() { return Component.literal("Mana Extractor"); }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            ModMessages.sendToPlayer(new FluidSyncS2CPacket(getFluidStack(), getFluidStackMana(), worldPosition), serverPlayer);
        }
        return new ManaExtractorMenu(id, inventory, this, data);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.put("inventory", itemHandler.serializeNBT(registries));
        nbt.putInt("mana_extractor.progress", progress);
        nbt.put("fluid_tank", fluidTank.writeToNBT(registries, new CompoundTag()));
        nbt.put("mana_tank", manaTank.writeToNBT(registries, new CompoundTag()));
        super.saveAdditional(nbt, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        itemHandler.deserializeNBT(registries, nbt.getCompound("inventory"));
        progress = nbt.getInt("mana_extractor.progress");
        fluidTank.readFromNBT(registries, nbt.getCompound("fluid_tank"));
        manaTank.readFromNBT(registries, nbt.getCompound("mana_tank"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) inventory.setItem(i, itemHandler.getStackInSlot(i));
        Containers.dropContents(level, worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaExtractorBlockEntity entity) {
        if (level.isClientSide()) return;

        if (hasFilterInFirstSlot(entity) && hasManaFlowerInRange(entity) && entity.fluidTank.getFluidAmount() >= 500) {
            entity.progress++;
            if (entity.progress >= entity.maxProgress) {
                entity.fluidTank.drain(500, FluidAction.EXECUTE);
                entity.resetProgress();
                ItemStack filter = entity.itemHandler.getStackInSlot(0);
                filter.setDamageValue(filter.getDamageValue() + 1);
                if (filter.getDamageValue() >= filter.getMaxDamage()) entity.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                entity.manaTank.fill(new FluidStack(ModFluids.SOURCE_MANA_WATER.get(), 250), FluidAction.EXECUTE);
            }
            setChanged(level, pos, state);
        } else {
            entity.progress = 0;
            setChanged(level, pos, state);
        }

        if (!entity.itemHandler.getStackInSlot(1).isEmpty()) transferItemFluidToFluidTank(entity);
    }

    private static void transferItemFluidToFluidTank(ManaExtractorBlockEntity entity) {
        ItemStack stack = entity.itemHandler.getStackInSlot(1);
        if (stack.is(Items.WATER_BUCKET) && entity.fluidTank.getSpace() >= 1000) {
            entity.fluidTank.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);
            entity.itemHandler.extractItem(1, 1, false);
            entity.itemHandler.insertItem(1, new ItemStack(Items.BUCKET), false);
        }
    }

    private static boolean hasFilterInFirstSlot(ManaExtractorBlockEntity entity) {
        return !entity.itemHandler.getStackInSlot(0).isEmpty();
    }

    private static boolean hasManaFlowerInRange(ManaExtractorBlockEntity entity) {
        BlockPos p = entity.getBlockPos();
        return entity.level.getBlockState(p.east()).is(ModBlocks.MANA_FLOWER.get())
                || entity.level.getBlockState(p.west()).is(ModBlocks.MANA_FLOWER.get())
                || entity.level.getBlockState(p.south()).is(ModBlocks.MANA_FLOWER.get())
                || entity.level.getBlockState(p.north()).is(ModBlocks.MANA_FLOWER.get());
    }

    private void resetProgress() { progress = 0; }
}
