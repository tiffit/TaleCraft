package talecraft.client;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import talecraft.Reference;
import talecraft.client.ClientRenderer.VisualMode;
import talecraft.proxy.ClientProxy;

public class InfoBar {
	private StringBuilder builder = new StringBuilder(255);
	private boolean enabled = true;
	private int lastHeight = 0;

	public void display(Minecraft mc, WorldClient theWorld, ClientProxy clientProxy) {
		if(!ClientProxy.settings.getBoolean("client.infobar.enabled")) {
			lastHeight = 0;
			return;
		}
		
		if(!enabled) {
			lastHeight = 0;
			return;
		}

		// begin building string
		builder.setLength(0);
		writeModVersionInfo();

		if(mc.player.inventory.getCurrentItem() != null && ClientProxy.settings.getBoolean("client.infobar.heldItemInfo")) {
			writeHeldItemInfo(mc.player.inventory.getCurrentItem());
		}

		if(mc.objectMouseOver != null && ClientProxy.settings.getBoolean("client.infobar.movingObjectPosition")) {
			writeRayTraceResultPositionInfo(mc, theWorld, mc.objectMouseOver);
		}

		if(ClientProxy.settings.getBoolean("client.infobar.visualizationMode")) {
			if(clientProxy.getRenderer().getVisualizationMode() != VisualMode.Default) {
				writeVisualizationModeInfo(clientProxy.getRenderer().getVisualizationMode());
			}
		}

		if(ClientProxy.settings.getBoolean("client.infobar.showFPS")) {
			builder.append(' ');
			builder.append(Minecraft.getDebugFPS());
			builder.append(" FPS");
		}

		if(ClientProxy.settings.getBoolean("client.infobar.showRenderables")) {
			builder.append(" [");
			builder.append(clientProxy.getRenderer().getStaticCount());
			builder.append(", ");
			builder.append(clientProxy.getRenderer().getTemporablesCount());
			builder.append("]");
		}

		// Finally, draw the whole thing!
		Gui.drawRect(0, 0, mc.displayWidth, mc.fontRenderer.FONT_HEIGHT+1, 0xAA000000);
		mc.fontRenderer.drawString(builder.toString(), 1, 1, 14737632);
		lastHeight = mc.fontRenderer.FONT_HEIGHT+1;

		if(mc.player != null && mc.player.getEntityData().hasKey("tcWand") && ClientProxy.settings.getBoolean("client.infobar.showWandInfo")) {
			NBTTagCompound tcWand = mc.player.getEntityData().getCompoundTag("tcWand");

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

				builder.append(TextFormatting.DARK_GRAY);
				builder.append(Arrays.toString(a));
				builder.append(TextFormatting.GRAY);
				builder.append(" | ");
				builder.append(TextFormatting.WHITE);
				builder.append(Arrays.toString(b));
				builder.append(TextFormatting.GRAY);
				builder.append(" -> ");
				builder.append(TextFormatting.YELLOW);
				builder.append(Arrays.toString(vol));
				builder.append(TextFormatting.GRAY);
				builder.append(" = ");
				builder.append(TextFormatting.BLUE);
				builder.append(volume);
				builder.append(TextFormatting.RESET);
			}

			Gui.drawRect(0, 10, mc.displayWidth, mc.fontRenderer.FONT_HEIGHT+11, 0xAA000000);
			mc.fontRenderer.drawString(builder.toString(), 1, 11, 14737632);
			lastHeight += mc.fontRenderer.FONT_HEIGHT+1;
		}
	}

	private void writeModVersionInfo() {
		builder.append(TextFormatting.YELLOW);
		builder.append("TaleCraft ");
		builder.append(TextFormatting.RESET);
		builder.append(Reference.MOD_VERSION);
	}

	private void writeHeldItemInfo(ItemStack item) {
		builder.append(' ');
		builder.append(TextFormatting.ITALIC);

		if(Minecraft.getMinecraft().player.isSneaking())
			builder.append(item);
		else
			builder.append(item.getDisplayName());

		builder.append(TextFormatting.RESET);
	}

	private void writeVisualizationModeInfo(VisualMode visualMode) {
		builder.append(' ');
		builder.append(TextFormatting.BLUE);

		builder.append('[');
		builder.append(visualMode.ordinal());
		builder.append(':');
		builder.append(visualMode.getName());
		builder.append(']');
		
		builder.append(TextFormatting.RESET);
	}

	private void writeRayTraceResultPositionInfo(Minecraft mc, WorldClient theWorld, RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK && result.getBlockPos() != null) {
			builder.append(TextFormatting.GREEN);
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
				IBlockState state = mc.world.getBlockState(lookAt);

				if(b && state.getBlock().getRegistryName() != null) {
					ResourceLocation identifier = state.getBlock().getRegistryName();
					builder.append(identifier.getResourceDomain()).append(":").append(identifier.getResourcePath()).append("/").append(state.getBlock().getMetaFromState(state));
				} else {
					if(state == null) {
						builder.append("NULL STATE");
					} else if(state.getBlock() == null) {
						builder.append("INVALID STATE");
					} else if(Item.getItemFromBlock(state.getBlock()) == null) {
						String name = state.getBlock().getLocalizedName();
						if(name == null)
							builder.append("NO ITEM");
						else
							builder.append(name);
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
			builder.append(TextFormatting.RESET);
		}
		else if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null)
		{
			builder.append(TextFormatting.RED);
			Entity ent = result.entityHit;
			builder.append(' ');
			builder.append('[');
			builder.append(TextFormatting.getTextWithoutFormattingCodes(ent.getName()));
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
			builder.append(TextFormatting.RESET);
		}

		// Look Direction
		if(ClientProxy.settings.getBoolean("client.infobar.showLookDirectionInfo"))
		{
			EntityPlayer playerIn = mc.player;

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
			case EAST:	builder.append(TextFormatting.RED).append("+x"); break;
			case WEST:	builder.append(TextFormatting.DARK_RED).append("-x"); break;
			case UP:	builder.append(TextFormatting.GREEN).append("+y"); break;
			case DOWN:	builder.append(TextFormatting.DARK_GREEN).append("-y"); break;
			case SOUTH:	builder.append(TextFormatting.BLUE).append("+z"); break;
			case NORTH:	builder.append(TextFormatting.DARK_AQUA).append("-z"); break;
			}

			builder.append(TextFormatting.RESET).append(' ');

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
			builder.append((int)MathHelper.wrapDegrees(playerIn.rotationYaw));
			
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
