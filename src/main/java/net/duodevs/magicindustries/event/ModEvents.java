package net.duodevs.magicindustries.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import net.duodevs.magicindustries.DataContainers.PlayerMana;
import net.duodevs.magicindustries.DataContainers.PlayerManaProvider;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.networking.packet.S2CManaSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

public final class ModEvents {
    private ModEvents() {}

    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
            trades.get(4).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 32),
                    new ItemStack(ModBlocks.MANA_FLOWER.get()),
                    1, 8, 0.02F
            ));
        }
    }

    public static void addCustomWanderingTrades(WandererTradesEvent event) {
        event.getGenericTrades().add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 16),
                new ItemStack(ModBlocks.MANA_FLOWER.get()),
                1, 8, 0.2F
        ));
    }

    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath() && event.getOriginal().hasData(PlayerManaProvider.PLAYER_MANA)) {
            PlayerMana oldMana = event.getOriginal().getData(PlayerManaProvider.PLAYER_MANA);
            event.getEntity().getData(PlayerManaProvider.PLAYER_MANA).setMana(oldMana.getMana());
        }
    }

    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
            syncMana(player);
        }
    }

    public static void syncMana(ServerPlayer player) {
        PlayerMana mana = player.getData(PlayerManaProvider.PLAYER_MANA);
        ModMessages.sendToPlayer(new S2CManaSyncPacket(mana.getMana()), player);
    }
}
