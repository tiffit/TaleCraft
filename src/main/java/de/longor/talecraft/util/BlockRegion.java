package de.longor.talecraft.util;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.Height;

public final class BlockRegion implements Iterable<BlockPos>{
	private final int minX;
	private final int minY;
	private final int minZ;
	private final int maxX;
	private final int maxY;
	private final int maxZ;
	private final int width;
	private final int height;
	private final int length;
	private final float centerX;
	private final float centerY;
	private final float centerZ;
	private final BlockPos center;
	private final BlockPos min;
	private final BlockPos max;
	
	public BlockRegion(int[] bounds) {
		this.minX = Math.min(bounds[0], bounds[3]);
		this.minY = Math.min(bounds[1], bounds[4]);
		this.minZ = Math.min(bounds[2], bounds[5]);
		this.maxX = Math.max(bounds[0], bounds[3]);
		this.maxY = Math.max(bounds[1], bounds[4]);
		this.maxZ = Math.max(bounds[2], bounds[5]);
		this.width = maxX - minX;
		this.height = maxY - minY;
		this.length = maxZ - minZ;
		this.centerX = (minX + maxX) / 2f;
		this.centerY = (minY + maxY) / 2f;
		this.centerZ = (minZ + maxZ) / 2f;
		this.center = new BlockPos(centerX, centerY, centerZ);
		this.min = new BlockPos(minX, minY, minZ);
		this.max = new BlockPos(maxX, maxY, maxZ);
	}
	
	public BlockRegion(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.width = maxX - minX;
		this.height = maxY - minY;
		this.length = maxZ - minZ;
		this.centerX = (minX + maxX) / 2f;
		this.centerY = (minY + maxY) / 2f;
		this.centerZ = (minZ + maxZ) / 2f;
		this.center = new BlockPos(centerX, centerY, centerZ);
		this.min = new BlockPos(minX, minY, minZ);
		this.max = new BlockPos(maxX, maxY, maxZ);
	}
	
	public BlockRegion(BlockPos center, int extent) {
		this.center = center;
		this.centerX = center.getX();
		this.centerY = center.getY();
		this.centerZ = center.getZ();
		this.width  = extent*2+1;
		this.height = extent*2+1;
		this.length = extent*2+1;
		
		this.min = new BlockPos(centerX-extent, centerY-extent, centerZ-extent);
		this.max = new BlockPos(centerX+extent, centerY+extent, centerZ+extent);
		this.minX = min.getX();
		this.minY = min.getY();
		this.minZ = min.getZ();
		this.maxX = max.getX();
		this.maxY = max.getY();
		this.maxZ = max.getZ();
	}

	public BlockRegion(BlockPos position, int width, int height, int length) {
		this.center = position;
		this.centerX = center.getX();
		this.centerY = center.getY();
		this.centerZ = center.getZ();
		this.width  = width/2;
		this.height = height/2;
		this.length = length/2;
		
		this.min = new BlockPos(centerX-width, centerY-height, centerZ-length);
		this.max = new BlockPos(centerX+width, centerY+height, centerZ+length);
		this.minX = min.getX();
		this.minY = min.getY();
		this.minZ = min.getZ();
		this.maxX = max.getX();
		this.maxY = max.getY();
		this.maxZ = max.getZ();
	}

	public final int getMinX() {
		return minX;
	}

	public final int getMinY() {
		return minY;
	}

	public final int getMinZ() {
		return minZ;
	}

	public final int getMaxX() {
		return maxX;
	}

	public final int getMaxY() {
		return maxY;
	}

	public final int getMaxZ() {
		return maxZ;
	}

	public final BlockPos getMin() {
		return min;
	}
	
	public final BlockPos getMax() {
		return max;
	}

	public final float getCenterX() {
		return centerX;
	}

	public final float getCenterY() {
		return centerY;
	}

	public final float getCenterZ() {
		return centerZ;
	}

	public final BlockPos getCenter() {
		return center;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final int getLength() {
		return length;
	}

	@Override
	public Iterator<BlockPos> iterator() {
		return new BlockRegionIterator(this);
	}
	
	public final class BlockRegionIterator implements Iterator<BlockPos> {
		private final MutableBlockPos XYZ;
		private final BlockRegion region;
		
		private BlockRegionIterator(BlockRegion blockRegion) {
			this.region = blockRegion;
			this.XYZ = new MutableBlockPos(0,0,0);
		}
		
		@Override
		public boolean hasNext() {
			// Get current position.
			int x = this.XYZ.getX();
			int y = this.XYZ.getY();
			int z = this.XYZ.getZ();
			
			// Compute next step!
			if (x < region.maxX) {
				++x;
			} else if (y < maxY) {
				x = minX;
				++y;
			} else if (z < maxZ) {
				x = minX;
				y = minY;
				++z;
			}
			
			// Mutate position.
			XYZ.set(x, y, z);
			return true;
		}
		
		@Override
		public BlockPos next() {
			return XYZ;
		}
	}
	
}
