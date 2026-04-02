package net.duodevs.magicindustries.networking.packet;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ExampleC2SPacket {
   public ExampleC2SPacket() {
   }

   public ExampleC2SPacket(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

   public boolean handle(Supplier<Context> supplier) {
      Context context = supplier.get();
      context.enqueueWork(() -> {
         ServerPlayer player = context.getSender();
         ServerLevel level = player.serverLevel();
      });
      return true;
   }
}
