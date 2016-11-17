package talecraft.voxelator.predicates;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.util.GObjectTypeHelper;
import talecraft.util.MutableBlockPos;
import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.CachedWorldDiff;
import talecraft.voxelator.VXPredicate;
import talecraft.voxelator.Voxelator.FilterFactory;
import talecraft.voxelator.params.BlockstateBrushParameter;

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
