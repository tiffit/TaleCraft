package de.longor.talecraft.items;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.entities.EntityPoint;
import de.longor.talecraft.items.TCItem.ItemState;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TeleporterItem extends TCItem {
	
	public ItemState getItemState(){
		return ItemState.STABLE;
	}
	
    public boolean onItemUse(
    		ItemStack stack, EntityPlayer playerIn, World worldIn,
    		BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ
    ) {
    	if(worldIn.isRemote)
    		return true;
    	
    	if(worldIn.getGameRules().getBoolean("disableTCTeleporter")) {
    		return true;
    	}
    	
        // Get new Position
        double nX = pos.getX() + 0.5;
        double nZ = pos.getZ() + 0.5;
        double nY = pos.getY() + 1;
        
        // Get Old Rotation
        float rY = playerIn.rotationYaw;
        float rP = playerIn.rotationPitch;
    	
    	// Teleport
    	if(playerIn instanceof EntityPlayerMP) {
    		// Its a MP player
    		
			if(playerIn.ridingEntity == null) {
				((EntityPlayerMP) playerIn).playerNetServerHandler.setPlayerLocation(nX,nY,nZ, rY, rP);
				
				if(playerIn.isSprinting()) {
					playerIn.motionX *= 5;
					playerIn.motionZ *= 5;
				}
				
				playerIn.velocityChanged = true;
			} else {
				Entity riding = playerIn.ridingEntity;
				
				if(riding instanceof EntityPoint) {
					return true;
				}
				
				riding.setPositionAndUpdate(nX, nY+0.01f, nZ);
				riding.velocityChanged = true;
			}
			
			playerIn.worldObj.playSoundAtEntity(
					playerIn, "mob.endermen.portal",
					1.5f, (float) (1f + Math.random()*0.1)
			);
    	}
        
    	return true;
    }
	
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if(worldIn.isRemote)
    		return itemStackIn;
    	
    	if(worldIn.getGameRules().getBoolean("disableTCTeleporter")) {
    		return itemStackIn;
    	}
    	
    	float lerp = 1F;
    	float dist = 256;
    	
        Vec3 start = this.getPositionEyes(lerp, playerIn);
        Vec3 direction = playerIn.getLook(lerp);
        Vec3 end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);
    	
        MovingObjectPosition MOP = worldIn.rayTraceBlocks(start, end, false, false, false);
    	
        if(MOP == null)
        	return itemStackIn;
        
        if(MOP.typeOfHit == MovingObjectType.ENTITY) {
        	TaleCraft.logger.info("Hit Entity: " + MOP.entityHit);
        }
        
        if(MOP.typeOfHit == MovingObjectType.BLOCK) {
        	// Extract Block Hit
        	BlockPos newPos = MOP.getBlockPos();
        	
        	// Get new Position
        	double nX = newPos.getX() + 0.5;
        	double nZ = newPos.getZ() + 0.5;
        	double nY = newPos.getY() + 1;
        	
        	if(playerIn.isSneaking()) {
        		nY = playerIn.posY;
        	}
        	
        	// Get Old Rotation
        	float rY = playerIn.rotationYaw;
        	float rP = playerIn.rotationPitch;
        	
        	// Teleport
        	if(playerIn instanceof EntityPlayerMP) {
        		// Its a MP player
        		
				if(playerIn.ridingEntity == null) {
					((EntityPlayerMP) playerIn).playerNetServerHandler.setPlayerLocation(nX,nY,nZ, rY, rP);
					
					if(playerIn.isSprinting()) {
						playerIn.motionX *= 5;
						playerIn.motionZ *= 5;
					}
					
					playerIn.velocityChanged = true;
				} else {
					Entity riding = playerIn.ridingEntity;
					
					if(riding instanceof EntityPoint) {
						return itemStackIn;
					}
					
					riding.setPositionAndUpdate(nX, nY+0.01f, nZ);
					riding.velocityChanged = true;
				}
				
				playerIn.worldObj.playSoundAtEntity(
						playerIn, "mob.endermen.portal",
						1.5f, (float) (1f + Math.random()*0.1)
				);
        	}
        }
        
        return itemStackIn;
    }
    
    @Override
    // Warning: Forge Method
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
    	if(player.worldObj.getGameRules().getBoolean("disableTCTeleporter")) {
    		return false;
    	}
    	
    	TaleCraft.logger.info("Mounting: " + entity);
    	player.mountEntity(entity);
    	player.velocityChanged = true;
    	entity.velocityChanged = true;
    	
    	// by returning TRUE, we prevent damaging the entity being hit.
        return true;
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
