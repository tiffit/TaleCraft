package de.longor.talecraft.voxelator;

import de.longor.talecraft.util.MutableBlockPos;
import net.minecraft.util.math.BlockPos;

public abstract class VXAction {

	public static enum VXActions{
		Grassify("Grassify"), Replace("Replace"), VariationsReplace("Variations Replace");
		
		String name;
		
		VXActions(String name){
			this.name = name;
		}
		
		public int getID(){
			return ordinal();
		}
		
		@Override
		public String toString(){
			return name;
		}
		
		public static VXActions get(int id){
			return VXActions.values()[id];
		}
	}
	
	/**
	 * Accepts a bunch of parameters and modifies the world.
	 * The 'how' is implementation specific.
	 **/
	public abstract void apply(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld);

}
