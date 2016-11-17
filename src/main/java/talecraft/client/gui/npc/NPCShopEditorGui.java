package talecraft.client.gui.npc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADItemButton;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADButton.ButtonModel;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.layout.QADListLayout;
import talecraft.client.gui.qad.model.AbstractButtonModel;
import talecraft.client.gui.qad.model.DefaultTextFieldModel;
import talecraft.entity.NPC.NPCInventoryData;
import talecraft.entity.NPC.NPCShop.NPCTrade;

public class NPCShopEditorGui extends QADGuiScreen {
	private NPCEditorGui npcGui;
	private QADScrollPanel mainPanel;
	private final List<NPCTrade> trades;
	
	public NPCShopEditorGui(NPCEditorGui npcGui) {
		this.setBehind(npcGui);
		this.returnScreen = npcGui;
		this.npcGui = npcGui;
		trades = npcGui.shop.getTrades();
	}

	@Override
	public void buildGui() {
		mainPanel = new QADScrollPanel();
		addComponent(new QADButton(1, 1, this.width-52, new ButtonModel() {
			
			@Override
			public void onClick() {
				Item item = Item.getItemById(trades.size() + 1);
				if(item == null) item = Items.DIAMOND;
				trades.add(new NPCTrade(new ItemStack(item), new ItemStack(item)));
				displayGuiScreen(new NPCShopEditorGui(npcGui));
			}

			@Override
			public String getText() {
				return "Add Trade";
			}

			@Override
			public ResourceLocation getIcon() {return null;}

			@Override
			public void setText(String newText) {}

			@Override
			public void setIcon(ResourceLocation newIcon) {}
			
			
		}));
		addComponent(new QADButton(this.width-50, 1, 50, new ButtonModel() {

			@Override
			public void onClick() {
				displayGuiScreen(npcGui);
			}

			@Override
			public String getText() {
				return "Save";
			}

			@Override
			public ResourceLocation getIcon() {return null;}

			@Override
			public void setText(String newText) {}

			@Override
			public void setIcon(ResourceLocation newIcon) {}
			
			
		}));
		for(int i = 0; i < trades.size(); i++){
			final int index = i;
			final NPCTrade trade = trades.get(index);
			for(int j = 0; j < 2; j++){
				final int type = j;
				final ItemStack stack = type == 0 ? trade.getBuying() : trade.getSelling();
				if(stack == null) continue;
				final QADPanel panel = new QADPanel();
				panel.setBounds(0, (i*30)*3 + j*30, this.width, 90);
				panel.setBackgroundColor(1);
				if(j == 0) panel.addComponent(QADFACTORY.createLabel("Trade #" + (i + 1), 3, 3));
				panel.addComponent(new QADButton(32, 15, 150, new AbstractButtonModel(stack.getItem().getItemStackDisplayName(stack)) {
					@Override
					public void onClick() {
						displayGuiScreen(new ChooseTradeItemGui(index, type));
					}}
				));
				panel.addComponent(QADFACTORY.createLabel("x", 25, 24));
				panel.addComponent(QADFACTORY.createLabel("with meta", 184, 24));
				QADNumberTextField amountField = new QADNumberTextField(fontRendererObj, 3, 15, 20, 20, stack.stackSize, NumberType.INTEGER);
				amountField.setModel(new DefaultTextFieldModel() {
					boolean empty = false;
					@Override
					public void setText(String text) {
						if(text.equals("")){
							empty = true;
							return;
						}
						empty = false;
						int size = 0;
						try{
							size = Integer.valueOf(text);
						}catch(NumberFormatException e){
							return;
						}
						stack.stackSize = size;
					}
					
					@Override
					public int getTextLength() {
						return getText().length();
					}
					
					@Override
					public String getText() {
						return empty ? "" : "" + stack.stackSize;
					}
					
					@Override
					public char getCharAt(int i) {
						return getText().charAt(i);
					}
				});
				amountField.setMaxStringLength(2);
				amountField.setRange(0, 64);
				panel.addComponent(amountField);
				QADNumberTextField metaField = new QADNumberTextField(fontRendererObj, 230, 15, 40, 20, stack.getMetadata(), NumberType.INTEGER);
				metaField.setModel(new DefaultTextFieldModel() {
					boolean empty = false;
					@Override
					public void setText(String text) {
						if(text.equals("")){
							empty = true;
							return;
						}
						empty = false;
						int meta = 0;
						try{
							meta = Integer.valueOf(text);
						}catch(NumberFormatException e){
							return;
						}
						stack.setItemDamage(meta);
					}
					
					@Override
					public int getTextLength() {
						return getText().length();
					}
					
					@Override
					public String getText() {
						return empty ? "" : "" + stack.getMetadata();
					}
					
					@Override
					public char getCharAt(int i) {
						return getText().charAt(i);
					}
				});
				panel.addComponent(metaField);
				if(j == 0){
					panel.addComponent(QADFACTORY.createLabel("Stock: ", 3, 47));
					QADNumberTextField stockField = new QADNumberTextField(fontRendererObj, 45, 39, 40, 20, trade.getStock(), NumberType.INTEGER);
					stockField.setModel(new DefaultTextFieldModel() {
						boolean empty = false;
						@Override
						public void setText(String text) {
							if(text.equals("")){
								empty = true;
								return;
							}
							empty = false;
							int stock = 0;
							try{
								stock = Integer.valueOf(text);
							}catch(NumberFormatException e){
								return;
							}
							trade.setStock(stock);
						}
						
						@Override
						public int getTextLength() {
							return getText().length();
						}
						
						@Override
						public String getText() {
							return empty ? "" : "" + trade.getStock();
						}
						
						@Override
						public char getCharAt(int i) {
							return getText().charAt(i);
						}
					});
					panel.addComponent(stockField);
				}
				
				panel.addComponent(QADFACTORY.createLabel("NBT: ", 3, 75));
				QADTextField nbtField = new QADTextField(fontRendererObj, 45, 67, 350, 20);
				nbtField.setModel(new DefaultTextFieldModel() {
					String text = stack.hasTagCompound() ? stack.getTagCompound().toString() : "{}";
					@Override
					public void setText(String text) {
						this.text = text;
						try {
							NBTTagCompound tag = JsonToNBT.getTagFromJson(text);
							stack.setTagCompound(text.equals("{}") ? null : tag);
							System.out.println(stack.getTagCompound());
							setTextColor(0xffffff);
						} catch (NBTException e) {
							setTextColor(0xff0000);
						}
					}
					
					@Override
					public int getTextLength() {
						return getText().length();
					}
					
					@Override
					public String getText() {
						return text;
					}
					
					@Override
					public char getCharAt(int i) {
						return getText().charAt(i);
					}
				});
				panel.addComponent(nbtField);
				if(j == 0){
					QADButton button = new QADButton(QADButton.ICON_DELETE);
					button.setPosition(panel.getWidth()-20-2, 0);
					button.setAction(new Runnable(){
						@Override
						public void run() {
							trades.remove(index);
							displayGuiScreen(new NPCShopEditorGui(npcGui));
						}
					});
					panel.addComponent(button);
				}
				mainPanel.addComponent(panel);
			}
		}
		mainPanel.setLayout(new QADListLayout());
		addComponent(mainPanel);
	}
	
	@Override
	public void onGuiClosed() {}
	
	@Override
	public void layoutGui() {
		mainPanel.setY(22);
		mainPanel.setSize(this.width, this.height - 20);
	}
	
	private class ChooseTradeItemGui extends QADGuiScreen {
		private QADScrollPanel panel;
		private QADTextField searchField;
		private String lastSearch;
		private final int index;
		private final int type;
		private final List<ItemStack> acceptableItems;
		
		public ChooseTradeItemGui(int index, int type) {
			this.setBehind(npcGui);
			this.index = index;
			this.type = type;
			acceptableItems = NPCInventoryData.getAcceptableItems(EntityEquipmentSlot.MAINHAND);
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
				if(item.getItem().getItemStackDisplayName(item).toLowerCase().contains(search.toLowerCase())){
					searchedItems.add(item);
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
			for(final ItemStack stack : items){
				QADButton component = new QADItemButton(stack.getItem().getItemStackDisplayName(stack), stack);
				component.simplified = true;
				component.textAlignment = 0;
				component.setAction( new Runnable() {
					@Override public void run() {
						stack.setItem(stack.getItem());
						NPCTrade trade = trades.get(index);
						ItemStack buy1 = trade.getBuying();
						ItemStack sell = trade.getSelling();
						switch(type){
						case 0: buy1 = stack;
								break;
						case 1: sell = stack;
								break;
						}
						trades.remove(index);
						NPCTrade npctrade = new NPCTrade(buy1, sell);
						if(trades.size() < index) trades.add(index, npctrade);
						else trades.add(npctrade);
						displayGuiScreen(new NPCShopEditorGui(npcGui));
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
