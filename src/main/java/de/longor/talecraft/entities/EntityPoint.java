package de.longor.talecraft.entities;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.items.TeleporterItem;
import de.longor.talecraft.items.WandItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class EntityPoint extends Entity {

	public EntityPoint(World worldIn) {
		super(worldIn);
        this.setSize(0.5F, 0.5F);
	}
	
	@Override
	protected void entityInit() {
		// This is a point. There is nothing to do here.
	}

    protected boolean canTriggerWalking() {
        return false;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        if(this.riddenByEntity != null) {
        	this.riddenByEntity.rotationPitch = this.rotationPitch;
        	this.riddenByEntity.rotationYaw = this.rotationYaw;
        	this.riddenByEntity.prevRotationPitch = this.rotationPitch;
        	this.riddenByEntity.prevRotationYaw = this.rotationYaw;
        }
    }

    public boolean canBeCollidedWith() {
        return TaleCraft.proxy.isBuildMode();
    }

    public boolean isEntityInvulnerable(DamageSource p_180431_1_) {
        return !p_180431_1_.isCreativePlayer();
    }

    public boolean interact(EntityPlayer player) {
    	return false;
    }

    public boolean interactFirst(EntityPlayer playerIn) {
    	if(playerIn.worldObj.isRemote)
    		return false;
    	
    	ItemStack itemStack = playerIn.getCurrentEquippedItem();
    	
    	if(itemStack == null)
    		return false;
    	
    	if(itemStack.getItem() == Items.name_tag) {
    		this.setCustomNameTag(itemStack.getDisplayName());
        	return true;
    	}
    	
    	return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
    	if(worldObj.isRemote)
    		return false;
    	
    	if(source.isCreativePlayer()) {
    		ItemStack heldItem = ((EntityPlayerMP)source.getEntity()).getHeldItem();
    		
    		if(heldItem != null) {
    			if(heldItem.getItem() instanceof TeleporterItem) {
        			return false;
    			}
    			if(heldItem.getItem() instanceof ItemNameTag) {
    	    		this.setCustomNameTag(heldItem.getDisplayName());
        			return false;
    			}
    			if(heldItem.getItem() instanceof WandItem) {
        			return false;
    			}
    		}
    		
    		this.setDead();
        	return true;
    	}
    	
		return false;
    }
    
    public AxisAlignedBB getBoundingBox() {
    	if(Boolean.FALSE.booleanValue()) {
        	final double f = getCollisionBorderSize();
            return new AxisAlignedBB(0, 0, 0, 0, 0, 0).offset(prevPosX, prevPosY, prevPosZ).expand(f, f, f);
    	}
    	
    	return null;
    }
    
    public float getEyeHeight() {
        return height*0.5f;
    }
    
    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.setPositionAndRotation(this.posX, this.posY+(this.height/2)-riddenByEntity.getEyeHeight(), this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
    
    public double getYOffset()
    {
        return 0.0D;
    }
    
    public double getMountedYOffset()
    {
        return 0.0D;
    }
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
		// This is a point. There is nothing to do here.
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		// This is a point. There is nothing to do here.
	}
	
}
