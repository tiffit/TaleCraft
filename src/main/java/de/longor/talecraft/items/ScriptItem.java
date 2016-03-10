package de.longor.talecraft.items;

import java.util.List;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.invoke.NullInvoke;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Finish implementing this item.
public class ScriptItem extends TCItem {
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(worldIn.isRemote)
    		return true;
    	
    	NBTTagCompound compound = getNBT(stack);
    	
    	// get invoke
    	IInvoke invoke = IInvoke.Serializer.read(compound.getCompoundTag("invoke_on_use"));
    	
    	// make sure to not waste time
    	if(invoke == null) return true;
    	if(invoke instanceof NullInvoke) return true;
    	
    	// execute invoke
    	Invoke.invoke(invoke, new TempItemStackInvokeSource(worldIn, new BlockPos(hitX, hitY, hitZ), playerIn), null, EnumTriggerState.ON);
    	
		return true;
    }

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if(worldIn.isRemote)
    		return itemStackIn;
    	
    	NBTTagCompound compound = getNBT(itemStackIn);
    	
    	// get invoke
    	IInvoke invoke = IInvoke.Serializer.read(compound.getCompoundTag("invoke_on_rclick"));
    	
    	// make sure to not waste time
    	if(invoke == null) return itemStackIn;
    	if(invoke instanceof NullInvoke) return itemStackIn;
    	
    	// execute invoke
    	Invoke.invoke(invoke, new TempItemStackInvokeSource(worldIn, playerIn.getPosition(), playerIn), null, EnumTriggerState.ON);
    	
    	return itemStackIn;
    }
    
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    	NBTTagCompound compound = getNBT(stack);
    	
    	// get invoke
    	IInvoke invoke = IInvoke.Serializer.read(compound.getCompoundTag("invoke_on_lclick"));
    	
    	// make sure to not waste time
    	if(invoke == null) return false;
    	if(invoke instanceof NullInvoke) return false;
    	
    	// execute invoke
    	Invoke.invoke(invoke, new TempItemStackInvokeSource(player.worldObj, player.getPosition(), player), null, EnumTriggerState.ON);
    	
    	return false;
    }
	
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    	// Should I allow this?
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    	NBTTagCompound compound = getNBT(stack);
    	
    	NBTTagList lore = compound.getTagList("lore", NBT.TAG_STRING);
    	if(lore.hasNoTags()) return;
    	
    	for(int i = 0; i < lore.tagCount(); i++) {
    		tooltip.add(lore.getStringTagAt(i));
    	}
    }
	
    private static final NBTTagCompound getNBT(ItemStack stack) {
    	NBTTagCompound comp = stack.getTagCompound();
    	
    	if(comp == null) {
    		comp = new NBTTagCompound();
    		stack.setTagCompound(comp);
    	}
    	
    	return comp;
	}
    
    private static class TempItemStackInvokeSource implements IInvokeSource, ICommandSender {
    	World world;
    	Entity holder;
    	BlockPos position;
    	Scriptable scriptScope;
    	
		public TempItemStackInvokeSource(World worldIn, BlockPos positionIn, Entity holderIn) {
			this.world = worldIn;
			this.holder = holderIn;
			this.position = positionIn;
		}
		
		@Override
		public Scriptable getInvokeScriptScope() {
			if(scriptScope == null) {
				
				// if(holder != null) ... ?
				
				scriptScope = TaleCraft.globalScriptManager.createNewWorldScope(world);
			}
			
			return scriptScope;
		}
		
		@Override
		public ICommandSender getInvokeAsCommandSender() {
			return this;
		}
		
		@Override
		public BlockPos getInvokePosition() {
			return position;
		}
		
		@Override
		public World getInvokeWorld() {
			return world;
		}
		
		@Override
		public void getInvokes(List<IInvoke> invokes) {
			// nope
		}
		
		@Override
		public String getName() {
			return "ItemStack Invoke Source";
		}
		
		@Override
		public IChatComponent getDisplayName() {
			return new ChatComponentText(getName());
		}
		
		@Override
		public void addChatMessage(IChatComponent message) {
			if(holder != null)
				holder.addChatMessage(message);
		}
		
		@Override
		public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
			return true;
		}
		
		@Override
		public BlockPos getPosition() {
			return position;
		}
		
		@Override
		public Vec3 getPositionVector() {
			return new Vec3(position.getX(), position.getY(), position.getZ());
		}
		
		@Override
		public World getEntityWorld() {
			return world;
		}
		
		@Override
		public Entity getCommandSenderEntity() {
			return holder;
		}
		
		@Override
		public boolean sendCommandFeedback() {
			return true;
		}
		
		@Override
		public void setCommandStat(Type type, int amount) {
			if(holder != null)
				holder.setCommandStat(type, amount);
		}

		@Override
		public void getInvokeColor(float[] color) {
			color[0] = 0.0f;
			color[1] = 0.0f;
			color[2] = 1.0f;
		}


    }
    
}
