package talecraft.script.wrappers.entity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.TaleCraft;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.NPC.NPCData;
import talecraft.script.wrappers.nbt.CompoundTagWrapper;

public class NPCObjectWrapper extends EntityLivingObjectWrapper{
	private EntityNPC npc;

	public NPCObjectWrapper(EntityNPC npc) {
		super(npc);
		this.npc = npc;
	}
	
	public void setName(String name){
		npc.getNPCData().setName(name);
	}
	
	public void setPos(float x, float y, float z){
		npc.setPosition(x, y, z);
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public float getX(){
		return (float) npc.posX;
	}
	
	public float getY(){
		return (float) npc.posY;
	}
	
	public float getZ(){
		return (float) npc.posZ;
	}
	
	public CompoundTagWrapper getScriptData(){
		return new CompoundTagWrapper(npc.getScriptData());
	}
	
	public void clearScriptData(){
		npc.setScriptData(new NBTTagCompound());
	}
	
	public NPCData getNPCData(){
		return npc.getNPCData();
	}
	
	@Deprecated //Use navigateTo(double x, double y, double z) instead
	public boolean moveToBlock(double x, double y, double z){
		return moveToBlock(x, y, z, 0.5f);
	}
	
	@Deprecated //Use navigateTo(double x, double y, double z, double speed) instead
	public boolean moveToBlock(double x, double y, double z, float speed){
		return npc.moveToPos(x, y, z, speed);
	}

}
