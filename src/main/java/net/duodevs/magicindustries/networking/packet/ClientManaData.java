package net.duodevs.magicindustries.networking.packet;

public class ClientManaData {
    private static int playerMana;
    public static void set(int mana) { playerMana = mana; }
    public static int get() { return playerMana; }
}
