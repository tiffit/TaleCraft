package talecraft.client.gui.npc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADItemButton;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADButton.ButtonModel;
import talecraft.client.gui.qad.layout.QADListLayout;
import talecraft.entity.NPC.NPCInventoryData;

public class NPCInventoryEditorGui extends QADGuiScreen {
	private NPCEditorGui npcGui;
	
	public NPCInventoryEditorGui(NPCEditorGui npcGui) {
		this.setBehind(npcGui);
		this.returnScreen = npcGui;
		this.npcGui = npcGui;
	}

	@Override
	public void buildGui() {
		for(int i = 0; i < 6; i++){
			final int index = i;
			QADButton equipmentbutton = QADFACTORY.createButton("Change Equipment", 3 + (i < 2 ? (120) : 0), 5 + (index >= 2 ? (150 - index*30) : index*30), 100).setModel(new ButtonModel(){
				@Override
				public void onClick() {
					displayGuiScreen(new ChooseInvItemGui(EntityEquipmentSlot.values()[index]));
				}

				@Override
				public String getText() {
					return "Change " + EntityEquipmentSlot.values()[index].getName();
				}

				@Override
				public ResourceLocation getIcon() {
					return null;
				}

				@Override
				public void setText(String newText) {
				}

				@Override
				public void setIcon(ResourceLocation newIcon) {
				}
			
			});
			addComponent(equipmentbutton);
		}
		QADButton tradesbutton = QADFACTORY.createButton("Change Trades", 123, 65, 100).setModel(new ButtonModel(){
			@Override
			public void onClick() {
				displayGuiScreen(new NPCShopEditorGui(npcGui));
			}

			@Override
			public String getText() {
				return "Change Trades";
			}

			@Override
			public ResourceLocation getIcon() {
				return null;
			}

			@Override
			public void setText(String newText) {
			}

			@Override
			public void setIcon(ResourceLocation newIcon) {
			}
		
		});
		addComponent(tradesbutton);
		QADButton dropsbutton = QADFACTORY.createButton("Change Drops", 123, 95, 100).setModel(new ButtonModel(){
			@Override
			public void onClick() {
				displayGuiScreen(new NPCDropsEditorGui(npcGui));
			}

			@Override
			public String getText() {
				return "Change Drops";
			}

			@Override
			public ResourceLocation getIcon() {
				return null;
			}

			@Override
			public void setText(String newText) {
			}

			@Override
			public void setIcon(ResourceLocation newIcon) {
			}
		
		});
		addComponent(dropsbutton);
	}
	
	@Override
	public void onGuiClosed() {}
	
	
	private class ChooseInvItemGui extends QADGuiScreen {
		private QADScrollPanel panel;
		private QADTextField searchField;
		private String lastSearch;
		private EntityEquipmentSlot slot;
		private final List<ItemStack> acceptableItems;
		
		public ChooseInvItemGui(EntityEquipmentSlot slot) {
			this.setBehind(npcGui);
			this.slot = slot;
			this.returnScreen = NPCInventoryEditorGui.this;
			acceptableItems = NPCInventoryData.getAcceptableItems(slot);
		}

		@Override
		public void buildGui() {
			rebuild(getForSearch(""));
			searchField = new QADTextField(this.width - 150, 20, 125, 20);
			searchField.setText("");
			lastSearch = "";
			addComponent(searchField.setTooltip("Search"));
		}
		
		@Override
		public void updateGui() {
			super.updateGui();
			if(!lastSearch.equals(searchField.getText())){
				lastSearch = searchField.getText();
				updateComponents();
			}
		}
		
		@Override
		public void onGuiClosed() {}

		@Override
		public void layoutGui() {
			panel.setSize(this.width, this.height);
		}
		
		private List<ItemStack> getForSearch(String search){
			List<ItemStack> searchedItems = new ArrayList<ItemStack>();
			for(ItemStack item : acceptableItems){
				List<ItemStack> subitems = new ArrayList<ItemStack>();
				item.getItem().getSubItems(item.getItem(), CreativeTabs.INVENTORY, subitems);
				for(final ItemStack stack : subitems){
					if(item.getItem().getItemStackDisplayName(stack).toLowerCase().contains(search.toLowerCase())){
						searchedItems.add(stack);
					}
				}
			}
			return searchedItems;
		}
		
		private void updateComponents(){
			String search = searchField.getText();
			rebuild(getForSearch(search));
			searchField = new QADTextField(this.width - 150, 20, 125, 20);
			searchField.setText(lastSearch);
			searchField.setFocused(true);
			addComponent(searchField.setTooltip("Search"));
			layoutGui();
			relayout();
		}
		
		private void rebuild(List<ItemStack> items){
			removeAllComponents();
			panel = new QADScrollPanel();
			panel.setPosition(0, 0);
			panel.setSize(200, 200);
			QADButton clear = new QADButton("Clear Slot");
			clear.simplified = true;
			clear.textAlignment = 0;
			clear.setAction( new Runnable() {
				@Override public void run() {
					npcGui.data.setItem(slot, null);
					displayGuiScreen(returnScreen);
				}
			});
			clear.setHeight(18);
			panel.addComponent(clear);
			for(final ItemStack stack : items){
				QADButton component = new QADItemButton(stack.getItem().getItemStackDisplayName(stack), stack);
				component.simplified = true;
				component.textAlignment = 0;
				component.setAction( new Runnable() {
					@Override public void run() {
						npcGui.data.setItem(slot, stack);
						displayGuiScreen(returnScreen);
					}
				});
				component.setHeight(0);
				panel.addComponent(component);
				}
			panel.setLayout(new QADListLayout(.5D, 18));
			addComponent(panel);
		}
	}
}
