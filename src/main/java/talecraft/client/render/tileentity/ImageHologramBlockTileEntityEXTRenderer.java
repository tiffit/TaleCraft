package talecraft.client.render.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import talecraft.client.ClientResources;
import talecraft.proxy.ClientProxy;
import talecraft.tileentity.ImageHologramBlockTileEntity;
import talecraft.util.Vec3f;

public class ImageHologramBlockTileEntityEXTRenderer implements
IEXTTileEntityRenderer<ImageHologramBlockTileEntity> {

	@Override
	public void render(ImageHologramBlockTileEntity tileentity, TileEntityRendererDispatcher dispatcher, double posX, double posY, double posZ, float partialTicks) {
		if(!tileentity.isActive()) return;

		String locationStr = tileentity.getTextureLocation();

		if(locationStr.equalsIgnoreCase("#atlas")) {
			ClientProxy.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		} else {
			ResourceLocation location = new ResourceLocation(locationStr);

			ITextureObject texture = ClientProxy.mc.renderEngine.getTexture(location);

			if(texture != null)
				GlStateManager.bindTexture(texture.getGlTextureId());
			else {
				ClientProxy.mc.renderEngine.bindTexture(location);
				ClientProxy.mc.renderEngine.bindTexture(ClientResources.textureSelectionBoxFF);
			}
		}
		
		final BlockPos teorigin = tileentity.getPos();
		
		final Vec3f origin = tileentity.getHologramOffsetRelative() ?
			new Vec3f((float)dispatcher.entityX, (float)dispatcher.entityY, (float)dispatcher.entityZ) :
			new Vec3f(teorigin.getX(), teorigin.getY(), teorigin.getZ());
		
		float x = origin.x + tileentity.getHologramOffsetX();
		float y = origin.y + tileentity.getHologramOffsetY();
		float z = origin.z + tileentity.getHologramOffsetZ();

		float pitch = tileentity.getHologramPitch();
		float yaw = tileentity.getHologramYaw();

		float w = tileentity.getHologramWidth() / 2f;
		float h = tileentity.getHologramHeight() / 2f;

		float u = 1 * tileentity.getHologramUscale();
		if(w < 0) {
			u = (w = w*-1) * 2f;
		}

		float v = 1 * tileentity.getHologramVscale();
		if(h < 0) {
			v = (h = h*-1) * 2f;
		}
		
		int argb = tileentity.getHologramColor();
		float a = (float)((argb>>24) & 0xFF) / 256F;
		float r = (float)((argb>>16) & 0xFF) / 256F;
		float g = (float)((argb>>8) & 0xFF) / 256F;
		float b = (float)(argb & 0xFF) / 256F;
		
		GlStateManager.pushMatrix();

		GlStateManager.translate(x, y, z);

		GlStateManager.rotate(yaw, 0, 1, 0);
		GlStateManager.rotate(pitch, 1, 0, 0);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		{
			// setup
			GlStateManager.color(r, g, b, a);
			vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			vertexbuffer.setTranslation(0, 0, 0);
			// negative z | north
			vertexbuffer.normal(0, 0, -1);
			vertexbuffer.color(r, g, b, a);
			vertexbuffer.pos(-w, +h, 0).tex(u, 0).endVertex();
			vertexbuffer.pos(+w, +h, 0).tex(0, 0).endVertex();
			vertexbuffer.pos(+w, -h, 0).tex(0, v).endVertex();
			vertexbuffer.pos(-w, -h, 0).tex(u, v).endVertex();
			// positive z | south
			vertexbuffer.normal(0, 0, 1);
			vertexbuffer.color(r, g, b, a);
			vertexbuffer.pos(+w, +h, 0).tex(u, 0).endVertex();
			vertexbuffer.pos(-w, +h, 0).tex(0, 0).endVertex();
			vertexbuffer.pos(-w, -h, 0).tex(0, v).endVertex();
			vertexbuffer.pos(+w, -h, 0).tex(u, v).endVertex();
			// draw
			tessellator.draw();
		}

		GlStateManager.popMatrix();

		if(Boolean.FALSE.booleanValue()) { // Debug
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.scale(1f/32f, -1f/32f, 1f/32f);
			ClientProxy.mc.fontRenderer.drawString("PATH " + locationStr, 0, 32, 0xFFFFFFFF);
			GlStateManager.popMatrix();
		}
	}
}
