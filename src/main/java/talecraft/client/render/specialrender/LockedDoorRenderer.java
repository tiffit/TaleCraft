package talecraft.client.render.specialrender;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import talecraft.tileentity.LockedDoorTileEntity;

public class LockedDoorRenderer extends TileEntitySpecialRenderer<LockedDoorTileEntity> {

	@Override
	public void render(LockedDoorTileEntity te, double posX, double posY, double posZ, float partialTicks, int destroyStage, float wat) {
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.resetColor();

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

		// get tessellator
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		String textureKeyName = te.useSilverKey ? "silver" : "gold";
		this.bindTexture(new ResourceLocation("talecraft:textures/"+textureKeyName+"_lock.png"));
		vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
//		if(te.isCorner){
//			vertexbuffer.pos(1, 3, 1).tex(0, 1).endVertex();
//			vertexbuffer.pos(1, 3, 1).tex(0, 1).endVertex();
//			vertexbuffer.pos(1, 3, 1).tex(0, 1).endVertex();
//			vertexbuffer.pos(1, 3, 1).tex(0, 1).endVertex();
//		}
		
		if(te.isCorner){
			if(te.corner.isZ()){ //I move the keyhole up a bit from the block to fix it from glitching with the block
				vertexbuffer.pos(-.1,1.5,1.5).tex(1, 0).endVertex();
				vertexbuffer.pos(-.1,1.5,0.5).tex(0, 0).endVertex();
				vertexbuffer.pos(-.1,0.5,0.5).tex(0, 1).endVertex();
				vertexbuffer.pos(-.1,0.5,1.5).tex(1, 1).endVertex();
				
				vertexbuffer.pos(1.1,0.5,1.5).tex(1, 1).endVertex();
				vertexbuffer.pos(1.1,0.5,0.5).tex(0, 1).endVertex();
				vertexbuffer.pos(1.1,1.5,0.5).tex(0, 0).endVertex();
				vertexbuffer.pos(1.1,1.5,1.5).tex(1, 0).endVertex();
			}else{
//				vertexbuffer.pos(-.1,1.5,1.5).tex(1, 0).endVertex();
//				vertexbuffer.pos(-.1,1.5,0.5).tex(0, 0).endVertex();
//				vertexbuffer.pos(-.1,0.5,0.5).tex(0, 1).endVertex();
//				vertexbuffer.pos(-.1,0.5,1.5).tex(1, 1).endVertex();
				
				vertexbuffer.pos(1.5,1.5,1.1).tex(1, 0).endVertex();
				vertexbuffer.pos(0.5,1.5,1.1).tex(0, 0).endVertex();
				vertexbuffer.pos(0.5,0.5,1.1).tex(0, 1).endVertex();
				vertexbuffer.pos(1.5,0.5,1.1).tex(1, 1).endVertex();
				
				vertexbuffer.pos(1.5,0.5,-.1).tex(1, 1).endVertex();
				vertexbuffer.pos(0.5,0.5,-.1).tex(0, 1).endVertex();
				vertexbuffer.pos(0.5,1.5,-.1).tex(0, 0).endVertex();
				vertexbuffer.pos(1.5,1.5,-.1).tex(1, 0).endVertex();
				
				
			}
		}
		
		
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.enableCull();
	}

}
