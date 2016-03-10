package de.longor.talecraft.client;

import java.io.File;
import java.io.IOException;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class ClientSettings {
	NBTTagCompound settings = new NBTTagCompound();
	Minecraft mc = Minecraft.getMinecraft();
	
	public void init() {
		{
			settings.setInteger("item.paste.reach", 9);
			settings.setInteger("item.paste.snap", 0);
			settings.setBoolean("invoke.tracker", false); // can cause lag, thus disabled at default
			settings.setBoolean("client.render.useAlternateSelectionTexture", false);
			settings.setBoolean("client.render.entity.point.fancy", true);
			settings.setBoolean("client.render.invokeVisualize", true);
			settings.setBoolean("client.infobar.enabled", true);
			settings.setBoolean("client.infobar.heldItemInfo", true);
			settings.setBoolean("client.infobar.movingObjectPosition", true);
			settings.setBoolean("client.infobar.visualizationMode", true);
			settings.setBoolean("client.infobar.showFPS", true);
			settings.setBoolean("client.infobar.showRenderables", true);
			settings.setBoolean("client.infobar.showWandInfo", true);
			settings.setBoolean("client.infobar.showLookDirectionInfo", true);
		}
		
    	File settingsFile = new File(Minecraft.getMinecraft().mcDataDir, "talecraft-client-settings.dat");
    	
    	if(!settingsFile.exists()) {
    		try {
				CompressedStreamTools.write(settings, settingsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	try {
			NBTTagCompound comp = CompressedStreamTools.read(settingsFile);
			settings.merge(comp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void getSettingsForServer(NBTTagCompound settingsForServer) {
		for(Object keyObj : settings.getKeySet()) {
			String key = (String)keyObj;
			
			if(key.startsWith("client.")) continue;
			if(key.startsWith("render.")) continue;
			
			settingsForServer.setTag(key, settings.getTag(key));
		}
	}

	public void save() {
		try {
	    	File settingsFile = new File(Minecraft.getMinecraft().mcDataDir, "talecraft-client-settings.dat");
			CompressedStreamTools.write(settings, settingsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public NBTTagCompound getNBT() {
		return settings;
	}
	
	public boolean getBoolean(String string) {
		return settings.getBoolean(string);
	}
	
	public int getInteger(String string) {
		return settings.getInteger(string);
	}
	
	public void setBoolean(String name, boolean newValue) {
		settings.setBoolean(name, newValue);
		save();
	}
	
	public void setInteger(String name, int newValue) {
		settings.setInteger(name, newValue);
		save();
	}
	
	public void send() {
		if(mc.thePlayer == null) return;
		
		String tccommand = "server.client.settings.update";
		NBTTagCompound settingsForServer = new NBTTagCompound();
		getSettingsForServer(settingsForServer);
		TaleCraft.network.sendToServer(new StringNBTCommand(tccommand, settingsForServer));
	}
	
}
