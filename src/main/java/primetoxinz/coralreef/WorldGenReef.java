package primetoxinz.coralreef;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * Created by primetoxinz on 7/16/17.
 */
public class WorldGenReef extends WorldGenerator
{
    public static boolean isMaterial(Material material, World world, BlockPos pos)
    {
        return world.getBlockState(pos).getMaterial() == material;
    }

    public static boolean isOceanFloor(World world, BlockPos pos)
    {
        return isMaterial(Material.WATER, world, pos.up(1))
            && isMaterial(Material.WATER, world, pos.up(2))
            && !(isMaterial(Material.WATER, world, pos));
    }

    private final IBlockState state;

    public WorldGenReef(IBlockState state)
    {
        this.state = state;
    }

    // basic reef needs at least two blocks of water above it (because coral needs one block above it)

    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        if (isOceanFloor(worldIn, pos))
        {
            if (rand.nextDouble() <= CoralReef.ConfigHandler.reef.reefSparsity)
            {
                worldIn.setBlockState(pos, state);
                if (rand.nextDouble() <= CoralReef.ConfigHandler.reef.coralSparsity)
                    CoralReef.CORAL.placeAt(worldIn, pos.up());
            }
        }
        return true;
    }
}