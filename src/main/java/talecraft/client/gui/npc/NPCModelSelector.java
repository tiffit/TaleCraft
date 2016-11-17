package talecraft.client.gui.npc;

import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.layout.QADListLayout;
import talecraft.client.gui.qad.model.AbstractButtonModel;
import talecraft.entity.NPC.EnumNPCModel;

public class NPCModelSelector extends QADGuiScreen {

	private QADScrollPanel panel;
	private NPCEditorGui npcGui;
	
	public NPCModelSelector(NPCEditorGui npcGui) {
		this.npcGui = npcGui;
		this.returnScreen = npcGui;
		setBehind(npcGui);
	}
	
	@Override
	public void buildGui() {
		panel = new QADScrollPanel();
		for(EnumNPCModel model : EnumNPCModel.values()){
			final EnumNPCModel currentModel = model;
			panel.addComponent(new QADButton(0, 0, 200, new AbstractButtonModel(currentModel.toString()) {
				@Override
				public void onClick() {
					npcGui.model = currentModel;
					if(npcGui.skin.getModelType() != currentModel) npcGui.skin = currentModel.getDefaultSkin();
					displayGuiScreen(npcGui);
				}
			}));
		}
		panel.setLayout(new QADListLayout());
		addComponent(panel);
	}
	
	@Override
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}

}
