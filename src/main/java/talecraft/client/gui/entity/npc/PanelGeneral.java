package talecraft.client.gui.entity.npc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import talecraft.client.gui.qad.QADDropdownBox;
import talecraft.client.gui.qad.QADDropdownBox.ListModel;
import talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADTickBox;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.entity.NPC.EnumNPCModel;
import talecraft.entity.NPC.EnumNPCSkin;
import talecraft.entity.NPC.NPCData;

public class PanelGeneral extends NPCPanel{

	private QADTextField NAME_FIELD;
	private QADTickBox NAME_SHOWN;
	private QADTextField MSG_FIELD;
	private QADTickBox INCLUDE_NAME;
	private EnumNPCModel MODEL;
	private QADDropdownBox SKIN_SELECTOR;
	private EnumNPCSkin SKIN;
	private QADTextField CUSTOMSKIN;
	private QADNumberTextField HEALTH_FIELD;
	private QADTickBox INVERLNERABLE;
	private QADTickBox MOVABLE;
	private QADTickBox BOSS;

	
	public PanelGeneral(NPCData data, int width, int height){
		super(data, width, height);
		
		addComponent(new QADLabel("Name", 4, 0));
		NAME_FIELD = new QADTextField(2, 10, 75, 20);
		NAME_FIELD.setText(data.getName());
		addComponent(NAME_FIELD);
		NAME_SHOWN = new QADTickBox(105 + width - 105 - 65, 10, 20, 20);
		NAME_SHOWN.getModel().setState(data.shouldShowName());
		NAME_SHOWN.setTooltip("Should the name be shown?");
		addComponent(NAME_SHOWN);
		
		addComponent(new QADLabel("Message", 82, 0));
		MSG_FIELD = new QADTextField(80, 10, width - 105 - 45, 20);
		MSG_FIELD.setMaxStringLength(1024);
		MSG_FIELD.setText(data.getMessage());
		addComponent(MSG_FIELD);
		INCLUDE_NAME = new QADTickBox(105 + width - 105 - 40, 10, 20, 20);
		INCLUDE_NAME.getModel().setState(data.shouldIncludeNameInMessage());
		INCLUDE_NAME.setTooltip("Should the name be included in the message?");
		addComponent(INCLUDE_NAME);
		
		addComponent(new QADLabel("Model", 152, 35));
		QADDropdownBox model_selector = new QADDropdownBox(new ModelListModel(), new ModelListModelItem(data.getModel()));
		model_selector.setBounds(150, 45, width/5, 20);
		addComponent(model_selector);
		
		addComponent(new QADLabel("Skin", 157 + width/5, 35));
		SKIN_SELECTOR = new QADDropdownBox(new SkinListModel(), new SkinListModelItem(data.getSkin()));
		SKIN_SELECTOR.setBounds(155 + width/5, 45, width - (155 + width/5) - 5, 20);
		addComponent(SKIN_SELECTOR);
		CUSTOMSKIN = new QADTextField(155 + width/5, 70, width - (155 + width/5) - 5, 20);
		EnumNPCSkin slec_skin = ((SkinListModelItem)SKIN_SELECTOR.getSelected()).skin;
		if(slec_skin == EnumNPCSkin.Custom)CUSTOMSKIN.setText(data.getCustomSkin());
		else{
			CUSTOMSKIN.setText(formatSkinLocation(slec_skin.getResourceLocation()));
			CUSTOMSKIN.setEnabled(false);
		}
		addComponent(CUSTOMSKIN);
		
		addComponent(new QADLabel("Max Health", 4, 70));
		HEALTH_FIELD = new QADNumberTextField(2, 80, 100, 20, data.getHealth(), NumberType.DECIMAL);
		HEALTH_FIELD.setRange(0.1D, 1000000D);
		addComponent(HEALTH_FIELD);
		
		INVERLNERABLE = new QADTickBox(105, 80, 20, 20);
		INVERLNERABLE.getModel().setState(data.isInvulnerable());
		INVERLNERABLE.setTooltip("Is the NPC Invurnerable?");
		addComponent(INVERLNERABLE);
		MOVABLE = new QADTickBox(130, 80, 20, 20);
		MOVABLE.getModel().setState(data.isMovable());
		MOVABLE.setTooltip("Can the NPC be pushed?");
		addComponent(MOVABLE);
		BOSS = new QADTickBox(130 + 25, 80, 20, 20);
		BOSS.getModel().setState(data.isBoss());
		BOSS.setTooltip("Is the NPC a boss?");
		addComponent(BOSS);
	}

	@Override
	public void save(NPCData data) {
		data.setName(NAME_FIELD.getText());
		data.setMessage(MSG_FIELD.getText());
		data.setShowName(NAME_SHOWN.getState());
		data.setNameInMessage(INCLUDE_NAME.getState());
		data.setModel(MODEL);
		data.setSkin(SKIN);
		data.setHealth(HEALTH_FIELD.getValue().floatValue());
		data.setInvulnerable(INVERLNERABLE.getState());
		data.setMovable(MOVABLE.getState());
		data.setBoss(BOSS.getState());
		data.setCustomSkin(CUSTOMSKIN.getText());
	}
	
	private String formatSkinLocation(ResourceLocation loc){
		String locStr = loc.toString();
		locStr = locStr.replace("minecraft:", "mc:");
		locStr = locStr.replace("talecraft:", "");
		locStr = locStr.replace("textures/entity/", "");
		return locStr;
	}
	
	private class ModelListModel implements ListModel{

		private List<ListModelItem> items = new ArrayList<ListModelItem>();
		private List<ListModelItem> filtered = new ArrayList<ListModelItem>();
		
		public ModelListModel() {
			for(EnumNPCModel model : EnumNPCModel.values()){
				items.add(new ModelListModelItem(model));
			}
			filtered.addAll(items);
		}
		
		
		@Override
		public void onSelection(ListModelItem item) {
			MODEL = ((ModelListModelItem) item).model;
			SKIN = MODEL.getDefaultSkin();
			if(SKIN_SELECTOR != null) SKIN_SELECTOR.setSelected(new SkinListModelItem(SKIN));
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
				ModelListModelItem model = (ModelListModelItem) item;
				if(model.getText().toLowerCase().contains(filter.toLowerCase())) filtered.add(item);
			}
		}

		@Override
		public List<ListModelItem> getFilteredItems() {return filtered;}

		@Override
		public boolean hasIcons() {return false;}

		@Override
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private static class ModelListModelItem implements ListModelItem{

		private final EnumNPCModel model;
		
		public ModelListModelItem(EnumNPCModel model) {
			this.model = model;
		}
		
		@Override
		public String getText() {
			return model.toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ModelListModelItem)) return false;
			return ((ModelListModelItem)obj).model.equals(model);
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
		
	}
	
	private class SkinListModel implements ListModel{

		private List<ListModelItem> items = new ArrayList<ListModelItem>();
		private List<ListModelItem> filtered = new ArrayList<ListModelItem>();
		
		public SkinListModel() {
			for(EnumNPCSkin skin : EnumNPCSkin.values()){
				items.add(new SkinListModelItem(skin));
			}
			filtered.addAll(items);
		}
		
		
		@Override
		public void onSelection(ListModelItem item) {
			SKIN = ((SkinListModelItem) item).skin;
			if(CUSTOMSKIN != null){
				if(SKIN == EnumNPCSkin.Custom){
					CUSTOMSKIN.setText("");
					CUSTOMSKIN.setEnabled(true);
				}else{
					CUSTOMSKIN.setEnabled(false);
					if(SKIN == EnumNPCSkin.Invisible) CUSTOMSKIN.setText("null");
					else CUSTOMSKIN.setText(formatSkinLocation(SKIN.getResourceLocation()));
				}
				
			}
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
			List<ListModelItem> blanks = new ArrayList<ListModelItem>();
			for(ListModelItem item : items){
				SkinListModelItem skin = (SkinListModelItem) item;
				if(skin.getText().toLowerCase().contains(filter.toLowerCase())){
					EnumNPCSkin skn = skin.skin;
					if(skn.getModelType() == MODEL) filtered.add(item);
					else if(skn.getModelType() == null) blanks.add(item);
				}
			}
			filtered.addAll(blanks);
		}

		@Override
		public List<ListModelItem> getFilteredItems() {return filtered;}

		@Override
		public boolean hasIcons() {return false;}

		@Override
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private static class SkinListModelItem implements ListModelItem{

		private final EnumNPCSkin skin;
		
		public SkinListModelItem(EnumNPCSkin skin) {
			this.skin = skin;
		}
		
		@Override
		public String getText(){
			return skin.toString() + (skin.hasAuthor() ? (" (" + skin.getAuthor() + ")") : "");
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof SkinListModelItem)) return false;
			return ((SkinListModelItem)obj).skin.equals(skin);
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return super.hashCode();
		}
		
	}
	
}
