package de.longor.talecraft.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;

/**
 * This is a actual mutable BlockPos class with a public constructor.
 * Use this whenever the creation of thousands of BlockPos is otherwise necessary.
 **/
public class MutableBlockPos extends BlockPos {
	public int x;
	public int y;
	public int z;
	private static final String __OBFID = "CL_00002329";

	public MutableBlockPos(int x_, int y_, int z_)
	{
		super(0, 0, 0);
		this.x = x_;
		this.y = y_;
		this.z = z_;
	}
	
	public MutableBlockPos(BlockPos blockpos) {
		super(0, 0, 0);
		this.x = blockpos.getX();
		this.y = blockpos.getY();
		this.z = blockpos.getZ();
	}
	
	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getZ()
	{
		return this.z;
	}

	public BlockPos crossProduct(Vec3i vec)
	{
		return super.crossProduct(vec);
	}
	
	public int x()
	{
		return this.x;
	}

	public int y()
	{
		return this.y;
	}

	public int z()
	{
		return this.z;
	}

	public Vec3i cross(Vec3i vec)
	{
		return super.crossProduct(vec);
	}

	public void set(int x2, int y2, int z2) {
		x = x2;
		y = y2;
		z = z2;
	}

	public void set(BlockPos pos) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
	}

	public void __add(BlockPos pos) {
		x += pos.getX();
		y += pos.getY();
		z += pos.getZ();
	}

	public void __sub(BlockPos pos) {
		x -= pos.getX();
		y -= pos.getY();
		z -= pos.getZ();
	}
	
}