package net.duodevs.magicindustries.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy {
    public void preInit() {}
    public void init() {}
    public void postInit() {}

    /** No-op on a dedicated server; implemented by ClientProxy on the physical client. */
    public void handleManaSync(int mana) {}

    /** Keeps client-only GUI classes out of common/dedicated-server class loading. */
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return null; }
}
