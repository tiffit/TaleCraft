package talecraft.blocks.world;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import talecraft.TaleCraft;
import talecraft.util.WorkbenchManager;

public class WorkbenchBlock extends TCWorldBlock{

	public static WorkbenchManager recipes = new WorkbenchManager();
	
	public WorkbenchBlock() {
		super();
		setSoundType(SoundType.WOOD);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(TaleCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	

	public static ItemStack findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn){
		for(IRecipe irecipe : recipes){
			if (irecipe.matches(craftMatrix, worldIn)){
				return irecipe.getCraftingResult(craftMatrix);
			}
		}
		return null;
	}
	
	public static ItemStack[] getRemainingItems(InventoryCrafting craftMatrix, World worldIn){
		for (IRecipe irecipe : recipes){
			if (irecipe.matches(craftMatrix, worldIn)){
				NonNullList<ItemStack> list = irecipe.getRemainingItems(craftMatrix);
				return list.toArray(new ItemStack[list.size()]);
			}
		}
		ItemStack[] aitemstack = new ItemStack[craftMatrix.getSizeInventory()];
		for (int i = 0; i < aitemstack.length; ++i){
			aitemstack[i] = craftMatrix.getStackInSlot(i);
		}
		return aitemstack;
	}

}
