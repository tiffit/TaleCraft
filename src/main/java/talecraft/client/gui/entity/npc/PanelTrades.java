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
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADTextField.TextChangeListener;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.entity.NPC.NPCData;
import talecraft.entity.NPC.NPCShop.NPCTrade;

public class PanelTrades extends NPCPanel{
	
	private List<NPCTrade> trades;
	private int index = 0;
	
	public PanelTrades(NPCData data, int width, int height){
		super(data, width, height);
		trades = new ArrayList<NPCTrade>(data.getShop().getTrades());
		addComponent(getPanel());
	}
	
	public QADPanel getPanel(){
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setBounds(0, 0, width, height);
		
		QADButton addtrade = new QADButton("Add Trade");
		addtrade.setBounds(width/4 + 1, 5, width/2, 20);
		addtrade.setAction(new Runnable() {
			@Override
			public void run() {
				trades.add(new NPCTrade(new ItemStack(Items.DIAMOND), new ItemStack(Items.APPLE)));
				resetPanel();
			}
		});
		panel.addComponent(addtrade);
		QADButton moveDown = new QADButton("<- Prev Trade");
		moveDown.setBounds(1, 5, width/4, 20);
		moveDown.setAction(new Runnable() {
			@Override
			public void run() {
				moveIndex(false);
			}
		});
		panel.addComponent(moveDown);
		if(index <= 0) moveDown.setEnabled(false);
		QADButton moveUp = new QADButton("Next Trade ->");
		moveUp.setBounds(width - width/4, 5, width/4 - 1, 20);
		moveUp.setAction(new Runnable() {
			@Override
			public void run() {
				moveIndex(true);
			}
		});
		if(index + 1 >= trades.size()) moveUp.setEnabled(false);
		panel.addComponent(moveUp);
		
		if(trades.size() > 0){
			panel.addComponent(new QADLabel("Trade #" + (index + 1) + "/" + trades.size(), 4, 30));
			
			QADDropdownBox itemSelectBuying = new QADDropdownBox(new ListItemListModel(trades.get(index).getBuying()), new ItemItem(trades.get(index).getBuying()));
			panel.addComponent(new QADLabel("Item (Buying)", 4, 50));
			itemSelectBuying.setBounds(4, 60, 200, 20);
			panel.addComponent(itemSelectBuying);
			
			panel.addComponent(new QADLabel("Amount", 207, 50));
			QADNumberTextField sizeFieldBuying = new QADNumberTextField(207, 60, 30, 20, trades.get(index).getBuying().getCount(), NumberType.INTEGER);
			sizeFieldBuying.textChangedListener = new SizeFieldChangeListener(trades.get(index).getBuying());
			sizeFieldBuying.setRange(0, trades.get(index).getBuying().getMaxStackSize());
			panel.addComponent(sizeFieldBuying);
			
			if(!trades.get(index).getBuying().hasTagCompound())trades.get(index).getBuying().setTagCompound(new NBTTagCompound());
			panel.addComponent(new QADLabel("NBT", 4, 120));
			QADTextField nbtFieldBuying = new QADTextField(trades.get(index).getBuying().getTagCompound().toString());
			nbtFieldBuying.textChangedListener = new NBTFieldChangeListener(trades.get(index).getBuying());
			nbtFieldBuying.setBounds(4, 130, 200, 20);
			panel.addComponent(nbtFieldBuying);
			
			QADDropdownBox itemSelectSelling = new QADDropdownBox(new ListItemListModel(trades.get(index).getSelling()), new ItemItem(trades.get(index).getSelling()));
			panel.addComponent(new QADLabel("Item (Selling)", 4, this.height/2));
			itemSelectSelling.setBounds(4, this.height/2 + 10, 200, 20);
			panel.addComponent(itemSelectSelling);
			
			panel.addComponent(new QADLabel("Amount", 207, this.height/2));
			QADNumberTextField sizeFieldSelling = new QADNumberTextField(207, this.height/2 + 10, 30, 20, trades.get(index).getSelling().getCount(), NumberType.INTEGER);
			sizeFieldSelling.textChangedListener = new SizeFieldChangeListener(trades.get(index).getSelling());
			sizeFieldSelling.setRange(0, trades.get(index).getSelling().getMaxStackSize());
			panel.addComponent(sizeFieldSelling);
			
			QADTextField nbtFieldSelling = new QADTextField(trades.get(index).getBuying().getTagCompound().toString());
			nbtFieldSelling.textChangedListener = new NBTFieldChangeListener(trades.get(index).getSelling());
			nbtFieldSelling.setBounds(4, this.height/2 + 70, 200, 20);
			panel.addComponent(nbtFieldSelling);
		}
		
		
		return panel;
	}
	
	private class NBTFieldChangeListener implements TextChangeListener{

		private ItemStack stack;
		
		public NBTFieldChangeListener(ItemStack stack) {
			this.stack = stack;
		}
		
		@Override
		public void call(QADTextField field, String text) {
			try{
				stack.setTagCompound(JsonToNBT.getTagFromJson(text));
				field.setTextColor(0xffffff);
			}catch (NBTException e){
				field.setTextColor(0xff0000);
				return;
			}
		}
	}
	
	private class SizeFieldChangeListener implements TextChangeListener{

		private ItemStack stack;
		
		public SizeFieldChangeListener(ItemStack stack) {
			this.stack = stack;
		}
		
		@Override
		public void call(QADTextField qadTextField, String text) {
			int amount = -1;
			try{
				amount = Integer.valueOf(text);
			}catch(NumberFormatException e){
				return;
			}
			stack.setCount(amount);
		}
		
	}
	
	private void moveIndex(boolean up){
		if(up){
			if(index == trades.size() - 1) return;
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
	
	class ListItemListModel implements ListModel {
		private List<ListModelItem> items;
		private List<ListModelItem> filtered;
		private ItemStack stack;
		
		public ListItemListModel(ItemStack is) {
			stack = is;
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
			NBTTagCompound oldStack = stack.serializeNBT();
			oldStack.setString("id", ii.stack.serializeNBT().getString("id"));
			stack = new ItemStack(oldStack);
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

	@Override
	public void save(NPCData data) {
		data.getShop().getTrades().clear();
		data.getShop().getTrades().addAll(trades);
	}
}
