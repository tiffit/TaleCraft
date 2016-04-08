package tiffit.talecraft.tileentity.specialrender;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import tiffit.talecraft.tileentity.SpikeBlockTileEntity;

public class SpikeBlockEntityRenderer extends TileEntitySpecialRenderer<SpikeBlockTileEntity> {

	@Override
	public void renderTileEntityAt(SpikeBlockTileEntity te, double posX, double posY, double posZ, float partialTicks, int destroyStage) {
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableBlend();
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
		this.bindTexture(new ResourceLocation("minecraft:textures/blocks/iron_block.png"));
		
		vertexbuffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(0.5, 1, 0.5).tex(0.5, 1).endVertex();
		vertexbuffer.pos(0,0,1).tex(0, 0).endVertex();
		vertexbuffer.pos(1,0,1).tex(1, 0).endVertex();
		
		vertexbuffer.pos(0.5, 1, 0.5).tex(0.5, 1).endVertex();
		vertexbuffer.pos(1,0,0).tex(0, 0).endVertex();
		vertexbuffer.pos(0,0,0).tex(1, 0).endVertex();
		
		vertexbuffer.pos(0.5, 1, 0.5).tex(0.5, 1).endVertex();
		vertexbuffer.pos(1,0,1).tex(1, 0).endVertex();
		vertexbuffer.pos(1,0,0).tex(1, 0).endVertex();
		
		vertexbuffer.pos(0.5, 1, 0.5).tex(0.5, 1).endVertex();
		vertexbuffer.pos(0,0,0).tex(0, 0).endVertex();
		vertexbuffer.pos(0,0,1).tex(1, 0).endVertex();
		
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.enableCull();
	}

}
