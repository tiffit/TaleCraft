package de.longor.talecraft.client.render.tileentity;

import net.minecraft.tileentity.TileEntity;

public interface IEXTTileEntityRenderer<T extends TileEntity> {
	public void render(T tileentity, double posX, double posY, double posZ, float partialTicks);
}
