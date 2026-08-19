package net.duodevs.magicindustries.item.custom;

import net.duodevs.magicindustries.DataContainers.PlayerManaProvider;
import net.duodevs.magicindustries.event.ModEvents;
import net.duodevs.magicindustries.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManaBottle extends Item {
   public ManaBottle(Properties properties) {
      super(properties);
   }

   @Override
   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      //super.finishUsingItem(stack, level, entity);

      if (entity instanceof Player player) {
         player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            mana.addMana(4);
            if (player instanceof ServerPlayer serverPlayer) {
               ModEvents.syncMana(serverPlayer);
            }
         });
         if (!player.getAbilities().instabuild) {
            stack.setCount(stack.getCount()-1);
            ItemStack emptyBottle = new ItemStack(ModItems.EMPTY_FLASK.get());



            if (stack.isEmpty()) {
               return emptyBottle;
            }

            if (!player.getInventory().add(emptyBottle)) {
               player.drop(emptyBottle, false);
            }
         }
      }
      return stack;
   }

}
