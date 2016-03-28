package de.longor.talecraft.client.gui.misc;

import de.longor.talecraft.Reference;
import de.longor.talecraft.TaleCraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import tiffit.talecraft.util.ConfigurationManager;

public class GuiConfiguration extends GuiConfig {

	public GuiConfiguration(GuiScreen gui) {
		super(gui, ConfigurationManager.getElements(), Reference.MOD_ID, true, true, GuiConfig.getAbridgedConfigPath(TaleCraft.config.toString()));
	}

}