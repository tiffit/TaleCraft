package de.longor.talecraft.voxelbrush_old.shapes;

import java.awt.image.Raster;

import de.longor.talecraft.voxelbrush_old.IShape;

public class CylinderShape implements IShape {
	int sx;
	int sy;
	int sz;
	
	public int ey;
	public double radius;
	
	public CylinderShape(int x, int y, int z, double rad, int height) {
		sx = x;
		sy = y;
		sz = z;
		radius = rad;
		ey = height;
	}
	
	public CylinderShape() {
		sx = 0;
		sy = 0;
		sz = 0;
		radius = 0;
		ey = 0;
	}
	
	@Override
	public String getName() {
		return "cylinder";
	}
	
	public String toString() {
		return "Cylinder ["+ey+", "+radius+"]";
	}
	
	@Override
	public boolean isBlockInShape(int x, int y, int z) {
		int dx = x - sx;
		int dy = y - sy;
		int dz = z - sz;
		dy = dy < 0 ? -dy : dy;
		return dy < ey && Math.sqrt(dx*dx + dz*dz) <= radius;
	}

	@Override
	public int[] getBounds() {
		int r = (int) (radius + 0.5d);
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
