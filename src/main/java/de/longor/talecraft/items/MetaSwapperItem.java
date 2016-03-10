package de.longor.talecraft.items;

import de.longor.talecraft.util.WorldHelper;
import de.longor.talecraft.util.WorldHelper.BlockRegionIterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MetaSwapperItem extends TCItem {

	private static final BlockRegionIterator swapFunction = new BlockRegionIterator(){
		@Override public void $(World world, IBlockState state, BlockPos position) {
			IBlockState oldState = state;
			Block block = oldState.getBlock();
			
			int oldMeta = block.getMetaFromState(oldState);
			int newMeta = (oldMeta + 1) & 0xF;
			
			IBlockState newState = block.getStateFromMeta(newMeta);
			world.setBlockState(position, newState);
		}
	};

	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote)
			return true;
		
		swapFunction.$(worldIn, worldIn.getBlockState(pos), pos);
		
		return true;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(world.isRemote)
			return stack;
		
		if(player.isSneaking()) {
			int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
			
			if(bounds == null) {
				player.addChatMessage(new ChatComponentText("No region selected with wand."));
			}
			
			WorldHelper.foreach(world, bounds, swapFunction);
			return stack;
		}
		
		return stack;
	}

}
