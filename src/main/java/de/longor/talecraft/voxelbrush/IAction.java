package de.longor.talecraft.voxelbrush;

import de.longor.talecraft.util.MutableBlockPos;
import net.minecraft.world.World;

public interface IAction {

	@Override
	public String toString();

	void act(World world, MutableBlockPos pos, int x, int y, int z);

}
