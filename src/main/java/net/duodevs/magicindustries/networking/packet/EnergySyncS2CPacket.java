package net.duodevs.magicindustries.networking.packet;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.block.entity.CoalGeneratorBlockEntity;
import net.duodevs.magicindustries.screen.CoalGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EnergySyncS2CPacket(int energy, BlockPos pos) implements CustomPacketPayload {
    public static final Type<EnergySyncS2CPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "energy_sync")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergySyncS2CPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override public EnergySyncS2CPacket decode(RegistryFriendlyByteBuf buf) {
            return new EnergySyncS2CPacket(buf.readInt(), buf.readBlockPos());
        }
        @Override public void encode(RegistryFriendlyByteBuf buf, EnergySyncS2CPacket packet) {
            buf.writeInt(packet.energy());
            buf.writeBlockPos(packet.pos());
        }
    };

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(EnergySyncS2CPacket packet, IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;
        if (minecraft.level.getBlockEntity(packet.pos()) instanceof CoalGeneratorBlockEntity blockEntity) {
            blockEntity.setEnergyLevel(packet.energy());
            if (minecraft.player != null && minecraft.player.containerMenu instanceof CoalGeneratorMenu menu
                    && menu.getBlockEntity().getBlockPos().equals(packet.pos())) {
                blockEntity.setEnergyLevel(packet.energy());
            }
        }
    }
}
