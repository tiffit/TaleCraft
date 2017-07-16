package talecraft.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

public interface IEXTTileEntityRenderer<T extends TileEntity> {
	void render(T tile, TileEntityRendererDispatcher dispatcher, double posX, double posY, double posZ, float partialTicks);
}
