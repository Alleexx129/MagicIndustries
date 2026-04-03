package net.duodevs.magicindustries.DataContainers;

import net.minecraft.nbt.CompoundTag;

public class PlayerMana {
    private int mana;

    public int getMana() {
        return mana;
    };

    public void setMana(int amount) {
        this.mana = Math.max(0, Math.min(100, amount));
    }

    public void subMana(int amount) {
        if (this.mana >= amount) {
            this.mana -= amount;
        } else {
            this.mana = 0;
        }
    }

    public void addMana(int amount) {
        if (this.mana + amount <= 100) {
            this.mana += amount;
        } else {
            this.mana = 100;
        }
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("mana", mana);
    }

    public void loadNBTData(CompoundTag nbt) {
        mana = nbt.getInt("mana");
    }
}
