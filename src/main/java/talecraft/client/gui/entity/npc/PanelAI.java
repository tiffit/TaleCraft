package talecraft.client.gui.entity.npc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.text.TextFormatting;
import talecraft.client.gui.qad.QADDropdownBox;
import talecraft.client.gui.qad.QADDropdownBox.ListModel;
import talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.QADTickBox;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.entity.NPC.EntityNPC.NPCType;
import talecraft.entity.NPC.NPCData;

public class PanelAI extends NPCPanel{
	
	private static final TextFormatting[] NAME_COLORS = new TextFormatting[]{
			TextFormatting.BLACK, TextFormatting.DARK_BLUE, TextFormatting.DARK_GREEN,
			TextFormatting.DARK_AQUA, TextFormatting.DARK_RED, TextFormatting.DARK_PURPLE,
			TextFormatting.GOLD, TextFormatting.GRAY, TextFormatting.DARK_GRAY,
			TextFormatting.BLUE, TextFormatting.GREEN, TextFormatting.AQUA,
			TextFormatting.RED, TextFormatting.LIGHT_PURPLE, TextFormatting.YELLOW, TextFormatting.WHITE
	};
	
	private NPCType TYPE;
	private TextFormatting COLOR;
	private QADNumberTextField DAMAGE_FIELD;
	private QADNumberTextField SPEED_FIELD;
	private QADNumberTextField YAW_FIELD;
	private QADNumberTextField PITCH_FIELD;
	private QADTickBox LOOK_AT_PLAYER;
	private QADDropdownBox COLOR_SELECTOR;
	
	public PanelAI(NPCData data, int width, int height){
		super(data, width, height);
		addComponent(new QADLabel("Aggression", 4, 0));
		QADDropdownBox type_selector = new QADDropdownBox(new AggroListModel(), new AggroListModelItem(data.getType()));
		type_selector.setBounds(2, 10, width/5, 20);
		addComponent(type_selector);
		addComponent(new QADLabel("Name Color", 7 + width/5, 0));
		COLOR_SELECTOR = new QADDropdownBox(new ColorListModel(), new ColorListModelItem(data.getNameColor()));
		COLOR_SELECTOR.setBounds(7 + width/5, 10, width/5, 20);
		addComponent(COLOR_SELECTOR);
		addComponent(new QADLabel("Damage", 4, 35));
		DAMAGE_FIELD = new QADNumberTextField(2, 45, width/5, 20, data.getDamage(), NumberType.DECIMAL);
		addComponent(DAMAGE_FIELD);
		addComponent(new QADLabel("Speed", 7 + width/5, 35));
		SPEED_FIELD = new QADNumberTextField(5 + width/5, 45, width/5, 20, data.getSpeed(), NumberType.DECIMAL);
		addComponent(SPEED_FIELD);
		
		addComponent(new QADLabel("Yaw", 4, 70));
		YAW_FIELD = new QADNumberTextField(2, 80, 50, 20, data.getYaw(), NumberType.DECIMAL);
		YAW_FIELD.setRange(0D, 360D);
		addComponent(YAW_FIELD);
		addComponent(new QADLabel("Pitch", 57, 70));
		PITCH_FIELD = new QADNumberTextField(55, 80, 50, 20, data.getPitch(), NumberType.DECIMAL);
		PITCH_FIELD.setRange(-90D, 90D);
		addComponent(PITCH_FIELD);
		
		LOOK_AT_PLAYER = new QADTickBox(110, 80, 20, 20);
		LOOK_AT_PLAYER.getModel().setState(data.doEyesFollow());
		LOOK_AT_PLAYER.setTooltip("Should the NPC look at the player?");
		addComponent(LOOK_AT_PLAYER);
	}

	@Override
	public void save(NPCData data) {
		data.setType(TYPE);
		data.setDamage(DAMAGE_FIELD.getValue().floatValue());
		data.setSpeed(SPEED_FIELD.getValue().doubleValue());
		data.setYaw(YAW_FIELD.getValue().floatValue());
		data.setPitch(PITCH_FIELD.getValue().floatValue());
		data.setEyesFollow(LOOK_AT_PLAYER.getState());
		data.setNameColor(COLOR);
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
			COLOR = TYPE.color;
			if(COLOR_SELECTOR != null)COLOR_SELECTOR.setSelected(new ColorListModelItem(COLOR));
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
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {
		}
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

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return super.hashCode();
		}
		
	}
	
	private class ColorListModel implements ListModel{

		private List<ListModelItem> items = new ArrayList<ListModelItem>();
		private List<ListModelItem> filtered = new ArrayList<ListModelItem>();
		
		public ColorListModel() {
			for(TextFormatting color : NAME_COLORS){
				items.add(new ColorListModelItem(color));
			}
			filtered.addAll(items);
		}
		
		
		@Override
		public void onSelection(ListModelItem item) {
			COLOR = ((ColorListModelItem) item).color;
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
				ColorListModelItem color = (ColorListModelItem) item;
				if(color.getText().toLowerCase().contains(filter.toLowerCase())) filtered.add(item);
			}
		}

		@Override
		public List<ListModelItem> getFilteredItems() {return filtered;}

		@Override
		public boolean hasIcons() {return false;}

		@Override
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private static class ColorListModelItem implements ListModelItem{

		private final TextFormatting color;
		
		public ColorListModelItem(TextFormatting color) {
			this.color = color;
		}
		
		@Override
		public String getText() {
			return StringUtils.capitalize(color.getFriendlyName().replace("_", " "));
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ColorListModelItem)) return false;
			return ((ColorListModelItem)obj).color.equals(color);
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return super.hashCode();
		}
		
	}
}
