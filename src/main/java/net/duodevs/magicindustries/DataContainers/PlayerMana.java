package net.duodevs.magicindustries.DataContainers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerMana implements INBTSerializable<CompoundTag> {
    private int mana;

    public int getMana() {
        return mana;
    }

    public void setMana(int amount) {
        this.mana = Math.max(0, Math.min(100, amount));
    }

    public void subMana(int amount) {
        setMana(this.mana - amount);
    }

    public void addMana(int amount) {
        setMana(this.mana + amount);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registryAccess) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("mana", mana);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registryAccess, CompoundTag nbt) {
        setMana(nbt.getInt("mana"));
    }
}
