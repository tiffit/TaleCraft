package tiffit.talecraft.util;

import java.util.List;

import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigurationManager {

	public static boolean useBarrierRendering;

	public static void init(Configuration config) {
		useBarrierRendering = config.getBoolean("particle_block_renderer", Configuration.CATEGORY_CLIENT, false, "Use the barrier-type system when rendering blocks");

		if(config.hasChanged()) {
			config.save();
		}
	}

	public static List<IConfigElement> getElements() {
		List<IConfigElement> elements = Lists.newArrayList();
		elements.addAll(new ConfigElement(TaleCraft.config.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements());
		return elements;
	}

}
