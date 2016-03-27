package de.longor.talecraft.voxelator.predicates;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateHasAirAbove extends VXPredicate {

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return fworld.isAirBlock(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()));
	}

}
