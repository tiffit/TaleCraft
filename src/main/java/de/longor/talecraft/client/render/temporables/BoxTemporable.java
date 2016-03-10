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

public class BoxTemporable implements ITemporaryRenderable {
	public int[] box;
	public long deletionTimepoint;
	public int color;
	
	public BoxTemporable() {
		deletionTimepoint = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
	}
	
	@Override
	public boolean canRemove() {
		return System.currentTimeMillis() >= deletionTimepoint;
	}
	
	@Override
	public void render(Minecraft mc, ClientProxy clientProxy,
			Tessellator tessellator, WorldRenderer worldrenderer,
			double partialTicks) {
		
		float ERROR = 1f / 16f;
		
		float minX = box[0];
		float minY = box[1];
		float minZ = box[2];
		float maxX = box[3];
		float maxY = box[4];
		float maxZ = box[5];
		
		float r = (float) ((color >> 16) & 0xFF) / 256f;
		float g = (float) ((color >> 8) & 0xFF) / 256f;
		float b = (float) (color & 0xFF) / 256f;
		float a = .25f;
		
		GlStateManager.enableBlend();
		GlStateManager.enableCull();
		GlStateManager.color(1f, 1f, 1f, 0.5f);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(ClientResources.texColorWhite);
		BoxRenderer.renderBox(tessellator, worldrenderer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
	}

}
