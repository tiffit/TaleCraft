package talecraft.voxelator.predicates;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;
import talecraft.util.MutableBlockPos;
import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.CachedWorldDiff;
import talecraft.voxelator.VXPredicate;
import talecraft.voxelator.Voxelator;
import talecraft.voxelator.Voxelator.FilterFactory;

public final class VXPredicateOR extends VXPredicate {
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "or";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			NBTTagList list = filterData.getTagList("filters", NBT.TAG_COMPOUND);
			int l = list.tagCount();
			VXPredicate[] a = new VXPredicate[l];
			
			for(int i = 0; i < l; i++) {
				a[i] = Voxelator.newFilter(list.getCompoundTagAt(i));
			}
			
			return new VXPredicateOR(a);
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
	
	private final VXPredicate[] predicates;

	public VXPredicateOR(VXPredicate... predicates) {
		this.predicates = predicates;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		for(VXPredicate predicate : predicates) {
			if(predicate.test(pos, center, offset, fworld))
				return true;
		}
		return false;
	}
}
