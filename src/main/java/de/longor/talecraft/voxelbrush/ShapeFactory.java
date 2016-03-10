package de.longor.talecraft.voxelbrush;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import de.longor.talecraft.voxelbrush.shapes.BoxShape;
import de.longor.talecraft.voxelbrush.shapes.CylinderShape;
import de.longor.talecraft.voxelbrush.shapes.HollowCylinderShape;
import de.longor.talecraft.voxelbrush.shapes.HollowSphereShape;
import de.longor.talecraft.voxelbrush.shapes.SphereShape;

public class ShapeFactory {
	
	public static final IShape create(String shapeName, NBTTagCompound comp, BlockPos position) {
		if(shapeName == null)
			return null;
		if(shapeName.isEmpty())
			return null;
		if(comp == null)
			return null;
		
		int cx = position.getX() + comp.getInteger("offsetX");
		int cy = position.getY() + comp.getInteger("offsetY");
		int cz = position.getZ() + comp.getInteger("offsetZ");
		
		if("box".equals(shapeName)) {
			return new BoxShape(cx,cy,cz,
					comp.getInteger("width"),
					comp.getInteger("height"),
					comp.getInteger("length")
			);
		}
		
		if("cylinder".equals(shapeName)) {
			return new CylinderShape(cx, cy, cz, comp.getDouble("radius"), comp.getInteger("height"));
		}
		
		if("hollowcylinder".equals(shapeName)) {
			return new HollowCylinderShape(cx, cy, cz, comp.getDouble("radius"), comp.getDouble("hollow"), comp.getInteger("height"));
		}
		
		if("sphere".equals(shapeName)) {
			return new SphereShape(cx, cy, cz, comp.getDouble("radius"));
		}
		
		if("hollowsphere".equals(shapeName)) {
			return new HollowSphereShape(cx, cy, cz, comp.getDouble("radius"), comp.getDouble("hollow"));
		}
		
		return null;
	}
	
}
