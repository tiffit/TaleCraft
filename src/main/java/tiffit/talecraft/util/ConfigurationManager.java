package tiffit.talecraft.util;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationManager {

	public static boolean USE_PARTICLE_RENDERING;
	
	public static void init(Configuration config){
		USE_PARTICLE_RENDERING = config.getBoolean("particle_block_renderer", Configuration.CATEGORY_CLIENT, false, "Use the barrier-type system when rendering blocks");
	}
	
}
