package tiffit.talecraft.entity.NPC;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import net.minecraft.util.text.TextFormatting;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkin;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkinType;

public class NPCSkinSelector extends QADGuiScreen {
	private QADScrollPanel panel;
	private NPCEditorGui npcGui;
	private NPCSkinType skinType;
	
	public NPCSkinSelector(NPCEditorGui npcGui) {
		this.setBehind(npcGui);
		this.returnScreen = npcGui;
		this.npcGui = npcGui;
	}

	@Override
	public void buildGui() {
		panel = new QADScrollPanel();
		panel.setPosition(0, 0);
		panel.setSize(200, 200);
		this.addComponent(panel);

		final int rowHeight = 20;

		NPCSkinType[] skins = NPCSkinType.values();
		panel.setViewportHeight(skins.length * rowHeight + 2);
		panel.allowLeftMouseButtonScrolling = true;

		int yOff = 1;
		for(final NPCSkinType skin : skins) {
			QADButton component = QADFACTORY.createButton(skin.name(), 2, yOff, 200 - 8, null);
			component.simplified = true;
			component.textAlignment = 0;
			component.setAction( new Runnable() {
				@Override public void run() {
					skinType = skin;
					panel.removeAllComponents();
					loadSkins();
				}
			});
			panel.addComponent(component);
			yOff += rowHeight;
		}
	}
	
	private void loadSkins(){
		final int rowHeight = 20;

		NPCSkin[] skins = NPCSkin.getSkinsWithType(skinType);
		panel.setViewportHeight(skins.length * rowHeight + 2);
		panel.allowLeftMouseButtonScrolling = true;

		int yOff = 1;
		for(final NPCSkin skin : skins) {
			QADButton component = new QADSkinButton(skin.name(), 2, yOff, 200 - 8, skin);
			component.simplified = true;
			component.textAlignment = 0;
			component.getModel().setIcon(skin.getResourceLocation());
			component.setAction( new Runnable() {
				@Override public void run() {
					npcGui.skin = skin;
					displayGuiScreen(getBehind());
				}
			});
			if(skin.hasAuthor()) component.setTooltip(TextFormatting.WHITE + "Author:", TextFormatting.GRAY + skin.getAuthor());
			panel.addComponent(component);
			yOff += rowHeight;
		}
		addComponent(panel);
	}

	@Override
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}

}
