package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.ClockBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.TCGuiScreen;
import de.longor.talecraft.client.gui.invoke.BlockInvokeHolder;
import de.longor.talecraft.client.gui.invoke.InvokePanelBuilder;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.proxy.ClientProxy;

public class GuiClockBlock extends QADGuiScreen {
	ClockBlockTileEntity tileEntity;
	
	public GuiClockBlock(ClockBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		
		addComponent(new QADLabel("Clock Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		int column0x = 4;
		int column0w = 60;
		int column1x = column0x + column0w + 2;
		int column1w = 100;
		int column2x = column1x + column1w + 2;
		int column2w = 100;
		int row0y = 22*1;
		int row1y = 22*2;
		int row2y = 22*3;
		int row3y = 22*4;
		int row4y = 22*5;
		int row5y = 22*6;
		int row6y = 22*7;
		int row7y = 22*8;
		int row8y = 22*9;
		
		{
			StringBuilder b = new StringBuilder(64);
			b.append(EnumChatFormatting.RED).append("Last known State: ").append(EnumChatFormatting.RESET);
			b.append(EnumChatFormatting.BOLD).append(tileEntity.active ? "ON" : "OFF").append(EnumChatFormatting.RESET);
			b.append(", repeat: "+tileEntity.repeat);
			b.append(", speed: "+tileEntity.speed);
			b.append(", time: "+tileEntity.time);
			
			addComponent(new QADLabel(b.toString(), column0x, row7y+6));
		}
		
		addComponent(new QADLabel(EnumChatFormatting.YELLOW + "Tick", column0x, row0y+6));
		InvokePanelBuilder.build(this, this, column1x, row0y, tileEntity.getTickInvoke(), new BlockInvokeHolder(position, "clockInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		addComponent(new QADLabel(EnumChatFormatting.YELLOW + "Start", column0x, row1y+6));
		InvokePanelBuilder.build(this, this, column1x, row1y, tileEntity.getStartInvoke(), new BlockInvokeHolder(position, "clockStartInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		addComponent(new QADLabel(EnumChatFormatting.YELLOW + "Stop", column0x, row2y+6));
		InvokePanelBuilder.build(this, this, column1x, row2y, tileEntity.getStopInvoke(), new BlockInvokeHolder(position, "clockStopInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		
		addComponent(new QADLabel("Repeats", column0x, row3y+6));
		addComponent(new QADLabel("Speed", column0x, row4y+6));
		addComponent(new QADLabel("Time", column0x, row5y+6));
		
		final QADTextField fieldRepeat = QADFACTORY.createNumberTextField(tileEntity.set_repeat, column1x+2, row3y+2, column1w-4, 1000000, 0);
		final QADTextField fieldSpeed = QADFACTORY.createNumberTextField(tileEntity.set_speed, column1x+2, row4y+2, column1w-4, 20*60, 1);
		final QADTextField fieldTime = QADFACTORY.createNumberTextField(tileEntity.set_time, column1x+2, row5y+2, column1w-4, 20*60*1, 1);
		fieldRepeat.setTooltip("The amount of times this clock will 'tick'.");
		fieldSpeed.setTooltip("How fast this clock will count down.");
		fieldTime.setTooltip("The number the countdown starts at.");
		addComponent(fieldRepeat);
		addComponent(fieldSpeed);
		addComponent(fieldTime);
		
		QADButton setDataButton = QADFACTORY.createButton("Apply", column1x, row6y, column1w, null);
		setDataButton.setAction(new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				NBTTagCompound invokeData = new NBTTagCompound();
				commandData.setTag("clockInvoke", invokeData);
				
				commandData.setInteger("init_repeat", fieldRepeat.asInteger(10));
				commandData.setInteger("init_speed", fieldSpeed.asInteger(1));
				commandData.setInteger("init_time", fieldTime.asInteger(20));
				
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		setDataButton.setTooltip("There is no auto-save, ", "so don't forget to click this button!");
		addComponent(setDataButton);
		
		QADButton buttonStart = QADFACTORY.createButton("Start", column0x, row8y, column0w, null);
		buttonStart.setAction(new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "start");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiClockBlock.this.mc.displayGuiScreen(null);
			}
		});
		buttonStart.setTooltip("Start the clocks countdown.");
		addComponent(buttonStart);
		
		QADButton buttonPause = QADFACTORY.createButton("Pause", column1x, row8y, column1w, null);
		buttonPause.setAction(new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "pause");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiClockBlock.this.mc.displayGuiScreen(null);
			}
		});
		buttonPause.setTooltip("This button pauses the clocks countdown.");
		addComponent(buttonPause);
		
		QADButton buttonStop = QADFACTORY.createButton("Stop", column2x, row8y, column2w, null);
		buttonStop.setAction(new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "stop");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiClockBlock.this.mc.displayGuiScreen(null);
			}
		});
		buttonStop.setTooltip("This button stops the clocks countdown.");
		addComponent(buttonStop);
		
	}
	
}
