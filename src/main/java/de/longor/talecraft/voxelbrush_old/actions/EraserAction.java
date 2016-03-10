package de.longor.talecraft.voxelbrush_old.actions;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelbrush_old.IAction;

public class EraserAction implements IAction {
	
	public String toString() {
		return "Erase";
	}
	
	@Override
	public void act(World world, MutableBlockPos pos, int x, int y, int z) {
		world.setBlockToAir(pos);
	}
}
