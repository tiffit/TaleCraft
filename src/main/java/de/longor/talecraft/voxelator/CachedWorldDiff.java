package de.longor.talecraft.voxelator;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.BlockSnapshot;

public class CachedWorldDiff implements IBlockAccess {
	private World world;
	private List<BlockSnapshot> changes;
	private List<BlockSnapshot> previous;
	
	public CachedWorldDiff(World world, List<BlockSnapshot> previous, List<BlockSnapshot> changes) {
		this.world = world;
		this.changes = changes;
		this.previous = previous;
	}
	
	public void setBlockState(BlockPos pos, IBlockState newState) {
		changes.add(new BlockSnapshot(world, pos, newState));
	}
	
	public IBlockState getBlockState(BlockPos pos) {
		return world.getBlockState(pos);
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return world.getTileEntity(pos);
	}

	@Override
	public int getCombinedLight(BlockPos pos, int p_175626_2_) {
		return world.getCombinedLight(pos, p_175626_2_);
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		return world.isAirBlock(pos);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
		return world.getBiomeGenForCoords(pos);
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		return world.extendedLevelsInChunkCache();
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return world.getStrongPower(pos, direction);
	}

	@Override
	public WorldType getWorldType() {
		return world.getWorldType();
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		return world.isSideSolid(pos, side, _default);
	}

	public void applyChanges(boolean physics) {
		long time = System.currentTimeMillis();
		int changeCount = 0;
		for(BlockSnapshot snapshot : changes) {
			previous.add(BlockSnapshot.getBlockSnapshot(world, snapshot.pos));
			snapshot.restore(true, physics);
			changeCount++;
		}
		
		TaleCraft.logger.info("Changed " + changeCount
				+ " block(s). Operation took " + ((double)System.currentTimeMillis()-(double)time)/1000D + "s."
		);
	}

}
