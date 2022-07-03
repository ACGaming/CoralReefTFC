package primetoxinz.coralreef;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

/**
 * Created by tyler on 8/17/16.
 */
public class BlockReef extends Block
{
    public static final PropertyInteger TYPES = PropertyInteger.create("types", 0, 1);

    public BlockReef()
    {
        super(Material.ROCK);
        setCreativeTab(CreativeTabs.MISC);
        setHardness(1.5F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
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
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPES);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        return plantable.getPlantType(world, pos.offset(direction)) == BlockCoral.CORAL;
    }
}