package de.longor.talecraft.voxelator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.actions.VXActionGrassify;
import de.longor.talecraft.voxelator.actions.VXActionReplace;
import de.longor.talecraft.voxelator.actions.VXActionVariationsReplace;

public abstract class VXAction {
	
	/**
	 * Accepts a bunch of parameters and modifies the world.
	 * The 'how' is implementation specific.
	 **/
	public abstract void apply(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld);
	
	public static VXAction newReplaceAction(IBlockState state) {
		return new VXActionReplace(state);
	}
	
	public static VXAction newGrassifyAction() {
		return new VXActionGrassify();
	}

	public static VXAction newVariationReplaceAction(IBlockState... states) {
		return new VXActionVariationsReplace(states);
	}
	
}