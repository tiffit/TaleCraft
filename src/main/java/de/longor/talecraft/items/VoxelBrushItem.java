package de.longor.talecraft.items;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.voxelator.Voxelator;
import de.longor.talecraft.voxelator.actions.VXActionReplace;
import de.longor.talecraft.voxelator.predicates.VXPredicateHeightLimit;
import de.longor.talecraft.voxelator.shapes.VXShapeSphere;
import de.longor.talecraft.voxelbrush_old.VoxelBrush;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoxelBrushItem extends TCItem {
	
	public ItemState getItemState(){
		return ItemState.UNFINISHED;
	}
	
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    	if(worldIn.isRemote)
    		return;
    	
    	if(worldIn.getGameRules().getBoolean("disableTCVoxelBrush")) {
    		return;
    	}
    	
		NBTTagCompound itemStackNbtTagCompound = stack.getTagCompound();
		
		if(itemStackNbtTagCompound == null) {
			itemStackNbtTagCompound = new NBTTagCompound();
			stack.setTagCompound(itemStackNbtTagCompound);
		}
		
		// Automatic initialization of the VoxelBrush.
		if(!itemStackNbtTagCompound.hasKey("vbData")) {
	    	TaleCraft.logger.info("Auto-Initializing VoxelBrush: " + stack);
	    	
	    	NBTTagCompound vbData = new NBTTagCompound();
			itemStackNbtTagCompound.setTag("vbData", vbData);
			
			NBTTagCompound shapeTag = new NBTTagCompound();
			shapeTag.setString("type", "sphere");
			shapeTag.setDouble("radius", 3.5);
			vbData.setTag("shape", shapeTag);
			
			NBTTagCompound action = new NBTTagCompound();
			action.setString("type", "replace");
			action.setString("blockID", "minecraft:stone");
			action.setString("blockMeta", "0");
			vbData.setTag("action", action);
		}
    	
    }
	
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    	if(world.isRemote)
    		return stack;
    	
    	if(!player.capabilities.isCreativeMode)
    		return stack;
    	
    	if(!player.capabilities.allowEdit)
    		return stack;
    	
    	if(!stack.hasTagCompound())
    		return stack;
    	
    	NBTTagCompound vbData = stack.getTagCompound().getCompoundTag("vbData");
    	
    	if(vbData.hasNoTags())
    		return stack;
    	
    	float lerp = 1F;
    	float dist = 256;
    	
        Vec3 start = this.getPositionEyes(lerp, player);
        Vec3 direction = player.getLook(lerp);
        Vec3 end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);
    	
        MovingObjectPosition MOP = world.rayTraceBlocks(start, end, false, false, false);
    	
        if(MOP == null)
        	return stack;
        
        VoxelBrush.func(world, vbData, MOP.getBlockPos());
        
        return stack;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    	if(!stack.hasTagCompound()){
    		super.addInformation(stack, playerIn, tooltip, advanced);
    		return;
    	}
    	
    	NBTTagCompound vbData = stack.getTagCompound().getCompoundTag("vbData");
    	
    	if(vbData.hasNoTags())
    		return;
    	
    	VoxelBrush.func(vbData, tooltip);
    }
    
    public Vec3 getPositionEyes(float partialTicks, EntityPlayer player)
    {
        if (partialTicks == 1.0F)
        {
            return new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        }
        else
        {
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks + (double)player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
            return new Vec3(d0, d1, d2);
        }
    }
    
}
