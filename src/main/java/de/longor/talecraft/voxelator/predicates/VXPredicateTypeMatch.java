package de.longor.talecraft.voxelator.predicates;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;

public final class VXPredicateTypeMatch extends VXPredicate {
	private final Block type;
	
	public VXPredicateTypeMatch(Block type) {
		this.type = type;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return fworld.getBlockState(pos).getBlock().equals(type);
	}

}
