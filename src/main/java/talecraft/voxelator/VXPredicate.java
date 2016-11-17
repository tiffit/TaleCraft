package talecraft.voxelator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import talecraft.util.MutableBlockPos;
import talecraft.voxelator.predicates.VXPredicateAND;
import talecraft.voxelator.predicates.VXPredicateAverageSmooth;
import talecraft.voxelator.predicates.VXPredicateBoxSmooth;
import talecraft.voxelator.predicates.VXPredicateHasAirAbove;
import talecraft.voxelator.predicates.VXPredicateHeightLimit;
import talecraft.voxelator.predicates.VXPredicateIsSolid;
import talecraft.voxelator.predicates.VXPredicateIsState;
import talecraft.voxelator.predicates.VXPredicateIsType;
import talecraft.voxelator.predicates.VXPredicateNOT;
import talecraft.voxelator.predicates.VXPredicateOR;
import talecraft.voxelator.predicates.VXPredicateRandom;

public abstract class VXPredicate{
	/**
	 * A predicate that always returns true.
	 * @deprecated Use instead: {@code de.longor.talecraft.voxelator.predicates.VXPredicateAlways}
	 **/
	@Deprecated
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
		return new VXPredicateIsType(type);
	}

	public static VXPredicate newStateMatch(IBlockState state) {
		return new VXPredicateIsState(state);
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
