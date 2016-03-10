package de.longor.talecraft.client.render.renderables;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.render.IRenderable;
import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.proxy.ClientProxy;

public class SelectionBoxRenderer implements IRenderable {

	@Override
	public void render(Minecraft mc, ClientProxy clientProxy,
			Tessellator tessellator, WorldRenderer worldrenderer,
			double partialTicks) {
		
		// Don't show the selection if we are not in BuildMode!
		if(!ClientProxy.isInBuildMode()) return;
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
		
		// Wand Selection Rendering
		NBTTagCompound playerData = mc.thePlayer.getEntityData();
		if(playerData.hasKey("tcWand")) {
			NBTTagCompound tcWand = playerData.getCompoundTag("tcWand");
			
			if(tcWand.hasKey("cursor")) {
				final float E = -1f / 64f;
				int[] cursor = tcWand.getIntArray("cursor");
				
				GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_POINT);
				GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
				GlStateManager.enableTexture2D();
				mc.getTextureManager().bindTexture(ClientResources.texColorWhite);
				BoxRenderer.renderBox(tessellator, worldrenderer, cursor[0]-E, cursor[1]-E, cursor[2]-E, cursor[0]+1+E, cursor[1]+1+E, cursor[2]+1+E, 1f,1f,1f,1f);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}
			
			// If not null, render the cursor selections boundaries.
			if(tcWand.hasKey("boundsA") && tcWand.hasKey("boundsB")) {
				// get bounds
				int[] a = tcWand.getIntArray("boundsA");
				int[] b = tcWand.getIntArray("boundsB");
				
				// make sure its correctly sorted
				int ix = Math.min(a[0], b[0]);
				int iy = Math.min(a[1], b[1]);
				int iz = Math.min(a[2], b[2]);
				int ax = Math.max(a[0], b[0]);
				int ay = Math.max(a[1], b[1]);
				int az = Math.max(a[2], b[2]);
				
				// 'error' offset
				final float E = 1f / 32f;
				
				// Prepare state
				GlStateManager.disableNormalize();
				GlStateManager.enableTexture2D();
				RenderHelper.disableStandardItemLighting();
				GL11.glLineWidth(2.5f);
				
				ResourceLocation texture = null;
				
				if(!TaleCraft.proxy.asClient().settings.getBoolean("client.render.useAlternateSelectionTexture")) {
					texture = ClientResources.textureSelectionBoxWS;
				} else {
					texture = ClientResources.textureSelectionBoxFF;
				}
				
				mc.getTextureManager().bindTexture(texture);
				
				// Render primary (with-depth) box
				BoxRenderer.renderSelectionBox(tessellator, worldrenderer, ix-E, iy-E, iz-E, ax+1+E, ay+1+E, az+1+E, 1);
				
				// Render secondary (no-depth) box
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				GlStateManager.disableDepth();
				mc.getTextureManager().bindTexture(ClientResources.texColorWhite);
				BoxRenderer.renderSelectionBox(tessellator, worldrenderer, ix-E, iy-E, iz-E, ax+1+E, ay+1+E, az+1+E, -1);
				GlStateManager.enableDepth();
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				
				mc.getTextureManager().bindTexture(ClientResources.texColorWhite);
				GL11.glBegin(GL11.GL_LINES);
				GL11.glColor4f(1, 0, 0, 1);
				GL11.glVertex3f(ix-E, iy-E, iz-E);
				GL11.glVertex3f(ax+1+E, iy-E, iz-E);
				GL11.glColor4f(0, 1, 0, 1);
				GL11.glVertex3f(ix-E, iy-E, iz-E);
				GL11.glVertex3f(ix-E, ay+1+E, iz-E);
				GL11.glColor4f(0, 0, 1, 1);
				GL11.glVertex3f(ix-E, iy-E, iz-E);
				GL11.glVertex3f(ix-E, iy-E, az+1+E);
				GL11.glEnd();
				
				GL11.glLineWidth(1.0f);
			}
		}
		
		GlStateManager.disableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	
	
}
