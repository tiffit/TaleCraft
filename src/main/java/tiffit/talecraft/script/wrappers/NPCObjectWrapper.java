package tiffit.talecraft.script.wrappers;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.entity.EntityObjectWrapper;
import de.longor.talecraft.script.wrappers.nbt.CompoundTagWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import tiffit.talecraft.entity.NPC.EntityNPC;
import tiffit.talecraft.entity.NPC.NPCData;

public class NPCObjectWrapper extends EntityObjectWrapper{
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
	
	public boolean moveToBlock(double x, double y, double z){
		return moveToBlock(x, y, z, 0.5f);
	}
	
	public boolean moveToBlock(double x, double y, double z, float speed){
		return npc.moveToPos(x, y, z, speed);
	}

}
