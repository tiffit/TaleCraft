package talecraft.client.render.renderers;

import java.util.HashMap;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import talecraft.client.render.metaworld.IMetadataRender;
import talecraft.proxy.ClientProxy;

public class ItemMetaWorldRenderer {
	
	public static HashMap<Item, IMetadataRender> ITEM_RENDERS = new HashMap<Item, IMetadataRender>();
	
	// CLIENT
	public static ClientProxy clientProxy;
	public static Tessellator tessellator;
	public static BufferBuilder vertexbuffer;
	public static double partialTicks;
	public static float partialTicksF;
	// CLIENT.PLAYER
	public static BlockPos playerPosition;
	public static EntityPlayerSP player;
	public static WorldClient world;

	// RENDER
	public static void render(Item item, ItemStack stack) {
		if(ITEM_RENDERS.containsKey(item)){
			IMetadataRender renderer = ITEM_RENDERS.get(item);
			renderer.render(item, stack, tessellator, vertexbuffer, partialTicks, playerPosition, player, world);
		}

	}
}
