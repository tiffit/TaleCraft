package de.longor.talecraft.util;

import java.util.function.Consumer;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldHelper {
	public static final void foreach(World world, int[] bounds, BlockRegionIterator function) {
		int ix = bounds[0];
		int iy = bounds[1];
		int iz = bounds[2];
		int ax = bounds[3];
		int ay = bounds[4];
		int az = bounds[5];
		foreach(world, ix, iy, iz, ax, ay, az, function);
	}
	
	/**
	 * Iterates trough every block and executes a given action.
	 * WARNING: This method assumes that the bounds given as parameters are already validated!
	 **/
	public static final void foreach(World world, int ix, int iy, int iz, int ax, int ay, int az, BlockRegionIterator function) {
		if(world == null)
			return;
		if(function == null)
			return;
		
		MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
		
		for(int y = iy; y <= ay; y++) {
			for(int z = iz; z <= az; z++) {
				for(int x = ix; x <= ax; x++) {
					pos.set(x, y, z);
					IBlockState state = world.getBlockState(pos);
					function.$(world, state, pos);
				}
			}
		}
	}
	
	public static final void fill(World world, int[] bounds, IBlockState block) {
		fill(world, bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], block);
	}
	
	public static final void replace(World world, int[] bounds, IBlockState block, IBlockState mask) {
		replace(world, bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], block, mask);
	}
	
	/**
	 * Iterates trough every block and replaces it with the given block.
	 * WARNING: This method assumes that the bounds given as parameters are already validated!
	 **/
	public static final void fill(World world, int ix, int iy, int iz, int ax, int ay, int az, IBlockState block) {
		if(world == null)
			return;
		
		MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
		
		for(int y = iy; y <= ay; y++) {
			for(int z = iz; z <= az; z++) {
				for(int x = ix; x <= ax; x++) {
					pos.set(x, y, z);
					world.setBlockState(new BlockPos(pos), block);
				}
			}
		}
	}
	
	public static final void replace(World world, int ix, int iy, int iz, int ax, int ay, int az, IBlockState block, IBlockState mask) {
		if(world == null)
			return;
		
		MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
		
		for(int y = iy; y <= ay; y++) {
			for(int z = iz; z <= az; z++) {
				for(int x = ix; x <= ax; x++) {
					pos.set(x, y, z);
					if(world.getBlockState(pos).equals(mask))
						world.setBlockState(new BlockPos(pos), block);
				}
			}
		}
	}
	
	/**
	 * Dummy functional interface with only one method.
	 **/
	public static interface BlockRegionIterator {
		void $(World world, IBlockState state, BlockPos position);
	}
	
}
