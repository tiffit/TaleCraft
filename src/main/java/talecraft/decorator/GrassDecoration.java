package talecraft.decorator;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrassDecoration extends StaticDecoration {

	public GrassDecoration() {
	}

	@Override
	public int plant(World world, BlockPos[] positions, NBTTagCompound options) {
		int updated = 0;
		for(BlockPos pos : positions){
			if(world.getBlockState(pos.down()) != null && world.getBlockState(pos.down()).isFullCube()){
				world.setBlockState(pos, Blocks.TALLGRASS.getStateFromMeta(1));
				updated++;
			}
		}
		return updated;
	}

	@Override
	public String name() {
		return "Grass";
	}

}
