package de.longor.talecraft.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;

public class TCItem extends Item {
	
	public enum ItemState{
		BROKEN, USELESS, BUGGY, UNFINISHED, STABLE, UNKNOWN;
	}
	
	public TCItem() {
        this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
	}
	
	public ItemState getItemState(){
		return ItemState.UNKNOWN;
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced){
		list.add("STATE: " + getItemState().toString());
	}
	
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float X, float Y, float Z) {
    	if(world.isRemote)
    		return true;
    	
    	world.markBlockForUpdate(pos);
    	
    	return true;
    }
    
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    	if(world.isRemote)
    		return stack;
    	
        return stack;
    }
    
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
    	return stack;
    }
    
    public boolean isDamageable()
    {
        return false;
    }
    
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return false;
    }
    
    @Override
    // Warning: Forge Method
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    	// by returning TRUE, we prevent damaging the entity being hit.
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    public boolean isItemTool(ItemStack stack)
    {
        return true;
    }
    
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }
    
    public boolean canHarvestBlock(Block blockIn) {
        return false;
    }

    public boolean canItemEditBlocks()
    {
        return false;
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
    	player.worldObj.markBlockForUpdate(pos);
        return true;
    }
    
    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }

    public boolean getShareTag()
    {
        return true;
    }
    
}
