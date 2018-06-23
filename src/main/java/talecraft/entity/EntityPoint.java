package talecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import talecraft.TaleCraft;
import talecraft.items.TeleporterItem;
import talecraft.items.WandItem;

public class EntityPoint extends Entity {

	public EntityPoint(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	@Override
	protected void entityInit() {
		// This is a point. There is nothing to do here.
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if(!this.getPassengers().isEmpty()) {
			for(Entity passenger : this.getPassengers()) {
				passenger.rotationPitch = this.rotationPitch;
				passenger.rotationYaw = this.rotationYaw;
				passenger.prevRotationPitch = this.rotationPitch;
				passenger.prevRotationYaw = this.rotationYaw;
			}
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return TaleCraft.proxy.isBuildMode();
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		return !source.isCreativePlayer();
	}

	public boolean interact(EntityPlayer player) {
		return false;
	}

	public boolean interactFirst(EntityPlayer player) {
		if(player.world.isRemote)
			return false;

		ItemStack stack = player.getHeldItemMainhand();

		if(stack == null)
			return false;

		if(stack.getItem() == Items.NAME_TAG) {
			this.setCustomNameTag(stack.getDisplayName());
			return true;
		}

		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(world.isRemote)
			return false;

		if(source.isCreativePlayer()) {
			ItemStack heldItem = ((EntityPlayerMP)source.getTrueSource()).getHeldItemMainhand();

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

	@Override
	public AxisAlignedBB getEntityBoundingBox() {
		if(Boolean.FALSE.booleanValue()) {
			final double f = getCollisionBorderSize();
			return new AxisAlignedBB(0, 0, 0, 0, 0, 0).offset(prevPosX, prevPosY, prevPosZ).expand(f, f, f);
		}

		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public float getEyeHeight() {
		return height*0.5f;
	}

	public void updateRiderPosition() {
		if(!this.getPassengers().isEmpty()) {
			for(Entity passenger : this.getPassengers()) {
				passenger.setPositionAndRotation(this.posX, this.posY+(this.height/2)-passenger.getEyeHeight(), this.posZ, this.rotationYaw, this.rotationPitch);
			}
		}
	}

	@Override
	public double getYOffset() {
		return 0.0D;
	}

	@Override
	public double getMountedYOffset() {
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
