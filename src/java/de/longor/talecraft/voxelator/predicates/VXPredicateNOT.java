package de.longor.talecraft.voxelator.predicates;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.Voxelator;
import de.longor.talecraft.voxelator.Voxelator.FilterFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateNOT extends VXPredicate {
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "not";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			VXPredicate filter = Voxelator.newFilter(filterData.getCompoundTag("filter"));
			
			return new VXPredicateNOT(filter);
		}
		
		@Override
		public NBTTagCompound newFilter(String[] parameters) {
			throw new UnsupportedOperationException("Not Yet Implemented!");
		}
		
		@Override
		public BrushParameter[] getParameters() {
			return BrushParameter.NO_PARAMETERS;
		}
	};
	
	private final VXPredicate predicate;

	public VXPredicateNOT(VXPredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return !predicate.test(pos, center, offset, fworld);
	}
}
