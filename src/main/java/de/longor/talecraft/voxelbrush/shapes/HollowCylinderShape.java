package de.longor.talecraft.voxelbrush.shapes;

import java.awt.image.Raster;

import de.longor.talecraft.voxelbrush.IShape;

public class HollowCylinderShape implements IShape {
	int sx;
	int sy;
	int sz;
	
	public int ey;
	public double radius;
	public double hollow;
	
	public HollowCylinderShape(int x, int y, int z, double rad, double hol, int height) {
		sx = x;
		sy = y;
		sz = z;
		radius = rad;
		hollow = hol;
		ey = height;
	}
	
	public HollowCylinderShape() {
		sx = 0;
		sy = 0;
		sz = 0;
		radius = 0;
		hollow = 0;
		ey = 0;
	}
	
	@Override
	public String getName() {
		return "hollowcylinder";
	}
	
	public String toString() {
		return "HollowCylinder ["+ey+", "+radius+", "+hollow+"]";
	}
	
	@Override
	public boolean isBlockInShape(int x, int y, int z) {
		int dx = x - sx;
		int dy = y - sy;
		int dz = z - sz;
		dy = dy < 0 ? -dy : dy;
		double dist = Math.sqrt(dx*dx + dz*dz);
		return (dy < ey && dy != ey) && (dist <= radius && dist > radius-hollow);
	}

	@Override
	public int[] getBounds() {
		int r = (int) (radius + 1.5d);
		return new int[]{
				sx - r +1, sy - ey +1, sz - r +1,
				sx + r -1, sy + ey -1, sz + r -1
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
