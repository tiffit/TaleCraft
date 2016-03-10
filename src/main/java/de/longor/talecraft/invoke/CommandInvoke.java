package de.longor.talecraft.invoke;

import net.minecraft.nbt.NBTTagCompound;

public class CommandInvoke implements IInvoke {
	public static final String TYPE = "CommandInvoke";
	private String command;
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void getColor(float[] color_out) {
		color_out[0] = 0.0f;
		color_out[1] = 0.5f;
		color_out[2] = 1.0f;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("command", command);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		command = compound.getString("command");
	}

}
