package talecraft.client.gui.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.util.Constants.NBT;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.model.nbtcompound.NBTByteTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTDoubleTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTFloatTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTIntegerTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTLongTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTShortTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTStringTextFieldModel;
import talecraft.client.gui.qad.model.nbtlist.NBTListDoubleTextFieldModel;
import talecraft.client.gui.qad.model.nbtlist.NBTListFloatTextFieldModel;
import talecraft.client.gui.qad.model.nbtlist.NBTListStringTextFieldModel;
import talecraft.util.MutableInteger;
import talecraft.util.RecursiveNBTIterator;
import talecraft.util.RecursiveNBTIterator.NBTTreeConsumer;

public class GuiNBTEditor extends QADGuiScreen {
	public static interface MergeButtonAction {
		public void merge(NBTTagCompound compound);
	}

	NBTTagCompound compound;
	MergeButtonAction mergeAction;

	QADScrollPanel scrollPanel;

	public GuiNBTEditor(NBTTagCompound compound, MergeButtonAction mergeAction) {
		this.compound = compound;
		this.mergeAction = mergeAction;
	}

	public GuiNBTEditor(NBTTagCompound compound) {
		this.compound = compound;
	}

	public GuiNBTEditor() {
		this.compound = new NBTTagCompound();
	}

	@Override
	public void buildGui() {
		{
			QADPanel panel = new QADPanel();
			panel.setPosition(0, 0);
			panel.setSize(9999, 22);
			panel.setBackgroundColor(0);
			addComponent(panel);

			// Header
			addComponent(QADFACTORY.createLabel("NBT Editor", 2, 2)).setFontHeight(fontRenderer.FONT_HEIGHT*2);
			// addComponent(QADFACTORY.createLabel("", 2, 2+10));
		}

		scrollPanel = new QADScrollPanel();
		scrollPanel.setPosition(0, 22);
		scrollPanel.setSize(width, height-22);
		addComponent(scrollPanel);

		buildNBTTree();

	}

	public static final int ROW_HEIGHT = 18;
	public static final int ROW_TABJMP = 8;

	private void buildNBTTree() {
		final MutableInteger yOff = new MutableInteger(ROW_HEIGHT);

		RecursiveNBTIterator.iterate(compound, new NBTTreeConsumer() {
			@Override public void consume(int depth, String name, NBTBase tag, NBTTagCompound parent) {
				int xPos = depth * ROW_TABJMP;
				int yPos = yOff.get();

				if(tag == null) {
					scrollPanel.addComponent(QADFACTORY.createLabel("---", xPos, yPos-8));
					return;
				}

				int type = tag.getId();

				scrollPanel.addComponent(QADFACTORY.createLabel(name, xPos, yPos));

				if(type == NBT.TAG_COMPOUND) {
					scrollPanel.addComponent(QADFACTORY.createLabel("Compound", getCenterX(), yPos));
				}

				if(type == NBT.TAG_LIST) {
					NBTTagList list = (NBTTagList) tag;
					int listtype = list.getTagType();
					int tagcount = list.tagCount();

					scrollPanel.addComponent(QADFACTORY.createLabel("List", getCenterX(), yPos));

					if(listtype == NBT.TAG_DOUBLE) {
						yPos += ROW_HEIGHT;
						for(int i = 0; i < tagcount; i++) {
							Number value = list.getDoubleAt(i);
							NumberType valuetype = NumberType.DECIMAL;
							QADNumberTextField textField = new QADNumberTextField(fontRenderer, getCenterX(), yPos, 200, 14, value, valuetype);
							textField.setModel(new NBTListDoubleTextFieldModel(list, i));
							scrollPanel.addComponent(textField);
							yPos += ROW_HEIGHT;
						}
					}

					if(listtype == NBT.TAG_FLOAT) {
						yPos += ROW_HEIGHT;
						for(int i = 0; i < tagcount; i++) {
							Number value = list.getFloatAt(i);
							NumberType valuetype = NumberType.DECIMAL;
							QADNumberTextField textField = new QADNumberTextField(fontRenderer, getCenterX(), yPos, 200, 14, value, valuetype);
							textField.setModel(new NBTListFloatTextFieldModel(list, i));
							scrollPanel.addComponent(textField);
							yPos += ROW_HEIGHT;
						}
					}

					if(listtype == NBT.TAG_STRING) {
						yPos += ROW_HEIGHT;
						for(int i = 0; i < tagcount; i++) {
							QADTextField textField = new QADTextField(fontRenderer, getCenterX(), yPos, 200, 14);
							textField.setModel(new NBTListStringTextFieldModel(list, i));
							scrollPanel.addComponent(textField);
							yPos += ROW_HEIGHT;
						}
					}
				}

				if(type == NBT.TAG_STRING) {
					QADTextField textField = new QADTextField(fontRenderer, getCenterX(), yPos-3, 200, 14);
					textField.setModel(new NBTStringTextFieldModel(name, parent));
					scrollPanel.addComponent(textField);
				}

				if(type == NBT.TAG_BYTE) {
					Number value = ((NBTTagByte)tag).getByte();
					NumberType valueType = NumberType.INTEGER;
					QADNumberTextField numberTextField = new QADNumberTextField(fontRenderer, getCenterX(), yPos-3, 200, 14, value, valueType);
					numberTextField.setRange(Byte.MIN_VALUE, Byte.MAX_VALUE);
					numberTextField.setModel(new NBTByteTextFieldModel(name, parent));
					scrollPanel.addComponent(numberTextField);
				}

				if(type == NBT.TAG_SHORT) {
					Number value = ((NBTTagShort)tag).getShort();
					NumberType valueType = NumberType.INTEGER;
					QADNumberTextField numberTextField = new QADNumberTextField(fontRenderer, getCenterX(), yPos-3, 200, 14, value, valueType);
					numberTextField.setRange(Short.MIN_VALUE, Short.MAX_VALUE);
					numberTextField.setModel(new NBTShortTextFieldModel(name, parent));
					scrollPanel.addComponent(numberTextField);
				}

				if(type == NBT.TAG_INT) {
					Number value = ((NBTTagInt)tag).getInt();
					NumberType valueType = NumberType.INTEGER;
					QADNumberTextField numberTextField = new QADNumberTextField(fontRenderer, getCenterX(), yPos-3, 200, 14, value, valueType);
					numberTextField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
					numberTextField.setModel(new NBTIntegerTextFieldModel(name, parent));
					scrollPanel.addComponent(numberTextField);
				}

				if(type == NBT.TAG_LONG) {
					Number value = ((NBTTagLong)tag).getLong();
					NumberType valueType = NumberType.INTEGER;
					QADNumberTextField numberTextField = new QADNumberTextField(fontRenderer, getCenterX(), yPos-3, 200, 14, value, valueType);
					numberTextField.setRange(Long.MIN_VALUE, Long.MAX_VALUE);
					numberTextField.setModel(new NBTLongTextFieldModel(name, parent));
					scrollPanel.addComponent(numberTextField);
				}

				if(type == NBT.TAG_FLOAT) {
					Number value = ((NBTTagFloat)tag).getFloat();
					NumberType valueType = NumberType.DECIMAL;
					QADNumberTextField numberTextField = new QADNumberTextField(fontRenderer, getCenterX(), yPos-3, 200, 14, value, valueType);
					numberTextField.setRange(Float.MIN_VALUE, Float.MAX_VALUE);
					numberTextField.setModel(new NBTFloatTextFieldModel(name, parent));
					scrollPanel.addComponent(numberTextField);
				}

				if(type == NBT.TAG_DOUBLE) {
					Number value = ((NBTTagDouble)tag).getDouble();
					NumberType valueType = NumberType.DECIMAL;
					QADNumberTextField numberTextField = new QADNumberTextField(fontRenderer, getCenterX(), yPos-3, 200, 14, value, valueType);
					numberTextField.setRange(Double.MIN_VALUE, Double.MAX_VALUE);
					numberTextField.setModel(new NBTDoubleTextFieldModel(name, parent));
					scrollPanel.addComponent(numberTextField);
				}

				yPos += ROW_HEIGHT;
				yOff.set(yPos);
			}
		});

		scrollPanel.setViewportHeight(yOff.get()+2);
	}

	@Override
	public void layoutGui() {
		scrollPanel.setSize(width, height-22);

	}

	public void data_tree_change() {

	}

}
