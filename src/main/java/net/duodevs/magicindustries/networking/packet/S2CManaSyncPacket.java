package net.duodevs.magicindustries.networking.packet;

import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record S2CManaSyncPacket(int mana) implements CustomPacketPayload {
    public static final Type<S2CManaSyncPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "mana_sync")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CManaSyncPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override public S2CManaSyncPacket decode(RegistryFriendlyByteBuf buf) { return new S2CManaSyncPacket(buf.readInt()); }
        @Override public void encode(RegistryFriendlyByteBuf buf, S2CManaSyncPacket packet) { buf.writeInt(packet.mana()); }
    };

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(S2CManaSyncPacket packet, IPayloadContext context) {
        ClientManaData.set(packet.mana());
    }
}
