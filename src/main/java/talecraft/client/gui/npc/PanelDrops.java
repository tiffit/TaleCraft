package talecraft.client.gui.npc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.entity.NPC.NPCData;
import talecraft.entity.NPC.NPCInventoryData.NPCDrop;
import talecraft.entity.NPC.NPCShop.NPCTrade;

public class PanelDrops extends NPCPanel{
	
	private List<NPCDrop> drops;
	private int index = 0;
	
	public PanelDrops(NPCData data, int width, int height){
		super(data, width, height);
		drops = new ArrayList<NPCDrop>(data.getDrops());
		addComponent(getPanel());
	}
	
	public QADPanel getPanel(){
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setBounds(0, 0, width, height);
		QADButton addtrade = new QADButton("Add Drop");
		addtrade.setBounds(width/4 + 1, 5, width/2, 20);
		addtrade.setAction(new Runnable() {
			@Override
			public void run() {
				drops.add(new NPCDrop(new ItemStack(Items.ROTTEN_FLESH), 1.0F));
				resetPanel();
			}
		});
		panel.addComponent(addtrade);
		addComponent(new QADLabel("Drop #" + (index) + "/" + drops.size(), 4, 30));
		QADButton moveDown = new QADButton("<- Prev Drop");
		moveDown.setBounds(1, 5, width/4, 20);
		moveDown.setAction(new Runnable() {
			@Override
			public void run() {
				moveIndex(false);
			}
		});
		panel.addComponent(moveDown);
		if(index <= 0) moveDown.setEnabled(false);
		QADButton moveUp = new QADButton("Next Drop ->");
		moveUp.setBounds(width - width/4, 5, width/4 - 1, 20);
		moveUp.setAction(new Runnable() {
			@Override
			public void run() {
				moveIndex(true);
			}
		});
		if(index >= drops.size()) moveUp.setEnabled(false);
		panel.addComponent(moveUp);
		
		return panel;
	}
	
	private void moveIndex(boolean up){
		if(up){
			if(index < drops.size()){
				index++;
				resetPanel();
			}
		}else{
			if(index > 0){
				index--;
				resetPanel();
			}
		}
	}
	
	public void resetPanel(){
		removeAllComponents();
		addComponent(getPanel());
	}

	@Override
	public void save(NPCData data) {
		
	}
}
