package talecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import talecraft.proxy.ClientProxy;

public interface IRenderable {

	void render(Minecraft mc, ClientProxy clientProxy, Tessellator tessellator, VertexBuffer vertexbuffer, double partialTicks);

}
