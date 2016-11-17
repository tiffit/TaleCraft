package talecraft.decorator;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class StaticDecoration implements Decoration {

	protected final Random rand;
	
	public StaticDecoration() {
		rand = new Random();
	}

	@Override
	public abstract int plant(World world, BlockPos[] positions, NBTTagCompound options);

}
