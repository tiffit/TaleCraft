package de.longor.talecraft.voxelator.actions;

import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXAction;
import de.longor.talecraft.voxelator.Voxelator.ActionFactory;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

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
