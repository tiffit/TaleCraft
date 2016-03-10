package de.longor.talecraft.client.gui.blocks;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import scala.inline;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.SummonBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.SummonBlockTileEntity.SummonOption;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor.RemoteEntityDataLink;
import de.longor.talecraft.client.gui.misc.GuiEntityTypeSelection;
import de.longor.talecraft.client.gui.misc.GuiEntityTypeSelection.EntityTypeDataLink;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADComponentContainer;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADNumberTextField;
import de.longor.talecraft.client.gui.qad.QADPanel;
import de.longor.talecraft.client.gui.qad.QADRectangularComponent;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADNumberTextField.NumberType;
import de.longor.talecraft.client.gui.qad.QADTextField.TextFieldModel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import de.longor.talecraft.client.gui.qad.model.DefaultTextFieldDecimalNumberModel;
import de.longor.talecraft.client.gui.qad.model.DefaultTextFieldIntegerNumberModel;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.NBTHelper;

public class GuiSummonBlock extends QADGuiScreen {
	private int headerHeight = 14;
	SummonBlockTileEntity tileEntity;
	
	QADScrollPanel mainContainer;
	
	QADPanel generalPanel;
	TWO_COLUMN_LAYOUT generalPanelLayout;
	
	List<QADPanel> summonOptionPanels;
	
	public GuiSummonBlock(SummonBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		{
			QADPanel panel = new QADPanel();
			panel.setPosition(0, 0);
			panel.setSize(9999, headerHeight);
			panel.setBackgroundColor(0);
			addComponent(panel);
		}
		
		addComponent(QADFACTORY.createLabel("Summon Block @ " + tileEntity.getPos(), 2, 2));
		
		mainContainer = new QADScrollPanel();
		mainContainer.setPosition(0, headerHeight);
		mainContainer.setSize(width, height-headerHeight);
		addComponent(mainContainer);
		
		generalPanel = new QADPanel();
		generalPanel.setBackgroundColor(2);
		mainContainer.addComponent(generalPanel);
		
		generalPanelLayout = new TWO_COLUMN_LAYOUT(generalPanel);
		
		// Count
		{
			QADLabel label = new QADLabel("Summon Count");
			
			final QADTextField field = new QADTextField(""+tileEntity.getSummonCount());
			
			DefaultTextFieldIntegerNumberModel model = new DefaultTextFieldIntegerNumberModel(tileEntity.getSummonCount());
			model.setValidatorPredicate(new Predicate<Integer>() {
				@Override public boolean apply(Integer input) {
					int i = input.intValue();
					
					if(i > 100) return false;
					if(i < 1  ) return false;
					
					send(i);
					return true;
				}
				
				// ONLY update the changed value
				private void send(int value) {
					BlockPos position = tileEntity.getPos();
					String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
					
					NBTTagCompound data = new NBTTagCompound();
					data.setInteger("summonCount", value);
					
					TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, data));
				}
			});
			field.setModel(model);
			
			generalPanelLayout.row(label, field);
		}
		
		// Region
		{
			QADLabel label = new QADLabel("Summon Region");
			QADButton button = new QADButton("Set Summon Region");
			button.setName("region.set");
			button.setTooltip("Sets the region in which","entities are summoned.");
			generalPanelLayout.row(label, button);
			
			button.setAction(new Runnable() {
				@Override public void run() {
					EntityPlayerSP playerSP = Minecraft.getMinecraft().thePlayer;
					int[] bounds = WandItem.getBoundsFromPLAYERorNULL(playerSP);
					
					if(bounds != null) {
						BlockPos position = tileEntity.getPos();
						String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
						
						NBTTagCompound data = new NBTTagCompound();
						data.setIntArray("summonRegionBounds", bounds);
						
						TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, data));
					}
				}
			});
		}
		
		// Use Weight As Count
		{
			QADLabel label = new QADLabel("Use weight as count?");
			QADTickBox tickBox = new QADTickBox(new TickBoxModel(){
				boolean state = tileEntity.getSummonWeightAsCount();
				
				@Override public void setState(boolean newState) {
					if(state != newState) {
						state = newState;
						
						BlockPos position = tileEntity.getPos();
						String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
						
						NBTTagCompound data = new NBTTagCompound();
						data.setBoolean("useWeightAsCount", newState);
						
						TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, data));
					}
				}
				
				@Override public boolean getState() {
					return state;
				}
				
				@Override public void toggleState() {
					setState(!getState());
				}
			});
			
			generalPanelLayout.row(label, tickBox);
		}
		
		{
			QADButton button = new QADButton(QADButton.ICON_ADD);
			button.setPosition(-99999, -99999);
			button.setName("option.add");
			button.setTooltip("Add new summon option.");
			mainContainer.addComponent(button);
			
			button.setAction(new Runnable() {
				@Override public void run() {
					SummonOption[] oldArray = tileEntity.getSummonOptions();
					SummonOption[] newArray = new SummonOption[oldArray.length+1];
					System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
					
					newArray[oldArray.length] = new SummonOption();
					newArray[oldArray.length].setWeight(1f);
					newArray[oldArray.length].setData(new NBTTagCompound());
					newArray[oldArray.length].getData().setString("id", "Zombie");
					
					tileEntity.setSummonOptions(newArray);
					updateSummonBlockData(true);
					displayGuiScreen(null);
				}
			});
		}
		
		buildGuiPanels();
		
	}
	
	private void buildGuiPanels() {
		summonOptionPanels = Lists.newArrayList();
		
		SummonOption[] options = tileEntity.getSummonOptions();
		
		for(int i = 0; i < options.length; i++) {
			final int optionIndex = i;
			final SummonOption option = options[optionIndex];
			
			QADPanel panel = new QADPanel();
			panel.setBackgroundColor(1);
			int panelHeight = 2;
			int rowHeight = 25;
			
			panel.addComponent(new QADLabel("Summon Option #"+optionIndex, 2, panelHeight+6));
			{
				QADButton button = new QADButton(QADButton.ICON_DELETE);
				button.setPosition(panel.getWidth()-20-2, panelHeight);
				button.setTooltip("Remove summon option #"+optionIndex+".");
				button.setName("option.remove");
				panel.addComponent(button);
				
				button.setAction(new Runnable() {
					final int index = optionIndex;
					@Override public void run() {
						SummonOption[] source = tileEntity.getSummonOptions();
						
						SummonOption[] result = new SummonOption[source.length - 1];
						System.arraycopy(source, 0, result, 0, index);
						if (source.length != index) {
						    System.arraycopy(source, index + 1, result, index, source.length - index - 1);
						}
						
						tileEntity.setSummonOptions(result);
						updateSummonBlockData(true);
					}
				});
			}
			panelHeight += 2;
			panelHeight += rowHeight;
			
			
			panel.addComponent(new QADLabel("Weight ", 2, panelHeight+6));
			{
				QADTextField field = new QADTextField(new DefaultTextFieldDecimalNumberModel(option.getWeight(),new Predicate<Double>() {
					@Override public boolean apply(Double input) {
						double i = input.doubleValue();
						
						if(i > 100) return false;
						if(i < 0  ) return false;
						
						send(i);
						return true;
					}
					
					// ONLY update the changed value
					private void send(double value) {
						option.setWeight((float)value);
						updateSummonBlockData(false);
					}
				}));
				field.setPosition(2+140, panelHeight);
				field.setSize(200-22, 20);
				
				panel.addComponent(field);
			}
			panelHeight += rowHeight;
			
			panel.addComponent(new QADLabel("Type ", 2, panelHeight+6));
			{
				QADButton button = new QADButton(option.getData().getString("id"));
				button.setPosition(2+140, panelHeight);
				button.setSize(200-22, 20);
				button.setEnabled(false);
				panel.addComponent(button);
			}
			{
				QADButton button = new QADButton(QADButton.ICON_INVEDIT);
				button.setPosition(2+120+200+2, panelHeight);
				button.setTooltip("Change entity type.");
				button.setAction(new Runnable() {
					@Override public void run() {
						TaleCraft.logger.info("Click!");
						
						GuiEntityTypeSelection guiScreen = new GuiEntityTypeSelection(GuiSummonBlock.this, new EntityTypeDataLink() {
							@Override public void setType(String identifier) {
								option.getData().setString("id", identifier);
								updateSummonBlockData(true);
							}
						});
						
						displayGuiScreen(guiScreen);
					}
				});
				panel.addComponent(button);
			}
			panelHeight += rowHeight;
			
			panel.addComponent(new QADLabel("Data ", 2, panelHeight+6));
			{
				QADTextField button = new QADTextField(NBTHelper.asJson(option.getData()));
				button.setMaxStringLength(65535);
				button.setPosition(2+140, panelHeight);
				button.setSize(200-22, 20);
				button.setEnabled(false);
				panel.addComponent(button);
			}
			{
				QADButton button = new QADButton(QADButton.ICON_INVEDIT);
				button.setPosition(2+120+200+2, panelHeight);
				button.setTooltip("Open entity editor.");
				button.setAction(new Runnable() {
					@Override public void run() {
						RemoteEntityDataLink dataLink = new RemoteEntityDataLink() {
							@Override public void updateData(NBTTagCompound entityData) {
								option.setData(entityData);
								updateSummonBlockData(true);
							}
						};
						
						GuiEntityEditor editor = new GuiEntityEditor(option.getData(), dataLink);
						editor.setBehind(GuiSummonBlock.this);
						displayGuiScreen(editor);
					}
				});
				panel.addComponent(button);
			}
			panelHeight += rowHeight;
			
			panel.setHeight(panelHeight+6);
			summonOptionPanels.add(panel);
			mainContainer.addComponent(panel);
			
			
		}
		
		this.relayout();
	}
	
	public void layoutGui() {
		mainContainer.setSize(width, height-headerHeight);
		int viewHeight = 0;
		
		generalPanel.setX(0);
		generalPanel.setY(0);
		generalPanel.setWidth(width);
		generalPanelLayout.layout(width);
		generalPanel.setHeight(generalPanelLayout.getHeight());
		viewHeight += generalPanelLayout.getHeight() + 4;
		
		mainContainer.getComponentByName("option.add").setPosition(width - 20 - 2 - 8, viewHeight - 2);
		viewHeight += 22;
		
		for(QADPanel panel : summonOptionPanels) {
			panel.setPosition(2, viewHeight);
			panel.setWidth(width-2-8);
			viewHeight += panel.getHeight() + 2;
			
			panel.getComponentByName("option.remove").setPosition(panel.getWidth()-20-2, 3);
		}
		
		mainContainer.setViewportHeight(viewHeight+2);
	}
	
	private void updateSummonBlockData(boolean resetScreen) {
		BlockPos position = tileEntity.getPos();
		String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(position);
		
		NBTTagCompound data = new NBTTagCompound();
		tileEntity.writeToNBT_do(data);
		
		TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, data));
		
		if(resetScreen)
			resetGuiScreen();
	}
	
	private static class TWO_COLUMN_LAYOUT {
		private QADComponentContainer container;
		private List<List<QADComponent>> rows;
		private int lastCalculatedHeight = 0;
		
		public TWO_COLUMN_LAYOUT(QADComponentContainer container) {
			this.container = container;
			this.rows = Lists.newArrayList();
			this.lastCalculatedHeight = 0;
		}
		
		public int getHeight() {
			return lastCalculatedHeight;
		}
		
		public void row(QADComponent... rowComponents) {
			List<QADComponent> rowComponentsList = Lists.newArrayList(rowComponents);
			rows.add(rowComponentsList);
			
			for(QADComponent component : rowComponentsList) {
				container.addComponent(component);
			}
		}
		
		public void layout(int $width) {
			if(rows.size() == 0) return;
			
			final int rowHeight = 18;
			
			final int $center = $width / 2;
			final int $left = 2;
			final int $right = $width-6-2;
			final int $top = rowHeight/2;
			final int $bottomPad = 0;
			final int $rightWidth = $right - $center;
			
			// increases
			int yOff = $top;
			
			for(List<QADComponent> row : rows) {
				
				QADComponent rowLabel = row.get(0);
				int rowLabelYOffset = 0;
				
				if(rowLabel instanceof QADLabel) {
					rowLabelYOffset = 6;
				}
				
				rowLabel.setX($left);
				rowLabel.setY(yOff+rowLabelYOffset);
				
				for(int i = 1; i < row.size(); i++) {
					QADComponent component = row.get(i);
					
					component.setX($center);
					component.setY(yOff);
					
					if(component instanceof QADRectangularComponent) {
						if(component instanceof QADTickBox) {
							yOff += 20 + 5;
							continue;
						}
						
						if(((QADRectangularComponent) component).canResize())
							((QADRectangularComponent) component).setWidth($rightWidth);
						
						int height = ((QADRectangularComponent) component).getHeight();
						
						if(height < rowHeight) {
							component.setY(yOff+(rowHeight/2)-(height/2));
							height = rowHeight;
						}
						
						yOff += height + 5;
					} else {
						yOff += rowHeight;
					}
				}
			}
			
			// done
			lastCalculatedHeight = yOff + $bottomPad;
		}
		
	}
	
}
