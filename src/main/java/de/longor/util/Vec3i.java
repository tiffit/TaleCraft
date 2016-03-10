package de.longor.util;

import de.longor.talecraft.util.MutableBlockPos;

public class Vec3i {
	public int x;
	public int y;
	public int z;
	
	public Vec3i() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vec3i(final int X, final int Y, final int Z) {
		x = X;
		y = Y;
		z = Z;
	}
	
	public Vec3i(final Vec3i vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}
	
	public Vec3i(final Vec3i a, final Vec3i b) {
		x = a.x + b.x;
		y = a.y + b.y;
		z = a.z + b.z;
	}
	
	public Vec3i copy() {
		return new Vec3i(x, y, z);
	}
	
	public Vec3i setX(final int X) {
		x = X;
		return this;
	}
	
	public Vec3i setY(final int Y) {
		y = Y;
		return this;
	}
	
	public Vec3i setZ(final int Z) {
		z = Z;
		return this;
	}
	
	public Vec3i set(final int X, final int Y, final int Z) {
		x = X;
		y = Y;
		z = Z;
		return this;
	}
	
	public Vec3i set(final Vec3i vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}
	
	public Vec3i multiply(final Vec3i vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		return this;
	}
	
	public Vec3i move(final int offset_x, final int offset_y, final int offset_z) {
		x += offset_x;
		y += offset_y;
		z += offset_z;
		return this;
	}
	
	public Vec3i move(final Vec3i offset) {
		x += offset.x;
		y += offset.y;
		z += offset.z;
		return this;
	}
	
	public MutableBlockPos getAsBlockPos() {
		return new MutableBlockPos(x, y, z);
	}
	
}
