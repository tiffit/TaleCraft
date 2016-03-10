package de.longor.talecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import de.longor.talecraft.proxy.ClientProxy;

public interface IRenderable {
	
	void render(Minecraft mc, ClientProxy clientProxy, Tessellator tessellator, WorldRenderer worldrenderer, double partialTicks);
	
}
