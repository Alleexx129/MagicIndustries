package net.duodevs.magicindustries.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import net.duodevs.magicindustries.block.ModBlocks;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

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
}
