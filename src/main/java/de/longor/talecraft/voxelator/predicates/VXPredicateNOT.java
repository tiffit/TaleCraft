package de.longor.talecraft.voxelator.predicates;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateNOT extends VXPredicate {
	private final VXPredicate predicate;

	public VXPredicateNOT(VXPredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return !predicate.test(pos, center, offset, fworld);
	}
}
