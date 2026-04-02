package net.duodevs.magicindustries.networking.packet;

import java.util.function.Supplier;
import net.duodevs.magicindustries.block.entity.ManaExtractorBlockEntity;
import net.duodevs.magicindustries.screen.ManaExtractorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class FluidSyncS2CPacket {
   private final FluidStack fluidStack;
   private final FluidStack fluidStack2;
   private final BlockPos pos;

   public FluidSyncS2CPacket(FluidStack fluidStack, FluidStack fluidStack2, BlockPos pos) {
      this.fluidStack = fluidStack;
      this.fluidStack2 = fluidStack2;
      this.pos = pos;
   }

   public FluidSyncS2CPacket(FriendlyByteBuf buf) {
      this.fluidStack = buf.readFluidStack();
      this.fluidStack2 = buf.readFluidStack();
      this.pos = buf.readBlockPos();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeFluidStack(this.fluidStack);
      buf.writeFluidStack(this.fluidStack2);
      buf.writeBlockPos(this.pos);
   }

   public boolean handle(Supplier<Context> supplier) {
      Context context = supplier.get();
      context.enqueueWork(() -> {
         if (Minecraft.getInstance().level.getBlockEntity(this.pos) instanceof ManaExtractorBlockEntity blockEntity) {
            blockEntity.setFluid(this.fluidStack);
            blockEntity.setFluidMana(this.fluidStack2);
            if (Minecraft.getInstance().player.containerMenu instanceof ManaExtractorMenu menu && menu.getBlockEntity().getBlockPos().equals(this.pos)) {
               menu.setFluid(this.fluidStack);
               menu.setFluidMana(this.fluidStack2);
            }
         }
      });
      return true;
   }
}
