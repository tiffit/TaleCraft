package talecraft.client.render.temporables;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import talecraft.client.ClientResources;
import talecraft.client.render.ITemporaryRenderable;
import talecraft.client.render.renderers.BoxRenderer;
import talecraft.proxy.ClientProxy;

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
			Tessellator tessellator, BufferBuilder vertexbuffer,
			double partialTicks) {
		float minX = box[0];
		float minY = box[1];
		float minZ = box[2];
		float maxX = box[3];
		float maxY = box[4];
		float maxZ = box[5];

		float r = ((color >> 16) & 0xFF) / 256f;
		float g = ((color >> 8) & 0xFF) / 256f;
		float b = (color & 0xFF) / 256f;
		float a = .25f;

		GlStateManager.enableBlend();
		GlStateManager.enableCull();
		GlStateManager.color(1f, 1f, 1f, 0.5f);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft.getMinecraft().renderEngine.bindTexture(ClientResources.texColorWhite);
		BoxRenderer.renderBox(tessellator, vertexbuffer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
	}

}
