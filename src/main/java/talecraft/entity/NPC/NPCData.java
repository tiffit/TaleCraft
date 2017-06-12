package talecraft.entity.NPC;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import talecraft.entity.NPC.EntityNPC.NPCType;
import talecraft.entity.NPC.NPCInventoryData.NPCDrop;
import talecraft.entity.NPC.quest.NPCQuest;

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
	private boolean boss;
	private float pitch;
	private float yaw;
	private boolean eyesfollow;
	private EnumNPCSkin skin;
	private String customskin;
	private EnumNPCModel model;
	private float attackDamage;
	private double movementspeed;
	private float health;
	private TextFormatting nameColor;
	
	private String interact_script;
	private String update_script;
	private String death_script;

	private NPCShop shop;
	private NPCQuest quest;
	
	/**The data for the NPC's inventory*/
	private NPCInventoryData invdata;
	
	public final EntityNPC npc;
	
	public NPCData(EntityNPC npc){
		this.npc = npc;
		invulnerable = true;
		name = "New NPC";
		message = "Hello %player%!";
		movable = false;
		namemsg = true;
		showname = true;
		boss = false;
		pitch = 0f;
		yaw = 0f;
		eyesfollow = true;
		skin = EnumNPCSkin.Steve;
		customskin = "";
		type = NPCType.Passive;
		model = EnumNPCModel.Player;
		attackDamage = 2f;
		health = 10f;
		movementspeed = 0.6D;
		nameColor = NPCType.Passive.color;
		shop = new NPCShop(npc);
		invdata = new NPCInventoryData();
		interact_script = "";
		death_script = "";
		update_script = "";
	}
	
	public NPCShop getShop(){
		return shop;
	}
	
	public void setShop(NPCShop shp){
		shop = shp;
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
	
	public String getInteractScript(){
		return interact_script;
	}
	
	public void setInteractScript(String str){
		interact_script = str;
	}
	
	public String getDeathScript(){
		return death_script;
	}
	
	public void setDeathScript(String str){
		death_script = str;
	}
	
	public String getUpdateScript(){
		return update_script;
	}
	
	public void setUpdateScript(String str){
		update_script = str;
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
	
	public boolean isBoss(){
		return boss;
	}
	
	public void setBoss(boolean bool){
		boss = bool;
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
	
	public float getHealth(){
		return health;
	}
	
	public void setHealth(float flt){
		health = flt;
		npc.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getHealth());
	}
	
	public EnumNPCSkin getSkin(){
		return skin;
	}
	
	public void setSkin(EnumNPCSkin skn){
		skin = skn;
	}
	
	public String getCustomSkin(){
		return customskin;
	}
	
	public void setCustomSkin(String cskn){
		customskin = cskn;
	}
	
	public EnumNPCModel getModel(){
		return model;
	}
	
	public void setModel(EnumNPCModel mdl){
		model = mdl;
	}
	
	public NPCType getType(){
		return type;
	}
	
	public void setType(NPCType typ){
		type = typ;
	}
	
	public TextFormatting getNameColor(){
		return nameColor;
	}
	
	public void setNameColor(TextFormatting nameCol){
		nameColor = nameCol;
	}
	
	public double getSpeed(){
		return movementspeed;
	}
	
	public void setSpeed(double dbl){
		movementspeed = dbl;
		npc.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getSpeed());
	}
	
	public void setItem(EntityEquipmentSlot slot, ItemStack stack){
		invdata.setItem(slot, stack);
	}
	
	public ItemStack getInvStack(EntityEquipmentSlot slot){
		return invdata.getItem(slot);
	}
	
	public List<NPCDrop> getDrops(){
		return invdata.getDrops();
	}
	
	public void setDrops(List<NPCDrop> drops){
		invdata.setDrops(drops);
	}
	
	public boolean hasQuest(){
		return quest != null;
	}
	
	public void setQuest(NPCQuest quest){
		this.quest = quest;
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
		tag.setString("customskin", customskin);
		tag.setInteger("model_id", model.ordinal());
		tag.setInteger("type_id", type.ordinal());
		tag.setFloat("attackdamage", attackDamage);
		tag.setBoolean("boss", boss);
		tag.setDouble("movementspeed", movementspeed);
		tag.setTag("inventory", invdata.toNBT());
		tag.setFloat("health", health);
		tag.setString("namecolor", nameColor.name());
		tag.setTag("shop", shop.toNBT());
		tag.setString("interact_script", interact_script);
		tag.setString("update_script",  update_script);
		tag.setString("death_script", death_script);
		tag.setBoolean("movable", movable);
		return tag;
	}
	
	public static NPCData fromNBT(EntityNPC npc, NBTTagCompound tag){
		NPCData data = new NPCData(npc);
		data.setInvulnerable(tag.getBoolean("invulnerable"));
		data.setName(tag.getString("name"));
		data.setMessage(tag.getString("message"));
		data.setNameInMessage(tag.getBoolean("namemsg"));
		data.setShowName(tag.getBoolean("showname"));
		data.setPitch(tag.getFloat("pitch"));
		data.setYaw(tag.getFloat("yaw"));
		data.setEyesFollow(tag.getBoolean("eyesfollow"));
		data.setSkin(EnumNPCSkin.values()[tag.getInteger("skin_id")]);
		data.setCustomSkin(tag.getString("customskin"));
		data.setModel(EnumNPCModel.values()[tag.getInteger("model_id")]);
		data.setType(NPCType.values()[tag.getInteger("type_id")]);
		if(!tag.hasKey("namecolor")) tag.setString("namecolor", data.type.color.name());
		data.setNameColor(TextFormatting.valueOf(tag.getString("namecolor")));
		data.setDamage(tag.getFloat("attackdamage"));
		data.setSpeed(tag.getDouble("movementspeed"));
		data.setBoss(tag.getBoolean("boss"));
		if(!tag.hasKey("health")) tag.setFloat("health", 10);//For backwards compatibility
		data.setHealth(tag.getFloat("health"));
		data.invdata = NPCInventoryData.fromNBT(tag.getCompoundTag("inventory"));
		data.shop = NPCShop.getShop(tag.getCompoundTag("shop"), npc);
		data.setInteractScript(tag.getString("interact_script"));
		data.setUpdateScript(tag.getString("update_script"));
		data.setDeathScript(tag.getString("death_script"));
		data.setMovable(tag.getBoolean("movable"));
		return data;
	}
	
}
