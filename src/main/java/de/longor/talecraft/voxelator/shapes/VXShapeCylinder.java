package de.longor.talecraft.voxelator.shapes;

import de.longor.talecraft.util.BlockRegion;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class VXShapeCylinder extends VXShape {
	private final BlockPos position;
	private final float radius;
	private final float radiusSquared;
	private final int height;

	public VXShapeCylinder(BlockPos position, float radius, int height) {
		this.position = position;
		this.radius = radius;
		this.radiusSquared = radius*radius;
		this.height = height;
	}

	@Override
	public BlockPos getCenter() {
		return position;
	}

	@Override
	public BlockRegion getRegion() {
		int sx = position.getX();
		int sy = position.getY();
		int sz = position.getZ();
		int r = (int) (radius + 0.5d);
		return new BlockRegion(
		sx - r +1, sy, sz - r +1,
		sx + r -1, sy + height -1, sz + r -1
		);
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int sx = position.getX();
		int sy = position.getY();
		int sz = position.getZ();
		int dx = x - sx;
		int dy = y - sy;
		int dz = z - sz;
		dy = dy < 0 ? -dy : dy;
		return dy < height && Math.sqrt(dx*dx + dz*dz) <= radius;
	}

}
