package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

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
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RelayBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.invoke.BlockInvokeHolder;
import de.longor.talecraft.client.gui.invoke.InvokePanelBuilder;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.NullInvoke;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiRelayBlock extends QADGuiScreen {
	RelayBlockTileEntity tileEntity;
	
	public GuiRelayBlock(RelayBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		
		addComponent(new QADLabel("Relay Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		addComponent(new QADLabel("Invokes: " + tileEntity.getInvokes().size(), 2 + 24, 16 + 6));
		
		addComponent(QADFACTORY.createButton("+", 2, 16, 20, new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "invoke_add");
				
				// Send command
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
				
				// close whatever gui is open
				Minecraft.getMinecraft().displayGuiScreen(null);
			}
		}).setTooltip("Adds a new invoke.","",EnumChatFormatting.RED+"WARNING:","The current screen will close","by pressing this button.","All changes will be lost!"));
		
		int i = 0;
		int yOff = 16+22;
		int allow = InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL;
		for(Entry<String, IInvoke> entry : tileEntity.getInvokes().entrySet()) {
			
			final String id = entry.getKey();
			final IInvoke invoke = entry.getValue();
			InvokePanelBuilder.build(this, this, 2 + 20 + 2, yOff, invoke, new BlockInvokeHolder(position, id), allow);
			
			addComponent(QADFACTORY.createButton(QADButton.ICON_DELETE, 2, yOff, 20, new Runnable() {
				@Override public void run() {
					String commandString = ClientNetworkHandler.makeBlockCommand(position);
					
					NBTTagCompound commandData = new NBTTagCompound();
					commandData.setString("command", "invoke_remove");
					commandData.setString("invokeToRemove", id);
					
					// Send command
					TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
					
					// close whatever gui is open
					Minecraft.getMinecraft().displayGuiScreen(null);
				}
			}).setTooltip("Remove invoke " + id));
			
			
			yOff += 22;
			i++;
		}
		
	}
	
}
