package talecraft.client.render.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import talecraft.TaleCraft;

public class BoxRenderer {
	public static final void renderWireBoxWithPointAndLines(
			float x0, float y0, float z0, float x1, float y1, float z1,
			float X, float Y, float Z,
			float r, float g, float b, float a) {
		float minX = x0;
		float minY = y0;
		float minZ = z0;
		float maxX = x1;
		float maxY = y1;
		float maxZ = z1;
		float x = X;
		float y = Y;
		float z = Z;
		
		// TODO: Try to make this method use VertexBuffer.

		GlStateManager.glBegin(GL11.GL_LINES);
		GlStateManager.color(r*0.5f, g*0.5f, b*0.5f, a);

		GlStateManager.glVertex3f(minX, minY, minZ);
		GlStateManager.glVertex3f(maxX, minY, minZ);

		GlStateManager.glVertex3f(minX, minY, minZ);
		GlStateManager.glVertex3f(minX, minY, maxZ);

		GlStateManager.glVertex3f(minX, minY, maxZ);
		GlStateManager.glVertex3f(maxX, minY, maxZ);

		GlStateManager.glVertex3f(maxX, minY, minZ);
		GlStateManager.glVertex3f(maxX, minY, maxZ);

		GlStateManager.glVertex3f(minX, maxY, minZ);
		GlStateManager.glVertex3f(maxX, maxY, minZ);

		GlStateManager.glVertex3f(minX, maxY, minZ);
		GlStateManager.glVertex3f(minX, maxY, maxZ);

		GlStateManager.glVertex3f(minX, maxY, maxZ);
		GlStateManager.glVertex3f(maxX, maxY, maxZ);

		GlStateManager.glVertex3f(maxX, maxY, minZ);
		GlStateManager.glVertex3f(maxX, maxY, maxZ);

		GlStateManager.glVertex3f(minX, minY, minZ);
		GlStateManager.glVertex3f(minX, maxY, minZ);

		GlStateManager.glVertex3f(maxX, minY, minZ);
		GlStateManager.glVertex3f(maxX, maxY, minZ);

		GlStateManager.glVertex3f(minX, minY, maxZ);
		GlStateManager.glVertex3f(minX, maxY, maxZ);

		GlStateManager.glVertex3f(maxX, minY, maxZ);
		GlStateManager.glVertex3f(maxX, maxY, maxZ);

		GlStateManager.color(r, g, b, a);

		if(y < minY || x < minX || z < minZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(minX, minY, minZ);
		}
		if(y < minY || x > maxX || z < minZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(maxX, minY, minZ);
		}
		if(y < minY || x < minX || z > maxZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(minX, minY, maxZ);
		}
		if(y < minY || x > maxX || z > maxZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(maxX, minY, maxZ);
		}
		if(y > maxY || x < minX || z < minZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(minX, maxY, minZ);
		}
		if(y > maxY || x > maxX || z < minZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(maxX, maxY, minZ);
		}
		if(y > maxY || x < minX || z > maxZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(minX, maxY, maxZ);
		}
		if(y > maxY || x > maxX || z > maxZ) {
			GlStateManager.glVertex3f(x, y, z);
			GlStateManager.glVertex3f(maxX, maxY, maxZ);
		}
		GlStateManager.glEnd();

	}

	public static final void renderBoxLine(Tessellator tessellator, BufferBuilder vertexbuffer,
			float x0, float y0, float z0, float x1, float y1, float z1,
			float r, float g, float b, float a) {

		float dx = x1 - x0;
		float dy = y1 - y0;
		float dz = z1 - z0;

		float lengthSquared = dx*dx + dy*dy + dz*dz;
		if(lengthSquared == 0) return;

		float length = (float) Math.sqrt(lengthSquared);
		float nx = dx / length;
		float ny = dy / length;
		float nz = dz / length;

		vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		float minX,minY,minZ,maxX,maxY,maxZ,tx,ty,tz, size = 0.05f;
		for(float t = TaleCraft.asClient().getRenderer().getLastPartialTicks() / 3f, step = 1f / 3f; t < length; t += step) {
			tx = x0 + nx*t;
			ty = y0 + ny*t;
			tz = z0 + nz*t;
			
			minX = tx - size;
			minY = ty - size;
			minZ = tz - size;
			maxX = tx + size;
			maxY = ty + size;
			maxZ = tz + size;
			renderBoxEmb(tessellator, vertexbuffer, minX, minY, minZ, maxX, maxY, maxZ);
		}
		tessellator.draw();

	}

	// XXX: Finish Implementation
	//	public static final void renderBoxLine(Tessellator tessellator, WorldRenderer worldrenderer,
	//			float x0, float y0, float z0, float x1, float y1, float z1,
	//			float r, float g, float b, float a) {
	//
	//		// float ???;
	//		// N = Negative / Min
	//		// P = Positive / Max
	//
	//		float dx = x1 - x0;
	//		float dy = y1 - y0;
	//		float dz = z1 - z0;
	//
	//		float lengthSquared = dx*dx + dy*dy + dz*dz;
	//		if(lengthSquared == 0) return;
	//
	//		float length = (float) Math.sqrt(lengthSquared);
	//
	//		Vec3 p0 = new Vec3(x0, y0, z0);
	//		Vec3 p1 = new Vec3(x1, y1, z1);
	//
	//		Vec3 d = new Vec3(dx, dy, dz);
	//
	//		Vec3 forward = new Vec3(dx/length, dy/length, dz/length);
	//		Vec3 up = UP.addVector(0, 0, 0);
	//		Vec3 left = up.crossProduct(forward);
	//
	//		float nnn;
	//		float pnn;
	//		float pnp;
	//		float nnp;
	//
	//		float npn;
	//		float ppn;
	//		float ppp;
	//		float npp;
	//
	//        worldrenderer.startDrawingQuads();
	//        worldrenderer.setBrightness(0xEE);
	//	}

	public static final void renderBoxEmb(Tessellator tessellator, BufferBuilder ren, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		// top
		VertexBufferHelper vertexbuffer = new VertexBufferHelper(ren);
		vertexbuffer.normal(0, +1, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
		vertexbuffer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
		// bottom
		vertexbuffer.normal(0, -1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
		// negative z | north
		vertexbuffer.normal(0, 0, -1);
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 1);
		// positive z | south
		vertexbuffer.normal(0, 0, +1);
		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
		// positive x | east
		vertexbuffer.normal(+1, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 0, 1);
		// negative x | west
		vertexbuffer.normal(-1, 0, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
	}


	public static final void renderBox(Tessellator tessellator, BufferBuilder ren, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		// top
		VertexBufferHelper vertexbuffer = new VertexBufferHelper(ren);
		vertexbuffer.startDrawingQuads();
		vertexbuffer.setBrightness(0xEE);

		vertexbuffer.normal(0, 1, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
		vertexbuffer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
		// bottom
		vertexbuffer.normal(0, -1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
		// negative z | north
		vertexbuffer.normal(0, 0, -1);
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 1);
		// positive z | south
		vertexbuffer.normal(0, 0, 1);
		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
		// positive x | east
		vertexbuffer.normal(1, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 0, 1);
		// negative x | west
		vertexbuffer.normal(-1, 0, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
		tessellator.draw();
	}

	public static final void renderBox(Tessellator tessellator, BufferBuilder ren, AxisAlignedBB aabb, float r, float g, float b, float a) {
		float minX = (float) aabb.minX;
		float minY = (float) aabb.minY;
		float minZ = (float) aabb.minZ;
		float maxX = (float) aabb.maxX;
		float maxY = (float) aabb.maxY;
		float maxZ = (float) aabb.maxZ;
		VertexBufferHelper vertexbuffer = new VertexBufferHelper(ren);
		// top
		vertexbuffer.startDrawingQuads();
		vertexbuffer.setBrightness(0xEE);
		vertexbuffer.color(r, g, b, a);

		// top
		vertexbuffer.normal(0, 1, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
		vertexbuffer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
		// bottom
		vertexbuffer.normal(0, -1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
		// negative z | north
		vertexbuffer.normal(0, 0, -1);
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 1);
		// positive z | south
		vertexbuffer.normal(0, 0, 1);
		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
		// positive x | east
		vertexbuffer.normal(1, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 0, 1);
		// negative x | west
		vertexbuffer.normal(-1, 0, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
		tessellator.draw();
	}

	public static final void renderBox(
			Tessellator tessellator, BufferBuilder ren,
			float minX, float minY, float minZ,
			float maxX, float maxY, float maxZ,
			float r, float g, float b, float a
			) {
//		VertexBufferHelper vertexbuffer = new VertexBufferHelper(ren);
//		GlStateManager.color(r, g, b, a);
//
//		vertexbuffer.startDrawingQuads();
//
//		vertexbuffer.setTranslation(0, 0, 0);
//		vertexbuffer.color(r, g, b, a);
//		vertexbuffer.setBrightness(0xEE);
//
//		// top
//		vertexbuffer.normal(0, 1, 0);
//		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
//		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
//		vertexbuffer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
//		vertexbuffer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
//		// bottom
//		vertexbuffer.normal(0, -1, 0);
//		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 0);
//		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 0);
//		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
//		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
//		// negative z | north
//		vertexbuffer.normal(0, 0, -1);
//		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 1, 0);
//		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
//		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 0, 1);
//		vertexbuffer.addVertexWithUV(minX, minY, minZ, 1, 1);
//		// positive z | south
//		vertexbuffer.normal(0, 0, 1);
//		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
//		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
//		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
//		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
//		// positive x | east
//		vertexbuffer.normal(1, 0, 0);
//		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
//		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
//		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
//		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 1);
//		// negative x | west
//		vertexbuffer.normal(-1, 0, 0);
//		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
//		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 0, 0);
//		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 1);
//		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 1, 1);
//
//		tessellator.draw();
	}

	public static final void renderBox(
			Tessellator tessellator, BufferBuilder ren,
			double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ,
			float r, float g, float b, float a
			) {
		VertexBufferHelper vertexbuffer = new VertexBufferHelper(ren);
		vertexbuffer.startDrawingQuads();

		vertexbuffer.color(r, g, b, a);
		vertexbuffer.setBrightness(0xFF);

		// top
		vertexbuffer.normal(0, 1, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
		vertexbuffer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
		// bottom
		vertexbuffer.normal(0, -1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
		// negative z | north
		vertexbuffer.normal(0, 0, -1);
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 0, 1);
		// positive z | south
		vertexbuffer.normal(0, 0, 1);
		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
		// positive x | east
		vertexbuffer.normal(1, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
		vertexbuffer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, 0, 1);
		// negative x | west
		vertexbuffer.normal(-1, 0, 0);
		vertexbuffer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, 1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, 1, 1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, 0, 1);
		tessellator.draw();
	}

	public static final void renderSelectionBox(//Used by selection
			Tessellator tessellator, BufferBuilder ren,
			float minX, float minY, float minZ,
			float maxX, float maxY, float maxZ, float a
			) {
		float U = Math.round(maxX - minX);
		float V = Math.round(maxY - minY);
		float W = Math.round(maxZ - minZ);
		float VOLUME = U*V*W;

		float SPEED = 10000;
		float UVW_DIVIDE = 1;

		if(VOLUME > (128*128*128)) {
			UVW_DIVIDE = 128f;
			SPEED = 10000;
		} else if(VOLUME > (16*16*16)) {
			UVW_DIVIDE = 16f;
			SPEED = 7500;
		} else {
			UVW_DIVIDE = 1;
			SPEED = 3000;
		}

		// divide
		U /= UVW_DIVIDE;
		V /= UVW_DIVIDE;
		W /= UVW_DIVIDE;

		float u0 = 0;
		float u1 = U;
		float v0 = 0;
		float v1 = V;
		float w0 = 0;
		float w1 = W;

		{
			final long speedL = (long) SPEED;
			final float speedF = SPEED;

			float f4 = Minecraft.getSystemTime() % speedL / speedF;
			float ADD = f4;

			u0 += ADD;
			u1 += ADD;
			v0 += ADD;
			v1 += ADD;
			w0 += ADD;
			w1 += ADD;
		}

		float r = 1;
		float g = 0.5f;
		float b = 0;

		if(a == 0) {
			a = 1;
			r = 1;
			g = 1;
			b = 1;
		}

		if(a == -1) {
			a = 1;
			r = 0;
			g = 0;
			b = 0;
		}

		if(a == -2) {
			a = 1;
			r = 0;
			g = 1;
			b = 0;
		}

		if(a == -3) {
			a = 1;
			r = 1;
			g = 0.25f;
			b = 0.25f;
		}
		
		VertexBufferHelper vertexbuffer = new VertexBufferHelper(ren);
		vertexbuffer.startDrawingQuads();
		vertexbuffer.color(r, g, b, a);
		vertexbuffer.setTranslation(0, 0, 0);
		vertexbuffer.setBrightness(0xFF);
		
		// top
		vertexbuffer.normal(0, 1, 0);
		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, u0, w0);
		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, u1, w0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, u1, w1);
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, u0, w1);
		// bottom
		vertexbuffer.normal(0, -1, 0);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, u0, w0);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, u1, w0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, u1, w1);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, u0, w1);
		// negative z | north
		vertexbuffer.normal(0, 0, -1);
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, u0, v0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, u1, v0);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, u1, v1);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, u0, v1);
		// positive z | south
		vertexbuffer.normal(0, 0, 1);
		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, u0, v0);
		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, u1, v0);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, u1, v1);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, u0, v1);
		//positive x | east
		vertexbuffer.normal(1, 0, 0);
		vertexbuffer.addVertexWithUV(maxX, maxY, minZ, w0, v0);
		vertexbuffer.addVertexWithUV(maxX, maxY, maxZ, w1, v0);
		vertexbuffer.addVertexWithUV(maxX, minY, maxZ, w1, v1);
		vertexbuffer.addVertexWithUV(maxX, minY, minZ, w0, v1);
		//negative x | west
		vertexbuffer.normal(-1, 0, 0);
		vertexbuffer.addVertexWithUV(minX, maxY, maxZ, w0, v0);
		vertexbuffer.addVertexWithUV(minX, maxY, minZ, w1, v0);
		vertexbuffer.addVertexWithUV(minX, minY, minZ, w1, v1);
		vertexbuffer.addVertexWithUV(minX, minY, maxZ, w0, v1);
		tessellator.draw();
	}

	//tiffit got lazy, he used a cheap way
	private static class VertexBufferHelper {
		private BufferBuilder rend;
		public VertexBufferHelper(BufferBuilder rend) {
			this.rend = rend;
		}

		public void startDrawingQuads() {
			rend.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		}

		public BufferBuilder pos(double x, double y, double z) {
			return rend.pos(x, y, z);
		}
		
		public void setTranslation(int x, int y, int z) {
			rend.setTranslation(x, y, z);
		}

		public void addVertexWithUV(double x, double y, double z, float u, float v) {
			rend.pos(x, y, z).tex(u, v).endVertex();
			
		}
		
		/*
		 * XXX: Temporarily fixed the bug where the rendering of
		 * the selection box is completely messed up,
		 * by commenting out the contents of the methods below.
		 */
		
		public void normal(float x, float y, float z) {
			// rend.normal(x, y, z);
		}

		public void setBrightness(int brightness) {
		// rend.lightmap((brightness>>4)&0xF, brightness&0xF);
		}

		public void color(float r, float g, float b, float a) {
			//rend.color(r, g, b, a);
		}
	}

}

