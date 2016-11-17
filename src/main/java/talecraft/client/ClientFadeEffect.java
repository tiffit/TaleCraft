package talecraft.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

public class ClientFadeEffect {

	private final ResourceLocation tex;
	private final int color;
	private final int time;
	private int current_time;
	
	public ClientFadeEffect(int color, int time, String texture){
		this.color = color;
		this.time = time;
		this.current_time = time;
		this.tex = new ResourceLocation(texture);
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
			if(tex != null) {
				GlStateManager.enableTexture2D();
				Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
			}
			
			// just in case
			GlStateManager.disableCull();
			
			float redF   = (float)((color>>16) & 0xFF) / 255f;
			float blueF  = (float)((color>>8) & 0xFF) / 255f;
			float greenF = (float)((color) & 0xFF) / 255f;
			float alphaF = (float) fade;
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(redF, greenF, blueF, alphaF);
				GL11.glTexCoord2f(0, 0); GL11.glVertex2f(0, 0);
				GL11.glTexCoord2f(1, 0); GL11.glVertex2f(2, 0);
				GL11.glTexCoord2f(1, 1); GL11.glVertex2f(2, 2);
				GL11.glTexCoord2f(0, 1); GL11.glVertex2f(0, 2);
			GL11.glEnd();
		}
		RenderHelper.disableStandardItemLighting();
		current_time--;
		if(current_time <= 0){
			ClientRenderer.fadeEffect = null;
		}
	}
	
}
