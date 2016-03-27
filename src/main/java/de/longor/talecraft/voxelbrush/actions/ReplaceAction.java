package de.longor.talecraft.voxelbrush.actions;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelbrush.IAction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ReplaceAction implements IAction{
	Block block;
	IBlockState blockState;

	public ReplaceAction(Block block, IBlockState blockState) {
		this.block = block;
		this.blockState = blockState;
	}

	@Override
	public String toString() {
		ResourceLocation ID = new ResourceLocation(block.getRegistryName());
		return "Replace ["+ID.getResourceDomain()+":"+ID.getResourcePath()+"/"+block.getMetaFromState(blockState)+"]";
	}

	@Override
	public void act(World world, MutableBlockPos pos, int x, int y, int z) {
		world.setBlockState(pos, blockState, 3);
	}

}
