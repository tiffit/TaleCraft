package de.longor.talecraft.items;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EraserItem extends TCItem {
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if(worldIn.isRemote)
    		return true;
    	
		// TaleCraft.logger.info("ITEM ERASER : Block Click -> " + pos);
    	
    	IBlockState state = Blocks.air.getDefaultState();
    	
    	// note: the bounds are already sorted
    	int[] bounds = WandItem.getBoundsFromPLAYERorNULL(playerIn);
    	
    	if(bounds == null) {
    		return true;
    	}
    	
    	final int maxvolume = 64*64*64;
    	final int volume = (bounds[3]-bounds[0]+1) * (bounds[4]-bounds[1]+1) * (bounds[5]-bounds[2]+1);
    	
    	if(volume >= maxvolume) {
    		// HELL NO!
    		if(playerIn instanceof EntityPlayerMP) {
    			String msg = EnumChatFormatting.RED + "ERROR: TOO MANY BLOCKS TO FILL -> " + volume + " >= " + maxvolume;
    			((EntityPlayerMP)playerIn).addChatMessage(new ChatComponentText(msg));
    		}
    		return true;
    	}
    	
    	WorldHelper.fill(worldIn, bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], state);
    	
        return true;
    }
    
}
