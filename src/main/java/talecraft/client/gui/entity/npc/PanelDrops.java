package talecraft.client.gui.entity.npc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADDropdownBox;
import talecraft.client.gui.qad.QADDropdownBox.ListModel;
import talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.qad.QADSlider;
import talecraft.client.gui.qad.QADSlider.SliderModel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADTextField.TextChangeListener;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.entity.NPC.NPCData;
import talecraft.entity.NPC.NPCInventoryData.NPCDrop;

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
		if(index + 1 >= drops.size()) moveUp.setEnabled(false);
		panel.addComponent(moveUp);
		
		
		if(drops.size() > 0){
			panel.addComponent(new QADLabel("Drop #" + (index + 1) + "/" + drops.size(), 4, 30));
			
			QADDropdownBox itemSelect = new QADDropdownBox(new ListItemListModel(), new ItemItem(drops.get(index).stack));
			panel.addComponent(new QADLabel("Item", 4, 50));
			itemSelect.setBounds(4, 60, 200, 20);
			panel.addComponent(itemSelect);
			
			QADSlider chanceSlider = new QADSlider(new ChanceSliderModel());
			chanceSlider.setBounds(4, 90, 200, 20);
			panel.addComponent(chanceSlider);
			
			panel.addComponent(new QADLabel("Amount", 207, 50));
			QADNumberTextField sizeField = new QADNumberTextField(207, 60, 30, 20, drops.get(index).stack.getCount(), NumberType.INTEGER);
			sizeField.textChangedListener = new SizeFieldChangeListener();
			sizeField.setRange(0, drops.get(index).stack.getMaxStackSize());
			panel.addComponent(sizeField);
			
			if(!drops.get(index).stack.hasTagCompound())drops.get(index).stack.setTagCompound(new NBTTagCompound());
			panel.addComponent(new QADLabel("NBT", 4, 120));
			QADTextField nbtField = new QADTextField(drops.get(index).stack.getTagCompound().toString());
			nbtField.textChangedListener = new NBTFieldChangeListener();
			nbtField.setBounds(4, 130, 200, 20);
			panel.addComponent(nbtField);
		}
		
		
		return panel;
	}
	
	private class NBTFieldChangeListener implements TextChangeListener{

		@Override
		public void call(QADTextField field, String text) {
			try{
				drops.get(index).stack.setTagCompound(JsonToNBT.getTagFromJson(text));
				field.setTextColor(0xffffff);
			}catch (NBTException e){
				field.setTextColor(0xff0000);
				return;
			}
		}
	}
	
	private class SizeFieldChangeListener implements TextChangeListener{

		@Override
		public void call(QADTextField qadTextField, String text) {
			int amount = -1;
			try{
				amount = Integer.valueOf(text);
			}catch(NumberFormatException e){
				return;
			}
			drops.get(index).stack.setCount(amount);
		}
		
	}
	
	private class ChanceSliderModel implements SliderModel<Float>{

		@Override
		public void setValue(Float value) {
			drops.get(index).chance = value;
		}

		@Override
		public Float getValue() {
			return drops.get(index).chance;
		}

		@Override
		public String getValueAsText() {
			return "Chance: " + ItemStack.DECIMALFORMAT.format(drops.get(index).chance);
		}

		@Override
		public void setSliderValue(float sliderValue) {
			drops.get(index).chance = sliderValue;
		}

		@Override
		public float getSliderValue() {
			return drops.get(index).chance;
		}
		
	}
	
	private void moveIndex(boolean up){
		if(up){
			if(index == drops.size() - 1) return;
			index++;
			resetPanel();
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
		data.setDrops(drops);
	}
	
	class ListItemListModel implements ListModel {
		private List<ListModelItem> items;
		private List<ListModelItem> filtered;
		
		public ListItemListModel() {
			items = new ArrayList<ListModelItem>();
			filtered = new ArrayList<ListModelItem>();
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			for(ResourceLocation rl : Item.REGISTRY.getKeys()){
				Item item = Item.REGISTRY.getObject(rl);
				stacks.add(new ItemStack(item));
			}
			for(ItemStack item : stacks){
				Item itm = item.getItem();
				if(itm == null) continue;
				NonNullList<ItemStack> subitems = NonNullList.create();
				itm.getSubItems(CreativeTabs.INVENTORY, subitems);
				for(final ItemStack stack : subitems){
					items.add(new ItemItem(stack));
				}
			}
		}
		@Override
		public boolean hasItems() {
			return getItemCount() > 0;
		}

		@Override
		public int getItemCount() {
			return items.size();
		}

		@Override
		public List<ListModelItem> getItems() {
			return items;
		}

		@Override
		public void applyFilter(String filterString) {
			filtered.clear();
			for(ListModelItem lmi : items){
				ItemItem ii = (ItemItem) lmi;
				if(ii.stack != null && (ii.stack.getItem().getItemStackDisplayName(ii.stack).toLowerCase().contains(filterString.toLowerCase()))){
					filtered.add(lmi);
				}
			}
		}

		@Override
		public List<ListModelItem> getFilteredItems() {
			return filtered;
		}

		@Override
		public boolean hasIcons() {
			return true;
		}

		@Override
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {
			if(((ItemItem)item).stack != null)renderer.drawItemStack(((ItemItem)item).stack, 2, 2);
		}
		
		@Override
		public void onSelection(ListModelItem selected) {
			ItemItem ii = (ItemItem) selected;
			NBTTagCompound oldStack = drops.get(index).stack.serializeNBT();
			oldStack.setString("id", ii.stack.serializeNBT().getString("id"));
			drops.get(index).stack = new ItemStack(oldStack);
		}
	}
		
	class ItemItem implements ListModelItem{

		private ItemStack stack;
			
		public ItemItem(ItemStack stack){
			this.stack = stack;
		}
			
		@Override
		public String getText() {
			return stack.getItem().getItemStackDisplayName(stack);
		}
			
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ItemItem))return false;
			return ItemStack.areItemStacksEqual(stack, ((ItemItem)obj).stack);
		}

		@Override
		public int hashCode() {
			return stack.hashCode();
		}
			
	}
}
