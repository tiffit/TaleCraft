package talecraft.items.world;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class HeartItem extends TCWorldItem {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)return ActionResult.newResult(EnumActionResult.PASS, stack);
		int amount = 2;
		boolean heal = true;
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("amount")) amount = stack.getTagCompound().getInteger("amount");
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("heal")) heal = stack.getTagCompound().getBoolean("heal");
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + amount);
		if(heal)player.heal(amount);
		stack.shrink(1);
		if(stack.getCount() <= 0) player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
}
