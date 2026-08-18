package net.duodevs.magicindustries.capability;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public final class ManaCapability {
    public static final ResourceLocation KEY = new ResourceLocation("magicindustries", "properties");
    @CapabilityInject(IPlayerMana.class) public static Capability<IPlayerMana> CAPABILITY = null;
    private ManaCapability() {}

    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerMana.class, new Capability.IStorage<IPlayerMana>() {
            @Nullable @Override public NBTBase writeNBT(Capability<IPlayerMana> capability, IPlayerMana instance, EnumFacing side) {
                NBTTagCompound tag = new NBTTagCompound(); tag.setInteger("mana", instance.getMana()); return tag;
            }
            @Override public void readNBT(Capability<IPlayerMana> capability, IPlayerMana instance, EnumFacing side, NBTBase nbt) {
                if (nbt instanceof NBTTagCompound) instance.setMana(((NBTTagCompound) nbt).getInteger("mana"));
            }
        }, PlayerMana::new);
    }

    public static Optional<IPlayerMana> get(EntityPlayer player) {
        if (player == null || CAPABILITY == null || !player.hasCapability(CAPABILITY, null)) return Optional.empty();
        return Optional.ofNullable(player.getCapability(CAPABILITY, null));
    }

    public static final class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private final IPlayerMana backend = new PlayerMana();
        @Override public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) { return capability == CAPABILITY; }
        @Nullable @Override public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) { return capability == CAPABILITY ? CAPABILITY.cast(backend) : null; }
        @Override public NBTTagCompound serializeNBT() { NBTTagCompound tag=new NBTTagCompound(); tag.setInteger("mana", backend.getMana()); return tag; }
        @Override public void deserializeNBT(NBTTagCompound nbt) { backend.setMana(nbt.getInteger("mana")); }
    }
}
