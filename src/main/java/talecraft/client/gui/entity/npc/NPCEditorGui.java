package talecraft.client.gui.entity.npc;

import java.util.UUID;

import talecraft.TaleCraft;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADPanel;
import talecraft.entity.NPC.NPCData;
import talecraft.network.packets.NPCDataPacket;

public class NPCEditorGui extends QADGuiScreen {
	
	private NPCData data;
	private QADPanel master;
	private PanelGeneral genpan;
	private PanelAI aipan;
	private PanelDrops dropspan;
	private PanelTrades tradespan;
	private PanelEquips equipspan;
	private PanelScripts scriptspan;
	
	private QADButton GENERAL_BUTTON;
	private QADButton AI_BUTTON;
	private QADButton TRADES_BUTTON;
	private QADButton DROPS_BUTTON;
	private QADButton EQUIPS_BUTTON;
	private QADButton SCRIPTS_BUTTON;
	
	private UUID uuid;
	
	public NPCEditorGui(UUID uuid, NPCData data){
		this.data = data;
		this.uuid = uuid;
	}
	
	@Override
	public void buildGui() {
		QADPanel tabs = new QADPanel();
		tabs.setBounds(0, 0, getWidth(), 30);
		tabs.setBackgroundColor(0);
		master = new QADPanel();
		master.setBounds(0, 17, getWidth(), getHeight() - 40);
		master.setBackgroundColor(2);
		addComponent(master);
		final int width_div = getWidth()/5;
		final int forward = 8;
		GENERAL_BUTTON = new QADButton(forward, 5, width_div, "General");
		genpan = new PanelGeneral(data, getWidth(), getHeight());
		TabRunnable genrunnable = new TabRunnable(GENERAL_BUTTON, genpan);
		GENERAL_BUTTON.setAction(genrunnable);
		tabs.addComponent(GENERAL_BUTTON);
		AI_BUTTON = new QADButton(forward + width_div + (width_div/4), 5, width_div, "AI");
		aipan = new PanelAI(data, getWidth(), getHeight());
		AI_BUTTON.setAction(new TabRunnable(AI_BUTTON, aipan));
		tabs.addComponent(AI_BUTTON);
		TRADES_BUTTON = new QADButton(forward + width_div*2 + (width_div/4)*2, 5, width_div, "Trades");
		tradespan = new PanelTrades(data, getWidth(), getHeight());
		TRADES_BUTTON.setAction(new TabRunnable(TRADES_BUTTON, tradespan));
		tabs.addComponent(TRADES_BUTTON);
		DROPS_BUTTON = new QADButton(forward + width_div*3 + (width_div/4)*3, 5, width_div, "Drops");
		dropspan = new PanelDrops(data, getWidth(), getHeight());
		DROPS_BUTTON.setAction(new TabRunnable(DROPS_BUTTON, dropspan));
		tabs.addComponent(DROPS_BUTTON);
		EQUIPS_BUTTON = new QADButton(forward, 18, width_div, "Equipment");
		equipspan = new PanelEquips(data, getWidth(), getHeight());
		EQUIPS_BUTTON.setAction(new TabRunnable(EQUIPS_BUTTON, equipspan));
		tabs.addComponent(EQUIPS_BUTTON);
		SCRIPTS_BUTTON = new QADButton(forward + width_div*2 + (width_div/4)*2, 18, width_div, "Scripts");
		scriptspan = new PanelScripts(data, getWidth(), getHeight());
		SCRIPTS_BUTTON.setAction(new TabRunnable(SCRIPTS_BUTTON, scriptspan));
		tabs.addComponent(SCRIPTS_BUTTON);
		int height = 10;
		GENERAL_BUTTON.setHeight(height);
		AI_BUTTON.setHeight(height);
		TRADES_BUTTON.setHeight(height);
		DROPS_BUTTON.setHeight(height);
		EQUIPS_BUTTON.setHeight(height);
		SCRIPTS_BUTTON.setHeight(height);
		
		addComponent(tabs);
		genrunnable.run();
		
		QADButton SAVE = new QADButton(0, getHeight() - 21, getWidth(), "Save");
		SAVE.setAction(new Runnable() {
			@Override
			public void run() {
				genpan.save(data);
				aipan.save(data);
				dropspan.save(data);
				tradespan.save(data);
				equipspan.save(data);
				scriptspan.save(data);
				TaleCraft.network.sendToServer(new NPCDataPacket(uuid, data.toNBT()));
				
			}
		});
		addComponent(SAVE);
	}
	
	private class TabRunnable implements Runnable{
		private QADPanel panel;
		private QADButton button;
		
		public TabRunnable(QADButton button, QADPanel panel) {
			this.panel = panel;
			this.button = button;
			panel.setBounds(master.getX(), master.getY(), master.getWidth(), master.getHeight());
		}
		
		@Override
		public void run() {
			GENERAL_BUTTON.setEnabled(true);
			AI_BUTTON.setEnabled(true);
			TRADES_BUTTON.setEnabled(true);
			DROPS_BUTTON.setEnabled(true);
			EQUIPS_BUTTON.setEnabled(true);
			SCRIPTS_BUTTON.setEnabled(true);
			button.setEnabled(false);
			master.removeAllComponents();
			master.addComponent(panel);
		}
	}
}
