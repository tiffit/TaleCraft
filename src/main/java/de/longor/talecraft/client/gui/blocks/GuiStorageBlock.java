package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.StorageBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.invoke.BlockInvokeHolder;
import de.longor.talecraft.client.gui.invoke.InvokePanelBuilder;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiStorageBlock extends QADGuiScreen {
	private StorageBlockTileEntity tileEntity;
	
	public GuiStorageBlock(StorageBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Storage Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		addComponent(QADFACTORY.createButton("Set Region & Store", 2, 16 + (22*0), 100, new Runnable() {
			@Override
			public void run() {
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(mc.thePlayer);
				
				if(bounds == null)
					return;

				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "set");
				commandData.setIntArray("bounds", bounds);
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));
		
		addComponent(QADFACTORY.createButton("Store", 2, 16+(22*1), 100, new Runnable() {
			@Override
			public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "store");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));
		
		addComponent(QADFACTORY.createButton("Trigger (Paste)", 2, 16+(22*2), 100, new Runnable() {
			@Override
			public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "trigger");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));
		
	}
	
}
