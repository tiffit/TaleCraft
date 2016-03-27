package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.FileScriptInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IScriptInvoke;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.nbt.NBTTagCompound;

public class ScriptBlockTileEntity extends TCTileEntity {
	IScriptInvoke scriptInvoke;

	public ScriptBlockTileEntity() {
		scriptInvoke = new FileScriptInvoke();
	}

	@Override
	public void init() {

	}

	public void triggerInvokeScript() {
		Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("reload")) {
			scriptInvoke.reloadScript();
			return;
		}

		if(command.equals("execute")) {
			triggerInvokeScript();
			return;
		}

		if(command.equals("reloadexecute")) {
			scriptInvoke.reloadScript();
			triggerInvokeScript();
			return;
		}

		super.commandReceived(command, data);
	}

	@Override
	public String getName() {
		return "ScriptBlock@"+pos;
	}

	@Override
	public String toString() {
		return "ScriptBlockTileEntity:{"+scriptInvoke+", "+getInvokeScriptScope()+"}";
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(scriptInvoke);
	}

	public IScriptInvoke getInvoke() {
		return scriptInvoke;
	}

	@Override
	public void readFromNBT_do(NBTTagCompound compound) {
		scriptInvoke = IInvoke.Serializer.readSI(compound.getCompoundTag("scriptInvoke"));
		scriptInvoke.reloadScript();
	}

	@Override
	public void writeToNBT_do(NBTTagCompound compound) {
		compound.setTag("scriptInvoke", IInvoke.Serializer.write(scriptInvoke));
	}

	public String getScriptName() {
		TaleCraft.logger.info("getScriptName() " + scriptInvoke.getScriptName());
		return scriptInvoke == null ? "" : scriptInvoke.getScriptName();
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 0.5f;
		color[2] = 0.0f;
	}

}
