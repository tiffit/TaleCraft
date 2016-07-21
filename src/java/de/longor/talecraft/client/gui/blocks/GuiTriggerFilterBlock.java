package de.longor.talecraft.client.gui.blocks;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.TriggerFilterBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.invoke.BlockInvokeHolder;
import de.longor.talecraft.client.gui.invoke.InvokePanelBuilder;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import de.longor.talecraft.network.StringNBTCommandPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class GuiTriggerFilterBlock extends QADGuiScreen {
	TriggerFilterBlockTileEntity tileEntity;


	public GuiTriggerFilterBlock(TriggerFilterBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();

		addComponent(new QADLabel("Trigger Filter Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getTriggerInvoke(), new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

		addComponent(new QADTickBox(2+16*0, 16+24)).setModel(new REMOTENBTTICKBOXMODEL("filter_on", tileEntity.getDoFilterOn())).setTooltip("Filter ON?");
		addComponent(new QADTickBox(2+16*1, 16+24)).setModel(new REMOTENBTTICKBOXMODEL("filter_off", tileEntity.getDoFilterOff())).setTooltip("Filter OFF");
		addComponent(new QADTickBox(2+16*2, 16+24)).setModel(new REMOTENBTTICKBOXMODEL("filter_invert", tileEntity.getDoFilterInvert())).setTooltip("Filter INVERT?");
		addComponent(new QADTickBox(2+16*3, 16+24)).setModel(new REMOTENBTTICKBOXMODEL("filter_ignore", tileEntity.getDoFilterIgnore())).setTooltip("Filter IGNORE?");
	}

	private class REMOTENBTTICKBOXMODEL implements TickBoxModel {
		String tagName;
		boolean lastKnownState;

		public REMOTENBTTICKBOXMODEL(String string, boolean doFilterOn) {
			tagName = string;
			lastKnownState = doFilterOn;
		}

		@Override
		public void setState(boolean newState) {
			lastKnownState = newState;

			String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(tileEntity.getPos());
			NBTTagCompound commandData = new NBTTagCompound();
			commandData.setBoolean(tagName, lastKnownState);
			TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
		}

		@Override
		public boolean getState() {
			return lastKnownState;
		}

		@Override
		public void toggleState() {
			setState(!getState());
		}
	}

}
