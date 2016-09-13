package de.longor.talecraft.client.gui.items.voxelator;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.client.gui.qad.QADDropdownBox.ListModel;
import de.longor.talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.voxelator.params.BlockstateBrushParameter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BlockStateListModel implements ListModel {

	private final NBTTagCompound tag;
	private String name;
	private ItemStack current;
	
	private List<ListModelItem> items;
	private List<ListModelItem> filtered;
	
	public BlockStateListModel(NBTTagCompound compound, BlockstateBrushParameter param) {
		this.tag = compound;
		name = param.getName();
		tag.setTag(name, new ItemStack(Blocks.STONE).serializeNBT());
		if(tag.hasKey(name))current = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(name));
		items = new ArrayList<ListModelItem>();
		filtered = new ArrayList<ListModelItem>();
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(Object block : Block.REGISTRY){
			stacks.add(new ItemStack((Block)block));
		}
		for(ItemStack item : stacks){
			Item itm = item.getItem();
			if(itm == null) continue;
			List<ItemStack> subitems = new ArrayList<ItemStack>();
			itm.getSubItems(itm, CreativeTabs.INVENTORY, subitems);
			for(final ItemStack stack : subitems){
				items.add(new BlockStateItem(stack));
			}
		}
	}

	@Override
	public void onSelection(ListModelItem selected) {
		BlockStateItem stateItem = (BlockStateItem) selected;
		tag.setTag(name, stateItem.stack.serializeNBT());
	}

	@Override
	public boolean hasItems() {
		return getItemCount() > 0;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public List<ListModelItem> getItems() {
		return items;
	}

	@Override
	public void applyFilter(String filterString) {
		filtered.clear();
		for(ListModelItem lmi : items){
			BlockStateItem bsi = (BlockStateItem) lmi;
			if(bsi.stack.getItem().getItemStackDisplayName(bsi.stack).toLowerCase().contains(filterString.toLowerCase())){
				filtered.add(lmi);
			}
		}
	}

	@Override
	public List<ListModelItem> getFilteredItems() {
		return filtered;
	}

	@Override
	public boolean hasIcons() {
		return true;
	}

	@Override
	public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {
		renderer.drawItemStack(((BlockStateItem)item).stack, 2, 2);
	}
	
	private static class BlockStateItem implements ListModelItem{

		private ItemStack stack;
		
		public BlockStateItem(ItemStack stack){
			this.stack = stack;
		}
		
		public BlockStateItem(IBlockState state){
			Block block = state.getBlock();
			stack = new ItemStack(block, 1, block.getMetaFromState(state));
		}
		
		@Override
		public String getText() {
			return stack.getItem().getItemStackDisplayName(stack);
		}
		
	}

}
