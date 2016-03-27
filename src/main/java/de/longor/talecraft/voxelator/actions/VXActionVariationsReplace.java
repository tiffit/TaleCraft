package de.longor.talecraft.voxelator.actions;

import java.util.Random;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXAction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class VXActionVariationsReplace extends VXAction {
	private final IBlockState[] states;
	private final Random random;

	public VXActionVariationsReplace(IBlockState... states) {
		this.states = states;
		this.random = new Random();
	}

	@Override
	public void apply(
			BlockPos pos,
			BlockPos center,
			MutableBlockPos offset,
			CachedWorldDiff fworld
			) {
		fworld.setBlockState(pos, states[random.nextInt(states.length)]);
	}
}
