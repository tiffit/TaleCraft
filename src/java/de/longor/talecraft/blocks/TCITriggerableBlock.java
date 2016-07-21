package de.longor.talecraft.blocks;

import de.longor.talecraft.invoke.EnumTriggerState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface TCITriggerableBlock {
	void trigger(World world, BlockPos position, EnumTriggerState triggerState);
}
