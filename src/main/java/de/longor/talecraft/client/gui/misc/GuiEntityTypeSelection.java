package de.longor.talecraft.client.gui.misc;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.util.GObjectTypeHelper;

public class GuiEntityTypeSelection extends QADGuiScreen {
	public static interface EntityTypeDataLink {
		public void setType(String identifier);
	}
	
	private EntityTypeDataLink dataLink;
	private QADScrollPanel panel;
	
	public GuiEntityTypeSelection(GuiScreen gui, EntityTypeDataLink dataLink) {
		this.setBehind(gui);
		this.returnScreen = gui;
		this.dataLink = dataLink;
	}
	
	public void buildGui() {
		panel = new QADScrollPanel();
		panel.setPosition(0, 0);
		panel.setSize(200, 200);
		this.addComponent(panel);
		
		final int rowHeight = 12;
		
		Collection<String> names = GObjectTypeHelper.getEntityNameList();
		
		// Sort dat list
		{
			List<String> names2 = Lists.newArrayList(names);
			names2.sort(new Comparator<String>() {
				@Override public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			names = names2;
		}
		
		panel.setViewportHeight(names.size() * rowHeight + 2);
		panel.allowLeftMouseButtonScrolling = true;
		
		int yOff = 1;
		for(final String typeName : names) {
			QADButton component = QADFACTORY.createButton(typeName, 2, yOff, width);
			component.simplified = true;
			component.textAlignment = 0;
			component.setHeight(12);
			component.setAction( new Runnable() {
				final String pt = typeName;
				@Override public void run() {
					dataLink.setType(pt);
					displayGuiScreen(getBehind());
				}
			});
			
			panel.addComponent(component);
			yOff += rowHeight;
		}
	}
	
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}
	
}