package talecraft.client.gui.entity.npc;

import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.entity.NPC.NPCData;

public class PanelScripts extends NPCPanel {
	
	private QADTextField SCRIPT_INTERACT;
	private QADTextField SCRIPT_UPDATE;
	private QADTextField SCRIPT_DEATH;
	
	public PanelScripts(NPCData data, int width, int height) {
		super(data, width, height);
		addComponent(new QADLabel("Interact Script", 4, 5));
		SCRIPT_INTERACT = new QADTextField(2, 15, width/4, 20);
		SCRIPT_INTERACT.setText(data.getInteractScript());
		addComponent(SCRIPT_INTERACT);
		
		addComponent(new QADLabel("Update Script", 4 + width/4 + 5, 5));
		SCRIPT_UPDATE = new QADTextField(2 + width/4 + 5, 15, width/4, 20);
		SCRIPT_UPDATE.setText(data.getUpdateScript());
		addComponent(SCRIPT_UPDATE);
		
		addComponent(new QADLabel("Death Script", 4 + width/4 + width/4 + 10, 5));
		SCRIPT_DEATH = new QADTextField(2 + width/4 + width/4 + 10, 15, width/4, 20);
		SCRIPT_DEATH.setText(data.getDeathScript());
		addComponent(SCRIPT_DEATH);
	}

	@Override
	public void save(NPCData data) {
		data.setInteractScript(SCRIPT_INTERACT.getText());
		data.setUpdateScript(SCRIPT_UPDATE.getText());
		data.setDeathScript(SCRIPT_DEATH.getText());
	}

}
