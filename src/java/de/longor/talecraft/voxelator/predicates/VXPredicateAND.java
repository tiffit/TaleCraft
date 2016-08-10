package de.longor.talecraft.voxelator.predicates;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.Voxelator;
import de.longor.talecraft.voxelator.Voxelator.FilterFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;

public final class VXPredicateAND extends VXPredicate {
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "and";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			NBTTagList list = filterData.getTagList("filters", NBT.TAG_COMPOUND);
			int l = list.tagCount();
			VXPredicate[] a = new VXPredicate[l];
			
			for(int i = 0; i < l; i++) {
				a[i] = Voxelator.newFilter(list.getCompoundTagAt(i));
			}
			
			return new VXPredicateAND(a);
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

	public VXPredicateAND(VXPredicate... predicates) {
		this.predicates = predicates;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		for(VXPredicate predicate : predicates) {
			if(!predicate.test(pos, center, offset, fworld))
				return false;
		}
		return true;
	}
}
