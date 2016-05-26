package de.longor.talecraft.voxelbrush;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.VXAction.VXActions;
import de.longor.talecraft.voxelator.VXShape.VXShapes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoxelBrush {

	public static final void func(World world, NBTTagCompound vbData, BlockPos position) {
		// TODO: Implement VoxelBrush Action

		NBTTagCompound shapeData = vbData.getCompoundTag("shape");
		IShape shape = ShapeFactory.create(shapeData.getString("type"), shapeData, position);

		if(shape == null) {
			TaleCraft.logger.error("VoxelBrush does not have a shape: " + vbData);
			return;
		}

		NBTTagCompound actionData = vbData.getCompoundTag("action");
		IAction action = ActionFactory.create(actionData.getString("type"), actionData);

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

		TaleCraft.logger.info("painting -> " + Arrays.toString(bounds) + " " + shapeData + " " + shape);

		MutableBlockPos pos = new MutableBlockPos(0,0,0);
		for(int y = iy; y <= ay; y++) {
			pos.y = y;
			for(int z = iz; z <= az; z++) {
				pos.z = z;
				for(int x = ix; x <= ax; x++) {
					pos.x = x;

					if(!shape.isBlockInShape(x, y, z))
						continue;

					action.act(world, pos, x, y, z);
				}
			}
		}

		TaleCraft.logger.info("done painting! -> " + Arrays.toString(bounds) + " " + vbData);
	}

	@SideOnly(Side.CLIENT)
	public static void addDesc(NBTTagCompound data, List<String> tooltip) {
		if(data.hasNoTags()){
			tooltip.add(TextFormatting.RED + "Not Defined Yet");
			return;
		}
		VXActions action = VXActions.get(data.getInteger("action"));
		VXShapes shape = VXShapes.get(data.getInteger("shape"));
		String shapeData = "";
		if(shape == VXShapes.Sphere) shapeData = "[r=" + data.getFloat("radius") + "]";
		if(shape == VXShapes.Box) shapeData  = "[w=" + data.getInteger("width") + ",h=" + data.getInteger("height") + ",l="+ data.getInteger("length") + "]";
		if(shape == VXShapes.Cylinder) shapeData = "[r=" + data.getFloat("radius") + ",h=" + data.getInteger("height") + "]";	
		tooltip.add("Shape: " + shape.toString() + shapeData);
		tooltip.add("Action: " + action.toString());
		tooltip.add("");

	}

}
