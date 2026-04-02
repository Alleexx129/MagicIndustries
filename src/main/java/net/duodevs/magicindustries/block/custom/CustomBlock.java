package net.duodevs.magicindustries.block.custom;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CustomBlock extends Block {
   public CustomBlock() {
      super(Properties.copy(Blocks.IRON_BLOCK).strength(4.0F).requiresCorrectToolForDrops());
   }

   public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
      if (pTool.getItem() instanceof PickaxeItem) {
         PickaxeItem pickaxe = (PickaxeItem)pTool.getItem();
         if (pickaxe.getTier().equals(Tiers.IRON) || pickaxe.getTier().equals(Tiers.DIAMOND) || pickaxe.getTier().equals(Tiers.NETHERITE)) {
            super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
         }
      } else {
         super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
      }
   }
}
