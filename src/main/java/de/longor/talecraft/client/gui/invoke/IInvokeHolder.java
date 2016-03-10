package de.longor.talecraft.client.gui.invoke;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Describes a holder for a single invoke.
 * THIS IS A CLIENT-SIDE INTERFACE.
 **/
public interface IInvokeHolder {
	
	/**
	 * In case of block: blockDataMarge X Y Z
	 * In case of entity: entityDataMerge UUID
	 **/
	public void sendInvokeUpdate(NBTTagCompound newInvokeData);
	
	public void sendCommand(String command, NBTTagCompound commandData);
	
	public void switchInvokeType(String type);
	
}
