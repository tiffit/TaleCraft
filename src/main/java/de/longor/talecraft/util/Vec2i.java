package de.longor.talecraft.util;


public class Vec2i {
	public int x;
	public int y;
	
	public Vec2i() {
		x = 0;
		y = 0;
	}
	
	public Vec2i(final int X, final int Y) {
		x = X;
		y = Y;
	}
	
	public Vec2i(final Vec2i vec) {
		x = vec.x;
		y = vec.y;
	}
	
	public Vec2i(final Vec2i a, final Vec2i b) {
		x = a.x + b.x;
		y = a.y + b.y;
	}
	
	public Vec2i copy() {
		return new Vec2i(x,y);
	}
	
	public Vec2i setX(final int X) {
		x = X;
		return this;
	}
	
	public Vec2i setY(final int Y) {
		y = Y;
		return this;
	}
	
	public Vec2i set(final int X, final int Y) {
		x = X;
		y = Y;
		return this;
	}
	
	public Vec2i set(final Vec2i vec) {
		x = vec.x;
		y = vec.y;
		return this;
	}
	
	public Vec2i multiply(final Vec2i vec) {
		x *= vec.x;
		y *= vec.y;
		return this;
	}
	
	public Vec2i move(final int offset_x, final int offset_y) {
		x += offset_x;
		y += offset_y;
		return this;
	}
	
	public Vec2i move(final Vec2i offset) {
		x += offset.x;
		y += offset.y;
		return this;
	}
	
	/**
	 * Returns a new blockpos were the y-Axis of this point is swapped to the z-Axis.
	 * @return A new BlockPos that has the coordinates (x,0,y).
	 **/
	public MutableBlockPos getAsBlockPos() {
		return new MutableBlockPos(x, 0, y);
	}
	
}
