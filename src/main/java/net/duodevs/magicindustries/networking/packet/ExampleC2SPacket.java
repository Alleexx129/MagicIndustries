package net.duodevs.magicindustries.networking.packet;

import net.duodevs.magicindustries.MagicIndustries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ExampleC2SPacket() implements CustomPacketPayload {
    public static final Type<ExampleC2SPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "example_c2s")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ExampleC2SPacket> STREAM_CODEC = StreamCodec.unit(new ExampleC2SPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ExampleC2SPacket packet, IPayloadContext context) {
        // Placeholder packet retained from the Forge project. No gameplay action was implemented.
    }
}
