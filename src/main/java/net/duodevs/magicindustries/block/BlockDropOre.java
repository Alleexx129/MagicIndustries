package net.duodevs.magicindustries.block;

import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/** Implements the original 1.20 loot-table drop distributions without adding XP. */
public class BlockDropOre extends Block {
    private final Supplier<Item> drop;
    private final int minRolls;
    private final int maxRolls;
    private final boolean bonusPerRoll;

    public BlockDropOre(Item drop, int minRolls, int maxRolls, boolean bonusPerRoll) {
        this(() -> drop, minRolls, maxRolls, bonusPerRoll);
    }

    public BlockDropOre(Supplier<Item> drop, int minRolls, int maxRolls, boolean bonusPerRoll) {
        super(Material.ROCK);
        this.drop = drop;
        this.minRolls = minRolls;
        this.maxRolls = maxRolls;
        this.bonusPerRoll = bonusPerRoll;
        setHarvestLevel("pickaxe", 2);
    }

    @Override public Item getItemDropped(IBlockState state, Random rand, int fortune) { return drop.get(); }

    @Override public int quantityDropped(Random rand) {
        return minRolls + (maxRolls > minRolls ? rand.nextInt(maxRolls - minRolls + 1) : 0);
    }

    @Override public int quantityDroppedWithBonus(int fortune, Random rand) {
        int rolls = quantityDropped(rand);
        if (fortune <= 0) return rolls;
        if (!bonusPerRoll) return rolls + rand.nextInt(fortune + 1);
        int total = 0;
        for (int i = 0; i < rolls; i++) total += 1 + rand.nextInt(fortune + 1);
        return total;
    }

    @Override public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) { return 0; }
}
