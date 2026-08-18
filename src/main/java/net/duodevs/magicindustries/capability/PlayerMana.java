package net.duodevs.magicindustries.capability;

public class PlayerMana implements IPlayerMana {
    private int mana;

    @Override public int getMana() { return mana; }

    @Override
    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(100, mana));
    }

    @Override public void addMana(int amount) { setMana(mana + amount); }
    @Override public void subMana(int amount) { setMana(mana - amount); }
}
