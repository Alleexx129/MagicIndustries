package net.duodevs.magicindustries.event;

import net.duodevs.magicindustries.capability.ManaCapability;
import net.duodevs.magicindustries.network.ModNetwork;
import net.duodevs.magicindustries.init.ModContent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class CommonEvents {
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(ManaCapability.KEY, new ManaCapability.Provider());
        }
    }

    @SubscribeEvent
    public void fillManaBucket(FillBucketEvent event) {
        if (event.getEmptyBucket().getItem() != Items.BUCKET || ModContent.MANA_WATER_BLOCK == null || ModContent.MANA_WATER_BUCKET == null) return;
        RayTraceResult target = event.getTarget();
        if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) return;
        BlockPos pos = target.getBlockPos();
        if (event.getWorld().getBlockState(pos).getBlock() != ModContent.MANA_WATER_BLOCK || !ModContent.MANA_WATER_BLOCK.isSourceBlock(event.getWorld(), pos)) return;

        event.setFilledBucket(new ItemStack(ModContent.MANA_WATER_BUCKET));
        if (!event.getWorld().isRemote) event.getWorld().setBlockToAir(pos);
        event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) {
        // Preserve the 1.20.1 source exactly: mana is copied for non-death clones only.
        if (!event.isWasDeath()) {
            ManaCapability.get(event.getOriginal()).ifPresent(oldMana ->
                ManaCapability.get(event.getEntityPlayer()).ifPresent(newMana ->
                    newMana.setMana(oldMana.getMana())
                )
            );
        }
    }

    @SubscribeEvent
    public void login(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            ModNetwork.syncMana((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void respawn(PlayerRespawnEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            ModNetwork.syncMana((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void changedDimension(PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            ModNetwork.syncMana((EntityPlayerMP) event.player);
        }
    }
}
