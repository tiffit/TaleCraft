package talecraft.decorator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Decoration {

    /**
    @param world The world to plant the structure in.
    @param positions Positions to plant structures at.
    @param options Options for the decorator (eg: Tree height/type, plant radius...).
    @return Number of blocks added/changed.
  **/
  public int plant(World world, BlockPos[] positions, NBTTagCompound options);
  public String name();
  
	
}
