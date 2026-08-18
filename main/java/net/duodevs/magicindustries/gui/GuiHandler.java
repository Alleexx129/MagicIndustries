package net.duodevs.magicindustries.gui;

import net.duodevs.magicindustries.MagicIndustries;
import net.duodevs.magicindustries.tile.TileCoalGenerator;
import net.duodevs.magicindustries.tile.TileManaExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int COAL_GENERATOR = 0;
    public static final int MANA_EXTRACTOR = 1;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (id == COAL_GENERATOR && te instanceof TileCoalGenerator)
            return new ContainerCoalGenerator(player.inventory, (TileCoalGenerator) te);
        if (id == MANA_EXTRACTOR && te instanceof TileManaExtractor)
            return new ContainerManaExtractor(player.inventory, (TileManaExtractor) te);
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return MagicIndustries.PROXY.getClientGuiElement(id, player, world, x, y, z);
    }
}
