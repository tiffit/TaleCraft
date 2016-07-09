package tiffit.talecraft.client.gui.voxelator;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.layout.QADListLayout;
import de.longor.talecraft.util.Vec2i;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import tiffit.talecraft.client.gui.npc.NPCInventoryEditorGui;
import tiffit.talecraft.client.gui.qad.QADItemButton;
import tiffit.talecraft.entity.NPC.NPCInventoryData;

public class BlockStateSelector extends QADGuiScreen {
	private QADScrollPanel panel;
	private QADTextField searchField;
	private String lastSearch;
	private VoxelatorGui voxGui;
	private final List<ItemStack> blocks;
	
	private boolean add = true;
	private int index = 0;
	
	public BlockStateSelector(VoxelatorGui voxGui) {
		this.setBehind(voxGui);
		this.returnScreen = voxGui;
		this.voxGui = voxGui;
		blocks = new ArrayList<ItemStack>();
		for(Block block : Block.REGISTRY){
			blocks.add(new ItemStack(block));
		}
	}
	
	public BlockStateSelector(VoxelatorGui voxGui, boolean add, int index) {
		this(voxGui);
		this.add = add;
		this.index = index;
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
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}
	
	private List<ItemStack> getForSearch(String search){
		List<ItemStack> searchedItems = new ArrayList<ItemStack>();
		for(ItemStack item : blocks){
			Item itm = item.getItem();
			if(itm == null) continue;
			List<ItemStack> subitems = new ArrayList<ItemStack>();
			itm.getSubItems(itm, CreativeTabs.INVENTORY, subitems);
			for(final ItemStack stack : subitems){
				if(item.getItem().getItemStackDisplayName(stack).toLowerCase().contains(search.toLowerCase())){
					searchedItems.add(stack);
				}
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
					IBlockState state = Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata());
					if(add)voxGui.blocks.add(state);
					else voxGui.blocks.set(index, state);
					displayGuiScreen(getBehind());
				}
			});
			component.setHeight(0);
			panel.addComponent(component);
			}
		panel.setLayout(new QADListLayout(.5D, 18));
		addComponent(panel);
	}
}

//public class BlockStateSelector extends QADGuiScreen {
//	private VoxelatorGui voxGui;
//	private QADScrollPanel panel;
//	private boolean add = true;
//	private int index = 0;
//	
//	public BlockStateSelector(VoxelatorGui voxGui) {
//		this.setBehind(voxGui);
//		this.returnScreen = voxGui;
//		this.voxGui = voxGui;
//	}
//	
//	public BlockStateSelector(VoxelatorGui voxGui, boolean add, int index) {
//		this.setBehind(voxGui);
//		this.returnScreen = voxGui;
//		this.voxGui = voxGui;
//		this.add = add;
//		this.index = index;
//	}
//
//	@Override
//	public void buildGui() {
//		panel = new QADScrollPanel();
//		panel.setPosition(0, 0);
//		panel.setSize(200, 200);
//		RegistryNamespacedDefaultedByKey<ResourceLocation, Block> blocks = Block.REGISTRY;
//		for(final Block block : blocks) {
//			QADButton component = new QADButton(block.getLocalizedName());
//			component.simplified = true;
//			component.textAlignment = 0;
//			component.setAction( new Runnable() {
//				@Override public void run() {
//					if(add)voxGui.blocks.add(block);
//					else voxGui.blocks.set(index, block);
//					displayGuiScreen(getBehind());
//				}
//			});
//			panel.addComponent(component);
//		}
//		panel.setLayout(new QADListLayout());
//		addComponent(panel);
//	}
//	@Override
//	public void layoutGui(){
//		panel.setSize(this.width, this.height);
//	}
//
//}

