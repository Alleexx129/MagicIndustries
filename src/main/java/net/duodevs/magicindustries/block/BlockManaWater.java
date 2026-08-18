package net.duodevs.magicindustries.block;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockManaWater extends BlockFluidClassic {

    public BlockManaWater(Fluid fluid) {
        super(fluid, Material.WATER);
        setQuantaPerBlock(4);
    }
}