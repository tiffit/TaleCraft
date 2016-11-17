package talecraft.decorator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class TreeDecoration extends StaticDecoration {
	
	WorldGenTrees gen;
	
	public TreeDecoration() {
		super();
		gen = new WorldGenTrees(true);
	}
	
	@Override
	public int plant(World world, BlockPos[] positions, NBTTagCompound options) {
		for(BlockPos pos : positions){
			gen.generate(world, rand, pos);
		}
		return 0;
	}

	@Override
	public String name() {
		return "Tree";
	}

}
