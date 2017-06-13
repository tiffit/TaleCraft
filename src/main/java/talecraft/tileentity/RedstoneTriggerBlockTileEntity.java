package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.FileScriptInvoke;
import talecraft.invoke.IInvoke;
import talecraft.invoke.Invoke;

public class RedstoneTriggerBlockTileEntity extends TCTileEntity {
	IInvoke triggerInvokeOn;
	IInvoke triggerInvokeOff;

	public RedstoneTriggerBlockTileEntity() {
		triggerInvokeOn = BlockTriggerInvoke.ZEROINSTANCE;
		triggerInvokeOff = BlockTriggerInvoke.ZEROINSTANCE;
	}

	@Override
	public void init() {
		// don't do anything
	}

	@Override
	public void readFromNBT_do(NBTTagCompound compound) {
		triggerInvokeOn = IInvoke.Serializer.read(compound.getCompoundTag("triggerInvokeOn"));
		triggerInvokeOff = IInvoke.Serializer.read(compound.getCompoundTag("triggerInvokeOff"));
		
		// backwards compatibility
		if(triggerInvokeOn.getType().equals("NullInvoke")) {
			triggerInvokeOn = IInvoke.Serializer.read(compound.getCompoundTag("triggerInvoke"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("triggerInvokeOn", IInvoke.Serializer.write(triggerInvokeOn));
		comp.setTag("triggerInvokeOff", IInvoke.Serializer.write(triggerInvokeOff));
		return comp;
	}

	public void invokeFromUpdateTick(EnumTriggerState triggerState, boolean onOff) {
		if(this.world.isRemote)
			return;

		if(onOff)
			Invoke.invoke(triggerInvokeOn, this, null, triggerState);
		else
			Invoke.invoke(triggerInvokeOff, this, null, triggerState);
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			Invoke.invoke(triggerInvokeOn, this, null, EnumTriggerState.ON);
			Invoke.invoke(triggerInvokeOff, this, null, EnumTriggerState.ON);
			return;
		}

		if(command.equals("reload")) {
			if(triggerInvokeOn != null && triggerInvokeOn instanceof FileScriptInvoke) {
				((FileScriptInvoke)triggerInvokeOn).reloadScript();
			}
			if(triggerInvokeOff != null && triggerInvokeOff instanceof FileScriptInvoke) {
				((FileScriptInvoke)triggerInvokeOff).reloadScript();
			}
			return;
		}

		super.commandReceived(command, data);
	}

	@Override
	public String getName() {
		return "RedstoneTrigger@"+pos;
	}

	@Override
	public String toString() {
		return "RedstoneTriggerTileEntity:{"+triggerInvokeOn+", "+triggerInvokeOff+"}";
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(triggerInvokeOn);
		invokes.add(triggerInvokeOff);
	}

	public IInvoke getInvokeOn() {
		return triggerInvokeOn;
	}

	public IInvoke getInvokeOff() {
		return triggerInvokeOff;
	}

	//	@Override
	//	public void getInvokesAsDataCompounds(List<NBTTagCompound> invokes) {
	//		invokes.add(triggerInvoke);
	//	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.75f;
		color[1] = 0.0f;
		color[2] = 0.0f;
	}

}
