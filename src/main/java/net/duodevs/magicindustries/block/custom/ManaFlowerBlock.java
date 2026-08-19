package net.duodevs.magicindustries.block.custom;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManaFlowerBlock extends FlowerBlock {

   public static final EnumProperty<Direction> FACING =
           BlockStateProperties.HORIZONTAL_FACING;

   private static final VoxelShape SHAPE =
           Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

   public ManaFlowerBlock(
           Holder<MobEffect> effect,
           float duration,
           Properties properties
   ) {
      super(
              new SuspiciousStewEffects(
                      List.of(
                              new SuspiciousStewEffects.Entry(
                                      effect,
                                      (int) (duration * 20.0F)
                              )
                      )
              ),
              properties
      );
   }

   @Override
   public VoxelShape getShape(
           BlockState state,
           BlockGetter level,
           BlockPos pos,
           CollisionContext context
   ) {
      return SHAPE;
   }

   @Override
   public void animateTick(
           BlockState state,
           Level level,
           BlockPos pos,
           RandomSource random
   ) {
      super.animateTick(state, level, pos, random);

      for (int i = 0; i < 2; i++) {
         level.addParticle(
                 ParticleTypes.FALLING_WATER,
                 pos.getX() + random.nextDouble(),
                 pos.getY() + random.nextDouble(),
                 pos.getZ() + random.nextDouble(),
                 1.0,
                 1.0,
                 1.0
         );
      }
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return this.defaultBlockState()
              .setValue(
                      FACING,
                      context.getHorizontalDirection().getOpposite()
              );
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rotation) {
      return state.setValue(
              FACING,
              rotation.rotate(state.getValue(FACING))
      );
   }

   @Override
   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(
              mirror.getRotation(state.getValue(FACING))
      );
   }

   @Override
   protected void createBlockStateDefinition(
           Builder<Block, BlockState> builder
   ) {
      builder.add(FACING);
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }
}