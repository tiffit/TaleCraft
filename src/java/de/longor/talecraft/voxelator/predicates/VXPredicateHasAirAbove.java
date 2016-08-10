package de.longor.talecraft.voxelator.predicates;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.Voxelator.FilterFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateHasAirAbove extends VXPredicate {
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "has_air_above";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			return new VXPredicateHasAirAbove();
		}
		
		@Override
		public NBTTagCompound newFilter(String[] parameters) {
			NBTTagCompound filterData = new NBTTagCompound();
			filterData.setString("type", getName());
			return filterData;
		}
		@Override
		public BrushParameter[] getParameters() {
			return BrushParameter.NO_PARAMETERS;
		}
	};

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return fworld.isAirBlock(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()));
	}

}
