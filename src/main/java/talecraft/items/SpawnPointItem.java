package talecraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import talecraft.entity.EntityPoint;

public class SpawnPointItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)
			return EnumActionResult.PASS;
		onItemRightClick(world, player, hand);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		float x = (float) player.posX;
		float y = (float) player.posY + player.getEyeHeight();
		float z = (float) player.posZ;

		if(player.isSneaking()) {
			BlockPos pos = player.getPosition();
			x = pos.getX() + 0.5f;
			y = pos.getY() + 0.5f;
			z = pos.getZ() + 0.5f;
		}

		float yaw = player.rotationYaw;
		float pitch = player.rotationPitch;

		EntityPoint pointEntity = new EntityPoint(world);
		pointEntity.setPositionAndRotation(x, y - pointEntity.height/2, z, yaw, pitch);
		world.spawnEntity(pointEntity);

		if(stack.hasDisplayName()) {
			String name = stack.getDisplayName();
			pointEntity.setCustomNameTag(name);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}
