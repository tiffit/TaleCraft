package de.longor.talecraft.voxelbrush;

import net.minecraft.world.World;
import de.longor.talecraft.util.MutableBlockPos;

public interface IAction {
	
	public String toString();
	
	void act(World world, MutableBlockPos pos, int x, int y, int z);
	
}
