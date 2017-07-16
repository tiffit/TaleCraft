package talecraft.client.render.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import talecraft.client.ClientResources;
import talecraft.client.render.renderers.BoxRenderer;
import talecraft.proxy.ClientProxy;
import talecraft.tileentity.StorageBlockTileEntity;

public class StorageBlockTileEntityEXTRenderer implements
IEXTTileEntityRenderer<StorageBlockTileEntity> {
	@Override
	public void render(
			StorageBlockTileEntity tileentity,
			TileEntityRendererDispatcher dispatcher, double posX, double posY,
			double posZ, float partialTicks
			) {
		if(!ClientProxy.isInBuildMode())return;

		int[] bounds = tileentity.getBounds();

		if(bounds != null) {
			ClientProxy.mc.renderEngine.bindTexture(ClientResources.texColorWhite);
			GlStateManager.disableCull();

			float minX = bounds[0];
			float minY = bounds[1];
			float minZ = bounds[2];
			float maxX = bounds[3]+1;
			float maxY = bounds[4]+1;
			float maxZ = bounds[5]+1;

			float error = 1f / 1024f;

			minX -= error;
			minY -= error;
			minZ -= error;

			maxX += error;
			maxY += error;
			maxZ += error;

			float[] color = new float[3];
			tileentity.getInvokeColor(color);

			float r = color[0];
			float g = color[1];
			float b = color[2];
			float a = 1f;

			float x = tileentity.getPos().getX() + 0.5f;
			float y = tileentity.getPos().getY() + 0.5f;
			float z = tileentity.getPos().getZ() + 0.5f;

			BoxRenderer.renderWireBoxWithPointAndLines(minX, minY, minZ, maxX, maxY, maxZ, x, y, z, r, g, b, a);

			GlStateManager.enableCull();
		}
	}
}