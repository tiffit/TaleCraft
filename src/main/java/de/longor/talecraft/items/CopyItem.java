package de.longor.talecraft.items;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.server.ServerClipboard;
import de.longor.talecraft.server.ServerHandler;
import de.longor.talecraft.server.ServerMirror;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class CopyItem extends TCItem {
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(worldIn.isRemote) {
    		rightClickClient(stack, worldIn, playerIn);
    	} else {
    		rightClickServer(stack, worldIn, playerIn);
    		worldIn.markBlockForUpdate(pos);
    	}
    	
    	return true;
    }
	
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    	if(worldIn.isRemote)
    		rightClickClient(stack, worldIn, playerIn);
    	else
    		rightClickServer(stack, worldIn, playerIn);
    	
    	return stack;
    }
    
	public static void rightClickServer(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		ServerMirror mirror = ServerHandler.getServerMirror(null);
		ServerClipboard clipboard = mirror.getClipboard();
		
		int[] bounds = WandItem.getBoundsFromPLAYERorNULL(playerIn);
		String keyString = "player."+playerIn.getGameProfile().getId().toString();
		
		if(bounds == null)
			return;
		
		ClipboardItem item = ClipboardItem.copyRegion(bounds, worldIn, keyString, (ICommandSender) playerIn);
		
		if(item != null)
			clipboard.put(keyString, item);
	}
	
	public static void rightClickClient(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		int[] bounds = WandItem.getBoundsFromPLAYERorNULL(playerIn);
		
		if(bounds == null)
			return;
		
		ClipboardItem item = ClipboardItem.copyRegion(bounds, worldIn, "player.self", (ICommandSender) playerIn);
		
		if(item != null) {
			TaleCraft.asClient().setClipboard(item);
		}
	}
    
	
	
	
	
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    	World world = player.worldObj;
    	String keyString = "player."+player.getGameProfile().getId().toString();
    	
    	if(world.isRemote)
    		leftClickClient(stack, player, entity, world, keyString);
    	else
    		leftClickServer(stack, player, entity, world, keyString);
    	
    	// by returning TRUE, we prevent damaging the entity being hit.
        return true;
    }
    
	private void leftClickClient(ItemStack stack, EntityPlayer player, Entity entity, World world, String keyString) {
		ClipboardItem item = ClipboardItem.copyEntity(entity.worldObj, entity, keyString);
    	
    	System.out.println("Click Client");
    	
		if(item != null) {
			TaleCraft.asClient().setClipboard(item);
		}
	}
	
	private void leftClickServer(ItemStack stack, EntityPlayer player, Entity entity, World world, String keyString) {
		ServerMirror mirror = ServerHandler.getServerMirror(null);
		ServerClipboard clipboard = mirror.getClipboard();
    	
    	System.out.println("Click Server");
		
		ClipboardItem item = ClipboardItem.copyEntity(entity.worldObj, entity, keyString);
		
		if(item != null) {
			clipboard.put(keyString, item);
		}
	}
	
}
