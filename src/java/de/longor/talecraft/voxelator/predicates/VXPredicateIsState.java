package de.longor.talecraft.voxelator.predicates;

import de.longor.talecraft.util.GObjectTypeHelper;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.Voxelator.FilterFactory;
import de.longor.talecraft.voxelator.params.BlockstateBrushParameter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateIsState extends VXPredicate {
	private static final BrushParameter[] PARAMS = new BrushParameter[]{
		new BlockstateBrushParameter("state", Blocks.AIR)
	};
	
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "is_state";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			return new VXPredicateIsState(GObjectTypeHelper.findBlockState(filterData.getString("state")));
		}
		
		@Override
		public NBTTagCompound newFilter(String[] parameters) {
			if(parameters.length == 1) {
				NBTTagCompound filterData = new NBTTagCompound();
				filterData.setString("type", getName());
				filterData.setString("state", parameters[0]);
				return filterData;
			}
			return null;
		}
		@Override
		public BrushParameter[] getParameters() {
			return PARAMS;
		}
	};
	
	private final IBlockState type;

	public VXPredicateIsState(IBlockState type) {
		this.type = type;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return fworld.getBlockState(pos).equals(type);
	}

}
