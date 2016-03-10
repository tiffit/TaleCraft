package de.longor.talecraft.voxelator.predicates;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;

public final class VXPredicateAverageSmooth extends VXPredicate {
	private final int size;
	private final Vec3i vec;
	
	public VXPredicateAverageSmooth(int size) {
		this.size = size;
		this.vec = new Vec3i(size, size, size);
	}
	
	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		int count = 0;
		int total = 0;
		float value = 0;
		Iterable<net.minecraft.util.BlockPos.MutableBlockPos> iterable = BlockPos.getAllInBoxMutable(pos.subtract(vec), pos.add(vec));
		for(final BlockPos checkpos : iterable) {
			if(!fworld.isAirBlock(checkpos)) {
				count++;
			}
			total++;
		}
		
		value = ((float)count / (float)total);
		
		return value > 0.5f;
	}
	
}
