package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.Invoke;

public class DelayBlockTileEntity extends TCTileEntity {
	IInvoke triggerInvoke;
	int delay;
	
	public DelayBlockTileEntity() {
		triggerInvoke = BlockTriggerInvoke.ZEROINSTANCE;
		delay = 20;
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(triggerInvoke);
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.00f;
		color[1] = 0.75f;
		color[2] = 1.00f;
	}

	@Override
	public String getName() {
		return "DelayBlock@"+this.getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		triggerInvoke = IInvoke.Serializer.read(comp.getCompoundTag("triggerInvoke"));
		delay = comp.getInteger("delay");
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
		comp.setInteger("delay", delay);
	}
	
	public void trigger(EnumTriggerState triggerState) {
		if(triggerState.getBooleanValue()) {
			worldObj.scheduleUpdate(getPos(), getBlockType(), delay);
		}
	}
	
	public void invokeFromUpdateTick() {
		Invoke.invoke(triggerInvoke, this, null, EnumTriggerState.IGNORE);
	}
    
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("set")) {
			delay = data.getInteger("delay");
			worldObj.markBlockForUpdate(pos);
			return;
		}
		
		super.commandReceived(command, data);
	}
	
	public IInvoke getInvoke() {
		return triggerInvoke;
	}
	
	public int getDelayValue() {
		return delay;
	}
	
}
