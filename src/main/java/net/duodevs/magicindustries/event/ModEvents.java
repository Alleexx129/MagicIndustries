package net.duodevs.magicindustries.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;

import net.duodevs.magicindustries.DataContainers.PlayerManaProvider;
import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.block.ModBlocks;
import net.duodevs.magicindustries.networking.ModMessages;
import net.duodevs.magicindustries.networking.packet.S2CManaSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;

@EventBusSubscriber(
   modid = "magicindustries"
)
public class ModEvents {
   @SubscribeEvent
   public static void addCustomTrades(VillagerTradesEvent event) {
      if (event.getType() == VillagerProfession.FARMER) {
         Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
         ((List<ItemListing>)trades.get(4))
            .add(
               (pTrader, pRandom) -> new MerchantOffer(new ItemStack(Items.EMERALD, 32), new ItemStack((ItemLike)ModBlocks.MANA_FLOWER.get(), 1), 1, 8, 0.02F)
            );
      }
   }

   @SubscribeEvent
   public static void addCustomWanderingTrades(WandererTradesEvent event) {
      List<ItemListing> genericTrades = event.getGenericTrades();
      List<ItemListing> rareTrades = event.getRareTrades();
      genericTrades.add(
         (pTrader, pRandom) -> new MerchantOffer(new ItemStack(Items.EMERALD, 16), new ItemStack((ItemLike)ModBlocks.MANA_FLOWER.get(), 1), 1, 8, 0.2F)
      );
   }

   @SubscribeEvent
   public static void onPlayerCloned(PlayerEvent.Clone event) {
      if (!event.isWasDeath()) {
         event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(newStore -> {
               newStore.setMana(oldStore.getMana());
            });
         });
      }
   }

   @SubscribeEvent
   public static void onPlayerJoin(EntityJoinLevelEvent event) {
      if(!event.getLevel().isClientSide && event.getEntity() instanceof Player player) {
         player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            System.out.println("DEBUG: Player joined with mana: " + mana.getMana());

            if (player instanceof ServerPlayer serverPlayer) {
               ModMessages.sendToPlayer(new S2CManaSyncPacket(mana.getMana()), serverPlayer);
            }
         });
      }
   }

   public static void syncMana(ServerPlayer player) {
      player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
         ModMessages.sendToPlayer(new S2CManaSyncPacket(mana.getMana()), player);
      });
   }

   @SubscribeEvent
   public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         if (!event.getObject().getCapability(PlayerManaProvider.PLAYER_MANA).isPresent()) {
            event.addCapability(ResourceLocation.fromNamespaceAndPath(MagicIndustries.MOD_ID, "properties"), new PlayerManaProvider());
         }
      }
   }
}
