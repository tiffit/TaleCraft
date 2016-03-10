package de.longor.talecraft.client.render.temporables;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.render.ITemporaryRenderable;
import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.proxy.ClientProxy;

public class LineToBoxTemporable implements ITemporaryRenderable {
	public int[] src;
	public int[] box;
	public long deletionTimepoint;
	
	@Override
	public boolean canRemove() {
		return System.currentTimeMillis() >= deletionTimepoint;
	}
	
	@Override
	public void render(Minecraft mc, ClientProxy clientProxy,
			Tessellator tessellator, WorldRenderer worldrenderer,
			double partialTicks) {
		
		float timeUntilRemoval = deletionTimepoint - System.currentTimeMillis();
		float fade = timeUntilRemoval / 1000f;
		
		float ERROR = 1f / 16f;
		
		float minX = box[0] - ERROR;
		float minY = box[1] - ERROR;
		float minZ = box[2] - ERROR;
		float maxX = box[3] + 1 + ERROR;
		float maxY = box[4] + 1 + ERROR;
		float maxZ = box[5] + 1 + ERROR;
		
		float r = 1;
		float g = 1;
		float b = 0;
		float a = 0.5f;
		
		float midX = (minX + maxX) / 2f;
		float midY = (minY + maxY) / 2f;
		float midZ = (minZ + maxZ) / 2f;
		
		float x0 = src[0] + 0.5f;
		float y0 = src[1] + 0.5f;
		float z0 = src[2] + 0.5f;
		float x1 = midX;
		float y1 = midY;
		float z1 = midZ;
		
		GlStateManager.enableBlend();
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(ClientResources.texColorOrange);
//		BoxRenderer.renderBox(tessellator, worldrenderer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
		
		BoxRenderer.renderBoxLine(tessellator, worldrenderer, x0, y0, z0, x1, y1, z1, r, g, b, a);
		BoxRenderer.renderWireBoxWithPointAndLines(minX, minY, minZ, maxX, maxY, maxZ, x0, y0, z0, r, g, b, a);
	}

}
