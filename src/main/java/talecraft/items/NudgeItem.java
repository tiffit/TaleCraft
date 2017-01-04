package talecraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class NudgeItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)
			return EnumActionResult.PASS;
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		nudge(player, player.isSneaking());

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	private void nudge(EntityPlayer player, boolean moveByBounds) {
		if(player == null)
			return;

		// Get WORLD
		World world = player.world;

		if(world.isRemote)
			return;

		// Get SIDE
		EnumFacing side = null;

		if(player.rotationPitch > 45) {
			side = EnumFacing.DOWN;
		} else if(player.rotationPitch < -45) {
			side = EnumFacing.UP;
		} else {
			side = player.getHorizontalFacing();
		}

		// If SNEAKING do INVERT FACING
		if(player.isSneaking()) {
			side = side.getOpposite();
		}

		// Get BOUNDS
		int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);

		if(bounds == null) {
			player.sendMessage(new TextComponentString(TextFormatting.RED+"Bounds invalid."));
			return;
		}

		long bounds_volume = WandItem.getBoundsVolume(bounds);

		if(bounds_volume > (32*32*32)) {
			player.sendMessage(new TextComponentString(TextFormatting.RED+"Selection is too big: " + bounds_volume));
			return;
		}

		// player.sendMessage(new ChatComponentText(TextFormatting.AQUA+"Nudge: " + direction + " " + bounds_volume));
		
		// Selection Bounds
		int ix = bounds[0];
		int iy = bounds[1];
		int iz = bounds[2];
		int ax = bounds[3];
		int ay = bounds[4];
		int az = bounds[5];
		
		// Selection Size
		int sx = ax-ix+1;
		int sy = ay-iy+1;
		int sz = az-iz+1;
		
		// Selection Movement
		int new_ix = ix;
		int new_iy = iy;
		int new_iz = iz;
		
		if(moveByBounds) {
			switch(side) {
				// x
				case EAST:	new_ix +=-sx; break;
				case WEST:	new_ix -=-sx; break;
				// y
				case UP:	  new_iy +=-sy; break;
				case DOWN:	new_iy -=-sy; break;
				// z
				case SOUTH:	new_iz +=-sz; break;
				case NORTH:	new_iz -=-sz; break;
				// ?!
				default: return;
			}
		} else {
			switch(side) {
				// x
				case EAST:	new_ix++; break;
				case WEST:	new_ix--; break;
				// y
				case UP:	  new_iy++; break;
				case DOWN:	new_iy--; break;
				// z
				case SOUTH:	new_iz++; break;
				case NORTH:	new_iz--; break;
				// ?!
				default: return;
			}
		}
		
		int moveX = new_ix - ix;
		int moveY = new_iy - iy;
		int moveZ = new_iz - iz;
		
		// Lets by terribly lazy and use an existing minecraft command to do the actual movement.
		
		StringBuilder builder = new StringBuilder(128);

		builder.append("/clone ");

		builder.append(ix).append(' ');
		builder.append(iy).append(' ');
		builder.append(iz).append(' ');

		builder.append(ax).append(' ');
		builder.append(ay).append(' ');
		builder.append(az).append(' ');

		builder.append(new_ix).append(' ');
		builder.append(new_iy).append(' ');
		builder.append(new_iz).append(' ');

		builder.append("replace");
		builder.append(' ');
		builder.append("move");

		// player.sendMessage(new ChatComponentText(EnumChatFormatting.GOLD+builder.toString()));
		FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, builder.toString());

		ix += moveX;
		iy += moveY;
		iz += moveZ;

		ax += moveX;
		ay += moveY;
		az += moveZ;

		WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
	}

}
