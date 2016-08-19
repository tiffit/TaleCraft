package de.longor.talecraft.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;

public class ClientFadeEffect {

	private int color;
	private int time;
	private int current_time;
	
	public ClientFadeEffect(int color, int time){
		this.color = color;
		time *=20;
		this.time = time;
		this.current_time = time;
		Minecraft.getMinecraft();
	}
	
	public void render(){
		double fade = 1.0f;
		
		if((current_time + 0.0)/(time + 0.0) < 0.25){
			fade = (current_time + 0.0)/(time - current_time);
		}
		
		// Draw Overlay
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(0, 2, 2, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		{
			int alpha = MathHelper.clamp_int((int) (fade * 255), 0, 255);
			int mixed = ((alpha & 0xFF) << 24) | (color);
			Gui.drawRect(-1, -1, 4, 4, mixed);
			
			// TODO: Implement TEXTURED fade effect
			// -> Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 0, 0, 0, 0);
		}
		RenderHelper.disableStandardItemLighting();
		current_time--;
		if(current_time <= 0){
			ClientRenderer.fadeEffect = null;
		}
	}
	
}
