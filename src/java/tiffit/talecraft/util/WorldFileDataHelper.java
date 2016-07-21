package tiffit.talecraft.util;

import java.io.File;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class WorldFileDataHelper {

	public static NBTTagCompound getTagFromFile(World world, String name){
		File worldF = world.getSaveHandler().getWorldDirectory();
		File dat = new File(worldF, name + ".dat");
		if(!dat.exists()){
			return new NBTTagCompound();
		}else{
			try {
				return CompressedStreamTools.read(dat);
			} catch (IOException e) {
				e.printStackTrace();
				return new NBTTagCompound();
			}
		}
	}
	
	public static void saveNBTToWorld(World world, String name, NBTTagCompound tag){
		File worldF = world.getSaveHandler().getWorldDirectory();
		File dat = new File(worldF, name + ".dat");
		if(!dat.exists()){
			try {
				dat.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			CompressedStreamTools.write(tag, dat);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
