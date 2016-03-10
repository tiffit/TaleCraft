package de.longor.talecraft.items;

import de.longor.talecraft.entities.EntityPoint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpawnPointItem extends TCItem {
	
    public boolean onItemUse(
    		ItemStack stack, EntityPlayer playerIn, World worldIn,
    		BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ
    ) {
    	if(worldIn.isRemote)
    		return true;
    	onItemRightClick(stack, worldIn, playerIn);
    	return true;
    }
	
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if(worldIn.isRemote)
    		return itemStackIn;
    	
    	float x = (float) playerIn.posX;
    	float y = (float) playerIn.posY + playerIn.getEyeHeight();
    	float z = (float) playerIn.posZ;
    	
    	if(playerIn.isSneaking()) {
    		BlockPos pos = playerIn.getPosition();
    		x = pos.getX() + 0.5f;
    		y = pos.getY() + 0.5f;
    		z = pos.getZ() + 0.5f;
    	}
    	
    	float yaw = playerIn.rotationYaw;
    	float pitch = playerIn.rotationPitch;
    	
    	EntityPoint pointEntity = new EntityPoint(worldIn);
    	pointEntity.setPositionAndRotation(x, y - pointEntity.height/2, z, yaw, pitch);
    	worldIn.spawnEntityInWorld(pointEntity);
    	
    	if(itemStackIn.hasDisplayName()) {
    		String name = itemStackIn.getDisplayName();
    		pointEntity.setCustomNameTag(name);
    	}
    	
    	return itemStackIn;
    }
	
}
