package talecraft.client.render.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.IRenderHandler;
import talecraft.TaleCraft;
import talecraft.client.ClientRenderer;
import talecraft.client.environment.Environments;
import talecraft.client.render.RenderModeHelper;

public class CustomSkyRenderer extends IRenderHandler {
	public static final CustomSkyRenderer instance = new CustomSkyRenderer();
	private boolean useDebugSky = false;

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		final ClientRenderer instance = TaleCraft.asClient().getRenderer();
		
		if(useDebugSky) {
			renderDebugSky(partialTicks, world, mc);
			
			// handle currently active VisualMode
			if(mc.player != null) {
				RenderModeHelper.ENABLE(instance.getVisualizationMode());
			}
			
			return;
		}
		
		if(Environments.isNonDefault()) {
			Environments.render_sky(instance, partialTicks, world, mc);
		}
		
		// handle currently active VisualMode
		if(mc.player != null) {
			RenderModeHelper.ENABLE(instance.getVisualizationMode());
		}
	}

	private void renderDebugSky(float partialTicks, WorldClient world, Minecraft mc) {
		GlStateManager.pushAttrib();
		GlStateManager.disableCull();
		GlStateManager.disableDepth();
		GlStateManager.disableFog();
		GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		final float B = 8;

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder ren = tess.getBuffer();

		ren.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		ren.setTranslation(0, 1, 0);
		ren.color(0, 0, 0, 255);

		// TOP
		ren.pos(+B, +B, -B);
		ren.pos(+B, +B, +B);
		ren.pos(-B, +B, +B);
		ren.pos(-B, +B, -B);
		// BOTTOM
		ren.pos(+B, -B, -B);
		ren.pos(+B, -B, +B);
		ren.pos(-B, -B, +B);
		ren.pos(-B, -B, -B);
		// ??? x
		ren.pos(-B, +B, -B);
		ren.pos(-B, +B, +B);
		ren.pos(-B, -B, +B);
		ren.pos(-B, -B, -B);
		// ??? x
		ren.pos(+B, +B, +B);
		ren.pos(+B, +B, -B);
		ren.pos(+B, -B, -B);
		ren.pos(+B, -B, +B);
		// ??? z
		ren.pos(+B, +B, -B);
		ren.pos(-B, +B, -B);
		ren.pos(-B, -B, -B);
		ren.pos(+B, -B, -B);
		// ??? z
		ren.pos(-B, +B, +B);
		ren.pos(+B, +B, +B);
		ren.pos(+B, -B, +B);
		ren.pos(-B, -B, +B);
		// end
		tess.draw();
		ren.setTranslation(0, 0, 0);

		GlStateManager.enableCull();
		GlStateManager.enableDepth();
		GlStateManager.popAttrib();
	}

	public void setDebugSky(boolean b) {
		useDebugSky = b;
	}

}
