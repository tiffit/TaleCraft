package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.Invoke;

public class TriggerFilterBlockTileEntity extends TCTileEntity {
	private IInvoke triggerInvoke;
	private boolean filter_on;
	private boolean filter_off;
	private boolean filter_invert;
	private boolean filter_ignore;
	
	public TriggerFilterBlockTileEntity() {
		triggerInvoke = BlockTriggerInvoke.ZEROINSTANCE;
		filter_on  = true;
		filter_off = false;
		filter_invert  = false;
		filter_ignore  = false;
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(triggerInvoke);
	}
	
	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.75f;
		color[1] = 1.00f;
		color[2] = 0.25f;
	}
	
	@Override
	public String getName() {
		return "TriggerFilterBlock@"+this.getPos();
	}
	
	public IInvoke getTriggerInvoke() {
		return triggerInvoke;
	}
	
	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		triggerInvoke = IInvoke.Serializer.read(comp.getCompoundTag("triggerInvoke"));
		filter_on = comp.getBoolean("filter_on");
		filter_off = comp.getBoolean("filter_off");
		filter_invert = comp.getBoolean("filter_invert");
		filter_ignore = comp.getBoolean("filter_ignore");
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
		comp.setBoolean("filter_on", filter_on);
		comp.setBoolean("filter_off", filter_off);
		comp.setBoolean("filter_invert", filter_invert);
		comp.setBoolean("filter_ignore", filter_ignore);
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			trigger(EnumTriggerState.IGNORE);
			return;
		}
		
		// fall trough
		super.commandReceived(command, data);
	}
	
	public void trigger(EnumTriggerState triggerState) {
		boolean can = false;
		
		switch(triggerState) {
			case ON: can |= filter_on; break;
			case OFF: can |= filter_off; break;
			case INVERT: can |= filter_invert; break;
			case IGNORE: can |= filter_ignore; break;
			default: break;
		}
		
		if(can) {
			Invoke.invoke(triggerInvoke, this, null, triggerState);
		}
	}

	public boolean getDoFilterOn() {
		return filter_on;
	}

	public boolean getDoFilterOff() {
		return filter_off;
	}

	public boolean getDoFilterInvert() {
		return filter_invert;
	}

	public boolean getDoFilterIgnore() {
		return filter_ignore;
	}
	
}
