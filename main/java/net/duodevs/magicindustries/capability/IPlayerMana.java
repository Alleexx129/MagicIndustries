package net.duodevs.magicindustries.capability;

public interface IPlayerMana {
    int getMana();
    void setMana(int mana);
    void addMana(int amount);
    void subMana(int amount);
}
