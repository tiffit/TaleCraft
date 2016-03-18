package de.longor.talecraft.client.render;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;

public interface IRenderable {

	void render(Minecraft mc, ClientProxy clientProxy, Tessellator tessellator, VertexBuffer vertexbuffer, double partialTicks);

}
