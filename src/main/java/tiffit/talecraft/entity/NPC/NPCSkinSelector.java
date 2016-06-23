package tiffit.talecraft.entity.NPC;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.layout.QADListLayout;
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

		NPCSkinType[] skins = NPCSkinType.values();
		for(final NPCSkinType skin : skins) {
			QADButton component = new QADButton(skin.name());
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
		}
		panel.setLayout(new QADListLayout(0.25, 20));
		addComponent(panel);
	}
	
	private void loadSkins(){
		NPCSkin[] skins = NPCSkin.getSkinsWithType(skinType);
		for(final NPCSkin skin : skins) {
			QADButton component = new QADSkinButton(skin.name(), skin);
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
		}
		panel.setLayout(new QADListLayout(0.25, 20));
		addComponent(panel);
	}

	@Override
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}

}
