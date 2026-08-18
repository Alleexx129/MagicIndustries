package net.duodevs.magicindustries.proxy;

import net.duodevs.magicindustries.block.BlockManaFlower;
import net.duodevs.magicindustries.event.ClientEvents;
import net.duodevs.magicindustries.gui.ContainerCoalGenerator;
import net.duodevs.magicindustries.gui.ContainerManaExtractor;
import net.duodevs.magicindustries.gui.GuiCoalGenerator;
import net.duodevs.magicindustries.gui.GuiHandler;
import net.duodevs.magicindustries.gui.GuiManaExtractor;
import net.duodevs.magicindustries.init.ModContent;
import net.duodevs.magicindustries.network.ClientManaData;
import net.duodevs.magicindustries.tile.TileCoalGenerator;
import net.duodevs.magicindustries.tile.TileManaExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new ClientEvents());

        ModelLoader.setCustomStateMapper(
                ModContent.MANA_FLOWER,
                new StateMap.Builder()
                        .ignore(BlockManaFlower.FACING)
                        .build()
        );
    }

    @Override
    public void handleManaSync(final int mana) {
        Minecraft.getMinecraft().addScheduledTask(() ->
                ClientManaData.setMana(mana)
        );
    }

    @Override
    public Object getClientGuiElement(
            int id,
            EntityPlayer player,
            World world,
            int x,
            int y,
            int z) {

        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (id == GuiHandler.COAL_GENERATOR && te instanceof TileCoalGenerator) {
            return new GuiCoalGenerator(
                    new ContainerCoalGenerator(
                            player.inventory,
                            (TileCoalGenerator) te
                    )
            );
        }

        if (id == GuiHandler.MANA_EXTRACTOR && te instanceof TileManaExtractor) {
            return new GuiManaExtractor(
                    new ContainerManaExtractor(
                            player.inventory,
                            (TileManaExtractor) te
                    )
            );
        }

        return null;
    }
}