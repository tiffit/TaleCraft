package talecraft.container;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import talecraft.TaleCraftBlocks;
import talecraft.blocks.world.WorkbenchBlock;

public class WorkbenchContainer extends ContainerWorkbench {
	
	private final World worldObj;
	private final BlockPos pos;
	
	public WorkbenchContainer(InventoryPlayer player, World worldObj, BlockPos pos) {
		super(player, worldObj, pos);
		this.worldObj = worldObj;
		this.pos = pos;
		this.inventorySlots.clear();
		this.inventoryItemStacks.clear();
		this.addSlotToContainer(new SlotWorkbenchCrafting(player.player, this.craftMatrix, this.craftResult, 0, 124, 35));

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new Slot(player, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			this.addSlotToContainer(new Slot(player, l, 8 + l * 18, 142));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	@Override
		public boolean canInteractWith(EntityPlayer playerIn){
		return this.worldObj.getBlockState(this.pos).getBlock() != TaleCraftBlocks.workbench ? false : playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
		public void onCraftMatrixChanged(IInventory inventoryIn){
		ItemStack stack = WorkbenchBlock.findMatchingRecipe(this.craftMatrix, this.worldObj);
		if(stack == null)stack = ItemStack.EMPTY;
		this.craftResult.setInventorySlotContents(0, stack);
	}
	
	public static class SlotWorkbenchCrafting extends SlotCrafting{

		private InventoryCrafting craftMatrix;
		private EntityPlayer player;
		
		public SlotWorkbenchCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
			super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
			craftMatrix = craftingInventory;
			this.player = player;
		}
		
		@Override
			public boolean isItemValid(@Nullable ItemStack stack){
			return player.isCreative();
		}
		
		@Override
		public ItemStack onTake(EntityPlayer playerIn, ItemStack stack){
				net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, craftMatrix);
				this.onCrafting(stack);
				net.minecraftforge.common.ForgeHooks.setCraftingPlayer(playerIn);
				ItemStack[] aitemstack = WorkbenchBlock.getRemainingItems(this.craftMatrix, playerIn.getEntityWorld());
				net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

				for (int i = 0; i < aitemstack.length; ++i)
				{
					ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
					ItemStack itemstack1 = aitemstack[i];

					if (itemstack != null)
					{
						this.craftMatrix.decrStackSize(i, 1);
						itemstack = this.craftMatrix.getStackInSlot(i);
					}

					if (itemstack1 != null)
					{
						if (itemstack == null)
						{
							this.craftMatrix.setInventorySlotContents(i, itemstack1);
						}
						else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1))
						{
							itemstack1.grow(itemstack.getCount());
							this.craftMatrix.setInventorySlotContents(i, itemstack1);
						}
						else if (!this.player.inventory.addItemStackToInventory(itemstack1))
						{
							this.player.dropItem(itemstack1, false);
						}
					}
				}
				return stack;
			}
		
	}
}
