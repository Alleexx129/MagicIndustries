package net.duodevs.magicindustries.network;

import io.netty.buffer.ByteBuf;
import net.duodevs.magicindustries.MagicIndustries;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** Common-side packet class. Client-only Minecraft classes live in ClientProxy so dedicated servers can load this safely. */
public class MessageManaSync implements IMessage {
    private int mana;

    public MessageManaSync() {}
    public MessageManaSync(int mana) { this.mana = mana; }

    @Override public void fromBytes(ByteBuf buf) { mana = buf.readInt(); }
    @Override public void toBytes(ByteBuf buf) { buf.writeInt(mana); }

    public static class Handler implements IMessageHandler<MessageManaSync, IMessage> {
        @Override
        public IMessage onMessage(MessageManaSync message, MessageContext ctx) {
            MagicIndustries.PROXY.handleManaSync(message.mana);
            return null;
        }
    }
}
