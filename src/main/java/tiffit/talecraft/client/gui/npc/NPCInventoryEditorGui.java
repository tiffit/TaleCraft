package tiffit.talecraft.client.gui.npc;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;
import de.longor.talecraft.client.gui.qad.layout.QADListLayout;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import tiffit.talecraft.entity.NPC.QADSkinButton;
import tiffit.talecraft.entity.NPC.NPCInventoryData;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkin;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkinType;

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
	}
	
	public void onGuiClosed() {}
	
	
	private class ChooseInvItemGui extends QADGuiScreen {
		private QADScrollPanel panel;
		private EntityEquipmentSlot slot;
		
		public ChooseInvItemGui(EntityEquipmentSlot slot) {
			this.setBehind(npcGui);
			this.slot = slot;
			this.returnScreen = NPCInventoryEditorGui.this;
		}

		@Override
		public void buildGui() {
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
			panel.addComponent(clear);
			for(final Item item : NPCInventoryData.getAcceptableItems(slot)) {
				final ItemStack stack = new ItemStack(item);
				QADButton component = new QADButton(item.getItemStackDisplayName(stack));
				component.simplified = true;
				component.textAlignment = 0;
				component.setAction( new Runnable() {
					@Override public void run() {
						npcGui.data.setItem(slot, stack);
						displayGuiScreen(returnScreen);
					}
				});
				panel.addComponent(component);
			}
			panel.setLayout(new QADListLayout());
			addComponent(panel);
		}
		
		public void onGuiClosed() {}

		@Override
		public void layoutGui() {
			panel.setSize(this.width, this.height);
		}
	}
}
