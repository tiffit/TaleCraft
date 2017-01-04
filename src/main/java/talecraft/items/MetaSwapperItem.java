package talecraft.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import talecraft.util.WorldHelper;
import talecraft.util.WorldHelper.BlockRegionIterator;

public class MetaSwapperItem extends TCItem {

	private static final BlockRegionIterator swapFunctionINCR = new BlockRegionIterator() {
		@Override
		public void $(World world, IBlockState state, BlockPos pos) {
			IBlockState oldState = state;
			Block block = oldState.getBlock();

			int oldMeta = block.getMetaFromState(oldState);
			int newMeta = (oldMeta + 1) & 0xF;

			IBlockState newState = block.getStateFromMeta(newMeta);
			world.setBlockState(pos, newState);
		}
	};

	private static final BlockRegionIterator swapFunctionDECR = new BlockRegionIterator() {
		@Override
		public void $(World world, IBlockState state, BlockPos pos) {
			IBlockState oldState = state;
			Block block = oldState.getBlock();

			int oldMeta = block.getMetaFromState(oldState);
			int newMeta = (oldMeta - 1) & 0xF;

			IBlockState newState = block.getStateFromMeta(newMeta);
			world.setBlockState(pos, newState);
		}
	};

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(worldIn.isRemote)
			return EnumActionResult.PASS;
		
		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		if(hand.equals(EnumHand.OFF_HAND)) {
			swapFunctionDECR.$(worldIn, worldIn.getBlockState(pos), pos);
		} else {
			swapFunctionINCR.$(worldIn, worldIn.getBlockState(pos), pos);
		}
		

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if(player.isSneaking()) {
			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);

			if(bounds == null) {
				player.sendMessage(new TextComponentString("No region selected with wand."));
			}
			
			WorldHelper.foreach(world, bounds, hand.equals(EnumHand.OFF_HAND) ? swapFunctionDECR : swapFunctionINCR);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}
