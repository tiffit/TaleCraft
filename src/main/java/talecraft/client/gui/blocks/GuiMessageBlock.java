package talecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.TaleCraft;
import talecraft.client.ClientNetworkHandler;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADTickBox;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.tileentity.MessageBlockTileEntity;

public class GuiMessageBlock extends QADGuiScreen {
	MessageBlockTileEntity tileEntity;

	QADTextField textField_message;
	QADTextField textField_selector;

	public GuiMessageBlock(MessageBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();

		addComponent(new QADLabel("Message Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

		textField_message = new QADTextField(fontRenderer, 3, 14+20+4, width-6, 20);
		textField_message.setText(tileEntity.getMessage());
		textField_message.setTooltip("The message to send.");
		addComponent(textField_message);

		textField_selector = new QADTextField(fontRenderer, 3, 14+20+4+20+4, width-6, 20);
		textField_selector.setText(tileEntity.getPlayerSelector());
		textField_selector.setTooltip("Selector to select players to send the message to.", "Default: @a");
		addComponent(textField_selector);

		final QADTickBox tickBox_tellraw = new QADTickBox(2+60+2, 14, 20, 20);
		tickBox_tellraw.getModel().setState(tileEntity.getTellRaw());
		tickBox_tellraw.setTooltip("Should the message be parsed as raw json?","Default: Off");
		addComponent(tickBox_tellraw);

		QADButton setDataButton = QADFACTORY.createButton("Apply", 2, 14, 60, null);
		setDataButton.setAction(new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();

				// data?
				commandData.setString("playerSelector", textField_selector.getText());
				commandData.setString("message", textField_message.getText());
				commandData.setBoolean("tellraw", tickBox_tellraw.getState());

				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				displayGuiScreen(null);
			}
		});
		setDataButton.setTooltip("There is no auto-save, ", "so don't forget to click this button!");
		addComponent(setDataButton);

	}

	@Override
	public void layoutGui() {
		textField_message.setWidth(width-6);
		textField_selector.setWidth(width-6);
	}

}
