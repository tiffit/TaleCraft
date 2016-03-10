package de.longor.talecraft.voxelbrush.actions;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelbrush.IAction;

public class GrassifyAction implements IAction {
	IBlockState airBlockState;
	IBlockState dirtBlockState;
	IBlockState grassBlockState;
	
	public GrassifyAction() {
		airBlockState = Blocks.air.getDefaultState();
		dirtBlockState = Blocks.dirt.getDefaultState();
		grassBlockState = Blocks.grass.getDefaultState();
	}
	
	public String toString() {
		return "Grassify";
	}
	
	@Override
	public void act(World world, MutableBlockPos pos, int x, int y, int z) {
		
		pos.y = y + 1;
		IBlockState above = world.getBlockState(pos);
		
		pos.y = y;
		IBlockState self = world.getBlockState(pos);
		
		if(above == airBlockState && isValidReplace(self)) {
			world.setBlockState(pos, grassBlockState);
			
			pos.y--; world.setBlockState(pos, dirtBlockState);
			pos.y--; world.setBlockState(pos, dirtBlockState);
		}
		
	}
	
	private boolean isValidReplace(IBlockState self) {
		if(self == Blocks.stone.getDefaultState()) {
			return true;
		}
		if(self == Blocks.sand.getDefaultState()) {
			return true;
		}
		if(self == Blocks.gravel.getDefaultState()) {
			return true;
		}
		if(self == Blocks.dirt.getDefaultState()) {
			return true;
		}
		if(self == Blocks.sandstone.getDefaultState()) {
			return true;
		}
		
//		if(self == Blocks.grass.getDefaultState()) {
//			return true;
//		}
		
		return false;
	}
	
}
