package net.duodevs.magicindustries.screen;

import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.block.entity.CoalGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class CoalGeneratorMenu extends AbstractContainerMenu {
   public final CoalGeneratorBlockEntity blockEntity;
   private final Level level;
   private final ContainerData data;
   private static final int HOTBAR_SLOT_COUNT = 9;
   private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
   private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
   private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
   private static final int VANILLA_SLOT_COUNT = 36;
   private static final int VANILLA_FIRST_SLOT_INDEX = 0;
   private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 36;
   private static final int TE_INVENTORY_SLOT_COUNT = 1;

   public CoalGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
      this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
   }

   public CoalGeneratorMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
      super((MenuType)ModMenuTypes.COAL_GENERATOR_MENU.get(), id);
      checkContainerSize(inv, 3);
      this.blockEntity = (CoalGeneratorBlockEntity)entity;
      this.level = inv.player.level();
      this.data = data;
      this.addPlayerInventory(inv);
      this.addPlayerHotbar(inv);
      this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> this.addSlot(new SlotItemHandler(handler, 0, 26, 36)));
      this.addDataSlots(data);
   }

   public boolean isCrafting() {
      return this.data.get(0) > 0;
   }

   public CoalGeneratorBlockEntity getBlockEntity() {
      return this.blockEntity;
   }

   public int getScaledProgress() {
      int progress = this.data.get(0);
      int maxProgress = this.data.get(1);
      int progressArrowSize = 81;
      return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
   }

   public ItemStack quickMoveStack(Player playerIn, int index) {
      Slot sourceSlot = (Slot)this.slots.get(index);
      if (sourceSlot != null && sourceSlot.hasItem()) {
         ItemStack sourceStack = sourceSlot.getItem();
         ItemStack copyOfSourceStack = sourceStack.copy();
         if (index < 36) {
            if (!this.moveItemStackTo(sourceStack, 36, 37, false)) {
               return ItemStack.EMPTY;
            }
         } else {
            if (index >= 37) {
               System.out.println("Invalid slotIndex:" + index);
               return ItemStack.EMPTY;
            }

            if (!this.moveItemStackTo(sourceStack, 0, 36, false)) {
               return ItemStack.EMPTY;
            }
         }

         if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
         } else {
            sourceSlot.setChanged();
         }

         sourceSlot.onTake(playerIn, sourceStack);
         return copyOfSourceStack;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public boolean stillValid(Player player) {
      return stillValid(ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()), player, (Block)ModBlocks.COAL_GENERATOR.get());
   }

   private void addPlayerInventory(Inventory playerInventory) {
      for (int i = 0; i < 3; i++) {
         for (int l = 0; l < 9; l++) {
            this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 70 + i * 18));
         }
      }
   }

   private void addPlayerHotbar(Inventory playerInventory) {
      for (int i = 0; i < 9; i++) {
         this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 128));
      }
   }
}
