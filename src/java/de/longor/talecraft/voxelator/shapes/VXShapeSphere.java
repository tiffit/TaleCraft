package de.longor.talecraft.voxelator.shapes;

import de.longor.talecraft.util.BlockRegion;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.CachedWorldDiff;
import de.longor.talecraft.voxelator.VXShape;
import de.longor.talecraft.voxelator.Voxelator.ShapeFactory;
import de.longor.talecraft.voxelator.params.BooleanBrushParameter;
import de.longor.talecraft.voxelator.params.FloatBrushParameter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class VXShapeSphere extends VXShape {
	private static final BrushParameter[] PARAMS = new BrushParameter[]{
		new FloatBrushParameter("radius", 1, 64, 5),
		new BooleanBrushParameter("hollow", false)
	};
	
	public static ShapeFactory FACTORY = new ShapeFactory() {
		@Override
		public String getName() {
			return "sphere";
		}
		@Override
		public VXShape newShape(NBTTagCompound shapeData, BlockPos origin) {
			int px = shapeData.getInteger("position.x") + origin.getX();
			int py = shapeData.getInteger("position.y") + origin.getY();
			int pz = shapeData.getInteger("position.z") + origin.getZ();
			
			float r = shapeData.getFloat("radius");
			boolean hollow = shapeData.getBoolean("hollow");
			
			return new VXShapeSphere(new BlockPos(px, py, pz), r, hollow);
		}
		
		@Override
		public NBTTagCompound newShape(String[] parameters) {
			if(parameters.length == 1) {
				NBTTagCompound shapeData = new NBTTagCompound();
				shapeData.setString("type", getName());
				shapeData.setFloat("radius", Float.parseFloat(parameters[0]));
				shapeData.setBoolean("hollow", Boolean.parseBoolean(parameters[1]));
				return shapeData;
			}
			
			return null;
		}
		@Override
		public BrushParameter[] getParameters() {
			return PARAMS;
		}
	};
	
	private final BlockPos position;
	private final float radius;
	private final float radiusSquared;
	private final boolean hollow;

	public VXShapeSphere(BlockPos position, float radius, boolean hollow) {
		this.position = position;
		this.radius = radius;
		this.radiusSquared = radius*radius;
		this.hollow = hollow;
	}

	@Override
	public BlockPos getCenter() {
		return position;
	}

	@Override
	public BlockRegion getRegion() {
		return new BlockRegion(position, MathHelper.ceiling_float_int(radius));
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return position.distanceSq(pos) < radiusSquared && (hollow ? !(new VXShapeSphere(position, radius-1, false)).test(pos, center, offset, fworld) : true);
	}

}
