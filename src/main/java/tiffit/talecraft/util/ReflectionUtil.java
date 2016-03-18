package tiffit.talecraft.util;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

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

}
