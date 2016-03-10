package de.longor.talecraft.client.render.entity;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

public class PointEntityRenderer extends Render {
	
	public PointEntityRenderer(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return ClientResources.texColorOrange;
	}
	
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
        if(!TaleCraft.proxy.asClient().isBuildMode())
        	return;
    	
        GL11.glPushMatrix();
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float r=1,g=1,b=1,a=1,yeoffset=entity.getEyeHeight();
		
        bindTexture(ClientResources.texColorWhite);
		
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		
		if(ClientProxy.settings.getBoolean("client.render.entity.point.fancy")) {
			GL11.glPushMatrix();
			GL11.glTranslated(x, y+yeoffset, z);
	    	GL11.glRotatef(entity.rotationYaw, 0, 1, 0);
	    	GL11.glRotatef(entity.rotationPitch, 1, 0, 0);
			
	    	GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
			GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
			
			for(int i = 0; i < 2; i++) {
				float E = (i + 1) / 16f;
				GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
				BoxRenderer.renderBox(tessellator, worldrenderer, -E, -E, -E, +E, +E, +E, 1, 1, 1, a);
				
				E *= 0.3f;
				GlStateManager.blendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_ALPHA);
				BoxRenderer.renderBox(tessellator, worldrenderer, -E, -E, -E, +E, +E, +E, 0, 0, 0, a);
			}
			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			GL11.glTranslated(x, y+yeoffset, z);
	    	GL11.glRotatef(entity.rotationYaw, 0, 1, 0);
	    	GL11.glRotatef(entity.rotationPitch, 1, 0, 0);
			float E = 1f / 2f;
			GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
			BoxRenderer.renderBox(tessellator, worldrenderer, -E, -E, -E, +E, +E, +E, 0, 0, 0, a);
			GL11.glPopMatrix();
		}
		
		GlStateManager.disableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		boolean shouldDrawName = (x*x+y*y+z*z) < 128;
		
        final String TEXT = entity.getName(); // tile.getStateAsString();
        if(TEXT != null && shouldDrawName) {
        	GL11.glPushMatrix();
        	GL11.glTranslated(x, y+yeoffset, z);
        	FontRenderer fntrnd = TaleCraft.proxy.asClient().mc.fontRendererObj;
        	final int TEXT_W = fntrnd.getStringWidth(TEXT);
        	final float HEX = 1f / 32f;
        	GlStateManager.translate(0, .75f, 0);
        	GlStateManager.rotate(180, 1, 0, 0);
        	GlStateManager.scale(HEX, HEX, HEX);
        	GlStateManager.rotate((float)(Minecraft.getMinecraft().thePlayer.rotationYawHead + 180), 0, 1, 0);
        	fntrnd.drawString(TEXT, -TEXT_W/2, 0, 0xFFFFFFFF);
        	GL11.glPopMatrix();
        }
		
        GL11.glPopMatrix();
        
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        RenderHelper.enableStandardItemLighting();
        bindTexture(ClientResources.texColorWhite); // this shouldn't be necessary?
    }
	
}
