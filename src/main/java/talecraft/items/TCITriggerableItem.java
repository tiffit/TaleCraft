package talecraft.items;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import talecraft.invoke.EnumTriggerState;

public interface TCITriggerableItem {
	void trigger(World world, EntityPlayerMP player, ItemStack stack);
}
