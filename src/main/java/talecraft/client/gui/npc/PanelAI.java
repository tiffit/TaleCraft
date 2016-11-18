package talecraft.client.gui.npc;

import java.util.ArrayList;
import java.util.List;

import talecraft.client.gui.qad.QADDropdownBox;
import talecraft.client.gui.qad.QADDropdownBox.ListModel;
import talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.entity.NPC.EntityNPC.NPCType;
import talecraft.entity.NPC.NPCData;

public class PanelAI extends NPCPanel{
	
	private NPCType TYPE;
	private QADNumberTextField DAMAGE_FIELD;
	private QADNumberTextField SPEED_FIELD;
	
	public PanelAI(NPCData data, int width, int height){
		super(data, width, height);
		addComponent(new QADLabel("Aggression", 4, 0));
		QADDropdownBox type_selector = new QADDropdownBox(new AggroListModel(), new AggroListModelItem(data.getType()));
		type_selector.setBounds(2, 10, width/5, 20);
		addComponent(type_selector);
		addComponent(new QADLabel("Damage", 4, 35));
		DAMAGE_FIELD = new QADNumberTextField(2, 45, width/5, 20, data.getDamage(), NumberType.DECIMAL);
		addComponent(DAMAGE_FIELD);
		addComponent(new QADLabel("Speed", 7 + width/5, 35));
		SPEED_FIELD = new QADNumberTextField(5 + width/5, 45, width/5, 20, data.getSpeed(), NumberType.DECIMAL);
		addComponent(SPEED_FIELD);
	}

	@Override
	public void save(NPCData data) {
		data.setType(TYPE);
		data.setDamage(DAMAGE_FIELD.getValue().floatValue());
		data.setSpeed(SPEED_FIELD.getValue().doubleValue());
	}
	
	private class AggroListModel implements ListModel{

		private List<ListModelItem> items = new ArrayList<ListModelItem>();
		private List<ListModelItem> filtered = new ArrayList<ListModelItem>();
		
		public AggroListModel() {
			for(NPCType type : NPCType.values()){
				items.add(new AggroListModelItem(type));
			}
			filtered.addAll(items);
		}
		
		
		@Override
		public void onSelection(ListModelItem item) {
			TYPE = ((AggroListModelItem) item).type;
		}

		@Override
		public boolean hasItems() {return true;}

		@Override
		public int getItemCount() {return items.size();}

		@Override
		public List<ListModelItem> getItems() {return items;}

		@Override
		public void applyFilter(String filter) {
			filtered.clear();
			for(ListModelItem item : items){
				AggroListModelItem type = (AggroListModelItem) item;
				if(type.getText().toLowerCase().contains(filter.toLowerCase())) filtered.add(item);
			}
		}

		@Override
		public List<ListModelItem> getFilteredItems() {return filtered;}

		@Override
		public boolean hasIcons() {return false;}

		@Override
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private static class AggroListModelItem implements ListModelItem{

		private final NPCType type;
		
		public AggroListModelItem(NPCType type) {
			this.type = type;
		}
		
		@Override
		public String getText() {
			return type.toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof AggroListModelItem)) return false;
			return ((AggroListModelItem)obj).type.equals(type);
		}
		
	}
}
