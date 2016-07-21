package de.longor.talecraft.client.render.temporables;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.render.ITemporaryRenderable;
import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;

public class BlockPosTemporable implements ITemporaryRenderable {
	public int[] pos;
	public long deletionTimepoint;
	public int color;

	public BlockPosTemporable() {

	}

	@Override
	public boolean canRemove() {
		return System.currentTimeMillis() >= deletionTimepoint;
	}

	@Override
	public void render(Minecraft mc, ClientProxy clientProxy,
			Tessellator tessellator, VertexBuffer vertexbuffer,
			double partialTicks) {

		GlStateManager.enableBlend();
		GlStateManager.color(1f, 1f, 1f, 0.5f);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		float x = pos[0];
		float y = pos[1];
		float z = pos[2];

		float r = ((color >> 16) & 0xFF) / 256f;
		float g = ((color >> 8 ) & 0xFF) / 256f;
		float b = ((color      ) & 0xFF) / 256f;
		float a = .5f;

		Minecraft.getMinecraft().renderEngine.bindTexture(ClientResources.texColorWhite);
		BoxRenderer.renderBox(tessellator, vertexbuffer, x, y, z, x+1, y+1, z+1, r, g, b, a);
	}

}
