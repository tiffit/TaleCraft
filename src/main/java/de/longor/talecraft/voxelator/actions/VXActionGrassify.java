package de.longor.talecraft.voxelator.actions;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXAction;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class VXActionGrassify extends VXAction {

	@Override
	public void apply(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		fworld.setBlockState(pos, Blocks.grass.getDefaultState());
		fworld.setBlockState(pos.down(1), Blocks.dirt.getDefaultState());
		fworld.setBlockState(pos.down(2), Blocks.dirt.getDefaultState());
	}

}
