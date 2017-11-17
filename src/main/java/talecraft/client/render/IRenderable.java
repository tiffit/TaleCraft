package talecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import talecraft.proxy.ClientProxy;

public interface IRenderable {

	void render(Minecraft mc, ClientProxy clientProxy, Tessellator tessellator, BufferBuilder vertexbuffer, double partialTicks);

}
