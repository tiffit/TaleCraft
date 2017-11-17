package talecraft.blocks.deco;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.TaleCraftBlocks;
import talecraft.TaleCraftTabs;
import talecraft.blocks.TCBlock;

public class BlankBlock extends TCBlock {
	public static final IProperty<Integer> SUB = PropertyInteger.create("sub", 0, 15);
	public int blockLayer = 0;
	public boolean ignoreSimilarity = true;

	public BlankBlock(SoundType sound) {
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftDecorationTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SUB, Integer.valueOf(0)));
		this.setSoundType(sound);
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(SUB).intValue();
	}

	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(SUB, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SUB).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {SUB});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int j = 0; j < 16; ++j) {
			list.add(new ItemStack(this, 1, j));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return blockLayer == 0 ? BlockRenderLayer.SOLID : BlockRenderLayer.CUTOUT;
	}

	@Deprecated
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (this == TaleCraftBlocks.deco_glass_a) {
			if (worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() != this) {
				return true;
			}

			if (block == this)
			{
				return false;
			}
		}

		return !this.ignoreSimilarity && block == this ? false : super.shouldSideBeRendered(state, worldIn, pos, side);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return blockLayer == 0;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return blockLayer == 0;
	}

	@Deprecated
	@Override //TODO Doesn't work
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.NORMAL; // Can be moved by pistons
	}

}
