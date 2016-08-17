package tiffit.talecraft.client.gui.voxelator;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADSlider;
import de.longor.talecraft.client.gui.qad.QADSlider.SliderModel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import de.longor.talecraft.voxelator.VXAction.VXActions;
import de.longor.talecraft.voxelator.VXShape.VXShapes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import tiffit.talecraft.packet.VoxelatorPacket;

public class VoxelatorGui extends QADGuiScreen {

	NBTTagCompound tag;
	int currentAction = 0;
	int currentShape = 0;
	float radius = 0.35f;
	float width = 0.3f;
	float height = 0.3f;
	float length = 0.3f;
	boolean hollow = false;
	ArrayList<IBlockState> blocks;
	ArrayList<QADComponent> params;
	
	public VoxelatorGui(NBTTagCompound compound){
		tag = compound;
		blocks = Lists.newArrayList();
		params = Lists.newArrayList();
		if(tag.hasKey("brush")){
			NBTTagCompound tag = this.tag.getCompoundTag("brush");
			currentAction = tag.getInteger("action");
			currentShape = tag.getInteger("shape");
			radius = tag.getFloat("radius")/10f;
			length = tag.getInteger("length")/10f;
			width = tag.getInteger("width")/10f;
			height = tag.getInteger("height")/10f;
			hollow = tag.getBoolean("hollow");
			int block_size = tag.getInteger("block_size");
			for(int i = 0; i < block_size; i++){
				blocks.add(Block.getStateById(tag.getInteger("block_id_" + i)));
			}
		}
	}
	
	@Override
	public void buildGui() {
		addComponent(new QADLabel("Voxelator", 2, 2));
		QADButton action = (QADButton) QADFACTORY.createButton("", 2, 10, 200).setModel(new ButtonModel(){
			String custom = null;
			@Override
			public void onClick() {
				incAction();
			}

			@Override
			public String getText() {
				return custom == null ? "Action: " + VXActions.get(currentAction).toString() : custom;
			}
			
			@Override
			public ResourceLocation getIcon() {
				return null;
			}

			@Override
			public void setText(String newText) {
				custom = newText;
			}

			@Override
			public void setIcon(ResourceLocation newIcon) {
			}
		}).setTooltip("The Action To Be Performed").setName("action");
		addComponent(action);
		
		QADButton shape = (QADButton) QADFACTORY.createButton("", 250, 10, 200).setModel(new ButtonModel(){
			String custom = null;
			@Override
			public void onClick() {
				incShape();
			}

			@Override
			public String getText() {
				return custom == null ? "Shape: " + VXShapes.get(currentShape).toString() : custom;
			}

			@Override
			public ResourceLocation getIcon() {
				return null;
			}
			@Override
			public void setText(String newText) {
				custom = newText;
			}

			@Override
			public void setIcon(ResourceLocation newIcon) {
			}
		}).setTooltip("The Shape To Be Created").setName("shape");
		addComponent(shape);
		
		QADButton save = (QADButton) QADFACTORY.createButton("", 325, 200, 75).setModel(new ButtonModel(){

			@Override
			public void onClick() {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("action", currentAction);
				tag.setInteger("shape", currentShape);
				tag.setFloat("radius", getRadius());
				tag.setInteger("length", (int) (VoxelatorGui.this.length*10));
				tag.setInteger("width", (int) (VoxelatorGui.this.width*10));
				tag.setInteger("height", (int) (VoxelatorGui.this.height*10));
				tag.setInteger("block_size", blocks.size());
				tag.setBoolean("hollow", hollow);
				for(int i = 0; i < blocks.size(); i++){
					tag.setInteger("block_id_" + i, Block.getStateId(blocks.get(i)));
				}
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				TaleCraft.network.sendToServer(new VoxelatorPacket(p.getUniqueID(), tag));
				VoxelatorGui.this.mc.displayGuiScreen(null);
			}

			@Override
			public String getText() {
				return "Save";
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
		}).setTooltip("Apply Changes").setName("replace_param");
		addComponent(save);
		
		QADTickBox hollow = new QADTickBox(475, 10, new TickBoxModel(){
			@Override
			public void setState(boolean newState) {
				VoxelatorGui.this.hollow = newState;
			}

			@Override
			public boolean getState() {
				return VoxelatorGui.this.hollow;
			}

			@Override
			public void toggleState() {
				setState(!getState());
			}
			
		});
		addComponent(hollow.setTooltip("Is this hollow?"));
	}
	
	private void incAction(){
		currentAction++;
		if(currentAction >= VXActions.values().length) currentAction = 0;
	}
	
	private void incShape(){
		currentShape++;
		if(currentShape >= VXShapes.values().length) currentShape = 0;
	}
	
	private String getNameFromState(IBlockState state){
		Block block = state.getBlock();
		ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
		return Item.getItemFromBlock(block).getItemStackDisplayName(stack);
	}
	
	@Override
	public void updateGui() {
		removeAll(params);
		if(currentAction == VXActions.Replace.getID()){
			QADButton param = (QADButton) QADFACTORY.createButton("", 2, 50, 200).setModel(new ButtonModel(){

				@Override
				public void onClick() {
					if(blocks.size() > 0){
						displayGuiScreen(new BlockStateSelector(VoxelatorGui.this, false, 0));
					}else{
						displayGuiScreen(new BlockStateSelector(VoxelatorGui.this));
					}
				}

				@Override
				public String getText() {
					return blocks.size() > 0 ? getNameFromState(blocks.get(0)) : "Set Replace Block";
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
			}).setTooltip("Click to change the block to be replaced with").setName("replace_param");
			addComponent(param);
			params.add(param);
		}
		if(currentAction == VXActions.VariationsReplace.getID()){
			for(int i = 0; i < blocks.size(); i++){
				final int index = i;
				QADButton param = (QADButton) QADFACTORY.createButton("", 2, 50 + 21*i, 200).setModel(new ButtonModel(){
					@Override
					public void onClick() {
						displayGuiScreen(new BlockStateSelector(VoxelatorGui.this, false, index));
					}

					@Override
					public String getText() {
						return getNameFromState(blocks.get(index));
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
				}).setTooltip("Click to change the block to be replaced with").setName("replace_param");
				addComponent(param);
				params.add(param);
			}
			QADButton param = (QADButton) QADFACTORY.createButton("", 2, 50 + blocks.size()*21, 200).setModel(new ButtonModel(){
				@Override
				public void onClick() {
					displayGuiScreen(new BlockStateSelector(VoxelatorGui.this));
				}

				@Override
				public String getText() {
					return "Add Replace Block";
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
			}).setTooltip("Click to change the block to be replaced with").setName("replace_param");
			addComponent(param);
			params.add(param);
		}
		if(currentShape == VXShapes.Sphere.getID()){
			addRadiusSlider(250, 50);
		}
		if(currentShape == VXShapes.Box.getID()){
			QADSlider length = new QADSlider(new SliderModel<Float>(){

				@Override
				public void setValue(Float value) {
					VoxelatorGui.this.length = value;
				}

				@Override
				public Float getValue() {
					return VoxelatorGui.this.length;
				}

				@Override
				public String getValueAsText() {
					return "Length: " + (int) (VoxelatorGui.this.length*10);
				}

				@Override
				public void setSliderValue(float sliderValue){
					VoxelatorGui.this.length = sliderValue;
				}

				@Override
				public float getSliderValue() {
					return VoxelatorGui.this.length;
				}
				
			});
			QADSlider width = new QADSlider(new SliderModel<Float>(){

				@Override
				public void setValue(Float value) {
					VoxelatorGui.this.width = value;
				}

				@Override
				public Float getValue() {
					return VoxelatorGui.this.width;
				}

				@Override
				public String getValueAsText() {
					return "Width: " + (int) (VoxelatorGui.this.width*10);
				}

				@Override
				public void setSliderValue(float sliderValue){
					VoxelatorGui.this.width = sliderValue;
				}

				@Override
				public float getSliderValue() {
					return VoxelatorGui.this.width;
				}
			});
			addHeightSlider(250, 130);
			length.setPosition(250, 50);
			length.setWidth(200);
			addComponent(length);
			params.add(length);
			width.setPosition(250, 90);
			width.setWidth(200);
			addComponent(width);
			params.add(width);
		}
		if(currentShape == VXShapes.Cylinder.getID()){
			addHeightSlider(250, 50);
			addRadiusSlider(250, 80);
		}
	}
	
	private float getRadius(){
		int rounded = (int) (this.radius*100);
		return rounded/10f == 0.0 ? 0.1f : rounded/10f;
	}
	
	private void addHeightSlider(int x, int y){
		QADSlider height = new QADSlider(new SliderModel<Float>(){

			@Override
			public void setValue(Float value) {
				VoxelatorGui.this.height = value;
			}

			@Override
			public Float getValue() {
				return VoxelatorGui.this.height;
			}

			@Override
			public String getValueAsText() {
				return "Height: " + (int) (VoxelatorGui.this.height*10);
			}

			@Override
			public void setSliderValue(float sliderValue){
				VoxelatorGui.this.height = sliderValue;
			}

			@Override
			public float getSliderValue() {
				return VoxelatorGui.this.height;
			}
			
		});
		
		height.setPosition(x, y);
		height.setWidth(200);
		addComponent(height);
		params.add(height);
	}
	
	private void addRadiusSlider(int x, int y){
		QADSlider radius = new QADSlider(new SliderModel<Float>(){

			@Override
			public void setValue(Float value) {
				VoxelatorGui.this.radius = value;
			}

			@Override
			public Float getValue() {
				return VoxelatorGui.this.radius;
			}

			@Override
			public String getValueAsText() {
				return "Radius: " + getRadius();
			}

			@Override
			public void setSliderValue(float sliderValue){
				VoxelatorGui.this.radius = sliderValue;
			}

			@Override
			public float getSliderValue() {
				return VoxelatorGui.this.radius;
			}
			
		});
		radius.setPosition(x, y);
		radius.setWidth(200);
		addComponent(radius);
		params.add(radius);
	}
	
}
