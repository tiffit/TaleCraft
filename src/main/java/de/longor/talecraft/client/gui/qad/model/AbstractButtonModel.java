package de.longor.talecraft.client.gui.qad.model;

import net.minecraft.util.ResourceLocation;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;

public abstract class AbstractButtonModel implements ButtonModel {
	public String text;
	public ResourceLocation icon;
	
	public AbstractButtonModel(String txt, ResourceLocation icn) {
		this.text = txt;
		this.icon = icn;
	}
	
	public AbstractButtonModel(String txt) {
		this.text = txt;
		this.icon = null;
	}
	
	public void setText(String newText) {
		this.text = newText;
	}
	
	public void setIcon(ResourceLocation newIcon) {
		this.icon = newIcon;
	}
	
	@Override public String getText() { return text; }
	@Override public ResourceLocation getIcon() { return icon; }
	
	@Override public abstract void onClick();
}