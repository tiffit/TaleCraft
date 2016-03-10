package de.longor.talecraft.voxelbrush.actions;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelbrush.IAction;

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
	
	public String toString() {
		UniqueIdentifier IDr = GameRegistry.findUniqueIdentifierFor(block);
		UniqueIdentifier IDm = GameRegistry.findUniqueIdentifierFor(maskblock);
		// return "Replace ["+ID.modId+":"+ID.name+"/"+block.getMetaFromState(blockState)+"]";
		return
			"Masked Replace ["+
			IDr.modId+":"+IDr.name+"/"+block.getMetaFromState(blockState)+
			" with "+
			IDm.modId+":"+IDm.name+"/"+maskblock.getMetaFromState(maskblockState)+"]";
	}
	
	@Override
	public void act(World world, MutableBlockPos pos, int x, int y, int z) {
		IBlockState state = world.getBlockState(pos);
		
		if(state == maskblockState)
			world.setBlockState(pos, blockState, 3);
	}
	
}
