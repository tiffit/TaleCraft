package talecraft.items;

import java.util.UUID;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityCloneItem extends TCItem {
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer attacker, Entity target) {
		if(!attacker.worldObj.isRemote){
			if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
			NBTTagCompound tag = stack.getTagCompound();
			tag.setTag("entity_data", new NBTTagCompound());
			NBTTagCompound data = tag.getCompoundTag("entity_data");
			target.writeToNBTAtomically(data);
			attacker.addChatMessage(new TextComponentString("Copied Entity \"" + target.getName() + ChatFormatting.RESET + "\"!"));
		}
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(stack.hasTagCompound()){
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("entity_data");
			tag.setUniqueId("UUID", UUID.randomUUID());
			Entity entity = EntityList.createEntityFromNBT(tag, world);
			if(entity == null){
				player.addChatMessage(new TextComponentString(ChatFormatting.RED + "An error occured while trying to clone that entity!"));
				return ActionResult.newResult(EnumActionResult.FAIL, stack);
			}
			entity.setPosition(player.posX, player.posY, player.posZ);
			world.spawnEntityInWorld(entity);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
}
