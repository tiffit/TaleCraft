package talecraft.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import talecraft.client.ClientRenderer.VisualMode;

public class RenderModeHelper {

	/**
	 * warning: this method will fuck up the rendering pipeline.
	 * only use this in the terrain render phase.
	 **/
	public static final void ENABLE(VisualMode visual) {
		switch(visual) {
		case Lighting:
			GL11.glPointSize(8.0f);
			GL11.glLineWidth(0.25f);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			break;
		case Nightvision:
			GL11.glPointSize(8.0f);
			GL11.glLineWidth(0.25f);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
			GlStateManager.disableFog();
			Minecraft.getMinecraft().player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, Integer.MAX_VALUE));
			break;
		case Wireframe:
			GL11.glLineWidth(0.25f);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
			GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_POINT);
			break;
		default:
			break;
		
		}
	}

	/**
	 * This method resets the states the wireframe-mode fucked up
	 **/
	public static void DISABLE() {
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glLineWidth(1.0f);
		GL11.glPointSize(1.0f);
		RenderHelper.enableStandardItemLighting();
	}

}
