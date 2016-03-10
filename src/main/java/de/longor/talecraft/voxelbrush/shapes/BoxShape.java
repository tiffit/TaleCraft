package de.longor.talecraft.voxelbrush.shapes;

import de.longor.talecraft.voxelbrush.IShape;

public class BoxShape implements IShape {
	int sx;
	int sy;
	int sz;
	
	int ex;
	int ey;
	int ez;
	
	public BoxShape(int x, int y, int z, int width, int height, int length) {
		sx = x;
		sy = y;
		sz = z;
		
		ex = width;
		ey = height;
		ez = length;
	}
	
	public BoxShape() {
		sx = 0;
		sy = 0;
		sz = 0;
		ex = 0;
		ey = 0;
		ez = 0;
	}
	
	@Override
	public String getName() {
		return "box";
	}
	
	public String toString() {
		return "Box ["+ex+", "+ey+", "+ez+"]";
	}
	
	@Override
	public boolean isBlockInShape(int x, int y, int z) {
		int dx = x - sx;
		int dy = y - sy;
		int dz = z - sz;
		
		dx = dx < 0 ? -dx : dx;
		dy = dy < 0 ? -dy : dy;
		dz = dz < 0 ? -dz : dz;
		
		return dx < ex && dy < ey && dz < ez;
	}
	
	@Override
	public int[] getBounds() {
		return new int[] {
			sx-ex+1,sy-ey+1,sz-ez+1,sx+ex-1,sy+ey-1,sz+ez-1
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
