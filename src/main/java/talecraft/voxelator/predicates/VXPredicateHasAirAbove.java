package talecraft.voxelator.predicates;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.util.MutableBlockPos;
import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.CachedWorldDiff;
import talecraft.voxelator.VXPredicate;
import talecraft.voxelator.Voxelator.FilterFactory;

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
