package talecraft.items;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCloneItem extends TCItem{
	
	public EntityCloneItem() {
		this.addPropertyOverride(new ResourceLocation("full"), new IItemPropertyGetter(){
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity){
                if (!stack.hasTagCompound()){
                    return 0.0F;
                }
                else{
                    NBTTagCompound tag = stack.getTagCompound();
                    return tag.hasKey("entity_data") ? 1.0F : 0.0F;
                }
            }
        });
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer attacker, Entity target) {
		if(!attacker.getEntityWorld().isRemote){
			if(attacker.isSneaking()){
				Entity ent = spawnEntity(stack, attacker.world, attacker);
				if(target.isBeingRidden())target.dismountRidingEntity();
				ent.startRiding(target, true);
			}else{
				if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				NBTTagCompound tag = stack.getTagCompound();
				tag.setTag("entity_data", new NBTTagCompound());
				NBTTagCompound data = tag.getCompoundTag("entity_data");
				target.writeToNBTAtomically(data);
				attacker.sendMessage(new TextComponentString("Copied Entity \"" + target.getName() + ChatFormatting.RESET + "\"!"));
			}
		}
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		if(stack.hasTagCompound()){
			Entity ent = spawnEntity(stack, world, player);
			if(ent == null)ActionResult.newResult(EnumActionResult.FAIL, stack);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
	private Entity spawnEntity(ItemStack stack, World world, EntityPlayer player){
		NBTTagCompound tag = stack.getTagCompound().getCompoundTag("entity_data");
		tag.setUniqueId("UUID", UUID.randomUUID());
		Entity entity = EntityList.createEntityFromNBT(tag, world);
		if(entity == null){
			player.sendMessage(new TextComponentString(ChatFormatting.RED + "An error occured while trying to clone that entity!"));
			return null;
		}
		entity.setPosition(player.posX, player.posY, player.posZ);
		world.spawnEntity(entity);
		return entity;
	}
	
}
