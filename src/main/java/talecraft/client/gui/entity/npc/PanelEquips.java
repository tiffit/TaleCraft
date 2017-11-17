package talecraft.client.gui.entity.npc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import talecraft.client.gui.qad.QADDropdownBox;
import talecraft.client.gui.qad.QADDropdownBox.ListModel;
import talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.entity.NPC.NPCData;

public class PanelEquips extends NPCPanel{
	
	private QADDropdownBox HELMET;
	private QADDropdownBox CHESTPLATE;
	private QADDropdownBox LEGGINGS;
	private QADDropdownBox BOOTS;
	private QADDropdownBox RIGHT_HAND;
	private QADDropdownBox LEFT_HAND;
	
	public PanelEquips(NPCData data, int width, int height){
		super(data, width, height);
		HELMET = new QADDropdownBox(new ListItemListModel(EntityEquipmentSlot.HEAD), new ItemItem(data.getInvStack(EntityEquipmentSlot.HEAD)));
		CHESTPLATE = new QADDropdownBox(new ListItemListModel(EntityEquipmentSlot.CHEST), new ItemItem(data.getInvStack(EntityEquipmentSlot.CHEST)));
		LEGGINGS = new QADDropdownBox(new ListItemListModel(EntityEquipmentSlot.LEGS), new ItemItem(data.getInvStack(EntityEquipmentSlot.LEGS)));
		BOOTS = new QADDropdownBox(new ListItemListModel(EntityEquipmentSlot.FEET), new ItemItem(data.getInvStack(EntityEquipmentSlot.FEET)));
		RIGHT_HAND = new QADDropdownBox(new ListItemListModel(null), new ItemItem(data.getInvStack(EntityEquipmentSlot.MAINHAND)));
		LEFT_HAND = new QADDropdownBox(new ListItemListModel(null), new ItemItem(data.getInvStack(EntityEquipmentSlot.OFFHAND)));
		addComponent(new QADLabel("Helmet", 6, 0));
		HELMET.setBounds(5, 10, 150, 22);
		addComponent(new QADLabel("Chestplate", 6, 40));
		CHESTPLATE.setBounds(5, 50, 150, 22);
		addComponent(new QADLabel("Legging", 6, 80));
		LEGGINGS.setBounds(5, 40*2 + 10, 150, 22);
		addComponent(new QADLabel("Boots", 6, 40*3));
		BOOTS.setBounds(5, 40*3 + 10, 150, 22);
		addComponent(new QADLabel("Main Hand", 201, 0));
		RIGHT_HAND.setBounds(200, 10, 150, 22);
		addComponent(new QADLabel("Off Hand", 201, 40));
		LEFT_HAND.setBounds(200, 50, 150, 22);
		addComponent(HELMET);
		addComponent(CHESTPLATE);
		addComponent(LEGGINGS);
		addComponent(BOOTS);
		addComponent(RIGHT_HAND);
		addComponent(LEFT_HAND);
	}

	@Override
	public void save(NPCData data) {
		data.setItem(EntityEquipmentSlot.HEAD, ((ItemItem)HELMET.getSelected()).stack);
		data.setItem(EntityEquipmentSlot.CHEST, ((ItemItem)CHESTPLATE.getSelected()).stack);
		data.setItem(EntityEquipmentSlot.LEGS, ((ItemItem)LEGGINGS.getSelected()).stack);
		data.setItem(EntityEquipmentSlot.FEET, ((ItemItem)BOOTS.getSelected()).stack);
		data.setItem(EntityEquipmentSlot.MAINHAND, ((ItemItem)RIGHT_HAND.getSelected()).stack);
		data.setItem(EntityEquipmentSlot.OFFHAND, ((ItemItem)LEFT_HAND.getSelected()).stack);
	}
	
	class ListItemListModel implements ListModel {
		private List<ListModelItem> items;
		private List<ListModelItem> filtered;
		// private EntityEquipmentSlot slot;
		
		public ListItemListModel(EntityEquipmentSlot slot) {
			// this.slot = slot;
			items = new ArrayList<ListModelItem>();
			filtered = new ArrayList<ListModelItem>();
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			for(ResourceLocation rl : Item.REGISTRY.getKeys()){
				Item item = Item.REGISTRY.getObject(rl);
				stacks.add(new ItemStack(item));
			}
			items.add(new ItemItem(null));
			for(ItemStack item : stacks){
				Item itm = item.getItem();
				if(itm == null) continue;
				NonNullList<ItemStack> subitems = NonNullList.create();
				itm.getSubItems(CreativeTabs.INVENTORY, subitems);
				for(final ItemStack stack : subitems){
					if(slot != null){
						if(!(stack.getItem() instanceof ItemArmor)) continue;
						ItemArmor armor = (ItemArmor) stack.getItem();
						if(armor.armorType != slot) continue;
					}
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
			filtered.add(new ItemItem(null));
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
		}
	}
		
	class ItemItem implements ListModelItem{

		private ItemStack stack;
			
		public ItemItem(ItemStack stack){
			this.stack = stack;
		}
			
		@Override
		public String getText() {
			return stack == null ? "None" : stack.getItem().getItemStackDisplayName(stack);
		}
			
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ItemItem))return false;
			return ItemStack.areItemStacksEqual(stack == null ? new ItemStack((Item)null) : stack, ((ItemItem)obj).stack == null ? new ItemStack((Item)null) : ((ItemItem)obj).stack);
		}

		@Override
		public int hashCode() {
			return stack.hashCode();
		}
			
	}
}
