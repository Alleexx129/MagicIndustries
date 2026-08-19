package net.duodevs.magicindustries.networking.packet;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.block.entity.ManaExtractorBlockEntity;
import net.duodevs.magicindustries.screen.ManaExtractorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FluidSyncS2CPacket(FluidStack fluidStack, FluidStack fluidStack2, BlockPos pos) implements CustomPacketPayload {
    public static final Type<FluidSyncS2CPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "fluid_sync")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidSyncS2CPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override public FluidSyncS2CPacket decode(RegistryFriendlyByteBuf buf) {
            return new FluidSyncS2CPacket(
                    FluidStack.OPTIONAL_STREAM_CODEC.decode(buf),
                    FluidStack.OPTIONAL_STREAM_CODEC.decode(buf),
                    buf.readBlockPos()
            );
        }
        @Override public void encode(RegistryFriendlyByteBuf buf, FluidSyncS2CPacket packet) {
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, packet.fluidStack());
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, packet.fluidStack2());
            buf.writeBlockPos(packet.pos());
        }
    };

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(FluidSyncS2CPacket packet, IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;
        if (minecraft.level.getBlockEntity(packet.pos()) instanceof ManaExtractorBlockEntity blockEntity) {
            blockEntity.setFluid(packet.fluidStack());
            blockEntity.setFluidMana(packet.fluidStack2());
            if (minecraft.player != null && minecraft.player.containerMenu instanceof ManaExtractorMenu menu
                    && menu.getBlockEntity().getBlockPos().equals(packet.pos())) {
                menu.setFluid(packet.fluidStack());
                menu.setFluidMana(packet.fluidStack2());
            }
        }
    }
}
