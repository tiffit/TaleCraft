package de.longor.talecraft.voxelator.predicates;

import java.util.Random;

import net.minecraft.util.BlockPos;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;

public final class VXPredicateRandom extends VXPredicate {
	private final Random random;
	private final float chance;
	
	/**
	 * Creates a new random predicate.
	 * This predicate generates values between 0 and 1,
	 * then checks if the generated value is higher than this parameter.
	 * @param chance 0 < chance < 1
	 **/
	public VXPredicateRandom(float chance) {
		this.random = new Random();
		this.chance = chance;
	}
	
	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return random.nextFloat() > chance;
	}
}
