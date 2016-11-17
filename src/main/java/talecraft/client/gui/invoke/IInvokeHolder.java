package talecraft.client.gui.invoke;

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
	void sendInvokeUpdate(NBTTagCompound newInvokeData);

	void sendCommand(String command, NBTTagCompound commandData);

	void switchInvokeType(String type);

}
