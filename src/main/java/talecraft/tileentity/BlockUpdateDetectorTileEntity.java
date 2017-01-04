package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.BlockTriggerInvoke;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.invoke.Invoke;

public class BlockUpdateDetectorTileEntity extends TCTileEntity {
	IInvoke detectorInvoke;

	public BlockUpdateDetectorTileEntity() {
		detectorInvoke = BlockTriggerInvoke.ZEROINSTANCE;
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(detectorInvoke);
	}

	@Override
	public String getName() {
		return "BlockUpdateDetector@"+pos;
	}

	@Override
	public String toString() {
		return "BlockUpdateDetectorTileEntity:{}";
	}

	public IInvoke getInvoke() {
		return detectorInvoke;
	}

	@Override
	public void init() {

	}

	@Override
	public void readFromNBT_do(NBTTagCompound compound) {
		detectorInvoke = IInvoke.Serializer.read(compound.getCompoundTag("detectorInvoke"));
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound compound) {
		compound.setTag("detectorInvoke", IInvoke.Serializer.write(detectorInvoke));
		return compound;
	}

	public void triggerUpdateInvoke(EnumTriggerState triggerState) {
		if(this.world.isRemote)
			return;

		Invoke.invoke(detectorInvoke, this, null, triggerState);
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			Invoke.invoke(detectorInvoke, this, null, EnumTriggerState.ON);
		}

		super.commandReceived(command, data);
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 1.0f;
		color[2] = 0.0f;
	}

}
