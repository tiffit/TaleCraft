package tiffit.talecraft.script.wrappers;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.entity.EntityLivingObjectWrapper;
import de.longor.talecraft.script.wrappers.entity.EntityObjectWrapper;
import de.longor.talecraft.script.wrappers.nbt.CompoundTagWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import tiffit.talecraft.entity.NPC.EntityNPC;
import tiffit.talecraft.entity.NPC.NPCData;

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
