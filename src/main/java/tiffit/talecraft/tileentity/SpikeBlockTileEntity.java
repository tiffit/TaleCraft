package tiffit.talecraft.tileentity;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.FileScriptInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IScriptInvoke;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.nbt.NBTTagCompound;

public class SpikeBlockTileEntity extends TCTileEntity{

	boolean active;
	boolean inverted;
	
	public SpikeBlockTileEntity() {
	}

	@Override
	public void init() {

	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("active")) {
			active = data.getBoolean("active");
			return;
		}

		super.commandReceived(command, data);
	}

	@Override
	public String getName() {
		return "SpikeBlock@"+pos;
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		active = comp.getBoolean("active");
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setBoolean("active", active);
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 0.5f;
		color[2] = 0.0f;
	}

}
