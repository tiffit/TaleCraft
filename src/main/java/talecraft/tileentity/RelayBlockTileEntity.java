package talecraft.tileentity;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.invoke.Invoke;

public class RelayBlockTileEntity extends TCTileEntity {
	private Map<String, IInvoke> invokes;

	public RelayBlockTileEntity() {
		invokes = Maps.newHashMap();
	}

	@Override
	public void init() {
		// don't do anything
	}

	@Override
	public String getName() {
		return "RelayBlock@"+pos;
	}

	@Override
	public String toString() {
		return "RelayBlockTileEntity:{}";
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.addAll(this.invokes.values());
	}

	public void triggerRelayInvoke(EnumTriggerState triggerState) {
		for (IInvoke invoke : invokes.values()) {
			Invoke.invoke(invoke, this, null, triggerState);
		}
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("invoke_add")) {
			String keyString = null;

			// Generate a unique ID between 0 and 1000.
			// Yes, this is inefficient as fuck.
			// No, I don't care about that.
			do {
				keyString = "rix" + (System.currentTimeMillis() % 1000);
			} while(invokes.containsKey(keyString));

			invokes.put(keyString, BlockTriggerInvoke.ZEROINSTANCE);
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
		}

		if(command.equals("invoke_remove")) {
			invokes.remove(data.getString("invokeToRemove"));
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
		}

		super.commandReceived(command, data);
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		invokes.clear();

		for(Object obj : comp.getKeySet()) {
			String key = (String) obj;

			if(key.startsWith("rix")) {
				NBTTagCompound rawinvoke = comp.getCompoundTag(key);
				IInvoke invoke = IInvoke.Serializer.read(rawinvoke);
				invokes.put(key, invoke);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		for(Entry<String, IInvoke> entry : invokes.entrySet()) {
			comp.setTag(entry.getKey(),IInvoke.Serializer.write(entry.getValue()));
		}
		return comp;
	}

	public Map<String, IInvoke> getInvokes() {
		return invokes;
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.5f;
		color[1] = 0.8f;
		color[2] = 0.0f;
	}

}
