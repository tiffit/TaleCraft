package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.invoke.Invoke;

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
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
		comp.setInteger("delay", delay);
		return comp;
	}

	public void trigger(EnumTriggerState triggerState) {
		if(triggerState.getBooleanValue()) {
			world.scheduleUpdate(getPos(), getBlockType(), delay);
		}
	}

	public void invokeFromUpdateTick() {
		Invoke.invoke(triggerInvoke, this, null, EnumTriggerState.IGNORE);
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("set")) {
			delay = data.getInteger("delay");
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
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
