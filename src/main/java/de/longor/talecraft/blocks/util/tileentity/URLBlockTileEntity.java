package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.NBTHelper;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class URLBlockTileEntity extends TCTileEntity {
	String url;
	String selector;
	
	public URLBlockTileEntity() {
		url = "https://www.reddit.com/r/talecraft/";
		selector = "@a";
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// none
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 1.0f;
		color[2] = 0.8f;
	}

	@Override
	public String getName() {
		return "URLBlock@"+this.getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		url = comp.getString("url");
		selector = comp.getString("selector");
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setString("url", url);
		comp.setString("selector", selector);
	}
	
	public String getURL() {
		return url;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public void trigger(EnumTriggerState triggerState) {
		if(triggerState.getBooleanValue()) {
			List<EntityPlayerMP> players = PlayerSelector.matchEntities(this, selector, EntityPlayerMP.class);
			StringNBTCommand command = new StringNBTCommand();
			command.command = "client.gui.openurl";
			command.data = NBTHelper.newSingleStringCompound("url",url);
			
			for(EntityPlayerMP player : players) {
				TaleCraft.network.sendTo(command, player);
			}
		}
	}
	
}
