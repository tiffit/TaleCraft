package de.longor.talecraft.client.gui.misc;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.misc.GuiItemTypeSelection.ItemTypeDataLink;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADPanel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADNumberTextField;
import de.longor.talecraft.client.gui.qad.QADNumberTextField.NumberType;
import de.longor.talecraft.client.gui.qad.model.nbtcompound.NBTByteTextFieldModel;
import de.longor.talecraft.client.gui.qad.model.nbtcompound.NBTShortTextFieldModel;
import de.longor.talecraft.client.gui.qad.model.nbtcompound.NBTStringTextFieldModel;

public class GuiItemStackEditor extends QADGuiScreen {
	private NBTTagCompound stack;
	
	QADButton buttonDone;
	QADTextField fieldType;
	QADNumberTextField fieldCount;
	QADNumberTextField fieldDamage;
	
	public GuiItemStackEditor(NBTTagCompound slot) {
		this.stack = slot;
	}
	
	public void buildGui() {
		{
			QADPanel panel = new QADPanel();
			panel.setPosition(0, 0);
			panel.setSize(9999, 22);
			panel.setBackgroundColor(0);
			addComponent(panel);
		}
		
		addComponent(QADFACTORY.createLabel("Item(-stack) Editor", 2, 2));
		
		buttonDone = addComponent(QADFACTORY.createButton("Done", 0, 0, 40, new Runnable() {
			@Override public void run() {
				if(getBehind() != null && getBehind() instanceof QADGuiScreen) {
					((QADGuiScreen) getBehind()).resetGuiScreen();
				}
				displayGuiScreen(getBehind());
			}
		}));
		
		// builder.append(slot.getString("id")).append("/");
		// builder.append(slot.getShort("Damage")).append(" x");
		// builder.append(slot.getByte("Count"));
		
		{
			addComponent(QADFACTORY.createLabel("Type", 2, 24+24*0+6));
			fieldType = new QADTextField(fontRendererObj, 80, 24+24*0, 140, 20);
			fieldType.setText(stack.getString("id"));
			fieldType.setModel(new NBTStringTextFieldModel("id", stack));
			addComponent(fieldType);
			
			addComponent(QADFACTORY.createButton("?", 2+80+2+140+2, 24+24*0, 20, new Runnable() {
				@Override public void run() {
					final GuiScreen behindPre = GuiItemStackEditor.this.getBehind();
					final GuiScreen returnScreen = GuiItemStackEditor.this.returnScreen;
					GuiItemStackEditor.this.setBehind(null);
					GuiItemStackEditor.this.returnScreen = null;
					
					displayGuiScreen(new GuiItemTypeSelection(GuiItemStackEditor.this, new ItemTypeDataLink() {
						public void setType(String identifier) {
							fieldType.setText(identifier);
						}
					}));
					
					TaleCraft.proxy.asClient().sheduleClientTickTask(new Runnable() {
						@Override public void run() {
							GuiItemStackEditor.this.setBehind(behindPre);
							GuiItemStackEditor.this.returnScreen = returnScreen;
						}
					});
				}
			}));
		}
		
		{
			addComponent(QADFACTORY.createLabel("Count", 2, 24+24*1+6));
			Number stackCount = stack.getByte("Count");
			QADNumberTextField fieldCount = new QADNumberTextField(fontRendererObj, 80, 24+24*1, 140, 20, stackCount, NumberType.INTEGER);
			fieldCount.setRange(0, 64+1);
			fieldCount.setModel(new NBTByteTextFieldModel("Count", stack));
			addComponent(fieldCount);
		}
		{
			addComponent(QADFACTORY.createLabel("Damage", 2, 24+24*2+6));
			Number stackDamage = stack.getShort("Damage");
			QADNumberTextField fieldDamage = new QADNumberTextField(fontRendererObj, 80, 24+24*2, 140, 20, stackDamage, NumberType.INTEGER);
			fieldDamage.setRange(Short.MIN_VALUE-1, Short.MAX_VALUE+1);
			fieldDamage.setModel(new NBTShortTextFieldModel("Damage", stack));
			addComponent(fieldDamage);
		}
		
	}
	
	public void layoutGui() {
		buttonDone.setX(width-40);
	}
	
}
