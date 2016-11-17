package talecraft.client.gui.invoke;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.TaleCraft;
import talecraft.client.ClientNetworkHandler;
import talecraft.network.packets.StringNBTCommandPacket;

public class BlockInvokeHolder implements IInvokeHolder {
	BlockPos blockPosition;
	String invokeName;

	public BlockInvokeHolder(BlockPos position, String invokeName) {
		this.blockPosition = position;
		this.invokeName = invokeName;
	}

	@Override
	public void sendInvokeUpdate(NBTTagCompound newInvokeData) {
		String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(blockPosition);
		NBTTagCompound commandData = new NBTTagCompound();
		commandData.setTag(invokeName, newInvokeData);
		TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
	}

	@Override
	public void sendCommand(String command, NBTTagCompound commandData) {
		String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(blockPosition);

		if(commandData == null)
			commandData = new NBTTagCompound();
		commandData.setString("command", command);

		// Send command
		TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));

		// Close whatever gui is open
		Minecraft.getMinecraft().displayGuiScreen(null);
	}

	@Override
	public void switchInvokeType(String type) {
		String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(blockPosition);
		NBTTagCompound commandData = new NBTTagCompound();
		NBTTagCompound invokeData = new NBTTagCompound();
		invokeData.setString("type", type);
		commandData.setTag(invokeName, invokeData);
		TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
	}

}
