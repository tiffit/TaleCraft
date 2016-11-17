package talecraft.client.gui.npc;

import net.minecraft.util.text.TextFormatting;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.layout.QADListLayout;
import talecraft.entity.NPC.EnumNPCModel;
import talecraft.entity.NPC.EnumNPCSkin;

public class NPCSkinSelector extends QADGuiScreen {
	private QADScrollPanel panel;
	private NPCEditorGui npcGui;
	private EnumNPCModel model;
	
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

		EnumNPCModel[] models = EnumNPCModel.values();
		for(final EnumNPCModel model : models) {
			QADButton component = new QADButton(model.name());
			component.simplified = true;
			component.textAlignment = 0;
			component.setAction( new Runnable() {
				@Override public void run() {
					NPCSkinSelector.this.model = model;
					panel.removeAllComponents();
					loadSkins();
				}
			});
			panel.addComponent(component);
		}
		QADButton component = new QADButton("Other");
		component.simplified = true;
		component.textAlignment = 0;
		component.setAction( new Runnable() {
			@Override public void run() {
				NPCSkinSelector.this.model = null;
				panel.removeAllComponents();
				loadSkins();
			}
		});
		panel.addComponent(component);
		panel.setLayout(new QADListLayout(0.28, 20));
		addComponent(panel);
	}
	
	private void loadSkins(){
		EnumNPCSkin[] skins = EnumNPCSkin.getSkinsWithModel(model);
		for(final EnumNPCSkin skin : skins) {
			QADButton component = new QADSkinButton(skin.toString(), skin);
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
		panel.setLayout(new QADListLayout(0.4, 20));
		addComponent(panel);
	}

	@Override
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}

}
