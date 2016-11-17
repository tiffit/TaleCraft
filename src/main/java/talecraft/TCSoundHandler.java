package talecraft;

import java.lang.reflect.Field;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public final class TCSoundHandler {

	public static SoundEvent SONG1, SONG2, SONG3, SONG4, SONG5, SONG6, SONG7, SONG8;
	public static SoundEvent EFFECT1, EFFECT2, EFFECT3, EFFECT4, EFFECT5, EFFECT6, EFFECT7, EFFECT8;
	public static SoundEvent EXTRA1, EXTRA2, EXTRA3, EXTRA4;
	
	public static SoundEvent DryFire;
	public static SoundEvent Reload;
	public static SoundEvent PistolFire;
	public static SoundEvent RifleFire;
	public static SoundEvent ShotgunFire;
	public static SoundEvent ShotgunReload;
	
	private static int index = 0;
	
	public static void init() {
		index = SoundEvent.REGISTRY.getKeys().size();
		
		SONG1 = register("SONG1");
		SONG2 = register("SONG2");
		SONG3 = register("SONG3");
		SONG4 = register("SONG4");
		SONG5 = register("SONG5");
		SONG6 = register("SONG6");
		SONG7 = register("SONG7");
		SONG8 = register("SONG8");
		
		EFFECT1 = register("EFFECT1");
		EFFECT2 = register("EFFECT2");
		EFFECT3 = register("EFFECT3");
		EFFECT4 = register("EFFECT4");
		EFFECT5 = register("EFFECT5");
		EFFECT6 = register("EFFECT6");
		EFFECT7 = register("EFFECT7");
		EFFECT8 = register("EFFECT8");
		
		EXTRA1 = register("EXTRA1");
		EXTRA2 = register("EXTRA2");
		EXTRA3 = register("EXTRA3");
		EXTRA4 = register("EXTRA4");
		
		DryFire = register("DryFire");
		Reload = register("Reload");
		PistolFire = register("PistolFire");
		RifleFire = register("RifleFire");
		ShotgunFire = register("ShotgunFire");
		ShotgunReload = register("ShotgunReload");
	}
	
	private static SoundEvent register(String name) {
		ResourceLocation loc = new ResourceLocation(Reference.MOD_ID + ":" + name);
		SoundEvent e = new SoundEvent(loc);
		SoundEvent.REGISTRY.register(index, loc, e);
		index++;
		return e;
	}
	
	public static enum SoundEnum{
		SONG1, SONG2, SONG3, SONG4, SONG5, SONG6, SONG7, SONG8,
		EFFECT1, EFFECT2, EFFECT3, EFFECT4, EFFECT5, EFFECT6, EFFECT7, EFFECT8,
		EXTRA1, EXTRA2, EXTRA3, EXTRA4;
		
		private SoundEvent se;
		
		public SoundEvent getSoundEvent(){
			if(se != null) return se;
			Class<TCSoundHandler> tcsh = TCSoundHandler.class;
			try {
				Field field = tcsh.getField(name());
				se = (SoundEvent) field.get(null);
				return se;
			} catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}
	}
	
}