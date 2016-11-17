package talecraft.voxelator;

import net.minecraft.util.math.BlockPos;
import talecraft.util.MutableBlockPos;

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
