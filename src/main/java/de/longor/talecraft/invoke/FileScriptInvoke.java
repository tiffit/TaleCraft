package de.longor.talecraft.invoke;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.longor.talecraft.TaleCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class FileScriptInvoke implements IInvoke, IScriptInvoke {
	public static final String TYPE = "FileScriptInvoke";
	String fileName;
	String bufferedScript;
	
	public FileScriptInvoke() {
		fileName = "";
		bufferedScript = null;
	}
	
	public FileScriptInvoke(String name) {
		fileName = name;
		bufferedScript = null;
	}
	
	private void loadScript() {
		MinecraftServer server = MinecraftServer.getServer();
		World world = server.getEntityWorld();
		bufferedScript = TaleCraft.globalScriptManager.loadScript(world, fileName);
	}
	
	@Override
	public void reloadScript() {
		bufferedScript = null;
		loadScript();
	}
	
	@Override
	public String getScript() {
		if(bufferedScript == null) {
			loadScript();
		}
		
		if(bufferedScript == null) {
			return "";
		}
		
		return bufferedScript;
	}

	@Override
	public String getScriptName() {
		return fileName;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void getColor(float[] color_out) {
		color_out[0] = 1.0f;
		color_out[1] = 0.0f;
		color_out[2] = 0.0f;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("scriptFileName", fileName.trim());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		fileName = compound.getString("scriptFileName").trim();
	}
	
}
