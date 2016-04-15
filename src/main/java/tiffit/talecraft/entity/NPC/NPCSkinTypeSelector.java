package tiffit.talecraft.entity.NPC;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkinType;

public class NPCSkinTypeSelector extends QADGuiScreen {
	private QADScrollPanel panel;
	private NPCEditorGui npcGui;
	
	public NPCSkinTypeSelector(NPCEditorGui npcGui) {
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

		NPCSkinType[] types = NPCSkinType.values();
		panel.setViewportHeight(types.length * rowHeight + 2);
		panel.allowLeftMouseButtonScrolling = true;

		int yOff = 1;
		for(final NPCSkinType type : types) {
			QADButton component = QADFACTORY.createButton(type.name(), 2, yOff, 200 - 8, null);
			component.simplified = true;
			component.textAlignment = 0;
			component.setAction( new Runnable() {
				@Override public void run() {
					displayGuiScreen(new NPCSkinSelector(npcGui));
				}
			});

			panel.addComponent(component);
			yOff += rowHeight;
		}
	}

	@Override
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}

}
