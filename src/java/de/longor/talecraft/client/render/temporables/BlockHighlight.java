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
import net.minecraft.util.math.BlockPos;

public class BlockHighlight implements ITemporaryRenderable {
	private BlockPos position;
	private long dietime;

	public BlockHighlight(BlockPos pos, double duration) {
		position = pos;
		dietime = System.currentTimeMillis() + (long)(duration * 1000d);
	}

	@Override
	public void render(Minecraft mc, ClientProxy clientProxy,
			Tessellator tessellator, VertexBuffer vertexbuffer,
			double partialTicks) {

		float minX = position.getX()+1;
		float minY = position.getY()+1;
		float minZ = position.getZ()+1;
		float maxX = position.getX();
		float maxY = position.getY();
		float maxZ = position.getZ();

		int color = 0xFF7F00;
		float r = ((color >> 16) & 0xFF) / 256f;
		float g = ((color >> 8) & 0xFF) / 256f;
		float b = (color & 0xFF) / 256f;
		float a = .75f;

		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.enableCull();
		GlStateManager.color(1f, 1f, 1f, 0.5f);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft.getMinecraft().renderEngine.bindTexture(ClientResources.texColorWhite);
		BoxRenderer.renderBox(tessellator, vertexbuffer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
		GlStateManager.enableDepth();
	}

	@Override
	public boolean canRemove() {
		return System.currentTimeMillis() > dietime;
	}

}
