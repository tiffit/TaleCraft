package talecraft.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import talecraft.invoke.EnumTriggerState;

public interface TCITriggerableBlock {
	void trigger(World world, BlockPos position, EnumTriggerState triggerState);
}
