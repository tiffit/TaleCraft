package talecraft.script.wrappers.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import talecraft.TaleCraft;
import talecraft.entity.EntityMovingBlock;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.NPC.NPCData;
import talecraft.entity.NPC.dialogue.NPCDialogue;
import talecraft.network.packets.DialogueOpenPacket;
import talecraft.script.wrappers.nbt.CompoundTagWrapper;

public class MovingBlockObjectWrapper extends EntityObjectWrapper{
	private EntityMovingBlock moving;
	
	public MovingBlockObjectWrapper(EntityMovingBlock moving) {
		super(moving);
		this.moving = moving;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	
	public void setBlock(String resourcelocation){
		setBlock(resourcelocation, 0);
	}
	
	public void setBlock(String resourcelocation, int meta){
		NBTTagCompound tag = getCurrentData();
		tag.setString("Block", resourcelocation);
		tag.setByte("Data", (byte) meta);
		moving.updateData(tag);
	}
	
	public void setInvisible(boolean bool){
		setFieldBoolean("invisible", bool);
	}
	
	public void setPushable(boolean bool){
		setFieldBoolean("pushable", bool);
	}
	
	public void setCollision(boolean bool){
		setFieldBoolean("collision", bool);
	}
	
	public void setNoGravity(boolean bool){
		setFieldBoolean("no_gravity", bool);
	}
	
	public void setMountYOffset(float amount){
		NBTTagCompound tag = getCurrentData();
		tag.setFloat("mount_y_offset", amount);
		moving.updateData(tag);
	}
	
	public void setFieldBoolean(String field, boolean bool){
		NBTTagCompound tag = getCurrentData();
		tag.setBoolean(field, bool);
		moving.updateData(tag);
	}
	
	private NBTTagCompound getCurrentData(){
		NBTTagCompound tag = new NBTTagCompound();
		moving.writeEntityToNBT(tag);
		return tag;
	}

}
