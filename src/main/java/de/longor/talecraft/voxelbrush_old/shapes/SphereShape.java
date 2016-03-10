package de.longor.talecraft.voxelbrush_old.shapes;

import de.longor.talecraft.voxelbrush_old.IShape;

public class SphereShape implements IShape {
	int sx;
	int sy;
	int sz;
	double radius;
	
	public SphereShape(int x, int y, int z, double rad) {
		sx = x;
		sy = y;
		sz = z;
		radius = rad;
	}
	
	public SphereShape() {
		sx = 0;
		sy = 0;
		sz = 0;
		radius = 0;
	}
	
	@Override
	public String getName() {
		return "sphere";
	}
	
	public String toString() {
		return "Sphere ["+radius+"]";
	}
	
	@Override
	public boolean isBlockInShape(int x, int y, int z) {
		int dx = x - sx;
		int dy = y - sy;
		int dz = z - sz;
		return Math.sqrt(dx*dx + dy*dy + dz*dz) <= radius;
	}
	
	@Override
	public int[] getBounds() {
		int r = (int) Math.ceil(radius);
		return new int[]{
				sx - r, sy - r, sz - r,
				sx + r, sy + r, sz + r
		};
	}

	@Override
	public int getOffsetX() {
		return sx;
	}

	@Override
	public int getOffsetY() {
		return sy;
	}

	@Override
	public int getOffsetZ() {
		return sz;
	}
	
}
