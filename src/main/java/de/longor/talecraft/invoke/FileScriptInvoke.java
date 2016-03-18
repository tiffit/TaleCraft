package de.longor.talecraft.invoke;

import de.longor.talecraft.TaleCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FileScriptInvoke implements IScriptInvoke {
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
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
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
