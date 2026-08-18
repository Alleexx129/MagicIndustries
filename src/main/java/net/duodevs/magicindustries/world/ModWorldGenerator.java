package net.duodevs.magicindustries.world;

import java.util.Random;
import net.duodevs.magicindustries.init.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * 1.12.2 world generation for Magic Industries.
 *
 * Vanilla 1.12.2 has no deepslate, so the backport uses the normal ore block
 * at every valid Y level and skips source heights outside the 0..255 world.
 */
public class ModWorldGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() != 0) return;

        for (int i = 0; i < 1; i++) {
            placeOriginalY(world, random, chunkX, chunkZ, uniform(random,-16,256), 2,
                ModContent.SAPPHIRE_ORE.getDefaultState());
        }

        for (int i = 0; i < 20; i++) {
            placeOriginalY(world, random, chunkX, chunkZ, uniform(random,-64,72), 9,
                ModContent.TUNGSTEN_ORE.getDefaultState());
        }

        for (int i = 0; i < 14; i++) {
            placeOriginalY(world, random, chunkX, chunkZ, trapezoid(random, -32, 64, 0), 6,
                ModContent.MITHRIL_ORE.getDefaultState());
        }

        for (int i = 0; i < 16; i++) {
            placeOriginalY(world, random, chunkX, chunkZ, trapezoid(random, -16, 112, 0), 10,
                ModContent.COPPER_ORE.getDefaultState());
        }
    }

    private static int uniform(Random r, int min, int max) {
        return min + r.nextInt(max - min + 1);
    }

    private static int trapezoid(Random r, int min, int max, int plateau) {
        int range = max - min;
        if (plateau >= range) return uniform(r, min, max);
        int lower = (range - plateau) / 2;
        int upper = range - lower;
        return min + r.nextInt(upper + 1) + r.nextInt(lower + 1);
    }

    private static void placeOriginalY(World world, Random random, int chunkX, int chunkZ,
                                       int y, int size, IBlockState state) {
        if (y < 0 || y > 255) return;
        int x = chunkX * 16 + random.nextInt(16);
        int z = chunkZ * 16 + random.nextInt(16);
        new WorldGenMinable(state, size).generate(world, random, new BlockPos(x, y, z));
    }
}
