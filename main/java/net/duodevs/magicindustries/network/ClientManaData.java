package net.duodevs.magicindustries.network;

public final class ClientManaData {
    private static int mana;
    private ClientManaData() {}
    public static int getMana() { return mana; }
    public static void setMana(int value) { mana = Math.max(0, Math.min(100, value)); }
}
