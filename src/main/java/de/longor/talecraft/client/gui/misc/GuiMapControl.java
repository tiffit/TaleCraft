package de.longor.talecraft.client.gui.misc;

import java.util.ArrayList;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADButtonBox;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADTextField;

public class GuiMapControl extends QADGuiScreen {
	
	public void buildGui(ArrayList<QADComponent> components) {
		
		addComponent(new QADLabel("World: " + mc.theWorld.getClass().getSimpleName(), 2, 2));
		addComponent(new QADLabel("Player: " + mc.thePlayer.getName(), 2, 12));
		
//		QADTextField field = new QADTextField(fontRendererObj, 32, 32, 120, 20);
//		field.autoCompleteOptions = new String[]{"This","Is","Text","Great!","abcdefghijklmnopqrstuvwxyz"};
//		components.add(field);
		
		/*
		QADButtonBox box = new QADButtonBox(
				new QADButton(0, 0, 160-4, "Weather, Clear"),
				new QADButton(0, 0, 160-4, "Weather, Rain"),
				new QADButton(0, 0, 160-4, "Weather, Thunder")
		);
		box.setPosition(2, 24);
		box.setSize(160, 20*8);
		components.add(box);
		//*/
		
//		QADScrollPanel scroll = new QADScrollPanel();
//		scroll.setPosition(2, 32);
//		scroll.setSize(100, 100);
//		scroll.components.add(new QADLabel("Text Text Text", 2, 2));
//		scroll.components.add(new QADLabel("Text Text Text", 2, 300 - 14));
//		scroll.components.add(new QADButton(0, 200, 50, "HERP"));
//		scroll.components.add(new QADButton(0+50, 200, 50, "DERP"));
//		components.add(scroll);
	}
	
	public void layoutGui() {
		
	}
	
}
