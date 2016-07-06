package tiffit.talecraft.entity.NPC;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tiffit.talecraft.entity.NPC.EntityNPC.NPCType;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkin;

/** The class that stores all of the data for each NPC*/
public class NPCData {

	/**Can the NPC take damage?*/
	private boolean invulnerable;
	private String name;
	/**Passive, Neutral or Aggressive*/
	private NPCType type;
	private String message;
	/**Can the NPC collide other entities?*/
	private boolean movable;
	private boolean namemsg;
	private boolean showname;
	private float pitch;
	private float yaw;
	private boolean eyesfollow;
	private NPCSkin skin;
	private float attackDamage;
	private double movementspeed;
	
	/**The data for the NPC's inventory*/
	private NPCInventoryData invdata;
	
	public NPCData(){
		invulnerable = true;
		name = "New NPC";
		message = "Hello %player%!";
		movable = false;
		namemsg = true;
		showname = true;
		pitch = 0f;
		yaw = 0f;
		eyesfollow = true;
		skin = NPCSkin.Steve;
		type = NPCType.Passive;
		attackDamage = 2f;
		movementspeed = 0.6D;
		invdata = new NPCInventoryData();
	}
	
	public boolean isInvulnerable(){
		return invulnerable;
	}
	
	public void setInvulnerable(boolean bool){
		invulnerable = bool;
	}
	
	public boolean doEyesFollow(){
		return eyesfollow;
	}
	
	public void setEyesFollow(boolean bool){
		eyesfollow = bool;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String str){
		name = str;
	}
	
	public String getMessage(){
		return message;
	}	
	
	public void setMessage(String str){
		message = str;
	}
	
	public boolean isMovable(){
		return movable;
	}
	
	public void setMovable(boolean bool){
		movable = bool;
	}
	
	public boolean shouldShowName(){
		return showname;
	}
	
	public void setShowName(boolean bool){
		showname = bool;
	}
	
	public boolean shouldIncludeNameInMessage(){
		return namemsg;
	}
	
	public void setNameInMessage(boolean bool){
		namemsg = bool;
	}
	
	public float getPitch(){
		return pitch;
	}
	
	public void setPitch(float flt){
		pitch = flt;
	}

	public float getYaw(){
		return yaw;
	}
	
	public void setYaw(float flt){
		yaw = flt;
	}
	
	public float getDamage(){
		return attackDamage;
	}
	
	public void setDamage(float flt){
		attackDamage = flt;
	}
	
	public NPCSkin getSkin(){
		return skin;
	}
	
	public void setSkin(NPCSkin skn){
		skin = skn;
	}
	
	public NPCType getType(){
		return type;
	}
	
	public void setType(NPCType typ){
		type = typ;
	}
	
	public double getSpeed(){
		return movementspeed;
	}
	
	public void setSpeed(double dbl){
		movementspeed = dbl;
	}
	
	public void setItem(EntityEquipmentSlot slot, ItemStack stack){
		invdata.setItem(slot, stack);
	}
	
	public ItemStack getInvStack(EntityEquipmentSlot slot){
		return invdata.getItem(slot);
	}
	
	public NBTTagCompound toNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("invulnerable", isInvulnerable());
		tag.setString("name", getName());
		tag.setString("message", getMessage());
		tag.setBoolean("namemsg", namemsg);
		tag.setBoolean("showname", showname);
		tag.setFloat("pitch", pitch);
		tag.setFloat("yaw", yaw);
		tag.setBoolean("eyesfollow", eyesfollow);
		tag.setInteger("skin_id", skin.ordinal());
		tag.setInteger("type_id", type.ordinal());
		tag.setFloat("attackdamage", attackDamage);
		tag.setDouble("movementspeed", movementspeed);
		tag.setTag("inventory", invdata.toNBT());
		return tag;
	}
	
	public static NPCData fromNBT(NBTTagCompound tag){
		NPCData data = new NPCData();
		data.setInvulnerable(tag.getBoolean("invulnerable"));
		data.setName(tag.getString("name"));
		data.setMessage(tag.getString("message"));
		data.setNameInMessage(tag.getBoolean("namemsg"));
		data.setShowName(tag.getBoolean("showname"));
		data.setPitch(tag.getFloat("pitch"));
		data.setYaw(tag.getFloat("yaw"));
		data.setEyesFollow(tag.getBoolean("eyesfollow"));
		data.setSkin(NPCSkin.values()[tag.getInteger("skin_id")]);
		data.setType(NPCType.values()[tag.getInteger("type_id")]);
		data.setDamage(tag.getFloat("attackdamage"));
		data.setSpeed(tag.getDouble("movementspeed"));
		data.invdata = NPCInventoryData.fromNBT(tag.getCompoundTag("inventory"));
		return data;
	}
	
}
