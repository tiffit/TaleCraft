package de.longor.talecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class RenderModeHelper {
	
	/**
	 * warning: this method will fuck up the rendering pipeline.
	 * only use this in the terrain render phase.
	 **/
	public static final void ENABLE(int wireframeMode) {
		switch(wireframeMode) {
		case 1: {
			GL11.glLineWidth(0.25f);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
			GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_POINT);
		}break;
		case 2: {
			GL11.glLineWidth(0.25f);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE); GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
		}break;
		case 3: {
			GL11.glPointSize(8.0f);
			GL11.glLineWidth(0.25f);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GL11.glDisable(GL11.GL_LIGHTING);
		}break;
		case 4: {
			GL11.glPointSize(8.0f);
			GL11.glLineWidth(0.25f);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			Minecraft.getMinecraft().thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, Integer.MAX_VALUE));
		}break;
		case 0:default:break;
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
		
		Minecraft.getMinecraft().thePlayer.getActivePotionEffects().clear();
	}
	
}
