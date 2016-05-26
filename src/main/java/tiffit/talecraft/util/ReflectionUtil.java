package tiffit.talecraft.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraftforge.fml.common.eventhandler.IEventListener;

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
			return image.getParentFile();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static SaveFormatComparator getSFC(GuiListWorldSelectionEntry entry){
		try{
			Field world = GuiListWorldSelectionEntry.class.getDeclaredField("field_186786_g");
			world.setAccessible(true);
			return (SaveFormatComparator) world.get(entry);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<IEventListener> getEventListeners(World world){
		try{
			Field eventListeners = World.class.getDeclaredField("eventListeners");
			eventListeners.setAccessible(true);
			return (List<IEventListener>) eventListeners.get(world);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
