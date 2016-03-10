package de.longor.talecraft.blocks;

import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.util.WorldHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface TCITriggerableBlock {
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState);
}
