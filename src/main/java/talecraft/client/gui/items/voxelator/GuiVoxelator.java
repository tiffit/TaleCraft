package talecraft.client.gui.items.voxelator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import talecraft.TaleCraft;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADDropdownBox;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.qad.QADRectangularComponent;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.QADSlider;
import talecraft.client.gui.qad.QADDropdownBox.ListModel;
import talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.network.packets.VoxelatorPacket;
import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.Voxelator;
import talecraft.voxelator.BrushParameter.BPType;
import talecraft.voxelator.Voxelator.ActionFactory;
import talecraft.voxelator.Voxelator.FilterFactory;
import talecraft.voxelator.Voxelator.ShapeFactory;
import talecraft.voxelator.actions.VXActionReplace;
import talecraft.voxelator.params.ListBrushParameter;
import talecraft.voxelator.predicates.VXPredicateAlways;
import talecraft.voxelator.shapes.VXShapeSphere;

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
	
	public void applyChanges() {
		System.out.println("Applying NBT: " + data);
		VoxelatorPacket packet = new VoxelatorPacket(mc.player.getUniqueID(), data);
		TaleCraft.network.sendToServer(packet);
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
			panel.addComponent(QADFACTORY.createLabel("Voxelator Settings", 88, 6)).setFontHeight(fontRenderer.FONT_HEIGHT*2);
			
			// Apply Button
			QADButton apply_button = (QADButton) QADFACTORY.createButton("Apply", 2, 0, 80).setEnabled(true).setName("apply");
			apply_button.setAction(new Runnable(){
				@Override
				public void run() {
					applyChanges();
				}
			});
			panel.addComponent(apply_button);
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
					QADRectangularComponent comp = getCompForType(param, 0, yOffset, width/3-3, shape$$);
					
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
			{
				FilterItem item = (FilterItem) box_filter.getSelected();
				BrushParameter[] params = item.factory.getParameters();
				
				int yOffset = 2;
				for(BrushParameter param : params) {
					QADRectangularComponent comp = getCompForType(param, 0, yOffset, width/3-3, filter$$);
					
					pan_filter.addComponent(comp);
					yOffset += 22;
				}
			}
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
			{
				ActionItem item = (ActionItem) box_action.getSelected();
				BrushParameter[] params = item.factory.getParameters();
				
				int yOffset = 2;
				for(BrushParameter param : params) {
					QADRectangularComponent comp = getCompForType(param, 0, yOffset, width/3-3, action$$);
					
					pan_action.addComponent(comp);
					yOffset += 22;
				}
			}
		}
		final int _color = 0xFF880000;
		box_action.setColor(_color);
		box_filter.setColor(_color);
		box_shape.setColor(_color);
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
	
	private QADRectangularComponent getCompForType(BrushParameter param, int x, int y, int width, NBTTagCompound compound){
		BPType type = param.getType();
		if(type == BPType.INTEGER){
			QADSlider slider = new QADSlider(new IntegerSliderModel(compound, param.asIntegerParameter()));
			slider.setBounds(0, y, width, 20);
			return slider;
		}
		if(type == BPType.FLOAT){
			QADSlider slider = new QADSlider(new FloatSliderModel(compound, param.asFloatParameter()));
			slider.setBounds(0, y, width, 20);
			return slider;
		}
		if(type == BPType.BOOLEAN){
			QADButton button = new QADButton(0, y, width, new VoxButtonModel(compound, param.asBooleanParameter()));
			button.setHeight(20);
			return button;
		}
		if(type == BPType.BLOCKSTATE){
			QADDropdownBox box = new QADDropdownBox(new BlockStateListModel(compound, param.asBlockstateParameter()));
			box.setBounds(0, y, width, 20);
			box.setColor(0xFFFFFFFF);
			return box;
		}
		if(type == BPType.LIST){
			ListBrushParameter lparam = param.asListParameter();
			QADPanel panel = new QADPanel();
			if(!compound.hasKey(param.getName()))compound.setTag(param.getName(), new NBTTagList());
			final NBTTagList taglist = compound.getTagList(param.getName(), 10);
			if(lparam.getEntryType() == BPType.BLOCKSTATE){
				panel.setBounds(0, y, width, taglist.tagCount()*20 + 20);
				panel.setBackgroundColor(2);
				for(int i = 0; i < taglist.tagCount(); i++){
					NBTTagCompound sing = taglist.getCompoundTagAt(i);
					QADDropdownBox box = new QADDropdownBox(new ListBlockStateListModel(sing), new ListBlockStateListModel.BlockStateItem(new ItemStack(sing)));
					box.setBounds(0, i*22, width, 20);
					box.setColor(0xFFFFFFFF);
					panel.addComponent(box);
				}
				QADButton button = new QADButton("Add");
				button.setBounds(0, taglist.tagCount()*22, width, 20);
				button.setAction(new Runnable(){
					@Override
					public void run() {
						taglist.appendTag(new ItemStack(Blocks.STONE).serializeNBT());
						GuiVoxelator.this.forceRebuildAll();
					}
				});
				panel.addComponent(button);
			}
			return panel;
		}
		return new QADButton(x, y, width, "error");
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
			list = new ArrayList<ListModelItem>();
			for(ShapeFactory sf : Voxelator.shapes.values()) {
				list.add(new ShapeItem(sf));
			}
			filteredList = new ArrayList<ListModelItem>();
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
			for(ListModelItem item : list){
				if(item.getText().contains(filterString)){
					filteredList.add(item);
				}
			}
		}
		
		@Override public boolean hasIcons() {return false;}
		@Override public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private class FilterListModel implements ListModel {
		final List<ListModelItem> list;
		final List<ListModelItem> filteredList;
		{
			list = new ArrayList<ListModelItem>();
			for(FilterFactory sf : Voxelator.filters.values()) {
				list.add(new FilterItem(sf));
			}
			filteredList = new ArrayList<ListModelItem>();
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
			for(ListModelItem item : list){
				if(item.getText().contains(filterString)){
					filteredList.add(item);
				}
			}
		}
		
		@Override public boolean hasIcons() {return false;}
		@Override public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
	private class ActionListModel implements ListModel {
		final List<ListModelItem> list;
		final List<ListModelItem> filteredList;
		{
			list = new ArrayList<ListModelItem>();
			for(ActionFactory sf : Voxelator.actions.values()) {
				list.add(new ActionItem(sf));
			}
			filteredList = new ArrayList<ListModelItem>();
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
			for(ListModelItem item : list){
				if(item.getText().contains(filterString)){
					filteredList.add(item);
				}
			}
		}
		
		@Override public boolean hasIcons() {return false;}
		@Override public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
	}
	
}
