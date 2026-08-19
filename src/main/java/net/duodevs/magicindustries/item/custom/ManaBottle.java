package net.duodevs.magicindustries.item.custom;

import net.duodevs.magicindustries.DataContainers.PlayerManaProvider;
import net.duodevs.magicindustries.event.ModEvents;
import net.duodevs.magicindustries.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManaBottle extends HoneyBottleItem {
    public ManaBottle(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            player.getData(PlayerManaProvider.PLAYER_MANA).addMana(4);
            if (player instanceof ServerPlayer serverPlayer) {
                ModEvents.syncMana(serverPlayer);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
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

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 15;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }
}
