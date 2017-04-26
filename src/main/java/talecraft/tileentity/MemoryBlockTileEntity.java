package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.invoke.Invoke;

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
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
		comp.setBoolean("triggered", triggered);
		return comp;
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			trigger(EnumTriggerState.ON);
			return;
		}

		if(command.equals("reset")) {
			triggered = false;
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
			return;
		}

		// fall trough
		super.commandReceived(command, data);
	}

	public void trigger(EnumTriggerState triggerState) {
		switch(triggerState) {
			case IGNORE:
				// no op
				break;
			case INVERT:
				trigger(triggered ? EnumTriggerState.OFF : EnumTriggerState.ON);
				break;
			case OFF:
				// reset
				if(triggered) {
					triggered = false;
					world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
				}
				break;
			case ON:
				if(!triggered) {
					Invoke.invoke(triggerInvoke, this, null, triggerState);
					triggered = true;
					world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
				}
				break;
			default:
				// no op
				break;
		}
		
		
	}

	public boolean getIsTriggered() {
		return triggered;
	}

}
