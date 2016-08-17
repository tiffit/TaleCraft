package tiffit.talecraft.client.gui.worlddesc;

import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldSelectorInjector { //It Rhymes!

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent e){
		if(e.getGui() != null && e.getGui().getClass() == GuiWorldSelection.class){
			e.setGui(new NewWorldSelector());
		}
	}
	
}
