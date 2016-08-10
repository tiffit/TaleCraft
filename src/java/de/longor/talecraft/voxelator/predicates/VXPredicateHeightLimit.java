package de.longor.talecraft.voxelator.predicates;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.Voxelator.FilterFactory;
import de.longor.talecraft.voxelator.params.IntegerBrushParameter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateHeightLimit extends VXPredicate {
	private static final BrushParameter[] PARAMS = new BrushParameter[]{
		new IntegerBrushParameter("height", 0, 255, 5)
	};
	
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "limit_height";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			return new VXPredicateHeightLimit(filterData.getInteger("height"));
		}
		
		@Override
		public NBTTagCompound newFilter(String[] parameters) {
			if(parameters.length == 1) {
				NBTTagCompound filterData = new NBTTagCompound();
				filterData.setString("type", getName());
				filterData.setInteger("height", Integer.parseInt(parameters[0]));
				return filterData;
			}
			return null;
		}

		@Override
		public BrushParameter[] getParameters() {
			return PARAMS;
		}
	};
	
	private final int height;

	public VXPredicateHeightLimit(int height) {
		this.height = height;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return pos.getY() <= height;
	}

}
