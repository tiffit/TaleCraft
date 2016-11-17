package talecraft.voxelator.predicates;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.util.MutableBlockPos;
import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.CachedWorldDiff;
import talecraft.voxelator.VXPredicate;
import talecraft.voxelator.Voxelator.FilterFactory;
import talecraft.voxelator.params.FloatBrushParameter;

public final class VXPredicateRandom extends VXPredicate {
	private static final BrushParameter[] PARAMS = new BrushParameter[]{
		new FloatBrushParameter("chance", 0, 1, 0.5f)
	};
	
	public static FilterFactory FACTORY = new FilterFactory() {
		@Override
		public String getName() {
			return "random";
		}
		
		@Override
		public VXPredicate newFilter(NBTTagCompound filterData) {
			return new VXPredicateRandom(filterData.getFloat("chance"));
		}
		
		@Override
		public NBTTagCompound newFilter(String[] parameters) {
			if(parameters.length == 1) {
				NBTTagCompound filterData = new NBTTagCompound();
				filterData.setString("type", getName());
				filterData.setFloat("chance", Float.parseFloat(parameters[0]));
				return filterData;
			}
			return null;
		}
		@Override
		public BrushParameter[] getParameters() {
			return PARAMS;
		}
	};
	
	private final Random random;
	private final float chance;

	/**
	 * Creates a new random predicate.
	 * This predicate generates values between 0 and 1,
	 * then checks if the generated value is higher than this parameter.
	 * @param chance 0 < chance < 1
	 **/
	public VXPredicateRandom(float chance) {
		this.random = new Random();
		this.chance = chance;
	}

	@Override
	public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
		return random.nextFloat() > chance;
	}
}
