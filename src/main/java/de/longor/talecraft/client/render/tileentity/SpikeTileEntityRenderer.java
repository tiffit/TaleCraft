package de.longor.talecraft.client.render.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tiffit.talecraft.tileentity.SpikeBlockTileEntity;

@SideOnly(Side.CLIENT)
public class SpikeTileEntityRenderer extends TileEntitySpecialRenderer<SpikeBlockTileEntity> {

	
	public SpikeTileEntityRenderer() {
	}

	@Override
	public void renderTileEntityAt(SpikeBlockTileEntity tile, double posX, double posY, double posZ, float partialTicks, int destroyStage) {
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.enableCull();
		GlStateManager.resetColor();

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

		// get tessellator
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		bindTexture(new ResourceLocation("minecraft:textures/blocks/iron_block.png"));
		vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		renderSpikes(vertexbuffer);
		
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.enableCull();
		GlStateManager.enableLighting();


	}	
	
	private void renderSpikes(VertexBuffer buf){
		float height = 0.6f;
		float width = 0.05f;
		
		buf.pos(.9f, 0, .9f).tex(0, 0).endVertex();
		buf.pos(.9f, height, .9f).tex(0, 0).endVertex();
		buf.pos(.9f - width, height, .9f - width).tex(0, 0).endVertex();
		buf.pos(.9f - width, 0, .9f - width).tex(0, 0).endVertex();
		
		buf.pos(.9f, 0, .9f).tex(0, 0).endVertex();
		buf.pos(.9f - width, 0, .9f - width).tex(0, 0).endVertex();
		buf.pos(.9f - width, height, .9f - width).tex(0, 0).endVertex();
		buf.pos(.9f, height, .9f).tex(0, 0).endVertex();
	}

}
