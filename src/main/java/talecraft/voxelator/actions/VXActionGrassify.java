package talecraft.voxelator.actions;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.util.MutableBlockPos;
import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.CachedWorldDiff;
import talecraft.voxelator.VXAction;
import talecraft.voxelator.Voxelator.ActionFactory;

public class VXActionGrassify extends VXAction {
	
	public static final ActionFactory FACTORY = new ActionFactory() {
		@Override public String getName() {
			return "grassify";
		}
		@Override public VXAction newAction(NBTTagCompound actionData) {
			return new VXActionGrassify();
		}
		@Override public NBTTagCompound newAction(String[] parameters) {
			NBTTagCompound actionData = new NBTTagCompound();
			actionData.setString("type", getName());
			return actionData;
		}
		@Override
		public BrushParameter[] getParameters() {
			return BrushParameter.NO_PARAMETERS;
		}
	};
	
	public VXActionGrassify() {
	}

	@Override
	public void apply(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		fworld.setBlockState(pos, Blocks.GRASS.getDefaultState());
		fworld.setBlockState(pos.down(1), Blocks.DIRT.getDefaultState());
		fworld.setBlockState(pos.down(2), Blocks.DIRT.getDefaultState());
	}

}
