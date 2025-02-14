package primetoxinz.coralreef;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.block.BlockLiquid.LEVEL;
import static net.minecraft.util.EnumFacing.WEST;

/**
 * Created by tyler on 8/17/16.
 */
public class BlockCoral extends Block implements IPlantable
{
    public static final int NTYPES = 6;
    public static final EnumPlantType CORAL = EnumPlantType.getPlantType("Coral");
    public static final PropertyInteger TYPES = PropertyInteger.create("types", 0, NTYPES - 1);
    protected static final AxisAlignedBB CORAL_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockCoral()
    {
        super(Material.WATER);
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.MISC);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        setDefaultState(getDefaultState().withProperty(TYPES, 0).withProperty(BlockLiquid.LEVEL, 15));
    }

    // these are not limited to height 1
    public boolean highVariant(int variant)
    {
        return variant > 3;
    }

    public boolean placeAt(World world, BlockPos bottom)
    {
        boolean placed = false;
        if (canPlaceBlockAt(world, bottom))
        {
            int variant = world.rand.nextInt(NTYPES);
            if (highVariant(variant))
            {
                int height = world.rand.nextInt(4);
                for (int i = 0; i < height; i++)
                {
                    BlockPos bp = bottom.up(i);
                    if (world.getBlockState(bp.up()).getMaterial() == Material.WATER) placed = world.setBlockState(bp, getDefaultState().withProperty(TYPES, variant));
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                placed = world.setBlockState(bottom, getDefaultState().withProperty(TYPES, variant));
            }
        }
        return placed;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(TYPES, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TYPES);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CORAL_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return Block.NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand)
    {

        // render bubbles
        if (CoralReef.ConfigHandler.bubbles && world.getBlockState(pos.up()).getMaterial() == Material.WATER)
        {
            double offset = 0.0625D;
            for (int i = 0; i < 6; i++)
            {
                double x1 = (pos.getX() + rand.nextDouble());
                double y1 = (pos.getY() + rand.nextDouble());
                double z1 = (pos.getZ() + rand.nextDouble());
                if (i == 0 && !world.getBlockState(pos.up()).isBlockNormalCube())
                {
                    y1 = (double) (pos.getY() + 1) + offset;
                }

                if (i == 1 && !world.getBlockState(pos.down()).isBlockNormalCube())
                {
                    y1 = (double) (pos.getY()) - offset;
                }

                if (i == 2 && !world.getBlockState(pos.offset(EnumFacing.SOUTH)).isBlockNormalCube())
                {
                    z1 = (double) (pos.getZ() + 1) + offset;
                }

                if (i == 3 && !world.getBlockState(pos.offset(EnumFacing.NORTH)).isBlockNormalCube())
                {
                    z1 = (double) (pos.getZ()) - offset;
                }

                if (i == 4 && !world.getBlockState(pos.offset(EnumFacing.EAST)).isBlockNormalCube())
                {
                    x1 = (double) (pos.getX() + 1) + offset;
                }

                if (i == 5 && !world.getBlockState(pos.offset(WEST)).isBlockNormalCube())
                {
                    x1 = (double) (pos.getX()) - offset;
                }

                if (x1 < (double) pos.getX() || x1 > (double) (pos.getY() + 1) || y1 < 0.0D || y1 > (double) (pos.getY() + 1) || z1 < (double) pos.getZ() || z1 > (double) (pos.getZ() + 1))
                {
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, x1, y1, z1, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPES);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos.down());
        Block block = state.getBlock();

        if (worldIn.getBlockState(pos.up()).getMaterial() != Material.WATER) return false;
        if (block.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)) return true;
        if (block == this)
        {
            int variant = state.getValue(TYPES);
            return highVariant(variant);
        }
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < NTYPES; ++i)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LEVEL, TYPES);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return CoralReef.ConfigHandler.coralLightLevel;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, state.getValue(TYPES));
    }

    public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
    {
        return canPlaceBlockAt(world, pos);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return CORAL;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos)
    {
        return world.getBlockState(pos);
    }

    protected boolean checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.canBlockStay(worldIn, pos, state))
        {
            return true;
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            return false;
        }
    }
}