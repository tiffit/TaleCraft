package de.longor.talecraft.client.render.tileentity;

import net.minecraft.tileentity.TileEntity;

public interface IEXTTileEntityRenderer<T extends TileEntity> {
	void render(T tile, double posX, double posY, double posZ, float partialTicks);
}
