package net.duodevs.magicindustries.block.entity;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.block.custom.ManaExtractorBlock;
import net.duodevs.magicindustries.fluid.CombinedFluidHandler;
import net.duodevs.magicindustries.fluid.ModFluids;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.networking.packet.FluidSyncS2CPacket;
import net.duodevs.magicindustries.recipe.ManaExtractorRecipe;
import net.duodevs.magicindustries.screen.ManaExtractorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaExtractorBlockEntity extends BlockEntity implements MenuProvider {
   private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
      protected void onContentsChanged(int slot) {
         ManaExtractorBlockEntity.this.setChanged();
      }

      public boolean isItemValid(int slot, @NotNull ItemStack stack) {
         return switch (slot) {
            case 0 -> stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "mana_filters")));
            case 1 -> stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
            default -> super.isItemValid(slot, stack);
         };
      }
   };
   private final FluidTank FLUID_TANK = new FluidTank(64000, stack -> stack.getFluid() == Fluids.WATER) {
      protected void onContentsChanged() {
         ManaExtractorBlockEntity.this.setChanged();
         if (!ManaExtractorBlockEntity.this.level.isClientSide()) {
            ModMessages.sendToClients(
               new FluidSyncS2CPacket(
                  new FluidStack(Fluids.WATER, ManaExtractorBlockEntity.this.FLUID_TANK.getFluidAmount()),
                  new FluidStack((Fluid)ModFluids.FLOWING_MANA_WATER.get(), ManaExtractorBlockEntity.this.FLUID_TANK_MANA.getFluidAmount()),
                  ManaExtractorBlockEntity.this.worldPosition
               )
            );
         }
      }

      public boolean isFluidValid(FluidStack stack) {
         return stack.getFluid() == Fluids.WATER;
      }
   };
   private final FluidTank FLUID_TANK_MANA = new FluidTank(100000, stack -> stack.getFluid() == ModFluids.FLOWING_MANA_WATER.get()) {
      protected void onContentsChanged() {
         ManaExtractorBlockEntity.this.setChanged();
         if (!ManaExtractorBlockEntity.this.level.isClientSide()) {
            ModMessages.sendToClients(
               new FluidSyncS2CPacket(
                  new FluidStack(Fluids.WATER, ManaExtractorBlockEntity.this.FLUID_TANK.getFluidAmount()),
                  new FluidStack((Fluid)ModFluids.FLOWING_MANA_WATER.get(), ManaExtractorBlockEntity.this.FLUID_TANK_MANA.getFluidAmount()),
                  ManaExtractorBlockEntity.this.worldPosition
               )
            );
         }
      }

      public boolean isFluidValid(FluidStack stack) {
         return stack.getFluid() == ModFluids.SOURCE_MANA_WATER.get();
      }
   };
   private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
   private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap = Map.of(
      Direction.DOWN,
      LazyOptional.of(() -> new WrappedHandler(this.itemHandler, i -> i == 2, (i, s) -> false)),
      Direction.NORTH,
      LazyOptional.of(() -> new WrappedHandler(this.itemHandler, index -> index == 1, (index, stack) -> this.itemHandler.isItemValid(1, stack))),
      Direction.SOUTH,
      LazyOptional.of(() -> new WrappedHandler(this.itemHandler, i -> i == 2, (i, s) -> false)),
      Direction.EAST,
      LazyOptional.of(() -> new WrappedHandler(this.itemHandler, i -> i == 1, (index, stack) -> this.itemHandler.isItemValid(1, stack))),
      Direction.WEST,
      LazyOptional.of(
         () -> new WrappedHandler(
               this.itemHandler,
               index -> index == 0 || index == 1,
               (index, stack) -> this.itemHandler.isItemValid(0, stack) || this.itemHandler.isItemValid(1, stack)
            )
      )
   );
   private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
   private LazyOptional<IFluidHandler> combinedFluidHandler = LazyOptional.empty();
   private LazyOptional<IFluidHandler> lazyFluidHandlerMana = LazyOptional.empty();
   protected final ContainerData data;
   private int progress = 0;
   private int maxProgress = 100;

   public void setFluid(FluidStack stack) {
      this.FLUID_TANK.setFluid(stack);
   }

   public void setFluidMana(FluidStack stack) {
      this.FLUID_TANK_MANA.setFluid(stack);
   }

   public FluidStack getFluidStack() {
      return new FluidStack(Fluids.WATER, this.FLUID_TANK.getFluidAmount());
   }

   public FluidStack getFluidStackMana() {
      return new FluidStack((Fluid)ModFluids.FLOWING_MANA_WATER.get(), this.FLUID_TANK_MANA.getFluidAmount());
   }

   public ManaExtractorBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)ModBlockEntities.MANA_EXTRACTOR.get(), pos, state);
      this.data = new ContainerData() {
         public int get(int index) {
            return switch (index) {
               case 0 -> ManaExtractorBlockEntity.this.progress;
               case 1 -> ManaExtractorBlockEntity.this.maxProgress;
               default -> 0;
            };
         }

         public void set(int index, int value) {
            switch (index) {
               case 0:
                  ManaExtractorBlockEntity.this.progress = value;
                  break;
               case 1:
                  ManaExtractorBlockEntity.this.maxProgress = value;
            }
         }

         public int getCount() {
            return 2;
         }
      };
   }

   public Component getDisplayName() {
      return Component.literal("Mana Extractor");
   }

   @Nullable
   public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
      ModMessages.sendToClients(new FluidSyncS2CPacket(this.getFluidStack(), this.getFluidStackMana(), this.worldPosition));
      return new ManaExtractorMenu(id, inventory, this, this.data);
   }

   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      if (cap == ForgeCapabilities.ITEM_HANDLER) {
         if (side == null) {
            return this.lazyItemHandler.cast();
         }

         if (this.directionWrappedHandlerMap.containsKey(side)) {
            Direction localDir = (Direction)this.getBlockState().getValue(ManaExtractorBlock.FACING);
            if (side != Direction.UP && side != Direction.DOWN) {
               return switch (localDir) {
                  case EAST -> this.directionWrappedHandlerMap.get(side.getClockWise()).cast();
                  case SOUTH -> this.directionWrappedHandlerMap.get(side).cast();
                  case WEST -> this.directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                  default -> this.directionWrappedHandlerMap.get(side.getOpposite()).cast();
               };
            }

            return this.directionWrappedHandlerMap.get(side).cast();
         }
      }

      return cap == ForgeCapabilities.FLUID_HANDLER ? this.combinedFluidHandler.cast() : super.getCapability(cap, side);
   }

   public void onLoad() {
      super.onLoad();
      this.lazyItemHandler = LazyOptional.of(() -> this.itemHandler);
      this.lazyFluidHandler = LazyOptional.of(() -> this.FLUID_TANK);
      this.lazyFluidHandlerMana = LazyOptional.of(() -> this.FLUID_TANK_MANA);
      this.combinedFluidHandler = LazyOptional.of(() -> new CombinedFluidHandler(this.FLUID_TANK, this.FLUID_TANK_MANA));
   }

   public void invalidateCaps() {
      super.invalidateCaps();
      this.lazyItemHandler.invalidate();
      this.lazyFluidHandler.invalidate();
      this.combinedFluidHandler.invalidate();
      this.lazyFluidHandlerMana.invalidate();
   }

   protected void saveAdditional(CompoundTag nbt) {
      nbt.put("inventory", this.itemHandler.serializeNBT());
      nbt.putInt("mana_extractor.progress", this.progress);
      nbt.put("fluid_tank", this.FLUID_TANK.writeToNBT(new CompoundTag()));
      nbt.put("mana_tank", this.FLUID_TANK_MANA.writeToNBT(new CompoundTag()));
      super.saveAdditional(nbt);
   }

   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
      this.progress = nbt.getInt("mana_extractor.progress");
      this.FLUID_TANK.readFromNBT(nbt.getCompound("fluid_tank"));
      this.FLUID_TANK_MANA.readFromNBT(nbt.getCompound("mana_tank"));
   }

   public void drops() {
      SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());

      for (int i = 0; i < this.itemHandler.getSlots(); i++) {
         inventory.setItem(i, this.itemHandler.getStackInSlot(i));
      }

      Containers.dropContents(this.level, this.worldPosition, inventory);
   }

   public static void tick(Level level, BlockPos pos, BlockState state, ManaExtractorBlockEntity pEntity) {
      if (!level.isClientSide()) {
         if (hasFilterInFirstSlot(pEntity) && hasManaFlowerInRange(pEntity)) {
            if (pEntity.getFluidStack().getAmount() >= 500) {
               pEntity.progress++;
               if (pEntity.progress >= pEntity.maxProgress) {
                  pEntity.FLUID_TANK.drain(500, FluidAction.EXECUTE);
                  pEntity.resetProgress();
                  pEntity.itemHandler.getStackInSlot(0).setDamageValue(pEntity.itemHandler.getStackInSlot(0).getDamageValue() + 1);
                  if (pEntity.itemHandler.getStackInSlot(0).getMaxDamage() <= pEntity.itemHandler.getStackInSlot(0).getDamageValue()) {
                     pEntity.itemHandler.setStackInSlot(0, new ItemStack(Items.AIR));
                  }

                  pEntity.FLUID_TANK_MANA.fill(new FluidStack((Fluid)ModFluids.SOURCE_MANA_WATER.get(), 250), FluidAction.EXECUTE);
               }

               setChanged(level, pos, state);
            } else {
               pEntity.progress = 0;
               setChanged(level, pos, state);
            }
         } else {
            pEntity.progress = 0;
            setChanged(level, pos, state);
         }

         if (hasFluidItemInSourceSlot(pEntity)) {
            transferItemFluidToFluidTank(pEntity);
         }
      }
   }

   private static void transferItemFluidToFluidTank(ManaExtractorBlockEntity pEntity) {
      pEntity.itemHandler.getStackInSlot(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
         int drainAmount = Math.min(pEntity.FLUID_TANK.getSpace(), 1000);
         FluidStack stack = handler.drain(drainAmount, FluidAction.SIMULATE);
         if (pEntity.FLUID_TANK.isFluidValid(stack)) {
            stack = handler.drain(drainAmount, FluidAction.EXECUTE);
            fillTankWithFluid(pEntity, stack, handler.getContainer());
         }
      });
   }

   private static void fillTankWithFluid(ManaExtractorBlockEntity pEntity, FluidStack stack, ItemStack container) {
      pEntity.FLUID_TANK.fill(stack, FluidAction.EXECUTE);
      pEntity.itemHandler.extractItem(1, 1, false);
      pEntity.itemHandler.insertItem(1, container, false);
   }

   private static boolean hasFluidItemInSourceSlot(ManaExtractorBlockEntity pEntity) {
      return pEntity.itemHandler.getStackInSlot(1).getCount() > 0;
   }

   private static void extractEnergy(ManaExtractorBlockEntity pEntity) {
   }

   private static boolean hasEnoughEnergy(ManaExtractorBlockEntity pEntity) {
      return true;
   }

   private static boolean hasFilterInFirstSlot(ManaExtractorBlockEntity pEntity) {
      return pEntity.itemHandler.getStackInSlot(0).getItem() != Items.AIR;
   }

   private static boolean hasManaFlowerInRange(ManaExtractorBlockEntity pEntity) {
      return pEntity.level.getBlockState(pEntity.getBlockPos().east()).getBlock() == ModBlocks.MANA_FLOWER.get()
         || pEntity.level.getBlockState(pEntity.getBlockPos().west()).getBlock() == ModBlocks.MANA_FLOWER.get()
         || pEntity.level.getBlockState(pEntity.getBlockPos().south()).getBlock() == ModBlocks.MANA_FLOWER.get()
         || pEntity.level.getBlockState(pEntity.getBlockPos().north()).getBlock() == ModBlocks.MANA_FLOWER.get();
   }

   private void resetProgress() {
      this.progress = 0;
   }

   private static void craftItem(ManaExtractorBlockEntity pEntity) {
      Level level = pEntity.level;
      SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());

      for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
         inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
      }

      Optional<ManaExtractorRecipe> recipe = level.getRecipeManager().getRecipeFor(ManaExtractorRecipe.Type.INSTANCE, inventory, level);
      if (hasRecipe(pEntity)) {
         pEntity.FLUID_TANK.drain(recipe.get().getFluid().getAmount(), FluidAction.EXECUTE);
         pEntity.itemHandler.extractItem(1, 1, false);
         pEntity.itemHandler
            .setStackInSlot(
               2, new ItemStack(recipe.get().getResultItem(level.registryAccess()).getItem(), pEntity.itemHandler.getStackInSlot(2).getCount() + 1)
            );
         pEntity.resetProgress();
      }
   }

   private static boolean hasRecipe(ManaExtractorBlockEntity entity) {
      Level level = entity.level;
      SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());

      for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
         inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
      }

      Optional<ManaExtractorRecipe> recipe = level.getRecipeManager().getRecipeFor(ManaExtractorRecipe.Type.INSTANCE, inventory, level);
      return recipe.isPresent()
         && canInsertAmountIntoOutputSlot(inventory)
         && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(level.registryAccess()))
         && hasCorrectFluidInTank(entity, recipe)
         && hasCorrectFluidAmountInTank(entity, recipe);
   }

   private static boolean hasCorrectFluidAmountInTank(ManaExtractorBlockEntity entity, Optional<ManaExtractorRecipe> recipe) {
      return entity.FLUID_TANK.getFluidAmount() >= recipe.get().getFluid().getAmount();
   }

   private static boolean hasCorrectFluidInTank(ManaExtractorBlockEntity entity, Optional<ManaExtractorRecipe> recipe) {
      return recipe.get().getFluid().equals(entity.FLUID_TANK.getFluid());
   }

   private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
      return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
   }

   private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
      return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
   }
}
