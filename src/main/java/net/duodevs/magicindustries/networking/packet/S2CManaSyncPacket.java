package net.duodevs.magicindustries.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class S2CManaSyncPacket {
    private final int mana;

    public S2CManaSyncPacket(int mana) {
        this.mana = mana;
    }

    public S2CManaSyncPacket(FriendlyByteBuf buf) {
        this.mana = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(mana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientManaData.set(this.mana);
        });
        return true;
    }
}

