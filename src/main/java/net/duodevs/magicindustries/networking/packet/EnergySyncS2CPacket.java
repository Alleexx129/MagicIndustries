package net.duodevs.magicindustries.networking.packet;

import java.util.function.Supplier;
import net.duodevs.magicindustries.block.entity.CoalGeneratorBlockEntity;
import net.duodevs.magicindustries.screen.CoalGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class EnergySyncS2CPacket {
   private final int energy;
   private final BlockPos pos;

   public EnergySyncS2CPacket(int energy, BlockPos pos) {
      this.energy = energy;
      this.pos = pos;
   }

   public EnergySyncS2CPacket(FriendlyByteBuf buf) {
      this.energy = buf.readInt();
      this.pos = buf.readBlockPos();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeInt(this.energy);
      buf.writeBlockPos(this.pos);
   }

   public boolean handle(Supplier<Context> supplier) {
      Context context = supplier.get();
      context.enqueueWork(() -> {
         if (Minecraft.getInstance().level.getBlockEntity(this.pos) instanceof CoalGeneratorBlockEntity blockEntity) {
            blockEntity.setEnergyLevel(this.energy);
            if (Minecraft.getInstance().player.containerMenu instanceof CoalGeneratorMenu menu && menu.getBlockEntity().getBlockPos().equals(this.pos)) {
               blockEntity.setEnergyLevel(this.energy);
            }
         }
      });
      return true;
   }
}
