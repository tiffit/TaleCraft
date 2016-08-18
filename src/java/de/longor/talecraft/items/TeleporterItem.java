package de.longor.talecraft.items;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.entities.EntityPoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TeleporterItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)
			return EnumActionResult.PASS;

		if(world.getGameRules().getBoolean("disableTCTeleporter")) {
			return EnumActionResult.PASS;
		}

		// Get new Position
		double nX = pos.getX() + 0.5;
		double nZ = pos.getZ() + 0.5;
		double nY = pos.getY() + 1;

		// Get Old Rotation
		float rY = player.rotationYaw;
		float rP = player.rotationPitch;

		// Teleport
		if(player instanceof EntityPlayerMP) {
			// Its a MP player
			BlockPos npos = new BlockPos(nX, nY, nZ);
			player.addChatComponentMessage(new TextComponentString("[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "] -> " + "[" + npos.getX() + ", " + npos.getY() + ", " + npos.getZ() + "]"));
			if(player.getRidingEntity() == null) {
				((EntityPlayerMP) player).connection.setPlayerLocation(nX,nY,nZ, rY, rP);

				if(player.isSprinting()) {
					player.motionX *= 5;
					player.motionZ *= 5;
				}

				player.velocityChanged = true;
			} else {
				Entity riding = player.getRidingEntity();

				if(riding instanceof EntityPoint) {
					return EnumActionResult.PASS;
				}
				
				riding.setPositionAndUpdate(nX, nY+0.01f, nZ);
				riding.velocityChanged = true;
			}

			player.worldObj.playSound(player, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1.5f, (float) (1f + Math.random()*0.1));
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if(world.getGameRules().getBoolean("disableTCTeleporter")) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}

		float lerp = 1F;
		float dist = 256;

		Vec3d start = this.getPositionEyes(lerp, player);
		Vec3d direction = player.getLook(lerp);
		Vec3d end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);

		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);

		if(result == null)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if(result.typeOfHit == RayTraceResult.Type.ENTITY) {
			TaleCraft.logger.info("Hit Entity: " + result.entityHit);
		}

		if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
			// Extract Block Hit
			BlockPos newPos = result.getBlockPos();

			// Get new Position
			double nX = newPos.getX() + 0.5;
			double nZ = newPos.getZ() + 0.5;
			double nY = newPos.getY() + 1;

			if(player.isSneaking()) {
				nY = player.posY;
			}

			// Get Old Rotation
			float rY = player.rotationYaw;
			float rP = player.rotationPitch;

			// Teleport
			if(player instanceof EntityPlayerMP) {
				// Its a MP player

				if(player.getRidingEntity() == null) {
					
					((EntityPlayerMP) player).connection.setPlayerLocation(nX,nY,nZ, rY, rP);

					if(player.isSprinting()) {
						player.motionX *= 5;
						player.motionZ *= 5;
					}

					player.velocityChanged = true;
				} else {
					Entity riding = player.getRidingEntity();

					if(riding instanceof EntityPoint) {
						return ActionResult.newResult(EnumActionResult.PASS, stack);
					}

					riding.setPositionAndUpdate(nX, nY+0.01f, nZ);
					riding.velocityChanged = true;
				}

				player.worldObj.playSound(player, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1.5f, (float) (1f + Math.random()*0.1));
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	// Warning: Forge Method
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if(player.worldObj.getGameRules().getBoolean("disableTCTeleporter")) {
			return false;
		}

		TaleCraft.logger.info("Mounting: " + entity);
		player.startRiding(entity);
		player.velocityChanged = true;
		entity.velocityChanged = true;

		// by returning TRUE, we prevent damaging the entity being hit.
		return true;
	}

	public Vec3d getPositionEyes(float partialTicks, EntityPlayer player) {
		if(partialTicks == 1.0F) {
			return new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		} else {
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * partialTicks + player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
			return new Vec3d(d0, d1, d2);
		}
	}

}