package de.longor.talecraft.voxelbrush.shapes;

import de.longor.talecraft.voxelbrush.IShape;

public class HollowSphereShape implements IShape {
	int sx;
	int sy;
	int sz;
	double radius;
	double hollow;
	
	public HollowSphereShape(int x, int y, int z, double rad, double hol) {
		sx = x;
		sy = y;
		sz = z;
		radius = rad;
		hollow = hol;
	}
	
	public HollowSphereShape() {
		sx = 0;
		sy = 0;
		sz = 0;
		radius = 0;
		hollow = 0;
	}
	
	@Override
	public String getName() {
		return "hollowsphere";
	}
	
	public String toString() {
		return "HollowSphere ["+radius+", "+hollow+"]";
	}
	
	@Override
	public boolean isBlockInShape(int x, int y, int z) {
		int dx = x - sx;
		int dy = y - sy;
		int dz = z - sz;
		double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
		return  dist <= radius && dist > radius-hollow;
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
