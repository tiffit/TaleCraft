package de.longor.talecraft.items;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.voxelbrush_old.VoxelBrush;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoxelBrushItem extends TCItem {

	@Override
	public ItemState getItemState() {
		return ItemState.UNFINISHED;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(world.isRemote)
			return;

		if(world.getGameRules().getBoolean("disableTCVoxelBrush")) {
			return;
		}

		NBTTagCompound stackCompound = stack.getTagCompound();

		if(stackCompound == null) {
			stackCompound = new NBTTagCompound();
			stack.setTagCompound(stackCompound);
		}

		// Automatic initialization of the VoxelBrush.
		if(!stackCompound.hasKey("vbData")) {
			TaleCraft.logger.info("Auto-Initializing VoxelBrush: " + stack);

			NBTTagCompound vbData = new NBTTagCompound();
			stackCompound.setTag("vbData", vbData);

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

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if(!player.capabilities.isCreativeMode)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if(!player.capabilities.allowEdit)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if(!stack.hasTagCompound())
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		NBTTagCompound vbData = stack.getTagCompound().getCompoundTag("vbData");

		if(vbData.hasNoTags())
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		float lerp = 1F;
		float dist = 256;

		Vec3d start = this.getPositionEyes(lerp, player);
		Vec3d direction = player.getLook(lerp);
		Vec3d end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);

		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);

		if(result == null)
			return ActionResult.newResult(EnumActionResult.PASS, stack);;

			VoxelBrush.func(world, vbData, result.getBlockPos());

			return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if(!stack.hasTagCompound()){
			super.addInformation(stack, player, tooltip, advanced);
			return;
		}

		NBTTagCompound vbData = stack.getTagCompound().getCompoundTag("vbData");

		if(vbData.hasNoTags())
			return;

		VoxelBrush.func(vbData, tooltip);

		super.addInformation(stack, player, tooltip, advanced);
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
