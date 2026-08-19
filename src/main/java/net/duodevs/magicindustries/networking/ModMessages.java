package net.duodevs.magicindustries.networking;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.networking.packet.EnergySyncS2CPacket;
import net.duodevs.magicindustries.networking.packet.ExampleC2SPacket;
import net.duodevs.magicindustries.networking.packet.FluidSyncS2CPacket;
import net.duodevs.magicindustries.networking.packet.S2CManaSyncPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ModMessages {
   private static SimpleChannel INSTANCE;
   private static int packetId = 0;

   private static int id() {
      return packetId++;
   }

   public static void register() {
      // Create ONE channel
      SimpleChannel net = ChannelBuilder
              .named(Identifier.fromNamespaceAndPath(MagicIndustries.MOD_ID, "messages"))
              .networkProtocolVersion(1)
              .simpleChannel();

      INSTANCE = net;

      // Register ALL packets to this ONE 'net' object
      net.messageBuilder(ExampleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
              .decoder(ExampleC2SPacket::new)
              .encoder(ExampleC2SPacket::toBytes)
              .consumerMainThread(ExampleC2SPacket::handle)
              .add();

      net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
              .decoder(EnergySyncS2CPacket::new)
              .encoder(EnergySyncS2CPacket::toBytes)
              .consumerMainThread(EnergySyncS2CPacket::handle)
              .add();

      net.messageBuilder(FluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
              .decoder(FluidSyncS2CPacket::new)
              .encoder(FluidSyncS2CPacket::toBytes)
              .consumerMainThread(FluidSyncS2CPacket::handle)
              .add();

      // ADD YOUR MANA PACKET HERE to the same 'net'
      net.messageBuilder(S2CManaSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
              .decoder(S2CManaSyncPacket::new)
              .encoder(S2CManaSyncPacket::toBytes)
              .consumerMainThread(S2CManaSyncPacket::handle)
              .add();
   }

   // Keep your helper methods using the single INSTANCE
   public static <MSG> void sendToServer(MSG message) {

      INSTANCE.send(message, PacketDistributor.SERVER.noArg());

   }


   public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {

      INSTANCE.send(message, PacketDistributor.PLAYER.with(player));

   }


   public static <MSG> void sendToClients(MSG message) {

      INSTANCE.send(message, PacketDistributor.ALL.noArg());

   }

}