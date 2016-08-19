package de.longor.talecraft.client.gui.items;

import java.util.ArrayList;
import java.util.List;

import tiffit.talecraft.packet.VoxelatorPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.qad.QADBoxLabel;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADDropdownBox;
import de.longor.talecraft.client.gui.qad.QADDropdownBox.ListModel;
import de.longor.talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADPanel;
import de.longor.talecraft.client.gui.qad.QADRectangularComponent;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.Voxelator;
import de.longor.talecraft.voxelator.Voxelator.ActionFactory;
import de.longor.talecraft.voxelator.Voxelator.FilterFactory;
import de.longor.talecraft.voxelator.Voxelator.ShapeFactory;
import de.longor.talecraft.voxelator.actions.VXActionReplace;
import de.longor.talecraft.voxelator.predicates.VXPredicateAlways;
import de.longor.talecraft.voxelator.shapes.VXShapeSphere;

public class GuiVoxelator extends QADGuiScreen {
	private final NBTTagCompound data;
	
	private QADDropdownBox box_shape;
	private QADDropdownBox box_filter;
	private QADDropdownBox box_action;
	private QADScrollPanel pan_shape;
	private QADScrollPanel pan_filter;
	private QADScrollPanel pan_action;
	
	public GuiVoxelator(NBTTagCompound compound) {
		if(compound == null)
			compound = new NBTTagCompound();
		
		this.data = compound;
		
		if(!data.hasKey("shape"))
			this.data.setTag("shape", new NBTTagCompound());
		if(!data.hasKey("filter"))
			this.data.setTag("filter", new NBTTagCompound());
		if(!data.hasKey("action"))
			this.data.setTag("action", new NBTTagCompound());
	}
	
	@Override
	public void buildGui() {
		{
			QADPanel panel = new QADPanel();
			panel.setName("menubar");
			panel.setPosition(0, 0);
			panel.setSize(9999, 20);
			panel.setBackgroundColor(0);
			addComponent(panel);
			
			// Header
			panel.addComponent(QADFACTORY.createLabel("Voxelator Settings", 88, 6)).setFontHeight(fontRendererObj.FONT_HEIGHT*2);
			
			// Apply Button
			panel.addComponent(QADFACTORY.createButton("Apply", 2, 0, 80).setEnabled(false).setName("apply"));
		}
		
		NBTTagCompound shape$$ = data.getCompoundTag("shape");
		NBTTagCompound filter$$ = data.getCompoundTag("filter");
		NBTTagCompound action$$ = data.getCompoundTag("action");
		
		{
			ListModel shapeModel = new ShapeListModel();
			ListModelItem defaultItem = null;
			if(!shape$$.hasKey("type")) {
				shape$$.setString("type", VXShapeSphere.FACTORY.getName());
			}
			String type$  =shape$$.getString("type");
			for(ListModelItem i : shapeModel.getItems()) {
				if(i instanceof ShapeItem && i.getText().equals(type$))
					defaultItem = i;
			}
			box_shape = new QADDropdownBox(shapeModel, defaultItem);
			
			pan_shape = new QADScrollPanel();
			pan_shape.setViewportHeight(60);
			addComponent(pan_shape);
			addComponent(box_shape);
			
			{
				ShapeItem item = (ShapeItem) box_shape.getSelected();
				BrushParameter[] params = item.factory.getParameters();
				
				int yOffset = 2;
				for(BrushParameter param : params) {
					String paramName = param.getName();
					
					QADRectangularComponent comp = new QADButton(0, yOffset, width/3-3, paramName);
					
					pan_shape.addComponent(comp);
					yOffset += 22;
				}
			}
		}
		
		{
			ListModel filterModel = new FilterListModel();
			ListModelItem defaultItem = null;
			if(! filter$$.hasKey("type")) {
				filter$$.setString("type", VXPredicateAlways.FACTORY.getName());
			}
			String type$ = filter$$.getString("type");
			System.out.println(" -> " + type$);
			for(ListModelItem i : filterModel.getItems()) {
				if(i instanceof FilterItem && i.getText().equals(type$))
					defaultItem = i;
			}
			box_filter = new QADDropdownBox(filterModel, defaultItem);
			
			pan_filter = new QADScrollPanel();
			pan_filter.setViewportHeight(60);
			addComponent(pan_filter);
			addComponent(box_filter);
		}
		
		{
			ListModel actionModel = new ActionListModel();
			ListModelItem defaultItem = null;
			if(! action$$.hasKey("type")) {
				action$$.setString("type", VXActionReplace.FACTORY.getName());
			}
			String type$ = action$$.getString("type");
			for(ListModelItem i : actionModel.getItems()) {
				if(i instanceof ActionItem && i.getText().equals(type$))
					defaultItem = i;
			}
			box_action = new QADDropdownBox(actionModel, defaultItem);
			
			pan_action = new QADScrollPanel();
			pan_action.setViewportHeight(60);
			addComponent(pan_action);
			addComponent(box_action);
		}
		
	}

	@Override
	public void layoutGui() {
		if(getComponents() == null || getComponents().isEmpty()) {
			return;
		}
		
		if(box_shape==null||box_filter==null||box_action==null) {
			return;
		}
		
		int tbHeight = 23;
		int bxHeight = 20;
		int tbbxheight = tbHeight + bxHeight;
		
		int columns = 3;
		int columnWidth = getContainerWidth() / columns;
		int columnHeight = getContainerHeight() - tbbxheight - 2;
		
		box_shape .setBounds(columnWidth*0+1, tbHeight, columnWidth-2, bxHeight);
		box_filter.setBounds(columnWidth*1+1, tbHeight, columnWidth-2, bxHeight);
		box_action.setBounds(columnWidth*2+1, tbHeight, columnWidth-2, bxHeight);
		
		pan_shape .setBounds(columnWidth*0+1, tbbxheight+1, columnWidth-2, columnHeight);
		pan_filter.setBounds(columnWidth*1+1, tbbxheight+1, columnWidth-2, columnHeight);
		pan_action.setBounds(columnWidth*2+1, tbbxheight+1, columnWidth-2, columnHeight);
	}
	
	public void applyChanges() {
		System.out.println("Applying NBT: " + data);
		
		/*
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		TaleCraft.network.sendToServer(new VoxelatorPacket(p.getUniqueID(), data));
		GuiVoxelator.this.mc.displayGuiScreen(null);
		//*/
	}
	
	
	
	
	
	
	public class ShapeItem implements ListModelItem {
		ShapeFactory factory;
		private ShapeItem(ShapeFactory fac) {
			this.factory = fac;
		}
		@Override public String getText() {
			return this.factory.getName();
		}
	}
	
	public class FilterItem implements ListModelItem {
		FilterFactory factory;
		private FilterItem(FilterFactory fac) {
			this.factory = fac;
		}
		@Override public String getText() {
			return this.factory.getName();
		}
	}
	
	public class ActionItem implements ListModelItem {
		ActionFactory factory;
		private ActionItem(ActionFactory fac) {
			this.factory = fac;
		}
		@Override public String getText() {
			return this.factory.getName();
		}
	}
	
	
	
	
	
	
	private class ShapeListModel implements ListModel {
		final List<ListModelItem> list;
		final List<ListModelItem> filteredList;
		{
			list = new ArrayList<>();
			for(ShapeFactory sf : Voxelator.shapes.values()) {
				list.add(new ShapeItem(sf));
			}
			filteredList = new ArrayList<>();
			filteredList.addAll(list);
		}
		
		@Override public void onSelection(ListModelItem selected) {
			NBTTagCompound $ = data.getCompoundTag("shape");
			String type = $.getString("type");
			if( ! type.equalsIgnoreCase(selected.getText())) {
				$.setString("type", selected.getText());
				GuiVoxelator.this.forceRebuildAll();
			}
		}
		@Override public boolean hasItems() {
			return true;
		}
		@Override public List<ListModelItem> getItems() {
			return list;
		}
		@Override public int getItemCount() {
			return list.size();
		}
		
		@Override public List<ListModelItem> getFilteredItems() {
			return filteredList;
		}
		@Override public void applyFilter(String filterString) {
			filteredList.clear();
			filteredList.addAll(list);
		}
		
		@Override public boolean hasIcons() {return false;}
		@Override public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private class FilterListModel implements ListModel {
		final List<ListModelItem> list;
		final List<ListModelItem> filteredList;
		{
			list = new ArrayList<>();
			for(FilterFactory sf : Voxelator.filters.values()) {
				list.add(new FilterItem(sf));
			}
			filteredList = new ArrayList<>();
			filteredList.addAll(list);
		}
		
		@Override public void onSelection(ListModelItem selected) {
			NBTTagCompound $ = data.getCompoundTag("filter");
			String type = $.getString("type");
			if( ! type.equalsIgnoreCase(selected.getText())) {
				$.setString("type", selected.getText());
				GuiVoxelator.this.forceRebuildAll();
			}
		}
		@Override public boolean hasItems() {
			return true;
		}
		@Override public List<ListModelItem> getItems() {
			return list;
		}
		@Override public int getItemCount() {
			return list.size();
		}
		
		@Override public List<ListModelItem> getFilteredItems() {
			return filteredList;
		}
		@Override public void applyFilter(String filterString) {
			filteredList.clear();
			filteredList.addAll(list);
		}
		
		@Override public boolean hasIcons() {return false;}
		@Override public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private class ActionListModel implements ListModel {
		final List<ListModelItem> list;
		final List<ListModelItem> filteredList;
		{
			list = new ArrayList<>();
			for(ActionFactory sf : Voxelator.actions.values()) {
				list.add(new ActionItem(sf));
			}
			filteredList = new ArrayList<>();
			filteredList.addAll(list);
		}
		
		@Override public void onSelection(ListModelItem selected) {
			NBTTagCompound $ = data.getCompoundTag("action");
			String type = $.getString("type");
			if( ! type.equalsIgnoreCase(selected.getText())) {
				$.setString("type", selected.getText());
				GuiVoxelator.this.forceRebuildAll();
			}
		}
		@Override public boolean hasItems() {
			return true;
		}
		@Override public List<ListModelItem> getItems() {
			return list;
		}
		@Override public int getItemCount() {
			return list.size();
		}
		
		@Override public List<ListModelItem> getFilteredItems() {
			return filteredList;
		}
		@Override public void applyFilter(String filterString) {
			filteredList.clear();
			filteredList.addAll(list);
		}
		
		@Override public boolean hasIcons() {return false;}
		@Override public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
}
