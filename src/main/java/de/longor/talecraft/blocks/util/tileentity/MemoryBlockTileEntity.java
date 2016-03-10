package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.Invoke;

public class MemoryBlockTileEntity extends TCTileEntity {
	private IInvoke triggerInvoke;
	private boolean triggered;
	
	public MemoryBlockTileEntity() {
		triggerInvoke = BlockTriggerInvoke.ZEROINSTANCE;
		triggered = false;
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(triggerInvoke);
	}
	
	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.25f;
		color[1] = 1.00f;
		color[2] = 0.75f;
	}
	
	@Override
	public String getName() {
		return "MemoryBlock@"+this.getPos();
	}
	
	public IInvoke getTriggerInvoke() {
		return triggerInvoke;
	}
	
	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		triggerInvoke = IInvoke.Serializer.read(comp.getCompoundTag("triggerInvoke"));
		triggered = comp.getBoolean("triggered");
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
		comp.setBoolean("triggered", triggered);
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			trigger(EnumTriggerState.ON);
			return;
		}
		
		if(command.equals("reset")) {
			triggered = false;
			worldObj.markBlockForUpdate(pos);
			return;
		}
		
		// fall trough
		super.commandReceived(command, data);
	}
	
	public void trigger(EnumTriggerState triggerState) {
		if(!triggered) {
			Invoke.invoke(triggerInvoke, this, null, triggerState);
			triggered = true;
			worldObj.markBlockForUpdate(pos);
		}
	}

	public boolean getIsTriggered() {
		return triggered;
	}
	
}
