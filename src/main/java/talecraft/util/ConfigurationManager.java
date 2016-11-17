package talecraft.util;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationManager {
	
	public static boolean USE_VERSION_CHECKER;
	
	
	public static void init(Configuration config){
		USE_VERSION_CHECKER = config.getBoolean("use_version_checker", Configuration.CATEGORY_CLIENT, true, "Checks for a new update and if one is found, will message you about.");
	}

}
