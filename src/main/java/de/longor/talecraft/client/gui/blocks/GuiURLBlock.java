package de.longor.talecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.MessageBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.URLBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiURLBlock extends QADGuiScreen {
	URLBlockTileEntity tileEntity;
	
	QADTextField textField_url;
	QADTextField textField_selector;
	
	public GuiURLBlock(URLBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		
		addComponent(new QADLabel("URL Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		textField_url = new QADTextField(fontRendererObj, 3, 14+20+4, width-6, 20);
		textField_url.setText(tileEntity.getURL());
		textField_url.setTooltip("The URL to open.");
		addComponent(textField_url);
		
		textField_selector = new QADTextField(fontRendererObj, 3, 14+20+4+20+4, width-6, 20);
		textField_selector.setText(tileEntity.getSelector());
		textField_selector.setTooltip("Selector to select players.", "Default: @a");
		addComponent(textField_selector);
		
		QADButton setDataButton = QADFACTORY.createButton("Apply", 2, 14, 60, null);
		setDataButton.setAction(new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				
				commandData.setString("selector", textField_selector.getText());
				commandData.setString("url", textField_url.getText());
				
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				displayGuiScreen(null);
			}
		});
		setDataButton.setTooltip("There is no auto-save, ", "so don't forget to click this button!");
		addComponent(setDataButton);
		
	}
	
	public void layoutGui() {
		textField_url.setWidth(width-6);
		textField_selector.setWidth(width-6);
	}
	
	
	
}
