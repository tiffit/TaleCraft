package de.longor.talecraft.voxelator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.predicates.VXPredicateAND;
import de.longor.talecraft.voxelator.predicates.VXPredicateAverageSmooth;
import de.longor.talecraft.voxelator.predicates.VXPredicateBoxSmooth;
import de.longor.talecraft.voxelator.predicates.VXPredicateHasAirAbove;
import de.longor.talecraft.voxelator.predicates.VXPredicateHeightLimit;
import de.longor.talecraft.voxelator.predicates.VXPredicateIsSolid;
import de.longor.talecraft.voxelator.predicates.VXPredicateNOT;
import de.longor.talecraft.voxelator.predicates.VXPredicateOR;
import de.longor.talecraft.voxelator.predicates.VXPredicateRandom;
import de.longor.talecraft.voxelator.predicates.VXPredicateStateMatch;
import de.longor.talecraft.voxelator.predicates.VXPredicateTypeMatch;

public abstract class VXPredicate{
	/** A predicate that always returns true. **/
	public static final VXPredicate ALWAYS = new VXPredicate(){
		@Override
		public boolean test(
				BlockPos pos,
				BlockPos center,
				MutableBlockPos offset,
				CachedWorldDiff fworld
		) {
			return true;
		}
	};
	
	/** @return SEE IMPLEMENTATION. **/
	public abstract boolean test(
			BlockPos pos,
			BlockPos center,
			MutableBlockPos offset,
			CachedWorldDiff fworld
	);

	public static VXPredicate newAND(VXPredicate...predicates) {
		return new VXPredicateAND(predicates);
	}

	public static VXPredicate newOR(VXPredicate...predicates) {
		return new VXPredicateOR(predicates);
	}

	public static VXPredicate newNOT(VXPredicate predicate) {
		return new VXPredicateNOT(predicate);
	}
	
	public static VXPredicate newTypeMatch(Block type) {
		return new VXPredicateTypeMatch(type);
	}
	
	public static VXPredicate newStateMatch(IBlockState state) {
		return new VXPredicateStateMatch(state);
	}
	
	public static VXPredicate newHeightLimit(int height) {
		return new VXPredicateHeightLimit(height);
	}
	
	public static VXPredicate newAverageSmooth(int size) {
		return new VXPredicateAverageSmooth(size);
	}
	
	public static VXPredicate newBoxSmooth(int size) {
		return new VXPredicateBoxSmooth(size);
	}

	public static VXPredicate newHasAirAbove() {
		return new VXPredicateHasAirAbove();
	}

	public static VXPredicate newIsSolid() {
		return new VXPredicateIsSolid();
	}

	public static VXPredicate newRandom(float chance) {
		return new VXPredicateRandom(chance);
	}
	
}
