package de.longor.talecraft.voxelbrush;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.util.WorldHelper;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
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
	public static void func(NBTTagCompound vbData, List tooltip) {
		// TODO: Implement VoxelBrush Tooltip
    	
    	NBTTagCompound compShape = vbData.getCompoundTag("shape");
    	NBTTagCompound compAction = vbData.getCompoundTag("action");
    	
    	IShape shape = ShapeFactory.create(compShape.getString("type"), compShape, new BlockPos(0, 0, 0));
    	IAction action = ActionFactory.create(compAction.getString("type"), compAction);
    	
    	tooltip.add("Shape: " + shape);
    	tooltip.add("Action: " + action);
    	
	}
	
}
