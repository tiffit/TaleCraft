package de.longor.talecraft.voxelator.shapes;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import de.longor.talecraft.util.BlockRegion;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXShape;

public class VXShapeBox extends VXShape {
	private final BlockPos position;
	private final int width;
	private final int height;
	private final int length;
	
	public VXShapeBox(BlockPos position, int width, int height, int length) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.length = length;
	}

	@Override
	public BlockPos getCenter() {
		return position;
	}

	@Override
	public BlockRegion getRegion() {
		return new BlockRegion(position, width, height, length);
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		int diffx = MathHelper.abs_int(pos.getX() - position.getX());
		int diffy = MathHelper.abs_int(pos.getY() - position.getY());
		int diffz = MathHelper.abs_int(pos.getZ() - position.getZ());
		return diffx < width && diffy < height && diffz < length;
	}

}
