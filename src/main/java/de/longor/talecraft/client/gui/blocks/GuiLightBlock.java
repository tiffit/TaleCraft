package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.LightBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADSlider;
import de.longor.talecraft.client.gui.qad.model.DefaultSliderModel;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiLightBlock extends QADGuiScreen {
	LightBlockTileEntity tileEntity;
	
	public GuiLightBlock(LightBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		this.setDoesPauseGame(false);
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Light @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		final QADSlider slider = addComponent(new QADSlider(new DefaultSliderModel(tileEntity.getLightValue(), 15)));
		slider.setX(2);
		slider.setY(16);
		slider.setSliderAction(new Runnable() {
			@Override public void run() {
				int newValue = (int) (slider.getSliderValue() * 16);
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "set");
				commandData.setInteger("lightValue", MathHelper.clamp_int(newValue, 0, 15));
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		
		QADButton buttonToggle = QADFACTORY.createButton("Toggle", 2, 40, 100, new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "toggle");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		addComponent(buttonToggle);
		
		QADButton buttonEnable = QADFACTORY.createButton("Enable", 2 + 100 + 2, 40, 50, new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "on");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		addComponent(buttonEnable);
		
		QADButton buttonDisable = QADFACTORY.createButton("Disable", 2 + 100 + 2 + 50 + 2, 40, 50, new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "off");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		addComponent(buttonDisable);
	}
	
}
