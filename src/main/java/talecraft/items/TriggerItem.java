package talecraft.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import talecraft.blocks.TCITriggerableBlock;
import talecraft.invoke.EnumTriggerState;

public class TriggerItem extends TCItem implements TCITriggerableItem{
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		NBTTagCompound comp = stack.getTagCompound();
		if(comp == null) {
			comp = new NBTTagCompound();
			comp.setInteger("trigger", EnumTriggerState.ON.ordinal());
			stack.setTagCompound(comp);
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(stack.hasTagCompound()){
			NBTTagCompound tag = stack.getTagCompound();
			EnumTriggerState state = EnumTriggerState.values()[tag.getInteger("trigger")];
			float lerp = 1F;
			float dist = 6;
			Vec3d start = this.getPositionEyes(lerp, player);
			Vec3d direction = player.getLook(lerp);
			Vec3d end = start.addVector(direction.x * dist, direction.y * dist, direction.z * dist);
			RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);
			if(result != null && result.typeOfHit != null && result.typeOfHit == Type.BLOCK){
				BlockPos pos = result.getBlockPos();
				Block block = world.getBlockState(pos).getBlock();
				if(block instanceof TCITriggerableBlock){
					((TCITriggerableBlock)block).trigger(world, pos, state);
					return ActionResult.newResult(EnumActionResult.PASS, stack);
				}
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void trigger(World world, EntityPlayerMP player, ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		int current = tag.getInteger("trigger");
		current++;
		if(current >= EnumTriggerState.values().length) current = 0;
		player.sendMessage(new TextComponentString("Changed trigger to: " + EnumTriggerState.values()[current]));
		tag.setInteger("trigger", current);
	}
	
}