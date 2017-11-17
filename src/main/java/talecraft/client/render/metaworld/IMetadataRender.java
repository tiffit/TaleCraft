package talecraft.client.render.metaworld;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface IMetadataRender {

	public void render(Item item, ItemStack stack,
			Tessellator tessellator, BufferBuilder buffer, double partialTick,
			BlockPos playerPos, EntityPlayerSP player, WorldClient world);
	
}
