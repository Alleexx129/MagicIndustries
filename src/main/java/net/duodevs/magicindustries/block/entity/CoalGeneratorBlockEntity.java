package net.duodevs.magicindustries.block.entity;

import java.util.Map;
import net.duodevs.magicindustries.block.custom.CoalGeneratorBlock;
import net.duodevs.magicindustries.item.ModItems;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.networking.packet.EnergySyncS2CPacket;
import net.duodevs.magicindustries.screen.CoalGeneratorMenu;
import net.duodevs.magicindustries.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoalGeneratorBlockEntity extends BlockEntity implements MenuProvider {
   private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
      protected void onContentsChanged(int slot) {
         CoalGeneratorBlockEntity.this.setChanged();
      }

      public boolean isItemValid(int slot, @NotNull ItemStack stack) {
         return switch (slot) {
            case 0 -> ForgeHooks.getBurnTime(stack.copy(), RecipeType.SMELTING) > 0;
            default -> super.isItemValid(slot, stack);
         };
      }
   };
   private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(69000, 0, 256) {
      @Override
      public void onEnergyChanged() {
         CoalGeneratorBlockEntity.this.setChanged();
         ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, CoalGeneratorBlockEntity.this.getBlockPos()));
      }
   };
   private static final int ENERGY_REQ = 32;
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
   private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
   protected final ContainerData data;
   private int progress = 0;
   private int maxProgress = 100;

   public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)ModBlockEntities.COAL_GENERATOR.get(), pos, state);
      this.data = new ContainerData() {
         public int get(int index) {
            return switch (index) {
               case 0 -> CoalGeneratorBlockEntity.this.progress;
               case 1 -> CoalGeneratorBlockEntity.this.maxProgress;
               default -> 0;
            };
         }

         public void set(int index, int value) {
            switch (index) {
               case 0:
                  CoalGeneratorBlockEntity.this.progress = value;
                  break;
               case 1:
                  CoalGeneratorBlockEntity.this.maxProgress = value;
            }
         }

         public int getCount() {
            return 2;
         }
      };
   }

   public Component getDisplayName() {
      return Component.literal("Heat Generator");
   }

   @Nullable
   public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
      ModMessages.sendToClients(new EnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), this.getBlockPos()));
      return new CoalGeneratorMenu(id, inventory, this, this.data);
   }

   public IEnergyStorage getEnergyStorage() {
      return this.ENERGY_STORAGE;
   }

   public void setEnergyLevel(int energy) {
      this.ENERGY_STORAGE.setEnergy(energy);
   }

   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      if (cap == ForgeCapabilities.ENERGY) {
         return this.lazyEnergyHandler.cast();
      } else {
         if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
               return this.lazyItemHandler.cast();
            }

            Direction localDir = (Direction)this.getBlockState().getValue(CoalGeneratorBlock.FACING);
            if (side == Direction.UP || side == Direction.NORTH || side == Direction.EAST || side == Direction.WEST || side == Direction.SOUTH) {
               return LazyOptional.of(() -> new IItemHandler() {
                     public int getSlots() {
                        return 1;
                     }

                     @NotNull
                     public ItemStack getStackInSlot(int slot) {
                        return CoalGeneratorBlockEntity.this.itemHandler.getStackInSlot(0);
                     }

                     @NotNull
                     public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                        return ForgeHooks.getBurnTime(stack, null) > 0 ? CoalGeneratorBlockEntity.this.itemHandler.insertItem(0, stack, simulate) : stack;
                     }

                     @NotNull
                     public ItemStack extractItem(int slot, int amount, boolean simulate) {
                        return ItemStack.EMPTY;
                     }

                     public int getSlotLimit(int slot) {
                        return 64;
                     }

                     public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                        return ForgeHooks.getBurnTime(stack, null) > 0;
                     }
                  }).cast();
            }

            if (side == Direction.DOWN) {
               return LazyOptional.of(
                     () -> new IItemHandler() {
                           public int getSlots() {
                              return 1;
                           }

                           @NotNull
                           public ItemStack getStackInSlot(int slot) {
                              return CoalGeneratorBlockEntity.this.itemHandler.getStackInSlot(0);
                           }

                           @NotNull
                           public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                              return stack;
                           }

                           @NotNull
                           public ItemStack extractItem(int slot, int amount, boolean simulate) {
                              ItemStack stackInSlot = CoalGeneratorBlockEntity.this.itemHandler.getStackInSlot(0);
                              return stackInSlot.getItem().equals(Items.BUCKET)
                                 ? CoalGeneratorBlockEntity.this.itemHandler.extractItem(0, 1, simulate)
                                 : ItemStack.EMPTY;
                           }

                           public int getSlotLimit(int slot) {
                              return 64;
                           }

                           public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                              return false;
                           }
                        }
                  )
                  .cast();
            }
         }

         return super.getCapability(cap, side);
      }
   }

   public void onLoad() {
      super.onLoad();
      this.lazyItemHandler = LazyOptional.of(() -> this.itemHandler);
      this.lazyEnergyHandler = LazyOptional.of(() -> this.ENERGY_STORAGE);
   }

   public void invalidateCaps() {
      super.invalidateCaps();
      this.lazyItemHandler.invalidate();
      this.lazyEnergyHandler.invalidate();
   }

   protected void saveAdditional(CompoundTag nbt) {
      nbt.put("inventory", this.itemHandler.serializeNBT());
      nbt.putInt("coal_generator.progress", this.progress);
      nbt.putInt("coal_generator.energy", this.ENERGY_STORAGE.getEnergyStored());
      super.saveAdditional(nbt);
   }

   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
      this.progress = nbt.getInt("coal_generator.progress");
      this.ENERGY_STORAGE.setEnergy(nbt.getInt("coal_generator.energy"));
   }

   public void drops() {
      SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());

      for (int i = 0; i < this.itemHandler.getSlots(); i++) {
         inventory.setItem(i, this.itemHandler.getStackInSlot(i));
      }

      Containers.dropContents(this.level, this.worldPosition, inventory);
   }

   public static boolean hasFullEnergy(CoalGeneratorBlockEntity pEntity) {
      return pEntity.ENERGY_STORAGE.getMaxEnergyStored() <= pEntity.ENERGY_STORAGE.getEnergyStored();
   }

   public static boolean hasEnoughItems(CoalGeneratorBlockEntity pEntity) {
      return pEntity.itemHandler.getStackInSlot(0).getCount() > 0;
   }

   public static void tick(Level level, BlockPos pos, BlockState state, CoalGeneratorBlockEntity pEntity) {
      if (!level.isClientSide()) {
         if (!hasFullEnergy(pEntity) && hasEnoughItems(pEntity)) {
            int burnTime = ForgeHooks.getBurnTime(pEntity.itemHandler.getStackInSlot(0), RecipeType.SMELTING);
            if (pEntity.itemHandler.getStackInSlot(0).getItem() == Items.LAVA_BUCKET) {
               burnTime = 20000;
            }

            double scalingFactor = Math.log((double)(burnTime + 1)) / Math.log(1601.0);
            pEntity.progress = (int)((double)pEntity.progress + scalingFactor * ((double)pEntity.maxProgress / 100.0));
            setChanged(level, pos, state);
            if (pEntity.progress >= pEntity.maxProgress) {
               if (pEntity.itemHandler.getStackInSlot(0).getItem() == Items.LAVA_BUCKET) {
                  pEntity.itemHandler.setStackInSlot(0, new ItemStack(Items.BUCKET));
                  pEntity.ENERGY_STORAGE.setEnergy(pEntity.ENERGY_STORAGE.getEnergyStored() + 12500);
               } else {
                  pEntity.itemHandler.getStackInSlot(0).shrink(1);
                  pEntity.ENERGY_STORAGE
                     .setEnergy(
                        pEntity.ENERGY_STORAGE.getEnergyStored()
                           + 1000 * (ForgeHooks.getBurnTime(pEntity.itemHandler.getStackInSlot(0), RecipeType.SMELTING) / 1600)
                     );
               }

               if (pEntity.ENERGY_STORAGE.getEnergyStored() > pEntity.ENERGY_STORAGE.getMaxEnergyStored()) {
                  pEntity.ENERGY_STORAGE.setEnergy(pEntity.ENERGY_STORAGE.getMaxEnergyStored());
               }

               setChanged(level, pos, state);
               ModMessages.sendToClients(new EnergySyncS2CPacket(pEntity.ENERGY_STORAGE.getEnergyStored(), pEntity.getBlockPos()));
               pEntity.resetProgress();
            }
         }
      }
   }

   private static void extractEnergy(CoalGeneratorBlockEntity pEntity) {
      pEntity.ENERGY_STORAGE.extractEnergy(32, false);
      pEntity.setChanged();
      ModMessages.sendToClients(new EnergySyncS2CPacket(pEntity.ENERGY_STORAGE.getEnergyStored(), pEntity.getBlockPos()));
   }

   private static boolean hasEnoughEnergy(CoalGeneratorBlockEntity pEntity) {
      return pEntity.ENERGY_STORAGE.getEnergyStored() >= 32 * pEntity.maxProgress;
   }

   private static boolean hasGemInFirstSlot(CoalGeneratorBlockEntity pEntity) {
      return pEntity.itemHandler.getStackInSlot(0).getItem() == ModItems.SAPPHIRE.get();
   }

   private void resetProgress() {
      this.setChanged();
      ModMessages.sendToClients(new EnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), this.getBlockPos()));
      this.progress = 0;
   }

   private static void craftItem(CoalGeneratorBlockEntity pEntity) {
      Level level = pEntity.level;
      SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());

      for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
         inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
      }
   }

   private static boolean hasRecipe(CoalGeneratorBlockEntity entity) {
      Level level = entity.level;
      SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());

      for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
         inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
      }

      return false;
   }
}
