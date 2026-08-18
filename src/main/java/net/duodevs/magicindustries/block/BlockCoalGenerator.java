package net.duodevs.magicindustries.block;

import net.duodevs.magicindustries.gui.GuiHandler;
import net.duodevs.magicindustries.tile.TileCoalGenerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCoalGenerator extends BlockMachine {
    public BlockCoalGenerator() { super(GuiHandler.COAL_GENERATOR); }
    @Override public TileEntity createNewTileEntity(World world, int meta) { return new TileCoalGenerator(); }
}
