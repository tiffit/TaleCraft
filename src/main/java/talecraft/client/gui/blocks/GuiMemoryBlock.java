package talecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.TaleCraft;
import talecraft.client.ClientNetworkHandler;
import talecraft.client.gui.invoke.BlockInvokeHolder;
import talecraft.client.gui.invoke.InvokePanelBuilder;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.model.AbstractButtonModel;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.tileentity.MemoryBlockTileEntity;

public class GuiMemoryBlock extends QADGuiScreen {
	MemoryBlockTileEntity tileEntity;

	public GuiMemoryBlock(MemoryBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();

		addComponent(new QADLabel("Memory Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getTriggerInvoke(), new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

		addComponent(new QADButton(2, 16+2+20+2, 60, new AbstractButtonModel("Reset") {
			@Override public void onClick() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "reset");
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				GuiMemoryBlock.this.mc.displayGuiScreen(null);
			}


		}));
		addComponent(new QADLabel("Triggered: " + tileEntity.getIsTriggered(), 2+60+2, 16+2+20+2+6));

	}

}
