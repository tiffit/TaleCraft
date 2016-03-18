package de.longor.talecraft.voxelator.actions;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXAction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class VXActionReplace extends VXAction {
	private final IBlockState state;

	public VXActionReplace(IBlockState state) {
		this.state = state;
	}

	@Override
	public void apply(BlockPos pos,BlockPos center,MutableBlockPos offset, CachedWorldDiff fworld) {
		fworld.setBlockState(pos, state);
	}
}