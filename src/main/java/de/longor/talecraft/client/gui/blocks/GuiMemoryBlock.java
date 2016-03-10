package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.InverterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.MemoryBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.invoke.BlockInvokeHolder;
import de.longor.talecraft.client.gui.invoke.InvokePanelBuilder;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import de.longor.talecraft.client.gui.qad.model.AbstractButtonModel;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiMemoryBlock extends QADGuiScreen {
	MemoryBlockTileEntity tileEntity;
	
	public GuiMemoryBlock(MemoryBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		
		addComponent(new QADLabel("Memory Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getTriggerInvoke(), new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		
		addComponent(new QADButton(2, 16+2+20+2, 60, new AbstractButtonModel("Reset") {
			@Override public void onClick() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "reset");
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiMemoryBlock.this.mc.displayGuiScreen(null);
			}
		}));
		addComponent(new QADLabel("Triggered: " + tileEntity.getIsTriggered(), 2+60+2, 16+2+20+2+6));
		
	}
	
}
