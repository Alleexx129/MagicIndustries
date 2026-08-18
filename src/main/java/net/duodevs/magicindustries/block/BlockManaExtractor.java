package net.duodevs.magicindustries.block;

import net.duodevs.magicindustries.gui.GuiHandler;
import net.duodevs.magicindustries.tile.TileManaExtractor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockManaExtractor extends BlockMachine {
    public BlockManaExtractor() { super(GuiHandler.MANA_EXTRACTOR); }
    @Override public TileEntity createNewTileEntity(World world, int meta) { return new TileManaExtractor(); }
}
