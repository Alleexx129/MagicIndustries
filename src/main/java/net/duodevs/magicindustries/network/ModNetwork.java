package net.duodevs.magicindustries.network;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.capability.ManaCapability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class ModNetwork {
    public static final SimpleNetworkWrapper CHANNEL =
        NetworkRegistry.INSTANCE.newSimpleChannel(new ResourceLocation(MagicIndustries.MODID, "main").toString());

    private ModNetwork() {}

    public static void init() {
        CHANNEL.registerMessage(MessageManaSync.Handler.class, MessageManaSync.class, 0, Side.CLIENT);
    }

    public static void syncMana(EntityPlayerMP player) {
        ManaCapability.get(player).ifPresent(mana -> CHANNEL.sendTo(new MessageManaSync(mana.getMana()), player));
    }
}
