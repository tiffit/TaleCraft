package de.longor.talecraft.voxelbrush_old;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.util.WorldHelper;
import de.longor.talecraft.voxelator.VXAction;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.VXShape;
import de.longor.talecraft.voxelator.Voxelator;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoxelBrush {

	public static final void func(World world, NBTTagCompound vbData, BlockPos position) {

		if(Boolean.TRUE.booleanValue()) {
			VXShape     shape     = VXShape.newBox(position, 16);
			VXPredicate predicate = VXPredicate.newAND(VXPredicate.newIsSolid(), VXPredicate.newHasAirAbove());
			// VXAction    action    = VXAction.newVariationReplaceAction(Blocks.grass.getStateFromMeta(0), Blocks.grass.getStateFromMeta(1));
			VXAction    action    = VXAction.newGrassifyAction();
			Voxelator.apply(shape, predicate, action, world);
			return;
		}


		IShape shape = ShapeFactory.create(null, vbData.getCompoundTag("shape"), position);

		if(shape == null) {
			TaleCraft.logger.error("VoxelBrush does not have a shape: " + vbData);
			return;
		}

		IAction action = ActionFactory.create(null, vbData.getCompoundTag("action"));

		if(action == null) {
			TaleCraft.logger.error("VoxelBrush does not have a action: " + vbData);
			return;
		}

		int[] bounds = shape.getBounds();
		int ix = bounds[0];
		int iy = bounds[1];
		int iz = bounds[2];
		int ax = bounds[3];
		int ay = bounds[4];
		int az = bounds[5];

		if(iy < 0) {
			iy = 0;
		}

		if(ay > 255) {
			ay = 255;
		}

		TaleCraft.logger.info("painting -> " + Arrays.toString(bounds) + " " + vbData);

		MutableBlockPos pos = new MutableBlockPos(0,0,0);
		int changes = 0;
		long time = System.currentTimeMillis();
		for(int y = iy; y <= ay; y++) {
			pos.y = y;
			for(int z = iz; z <= az; z++) {
				pos.z = z;
				for(int x = ix; x <= ax; x++) {
					pos.x = x;

					if(!shape.isBlockInShape(x, y, z))
						continue;

					action.act(world, pos, x, y, z);
					changes++;
				}
			}
		}

		TaleCraft.logger.info("done painting!"
				+ " changes: " + changes
				+ ", time: " + ((double)System.currentTimeMillis()-(double)time)/1000D + "s"
		);
	}

    @SideOnly(Side.CLIENT)
	public static void func(NBTTagCompound vbData, List tooltip) {
		// TODO: Implement VoxelBrush Tooltip

    	NBTTagCompound compShape = vbData.getCompoundTag("shape");
    	NBTTagCompound compAction = vbData.getCompoundTag("action");

    	IShape shape = ShapeFactory.create(compShape.getString("type"), compShape, new BlockPos(0, 0, 0));
    	IAction action = ActionFactory.create(compAction.getString("type"), compAction);

    	tooltip.add("Shape: " + shape);
    	tooltip.add("Action: " + action);
    	tooltip.add("");

	}

}
