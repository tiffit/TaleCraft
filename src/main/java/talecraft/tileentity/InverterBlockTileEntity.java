package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.invoke.Invoke;

public class InverterBlockTileEntity extends TCTileEntity {
	private IInvoke triggerInvoke;

	public InverterBlockTileEntity() {
		triggerInvoke = BlockTriggerInvoke.ZEROINSTANCE;
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(triggerInvoke);
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0;
		color[1] = 1;
		color[2] = 0;
	}

	@Override
	public String getName() {
		return "InverterBlock@"+this.getPos();
	}

	public IInvoke getTriggerInvoke() {
		return triggerInvoke;
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		triggerInvoke = IInvoke.Serializer.read(comp.getCompoundTag("triggerInvoke"));
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
		return comp;
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			trigger(EnumTriggerState.ON);
			return;
		}

		// fall trough
		super.commandReceived(command, data);
	}

	public void trigger(EnumTriggerState triggerState) {
		Invoke.invoke(triggerInvoke, this, null, triggerState.invert());
	}

}
