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
import de.longor.talecraft.voxelator.VXAction.VXActions;
import de.longor.talecraft.voxelator.VXShape.VXShapes;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import tiffit.talecraft.packet.VoxelatorPacket;

public class VoxelatorGui extends QADGuiScreen {

	int slot;
	NBTTagCompound tag;
	int currentAction = 0;
	int currentShape = 0;
	float radius = 0.35f;
	float width = 0.3f;
	float height = 0.3f;
	float lenght = 0.3f;
	ArrayList<Block> blocks;
	ArrayList<QADComponent> params;
	
	public VoxelatorGui(int slot, NBTTagCompound compound){
		this.slot = slot;
		tag = compound;
		blocks = Lists.newArrayList();
		params = Lists.newArrayList();
	}
	
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
				tag.setInteger("lenght", (int) (VoxelatorGui.this.lenght*10));
				tag.setInteger("width", (int) (VoxelatorGui.this.width*10));
				tag.setInteger("height", (int) (VoxelatorGui.this.height*10));
				tag.setInteger("block_size", blocks.size());
				for(int i = 0; i < blocks.size(); i++){
					tag.setInteger("block_id_" + i, Block.getIdFromBlock(blocks.get(i)));
				}
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				p.inventory.getCurrentItem().getTagCompound().setTag("data", tag);
				TaleCraft.network.sendToServer(new VoxelatorPacket(p.getUniqueID(), p.inventory.currentItem, tag));
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
	}
	
	private void incAction(){
		currentAction++;
		if(currentAction >= VXActions.values().length) currentAction = 0;
	}
	
	private void incShape(){
		currentShape++;
		if(currentShape >= VXShapes.values().length) currentShape = 0;
	}
	
	public void updateGui() {
		setPaused(true);
		removeAll(params);
		if(currentAction == VXActions.Replace.getID()){
			QADButton param = (QADButton) QADFACTORY.createButton("", 2, 50, 200).setModel(new ButtonModel(){

				@Override
				public void onClick() {
					displayGuiScreen(new BlockStateSelector(VoxelatorGui.this));
				}

				@Override
				public String getText() {
					return blocks.size() > 0 ? blocks.get(0).getRegistryName() : "Set Replace Block";
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
						return blocks.get(index).getRegistryName();
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
			radius.setPosition(250, 50);
			radius.setWidth(200);
			addComponent(radius);
			params.add(radius);
		}
		if(currentShape == VXShapes.Box.getID()){
			QADSlider lenght = new QADSlider(new SliderModel<Float>(){

				@Override
				public void setValue(Float value) {
					VoxelatorGui.this.lenght = value;
				}

				@Override
				public Float getValue() {
					return VoxelatorGui.this.lenght;
				}

				@Override
				public String getValueAsText() {
					return "Lenght: " + (int) (VoxelatorGui.this.lenght*10);
				}

				@Override
				public void setSliderValue(float sliderValue){
					VoxelatorGui.this.lenght = sliderValue;
				}

				@Override
				public float getSliderValue() {
					return VoxelatorGui.this.lenght;
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
			lenght.setPosition(250, 50);
			lenght.setWidth(200);
			addComponent(lenght);
			params.add(lenght);
			width.setPosition(250, 90);
			width.setWidth(200);
			addComponent(width);
			params.add(width);
			height.setPosition(250, 130);
			height.setWidth(200);
			addComponent(height);
			params.add(height);
		}
		setPaused(false);
	}
	
	private float getRadius(){
		int rounded = (int) (this.radius*100);
		return rounded/10f == 0.0 ? 0.1f : rounded/10f;
	}
	
}
