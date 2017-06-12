package talecraft.script.wrappers.entity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.TaleCraft;
import talecraft.entity.EntityMovingBlock;

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
	
	@Override
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
