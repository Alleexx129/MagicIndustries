package net.duodevs.magicindustries.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

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

    public void handle(CustomPayloadEvent.Context context) {
            ClientManaData.set(this.mana);
    }
}

