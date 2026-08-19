package net.duodevs.magicindustries.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent.Context;

public class ExampleC2SPacket {
   public ExampleC2SPacket() {
   }

   public ExampleC2SPacket(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

    public void handle(Context context) {
        ServerPlayer player = context.getSender();
        ServerLevel level = player.level();
    }
}
