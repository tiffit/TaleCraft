package tiffit.talecraft.entity.NPC;

import net.minecraft.nbt.NBTTagCompound;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkin;

public class NPCData {

	/**Can the NPC tale damage?*/
	private boolean invulnerable;
	private String name;
	private String message;
	private boolean movable;
	private boolean namemsg;
	private boolean showname;
	private float pitch;
	private float yaw;
	private boolean eyesfollow;
	private NPCSkin skin;
	
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
	
	public NPCSkin getSkin(){
		return skin;
	}
	
	public void setSkin(NPCSkin skn){
		skin = skn;
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
		return data;
	}
	
}
