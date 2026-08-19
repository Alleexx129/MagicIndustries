package net.duodevs.magicindustries.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CustomBlock extends Block {
   public CustomBlock() {
      super(Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(4.0F).requiresCorrectToolForDrops());
   }
}
