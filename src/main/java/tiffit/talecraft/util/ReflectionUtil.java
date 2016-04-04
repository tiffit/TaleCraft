package tiffit.talecraft.util;

import java.io.File;
import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.util.Timer;
import net.minecraft.world.storage.SaveFormatComparator;

public class ReflectionUtil {

	public static Timer getMinecraftTimer(){
		try {
			Field timerF = Minecraft.class.getDeclaredField("timer");
			timerF.setAccessible(true);
			return (Timer) timerF.get(Minecraft.getMinecraft());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static File getWorldFolderFromSelection(GuiListWorldSelectionEntry entry){
		try{
			Field world = GuiListWorldSelectionEntry.class.getDeclaredField("field_186789_j");
			world.setAccessible(true);
			File image = (File) world.get(entry);
			if(image == null) return null;
			File parent = image.getParentFile();
			return parent;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static SaveFormatComparator getSFC(GuiListWorldSelectionEntry entry){
		try{
			Field world = GuiListWorldSelectionEntry.class.getDeclaredField("field_186786_g");
			world.setAccessible(true);
			SaveFormatComparator sfc = (SaveFormatComparator) world.get(entry);
			return sfc;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
