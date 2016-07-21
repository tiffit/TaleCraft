package tiffit.talecraft.client.gui.npc;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADNumberTextField;
import de.longor.talecraft.client.gui.qad.QADNumberTextField.NumberType;
import de.longor.talecraft.client.gui.qad.QADPanel;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADSlider;
import de.longor.talecraft.client.gui.qad.QADSlider.SliderModel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.layout.QADListLayout;
import de.longor.talecraft.client.gui.qad.model.AbstractButtonModel;
import de.longor.talecraft.client.gui.qad.model.DefaultTextFieldModel;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;
import scala.util.parsing.json.JSON;
import tiffit.talecraft.client.gui.qad.QADItemButton;
import tiffit.talecraft.entity.NPC.NPCInventoryData;
import tiffit.talecraft.entity.NPC.NPCInventoryData.NPCDrop;

public class NPCDropsEditorGui extends QADGuiScreen {
	private NPCEditorGui npcGui;
	private QADScrollPanel mainPanel;
	
	public NPCDropsEditorGui(NPCEditorGui npcGui) {
		this.setBehind(npcGui);
		this.returnScreen = npcGui;
		this.npcGui = npcGui;
	}

	@Override
	public void buildGui() {
		mainPanel = new QADScrollPanel();
		addComponent(new QADButton(1, 1, this.width-52, new ButtonModel() {

			@Override
			public void onClick() {
				Item item = Item.getItemById(npcGui.drops.size() + 1);
				if(item == null) item = Items.DIAMOND;
				npcGui.drops.add(new NPCDrop(new ItemStack(item), 1));
				displayGuiScreen(new NPCDropsEditorGui(npcGui));
			}

			@Override
			public String getText() {
				return "Add Drop";
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
		for(int i = 0; i < npcGui.drops.size(); i++){
			final int index = i;
			final QADPanel panel = new QADPanel();
			panel.setBounds(0, i*30, this.width, 90);
			panel.setBackgroundColor(1);
			panel.addComponent(QADFACTORY.createLabel("Drop #" + (i + 1), 3, 3));
			panel.addComponent(new QADButton(32, 15, 150, new AbstractButtonModel(npcGui.drops.get(index).stack.getItem().getItemStackDisplayName(npcGui.drops.get(index).stack)) {
				@Override
				public void onClick() {
					displayGuiScreen(new ChooseDropItemGui(index));
				}}
			));
			panel.addComponent(QADFACTORY.createLabel("x", 25, 24));
			panel.addComponent(QADFACTORY.createLabel("with meta", 184, 24));
			QADNumberTextField amountField = new QADNumberTextField(fontRendererObj, 3, 15, 20, 20, npcGui.drops.get(index).stack.stackSize, NumberType.INTEGER);
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
					npcGui.drops.get(index).stack.stackSize = size;
				}
				
				@Override
				public int getTextLength() {
					return getText().length();
				}
				
				@Override
				public String getText() {
					return empty ? "" : "" + npcGui.drops.get(index).stack.stackSize;
				}
				
				@Override
				public char getCharAt(int i) {
					return getText().charAt(i);
				}
			});
			amountField.setMaxStringLength(2);
			amountField.setRange(0, 64);
			panel.addComponent(amountField);
			QADNumberTextField metaField = new QADNumberTextField(fontRendererObj, 230, 15, 40, 20, npcGui.drops.get(index).stack.getMetadata(), NumberType.INTEGER);
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
					npcGui.drops.get(index).stack.setItemDamage(meta);
				}
				
				@Override
				public int getTextLength() {
					return getText().length();
				}
				
				@Override
				public String getText() {
					return empty ? "" : "" + npcGui.drops.get(index).stack.getMetadata();
				}
				
				@Override
				public char getCharAt(int i) {
					return getText().charAt(i);
				}
			});
			panel.addComponent(metaField);
			panel.addComponent(QADFACTORY.createLabel("Chance: ", 3, 59 - fontRendererObj.FONT_HEIGHT));
			QADSlider chanceSlider = new QADSlider(new SliderModel<Float>(){
				@Override
				public void setValue(Float value) {
					npcGui.drops.get(index).chance = getFormatted(value);
				}

				@Override
				public Float getValue() {
					return npcGui.drops.get(index).chance;
				}

				@Override
				public String getValueAsText() {
					float chance = npcGui.drops.get(index).chance;
					chance *= 100;
					return "" + (int) chance + "%";
				}

				@Override
				public void setSliderValue(float sliderValue) {
					npcGui.drops.get(index).chance = getFormatted(sliderValue);
				}
				
				private float getFormatted(float flt){
					String formatted = ItemStack.DECIMALFORMAT.format(flt);
					return Float.parseFloat(formatted);
				}

				@Override
				public float getSliderValue() {
					return npcGui.drops.get(index).chance;
				}
				
			});
			chanceSlider.setSliderValue(npcGui.drops.get(index).chance);
			chanceSlider.setBounds(45, 43, 100, 20);
			panel.addComponent(chanceSlider);
			panel.addComponent(QADFACTORY.createLabel("NBT: ", 3, 75));
			QADTextField nbtField = new QADTextField(fontRendererObj, 45, 67, 350, 20);
			if(!npcGui.drops.get(index).stack.hasTagCompound()) npcGui.drops.get(index).stack.setTagCompound(new NBTTagCompound());
			nbtField.setModel(new DefaultTextFieldModel() {
				String text = npcGui.drops.get(index).stack.getTagCompound().toString();;
				@Override
				public void setText(String text) {
					this.text = text;
					try {
						npcGui.drops.get(index).stack.setTagCompound(JsonToNBT.getTagFromJson(text));
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
			QADButton button = new QADButton(QADButton.ICON_DELETE);
			button.setPosition(panel.getWidth()-20-2, 0);
			button.setAction(new Runnable(){
				@Override
				public void run() {
					npcGui.drops.remove(index);
					displayGuiScreen(new NPCDropsEditorGui(npcGui));
				}
			});
			panel.addComponent(button);
			mainPanel.addComponent(panel);
		}
		mainPanel.setLayout(new QADListLayout());
		addComponent(mainPanel);
	}
	
	public void onGuiClosed() {}
	
	@Override
	public void layoutGui() {
		mainPanel.setY(22);
		mainPanel.setSize(this.width, this.height - 20);
	}
	
	private class ChooseDropItemGui extends QADGuiScreen {
		private QADScrollPanel panel;
		private QADTextField searchField;
		private String lastSearch;
		private final int index;
		private final List<ItemStack> acceptableItems;
		
		public ChooseDropItemGui(int index) {
			this.setBehind(npcGui);
			this.index = index;
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
						npcGui.drops.get(index).stack.setItem(stack.getItem());
						displayGuiScreen(new NPCDropsEditorGui(npcGui));
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
