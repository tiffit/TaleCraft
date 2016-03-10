package de.longor.talecraft.client;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import de.longor.talecraft.Reference;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier;

public class InfoBar {
	private StringBuilder builder = new StringBuilder(255);
	private boolean enabled = true;
	private int lastHeight = 0;
	
	public void display(Minecraft mc, EntityPlayerSP player, WorldClient theWorld, ClientProxy clientProxy) {
		if(!clientProxy.settings.getBoolean("client.infobar.enabled")) {
			lastHeight = 0;
			return;
		}
		
		// begin building string
        builder.setLength(0);
        writeModVersionInfo();
        
        if(mc.thePlayer.inventory.getCurrentItem() != null && clientProxy.settings.getBoolean("client.infobar.heldItemInfo")) {
        	writeHeldItemInfo(mc.thePlayer.inventory.getCurrentItem());
        }
        
        if(mc.objectMouseOver != null && clientProxy.settings.getBoolean("client.infobar.movingObjectPosition")) {
        	writeMovingObjectPositionInfo(mc, theWorld, mc.objectMouseOver);
        }
        
        if(clientProxy.settings.getBoolean("client.infobar.visualizationMode")) {
        	if(clientProxy.getRenderer().getVisualizationMode() != 0) {
        		writeVisualizationModeInfo(clientProxy.getRenderer().getVisualizationMode());
        	}
        }
        
        if(clientProxy.settings.getBoolean("client.infobar.showFPS")) {
        	builder.append(' ');
        	builder.append(mc.getDebugFPS());
        	builder.append(" FPS");
        }
        
        if(clientProxy.settings.getBoolean("client.infobar.showRenderables")) {
            builder.append(" [");
            builder.append(clientProxy.getRenderer().getStaticCount());
        	builder.append(", ");
        	builder.append(clientProxy.getRenderer().getTemporablesCount());
        	builder.append("]");
        }
        
        // Finally, draw the whole thing!
		mc.ingameGUI.drawRect(0, 0, mc.displayWidth, mc.fontRendererObj.FONT_HEIGHT+1, 0xAA000000);
        mc.fontRendererObj.drawString(builder.toString(), 1, 1, 14737632);
        lastHeight = mc.fontRendererObj.FONT_HEIGHT+1;
        
        //*
        if(mc.thePlayer != null && mc.thePlayer.getEntityData().hasKey("tcWand") && clientProxy.settings.getBoolean("client.infobar.showWandInfo")) {
        	NBTTagCompound tcWand = mc.thePlayer.getEntityData().getCompoundTag("tcWand");
        	
        	builder.setLength(0);
        	
        	if(tcWand.hasKey("boundsA") && tcWand.hasKey("boundsB")) {
                builder.append(' ');
                
        		int[] a = tcWand.getIntArray("boundsA");
        		int[] b = tcWand.getIntArray("boundsB");
        		
        		long volX = (Math.abs(b[0]-a[0])+1);
        		long volY = (Math.abs(b[1]-a[1])+1);
        		long volZ = (Math.abs(b[2]-a[2])+1);
        		long[] vol = new long[]{volX,volY,volZ};
        		
        		long volume = volX * volY * volZ;
        		
                builder.append(EnumChatFormatting.DARK_GRAY);
        		builder.append(Arrays.toString(a));
                builder.append(EnumChatFormatting.GRAY);
                builder.append(" | ");
                builder.append(EnumChatFormatting.WHITE);
        		builder.append(Arrays.toString(b));
                builder.append(EnumChatFormatting.GRAY);
                builder.append(" -> ");
                builder.append(EnumChatFormatting.YELLOW);
        		builder.append(Arrays.toString(vol));
                builder.append(EnumChatFormatting.GRAY);
                builder.append(" = ");
                builder.append(EnumChatFormatting.BLUE);
                builder.append(volume);
                builder.append(EnumChatFormatting.RESET);
        	}
        	
    		mc.ingameGUI.drawRect(0, 10, mc.displayWidth, mc.fontRendererObj.FONT_HEIGHT+11, 0xAA000000);
        	mc.fontRendererObj.drawString(builder.toString(), 1, 11, 14737632);
        	lastHeight += mc.fontRendererObj.FONT_HEIGHT+1;
        }
        //*/
	}
	
	private void writeModVersionInfo() {
        builder.append(EnumChatFormatting.YELLOW);
        builder.append("TaleCraft ");
        builder.append(EnumChatFormatting.RESET);
        builder.append(Reference.MOD_VERSION);
	}
	
	private void writeHeldItemInfo(ItemStack item) {
    	builder.append(' ');
        builder.append(EnumChatFormatting.ITALIC);
        
        if(Minecraft.getMinecraft().thePlayer.isSneaking())
        	builder.append(item);
        else
        	builder.append(item.getDisplayName());
        
        builder.append(EnumChatFormatting.RESET);
	}
	
	private void writeVisualizationModeInfo(int wireframeMode) {
        builder.append(' ');
        builder.append(EnumChatFormatting.BLUE);
        
        switch(wireframeMode) {
        	case 1: builder.append("[1:wireframe mode]"); break;
        	case 2: builder.append("[2:backface mode]"); break;
        	case 3: builder.append("[3:lighting mode]"); break;
        	case 4: builder.append("[4:nightvision mode]"); break;
        	default: builder.append("[?:funny display mode]"); break;
        }
        
        builder.append(EnumChatFormatting.RESET);
	}

	private void writeMovingObjectPositionInfo(Minecraft mc, WorldClient theWorld, MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mop.getBlockPos() != null) {
            builder.append(EnumChatFormatting.GREEN);
			BlockPos lookAt = mc.objectMouseOver.getBlockPos();
	        builder.append(' ');
	        builder.append('[');
	        builder.append(lookAt.getX());
	        builder.append(' ');
	        builder.append(lookAt.getY());
	        builder.append(' ');
	        builder.append(lookAt.getZ());
	        
            if (theWorld.isBlockLoaded(lookAt)) {
		        builder.append(' ');
		        builder.append('=');
		        builder.append(' ');
		        
		        boolean b = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		        IBlockState state = mc.theWorld.getBlockState(lookAt);
		        
		        if(b) {
	        		UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(state.getBlock());
	        		builder.append(identifier.modId).append(":").append(identifier.name).append("/").append(state.getBlock().getMetaFromState(state));
		        } else {
		        	if(state == null) {
		        		builder.append("NULL-DATA ERROR");
		        	} else if(state.getBlock() == null) {
		        		builder.append("NULL-DATA ERROR");
		        	} else if(Item.getItemFromBlock(state.getBlock()) == null) {
		        		builder.append("NULL-DATA ERROR");
		        	} else {
		        		Block block = state.getBlock();
		        		int metadata = block.getMetaFromState(state);
		        		ItemStack itemStack = new ItemStack(block, 1, metadata);
		        		String displayName = itemStack.getDisplayName();
		        		builder.append(displayName);
		        	}
		        }
            }
            
	        builder.append(']');
            builder.append(EnumChatFormatting.RESET);
        }
        else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null)
        {
            builder.append(EnumChatFormatting.RED);
        	Entity ent = mop.entityHit;
	        builder.append(' ');
	        builder.append('[');
	        builder.append(ent.getName());
	        builder.append(' ');
	        builder.append('(');
	        builder.append((int)ent.lastTickPosX);
	        builder.append(' ');
	        builder.append((int)ent.lastTickPosY);
	        builder.append(' ');
	        builder.append((int)ent.lastTickPosZ);
	        builder.append(')');
	        
	        if(ent instanceof EntityLivingBase) {
	        	builder.append(' ');
	        	builder.append(((EntityLivingBase)ent).getHealth());
	        }
	        
	        builder.append(']');
            builder.append(EnumChatFormatting.RESET);
        }
        
        // Look Direction
        if(ClientProxy.proxy.settings.getBoolean("client.infobar.showLookDirectionInfo"))
        {
        	EntityPlayer playerIn = mc.thePlayer;
        	
        	EnumFacing directionSky = playerIn.getHorizontalFacing();
        	EnumFacing directionFull = null;
        	// EnumFacing direction = null;
    		
    		if(playerIn.rotationPitch > 45) {
    			directionFull = EnumFacing.DOWN;
    		} else if(playerIn.rotationPitch < -45) {
    			directionFull = EnumFacing.UP;
    		} else {
    			directionFull = playerIn.getHorizontalFacing();
    		}
    		
	        builder.append(" [");
	        
	        switch(directionFull) {
				case EAST:	builder.append(EnumChatFormatting.RED).append("+x"); break;
				case WEST:	builder.append(EnumChatFormatting.DARK_RED).append("-x"); break;
				case UP:	builder.append(EnumChatFormatting.GREEN).append("+y"); break;
				case DOWN:	builder.append(EnumChatFormatting.DARK_GREEN).append("-y"); break;
				case SOUTH:	builder.append(EnumChatFormatting.BLUE).append("+z"); break;
				case NORTH:	builder.append(EnumChatFormatting.DARK_AQUA).append("-z"); break;
	        }
	        
	        builder.append(EnumChatFormatting.RESET).append(' ');
	        
	        switch(directionSky) {
	        	case EAST:	builder.append("E"); break;
	        	case WEST:	builder.append("W"); break;
	        	case SOUTH:	builder.append("S"); break;
	        	case NORTH:	builder.append("N"); break;
	        	default: break;
	        }
	        
	        builder.append(',');
	        builder.append(' ');
    		builder.append((int)playerIn.rotationPitch);
    		builder.append(' ');
	        builder.append((int)MathHelper.wrapAngleTo180_float(playerIn.rotationYaw));
	        
	        builder.append(']');
        }
	}
	
	public boolean canDisplayInfoBar(Minecraft mc, ClientProxy clientProxy) {
		if(!clientProxy.isBuildMode())
			return false;
		
		if(mc.currentScreen == null)
			return true;
		
		if(mc.currentScreen instanceof GuiIngameMenu)
			return true;
		
		if(mc.currentScreen instanceof GuiChat)
			   return true;
		
		return false;
	}
	
	public void setEnabled(boolean boolean1) {
		enabled = boolean1;
	}
	
	public int getLastMaxY() {
		return lastHeight;
	}
}
