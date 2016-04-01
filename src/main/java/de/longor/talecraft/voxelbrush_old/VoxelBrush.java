package de.longor.talecraft.voxelbrush_old;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.VXAction;
import de.longor.talecraft.voxelator.VXAction.VXActions;
import de.longor.talecraft.voxelator.VXPredicate;
import de.longor.talecraft.voxelator.VXShape;
import de.longor.talecraft.voxelator.VXShape.VXShapes;
import de.longor.talecraft.voxelator.Voxelator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoxelBrush {

	//Enable to test voxelator (voxelbrush rewrite)
	public static final boolean VOXELATOR_TESTING = false;

	public static final void func(World world, NBTTagCompound vbData, BlockPos position) {

		if(VOXELATOR_TESTING) {
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
	public static void addDesc(NBTTagCompound data, List<String> tooltip) {
		if(data.hasNoTags()){
			tooltip.add(TextFormatting.RED + "Not Defined Yet");
			return;
		}
		//IShape shape = ShapeFactory.create(compShape.getString("type"), compShape, new BlockPos(0, 0, 0));
		VXActions action = VXActions.get(data.getInteger("action"));
		VXShapes shape = VXShapes.get(data.getInteger("shape"));
		String shapeData = "";
		if(shape == VXShapes.Sphere) shapeData = "[r=" + data.getFloat("radius") + "]";
		if(shape == VXShapes.Box) shapeData  = "[w=" + data.getInteger("width") + ",h=" + data.getInteger("height") + ",l="+ data.getInteger("lenght") + "]";
		tooltip.add("Shape: " + shape.toString() + shapeData);
		tooltip.add("Action: " + action.toString());
		tooltip.add("");

	}

}
