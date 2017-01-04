package talecraft.client.gui.entity.npc;

import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADTickBox;
import talecraft.entity.NPC.NPCData;

public class PanelQuests extends NPCPanel {

	private QADTickBox has_quests;
	
	public PanelQuests(NPCData data, int width, int height) {
		super(data, width, height);
		addComponent(new QADLabel("Has Quest:", 4, 5));
		has_quests = new QADTickBox(57, 2, 15, 15);
		has_quests.getModel().setState(data.hasQuest());
		addComponent(has_quests);
	}

	@Override
	public void save(NPCData data) {
		if(!has_quests.getState()){
			data.setQuest(null);
			return;
		}
	}

}
