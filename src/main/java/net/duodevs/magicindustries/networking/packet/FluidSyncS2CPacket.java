package net.duodevs.magicindustries.networking.packet;

import net.duodevs.magicindustries.block.entity.ManaExtractorBlockEntity;
import net.duodevs.magicindustries.screen.ManaExtractorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.event.network.CustomPayloadEvent.Context;

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
      this.fluidStack = FluidStack.loadFluidStackFromNBT(buf.readNbt());
      this.fluidStack2 = FluidStack.loadFluidStackFromNBT(buf.readNbt());
      this.pos = buf.readBlockPos();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(this.fluidStack.writeToNBT(new CompoundTag()));
      buf.writeNbt(this.fluidStack2.writeToNBT(new CompoundTag()));
      buf.writeBlockPos(this.pos);
   }

   public void handle(Context context) {
         if (Minecraft.getInstance().level.getBlockEntity(this.pos) instanceof ManaExtractorBlockEntity blockEntity) {
            blockEntity.setFluid(this.fluidStack);
            blockEntity.setFluidMana(this.fluidStack2);
            if (Minecraft.getInstance().player.containerMenu instanceof ManaExtractorMenu menu && menu.getBlockEntity().getBlockPos().equals(this.pos)) {
               menu.setFluid(this.fluidStack);
               menu.setFluidMana(this.fluidStack2);
            }
         }
   }
}
