package de.longor.talecraft.voxelator.shapes;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import de.longor.talecraft.util.BlockRegion;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXShape;

public class VXShapeSphere extends VXShape {
	private final BlockPos position;
	private final float radius;
	private final float radiusSquared;
	
	public VXShapeSphere(BlockPos position, float radius) {
		this.position = position;
		this.radius = radius;
		this.radiusSquared = radius*radius;
	}

	@Override
	public BlockPos getCenter() {
		return position;
	}

	@Override
	public BlockRegion getRegion() {
		return new BlockRegion(position, MathHelper.ceiling_float_int(radius));
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return position.distanceSq(pos) < radiusSquared;
	}

}
