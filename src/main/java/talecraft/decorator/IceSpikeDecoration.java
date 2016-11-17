package talecraft.decorator;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

public class IceSpikeDecoration extends StaticDecoration {
	
	WorldGenIceSpike gen;
	
	public IceSpikeDecoration() {
		super();
		gen = new WorldGenIceSpike();
	}
	
	@Override
	public int plant(World world, BlockPos[] positions, NBTTagCompound options) {
		for(BlockPos pos : positions){
			world.setBlockState(pos.down(), Blocks.SNOW.getDefaultState());
			gen.generate(world, rand, pos);
		}
		return 0;
	}

	@Override
	public String name() {
		return "Ice Spike";
	}

}
