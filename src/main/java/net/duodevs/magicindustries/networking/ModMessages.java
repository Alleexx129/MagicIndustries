package net.duodevs.magicindustries.networking;

import net.duodevs.magicindustries.networking.packet.EnergySyncS2CPacket;
import net.duodevs.magicindustries.networking.packet.ExampleC2SPacket;
import net.duodevs.magicindustries.networking.packet.FluidSyncS2CPacket;
import net.duodevs.magicindustries.networking.packet.S2CManaSyncPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModMessages {
    private ModMessages() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ExampleC2SPacket.TYPE, ExampleC2SPacket.STREAM_CODEC, ExampleC2SPacket::handle);
        registrar.playToClient(EnergySyncS2CPacket.TYPE, EnergySyncS2CPacket.STREAM_CODEC, EnergySyncS2CPacket::handle);
        registrar.playToClient(FluidSyncS2CPacket.TYPE, FluidSyncS2CPacket.STREAM_CODEC, FluidSyncS2CPacket::handle);
        registrar.playToClient(S2CManaSyncPacket.TYPE, S2CManaSyncPacket.STREAM_CODEC, S2CManaSyncPacket::handle);
    }

    public static void sendToServer(CustomPacketPayload message) {
        PacketDistributor.sendToServer(message);
    }

    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static void sendToClients(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers(message);
    }
}
