package de.longor.talecraft.items;

import de.longor.talecraft.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class EraserItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		onItemRightClick(stack, world, player, hand);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		IBlockState state = Blocks.air.getDefaultState();

		// Note: the bounds are already sorted
		int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);

		if(bounds == null) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}

		final int maxvolume = 64*64*64;
		final int volume = (bounds[3]-bounds[0]+1) * (bounds[4]-bounds[1]+1) * (bounds[5]-bounds[2]+1);

		if(volume >= maxvolume) {
			// HELL NO!
			if(player instanceof EntityPlayerMP) {
				String msg = TextFormatting.RED + "ERROR: TOO MANY BLOCKS TO ERASE -> " + volume + " >= " + maxvolume;
				((EntityPlayerMP)player).addChatMessage(new TextComponentString(msg));
			}
			return ActionResult.newResult(EnumActionResult.FAIL, stack);
		}

		WorldHelper.fill(world, bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], state);

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}
