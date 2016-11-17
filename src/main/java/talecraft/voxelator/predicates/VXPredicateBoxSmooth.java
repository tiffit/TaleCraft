package talecraft.voxelator.predicates;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import talecraft.util.MutableBlockPos;
import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.CachedWorldDiff;
import talecraft.voxelator.VXPredicate;
import talecraft.voxelator.Voxelator.FilterFactory;
import talecraft.voxelator.params.IntegerBrushParameter;

public final class VXPredicateBoxSmooth extends VXPredicate {
	private static final BrushParameter[] PARAMS = new BrushParameter[]{
		new IntegerBrushParameter("range", 2, 9, 6)
	};
	
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "boxsmooth";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			return new VXPredicateBoxSmooth(filterData.getInteger("range"));
		}
		
		@Override
		public NBTTagCompound newFilter(String[] parameters) {
			if(parameters.length == 1) {
				NBTTagCompound filterData = new NBTTagCompound();
				filterData.setString("type", getName());
				filterData.setInteger("range", Integer.parseInt(parameters[0]));
				return filterData;
			}
			return null;
		}

		@Override
		public BrushParameter[] getParameters() {
			return PARAMS;
		}
	};
	
	private final double sizeSquared;
	private final Vec3i vec;

	public VXPredicateBoxSmooth(int size) {
		this.sizeSquared = size * size;
		this.vec = new Vec3i(size, size-1, size);
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		int total = 0;
		float value = 0;
		Iterable<BlockPos.MutableBlockPos> iterable = BlockPos.getAllInBoxMutable(pos.subtract(vec), pos.add(vec));
		for(final BlockPos checkpos : iterable) {
			if(!fworld.isAirBlock(checkpos)) {
				value += checkpos.distanceSq(pos) / sizeSquared;
			}
			total++;
		}

		value /= total;
		value *= value;

		return value > 0.35f;
	}

}
