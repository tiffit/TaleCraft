package tiffit.talecraft.items;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.items.TCItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tiffit.talecraft.entity.NPC.EntityNPC;

public class NPCCloneItem extends TCItem {
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer attacker, Entity target) {
		if(target instanceof EntityNPC && !attacker.worldObj.isRemote){
			EntityNPC npc = (EntityNPC) target;
			if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("npc", new NBTTagCompound());
			npc.writeEntityToNBT(stack.getTagCompound().getCompoundTag("npc"));
			attacker.addChatMessage(new TextComponentString("Copied NPC \"" + npc.getName() + "\"."));
		}
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(stack.hasTagCompound()){
			NBTTagCompound npcTag = stack.getTagCompound().getCompoundTag("npc");
			EntityNPC npc = new EntityNPC(world);
			npc.setPosition(player.posX, player.posY, player.posZ);
			npc.readEntityFromNBT(npcTag);
			world.spawnEntityInWorld(npc);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
}
