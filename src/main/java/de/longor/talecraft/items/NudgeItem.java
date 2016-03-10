package de.longor.talecraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.world.World;

public class NudgeItem extends TCItem {
	
    public boolean onItemUse(
    		ItemStack stack, EntityPlayer playerIn, World worldIn,
    		BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ
    ) {
    	if(worldIn.isRemote)
    		return true;
    	
    	nudge(playerIn);
    	
    	return true;
    }
	
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if(worldIn.isRemote)
    		return itemStackIn;
    	
    	nudge(playerIn);
    	
		return itemStackIn;
    }
    
	private void nudge(EntityPlayer playerIn) {
		if(playerIn == null)
			return;
		
		// Get WORLD
		World world = playerIn.worldObj;
		
		if(world.isRemote)
			return;
		
		// Get FACING
		EnumFacing direction = null;
    	
		if(playerIn.rotationPitch > 45) {
			direction = EnumFacing.DOWN;
		} else if(playerIn.rotationPitch < -45) {
			direction = EnumFacing.UP;
		} else {
			direction = playerIn.getHorizontalFacing();
		}
		
		// If SNEAKING do INVERT FACING
		if(playerIn.isSneaking()) {
			direction = direction.getOpposite();
		}
		
		// Get BOUNDS
		int[] bounds = WandItem.getBoundsFromPLAYERorNULL(playerIn);
		
		if(bounds == null) {
			playerIn.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Bounds invalid."));
			return;
		}
		
		long bounds_volume = WandItem.getBoundsVolume(bounds);
		
		if(bounds_volume > (32*32*32)) {
			playerIn.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Selection is too big: " + bounds_volume));
			return;
		}
		
		// playerIn.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA+"Nudge: " + direction + " " + bounds_volume));
		
		int ix = bounds[0];
		int iy = bounds[1];
		int iz = bounds[2];
		int ax = bounds[3];
		int ay = bounds[4];
		int az = bounds[5];
		
		int new_ix = ix;
		int new_iy = iy;
		int new_iz = iz;
		
		switch(direction) {
			// x
			case EAST:	new_ix++; break;
			case WEST:	new_ix--; break;
			// y
			case UP:	new_iy++; break;
			case DOWN:	new_iy--; break;
			// z
			case SOUTH:	new_iz++; break;
			case NORTH:	new_iz--; break;
			// ?!
			default: return;
		}
		
		int moveX = new_ix - ix;
		int moveY = new_iy - iy;
		int moveZ = new_iz - iz;
		
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
		
		// playerIn.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD+builder.toString()));
		MinecraftServer.getServer().getCommandManager().executeCommand(playerIn, builder.toString());
		
		ix += moveX;
		iy += moveY;
		iz += moveZ;
		
		ax += moveX;
		ay += moveY;
		az += moveZ;
		
		WandItem.setBounds(playerIn, ix, iy, iz, ax, ay, az);
	}
	
}
