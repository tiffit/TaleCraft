package talecraft.items;

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
import talecraft.util.BlockRegion;
import talecraft.util.MutableBlockPos;
import talecraft.util.UndoRegion;
import talecraft.util.UndoTask;

public class EraserItem extends TCItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return EnumActionResult.PASS;
		
		// Cant prevent double calls? Welp, time for ugly hacks!
		if(isDoubleCall(stack, world.getTotalWorldTime())) {
			return EnumActionResult.PASS;
		}
		
		if(player.isSneaking()) {
			IBlockState mask = world.getBlockState(pos);
			
			if(applyEraser(world, player, mask))
				return EnumActionResult.SUCCESS;
			else
				return EnumActionResult.SUCCESS;
		} else {
			return onItemRightClick(world, player, hand).getType();
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		
		if(isDoubleCall(stack, world.getTotalWorldTime())) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
		
		if(!applyEraser(world, player, null)) {
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
	public static final boolean applyEraser(World world, EntityPlayer player, IBlockState mask) {
		// Note: the bounds are already sorted
		int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);

		if(bounds == null) {
			// Woopsy!
			if(player instanceof EntityPlayerMP) {
				String msg = TextFormatting.RED + "ERROR: NO BLOCKS SELECTED.";
				((EntityPlayerMP)player).sendMessage(new TextComponentString(msg));
			}
			return false;
		}

		final int maxvolume = 64*64*64;
		final int volume = (bounds[3]-bounds[0]+1) * (bounds[4]-bounds[1]+1) * (bounds[5]-bounds[2]+1);

		if(volume >= maxvolume) {
			// HELL NO!
			if(player instanceof EntityPlayerMP) {
				String msg = TextFormatting.RED + "ERROR: TOO MANY BLOCKS TO ERASE -> " + volume + " >= " + maxvolume;
				((EntityPlayerMP)player).sendMessage(new TextComponentString(msg));
			}
			return false;
		}
		UndoRegion before = new UndoRegion(new BlockRegion(bounds), world);
		applyEraser(world, mask, bounds);
		UndoRegion after = new UndoRegion(new BlockRegion(bounds), world);
		UndoTask.TASKS.add(new UndoTask(before, after, "Eraser", player.getName()));
		return true;
	}
	
	public static final void applyEraser(World world, IBlockState mask, int[] bounds) {
		int ix=bounds[0],iy=bounds[1],iz=bounds[2],ax=bounds[3],ay=bounds[4],az=bounds[5];
		
		IBlockState air = Blocks.AIR.getDefaultState();
		MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
		
		if(mask == null) {
			for(int y = iy; y <= ay; y++) {
				for(int z = iz; z <= az; z++) {
					for(int x = ix; x <= ax; x++) {
						pos.set(x, y, z);
						world.setBlockState(new BlockPos(pos), air);
					}
				}
			}
		} else {
			for(int y = iy; y <= ay; y++) {
				for(int z = iz; z <= az; z++) {
					for(int x = ix; x <= ax; x++) {
						pos.set(x, y, z);
						if(world.getBlockState(pos).equals(mask))
							world.setBlockState(new BlockPos(pos), air);
					}
				}
			}
		}
	}

}
