package de.longor.talecraft.voxelbrush.actions;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelbrush.IAction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MaskReplaceAction implements IAction{
	Block block;
	IBlockState blockState;
	Block maskblock;
	IBlockState maskblockState;

	public MaskReplaceAction(Block block, IBlockState blockState, Block maskblock, IBlockState maskblockState) {
		this.block = block;
		this.blockState = blockState;
		this.maskblock = maskblock;
		this.maskblockState = maskblockState;
	}

	@Override
	public String toString() {
		ResourceLocation IDr = new ResourceLocation(block.getRegistryName());
		ResourceLocation IDm = new ResourceLocation(maskblock.getRegistryName());
		return
				"Masked Replace ["+
				IDr.getResourceDomain()+":"+IDr.getResourcePath()+"/"+block.getMetaFromState(blockState)+
				" with "+
				IDm.getResourceDomain()+":"+IDm.getResourcePath()+"/"+maskblock.getMetaFromState(maskblockState)+"]";
	}

	@Override
	public void act(World world, MutableBlockPos pos, int x, int y, int z) {
		IBlockState state = world.getBlockState(pos);

		if(state == maskblockState)
			world.setBlockState(pos, blockState, 3);
	}

}
