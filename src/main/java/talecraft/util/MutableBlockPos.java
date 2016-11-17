package talecraft.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

/**
 * This is a actual mutable BlockPos class with a public constructor.
 * Use this whenever the creation of thousands of BlockPos is otherwise necessary.
 */
public class MutableBlockPos extends BlockPos {
	public int x;
	public int y;
	public int z;

	public MutableBlockPos(int x, int y, int z)
	{
		super(0, 0, 0);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public MutableBlockPos(BlockPos pos) {
		super(0, 0, 0);
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getZ() {
		return this.z;
	}

	@Override
	public BlockPos crossProduct(Vec3i vec) {
		return super.crossProduct(vec);
	}

	public int x() {
		return this.x;
	}

	public int y() {
		return this.y;
	}

	public int z() {
		return this.z;
	}

	public Vec3i cross(Vec3i vec) {
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