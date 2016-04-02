package de.longor.talecraft.voxelator;

import de.longor.talecraft.util.BlockRegion;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.shapes.VXShapeBox;
import de.longor.talecraft.voxelator.shapes.VXShapeSphere;
import net.minecraft.util.math.BlockPos;

public abstract class VXShape {
	
	public static enum VXShapes{
		Sphere("Sphere"), Box("Box"), Cylinder("Cylinder");
		
		String name;
		
		VXShapes(String name){
			this.name = name;
		}
	
		public String toString(){
			return name;
		}
		
		public int getID(){
			return ordinal();
		}
		
		public static VXShapes get(int id){
			return VXShapes.values()[id];
		}
		
	}
	
	public abstract BlockPos getCenter();
	public abstract BlockRegion getRegion();

	/** @return TRUE, if the given <i>pos</i> is inside the shape. FALSE if not. **/
	public abstract boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld);

	public static VXShape newSphere(BlockPos position, float radius) {
		return new VXShapeSphere(position, radius);
	}

	public static VXShape newBox(BlockPos position, int size) {
		return new VXShapeBox(position, size, size, size);
	}

	public static VXShape newBox(BlockPos position, int width, int height) {
		return new VXShapeBox(position, width, height, width);
	}

	public static VXShape newBox(BlockPos position, int width, int height, int length) {
		return new VXShapeBox(position, width, height, length);
	}

}