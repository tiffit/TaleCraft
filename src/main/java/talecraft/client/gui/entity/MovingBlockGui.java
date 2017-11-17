package talecraft.client.gui.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import talecraft.TaleCraft;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADDropdownBox;
import talecraft.client.gui.qad.QADDropdownBox.ListModel;
import talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADTickBox;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.network.packets.CreateMovingBlockPacket;

public class MovingBlockGui extends QADGuiScreen {

	private double x, y, z;
	
	private IBlockState STATE = Blocks.STONE.getDefaultState();;
	private QADTickBox INVISIBLE;
	private QADTickBox PUSHABLE;
	private QADTickBox COLLISION;
	private QADTickBox NO_GRAVITY;
	private QADNumberTextField MOUNT_Y_OFFSET;
	
	private QADTextField ON_TICK;
	private QADTextField ON_INTERACT;
	private QADTextField ON_COLLIDE;
	private QADTextField ON_DEATH;
	
	public MovingBlockGui(final double x, final double y, final double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void buildGui() {
		addComponent(new QADLabel("Block", 6, 5));
		QADDropdownBox STATE_SELECTOR = new QADDropdownBox(new ListBlockStateListModel());
		STATE_SELECTOR.setBounds(5, 15, 150, 20);
		addComponent(STATE_SELECTOR);
		
		addComponent(new QADLabel("Invisible", 22, 48));
		INVISIBLE = addComponent(new QADTickBox(5, 45, 15, 15));
		
		addComponent(new QADLabel("Pushable", 22, 48 + 20));
		PUSHABLE = addComponent(new QADTickBox(5, 45 + 20, 15, 15));
		
		addComponent(new QADLabel("Collision", 22, 48 + 20*2));
		COLLISION = addComponent(new QADTickBox(5, 45 + 20*2, 15, 15));
		
		addComponent(new QADLabel("No Gravity", 22, 48 + 20*3));
		NO_GRAVITY = addComponent(new QADTickBox(5, 45 + 20*3, 15, 15));
		
		addComponent(new QADLabel("Mount Y Offset", 6, 48 + 20*4));
		MOUNT_Y_OFFSET = addComponent(new QADNumberTextField(5, 58 + 20*4, 100, 20, 0, NumberType.DECIMAL));
		
		
		addComponent(new QADLabel("Scripts", this.width/5*2, 68 + 20*5, 0xff8888));
		
		addComponent(new QADLabel("On Tick", 6, 88 + 20*5));
		ON_TICK = addComponent(new QADTextField(5, 98 + 20*5, this.width/5, 20, ""));
		
		addComponent(new QADLabel("On Interact", 6*2 + this.width/5, 88 + 20*5));
		ON_INTERACT = (QADTextField) addComponent(new QADTextField(5*2 + this.width/5, 98 + 20*5, this.width/5, 20, "").setTooltip("Collision must be enabled!"));
		
		addComponent(new QADLabel("On Collide", 6*3 + this.width/5*2, 88 + 20*5));
		ON_COLLIDE = (QADTextField) addComponent(new QADTextField(5*3 + this.width/5*2, 98 + 20*5, this.width/5, 20, "").setTooltip("Pushable must be enabled!", "Collision must be disabled!"));
		
		addComponent(new QADLabel("On Death", 6*4 + this.width/5*3, 88 + 20*5));
		ON_DEATH = addComponent(new QADTextField(5*4 + this.width/5*3, 98 + 20*5, this.width/5, 20, ""));
		
		QADButton SAVE = new QADButton(this.width - 105, 5, 100, "Create");
		SAVE.setAction(new Runnable() {
			@Override
			public void run() {
				TaleCraft.network.sendToServer(new CreateMovingBlockPacket(
						x, y, z, mc.world.provider.getDimension(), //Location to place
						STATE, //Type of block
						INVISIBLE.getState(), PUSHABLE.getState(), COLLISION.getState(), NO_GRAVITY.getState(), //Boolean settings
						MOUNT_Y_OFFSET.getValue().floatValue(), //Numbers
						new String[]{ON_TICK.getText(), ON_INTERACT.getText(), ON_COLLIDE.getText(), ON_DEATH.getText()} //Scripts
						));
			}
		});
		addComponent(SAVE);
	}
	
	private class ListBlockStateListModel implements ListModel {
		private List<ListModelItem> items;
		private List<ListModelItem> filtered;
		
		public ListBlockStateListModel() {
			items = new ArrayList<ListModelItem>();
			filtered = new ArrayList<ListModelItem>();
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			for(Object block : Block.REGISTRY){
				stacks.add(new ItemStack((Block)block));
			}
			for(ItemStack item : stacks){
				Item itm = item.getItem();
				if(itm == null) continue;
				NonNullList<ItemStack> subitems = NonNullList.create();
				itm.getSubItems(CreativeTabs.INVENTORY, subitems);
				for(final ItemStack stack : subitems){
					if(!stack.isEmpty())items.add(new BlockStateItem(stack));
				}
			}
		}
		@Override
		public void onSelection(ListModelItem selected) {
			BlockStateItem stateItem = (BlockStateItem) selected;
			STATE = stateItem.getState();
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

	}
	private static class BlockStateItem implements ListModelItem{

		private ItemStack stack;
		
		public BlockStateItem(ItemStack stack){
			this.stack = stack;
		}
		
		@Override
		public String getText() {
			return stack.getItem().getItemStackDisplayName(stack);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof BlockStateItem))return false;
			return ItemStack.areItemStacksEqual(stack, ((BlockStateItem)obj).stack);
		}

		@Override
		public int hashCode() {
			return stack.hashCode();
		}
		
		private IBlockState getState(){
			Block block = Block.getBlockFromItem(stack.getItem());
			return block.getStateFromMeta(stack.getMetadata());
		}
		
	}
	
}
