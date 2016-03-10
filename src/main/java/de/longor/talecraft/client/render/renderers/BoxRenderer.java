package de.longor.talecraft.client.render.renderers;

import org.lwjgl.opengl.GL11;

import com.sun.javafx.geom.Vec3f;

import de.longor.talecraft.TaleCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldRenderer.State;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BoxRenderer {
	private static final Vec3 UP = new Vec3(0, 1, 0);
	
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
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor4f(r*0.5f, g*0.5f, b*0.5f, a);
		
		GL11.glVertex3f(minX, minY, minZ);
		GL11.glVertex3f(maxX, minY, minZ);
		
		GL11.glVertex3f(minX, minY, minZ);
		GL11.glVertex3f(minX, minY, maxZ);
		
		GL11.glVertex3f(minX, minY, maxZ);
		GL11.glVertex3f(maxX, minY, maxZ);
		
		GL11.glVertex3f(maxX, minY, minZ);
		GL11.glVertex3f(maxX, minY, maxZ);
		
		GL11.glVertex3f(minX, maxY, minZ);
		GL11.glVertex3f(maxX, maxY, minZ);
		
		GL11.glVertex3f(minX, maxY, minZ);
		GL11.glVertex3f(minX, maxY, maxZ);
		
		GL11.glVertex3f(minX, maxY, maxZ);
		GL11.glVertex3f(maxX, maxY, maxZ);
		
		GL11.glVertex3f(maxX, maxY, minZ);
		GL11.glVertex3f(maxX, maxY, maxZ);
		
		GL11.glVertex3f(minX, minY, minZ);
		GL11.glVertex3f(minX, maxY, minZ);
		
		GL11.glVertex3f(maxX, minY, minZ);
		GL11.glVertex3f(maxX, maxY, minZ);
		
		GL11.glVertex3f(minX, minY, maxZ);
		GL11.glVertex3f(minX, maxY, maxZ);
		
		GL11.glVertex3f(maxX, minY, maxZ);
		GL11.glVertex3f(maxX, maxY, maxZ);
		
		GL11.glColor4f(r, g, b, a);
		
		if(y < minY || x < minX || z < minZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(minX, minY, minZ);
		}
		if(y < minY || x > maxX || z < minZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(maxX, minY, minZ);
		}
		if(y < minY || x < minX || z > maxZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(minX, minY, maxZ);
		}
		if(y < minY || x > maxX || z > maxZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(maxX, minY, maxZ);
		}
		if(y > maxY || x < minX || z < minZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(minX, maxY, minZ);
		}
		if(y > maxY || x > maxX || z < minZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(maxX, maxY, minZ);
		}
		if(y > maxY || x < minX || z > maxZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(minX, maxY, maxZ);
		}
		if(y > maxY || x > maxX || z > maxZ) {
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(maxX, maxY, maxZ);
		}
		GL11.glEnd();
		
	}
	
	public static final void renderBoxLine(Tessellator tessellator, WorldRenderer worldrenderer,
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
		
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.color(r, g, b, a);
        //worldrenderer.setBrightness(0xFF); //TODO FIX
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
			renderBoxEmb(tessellator, worldrenderer, minX, minY, minZ, maxX, maxY, maxZ);
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
	
	public static final void renderBoxEmb(Tessellator tessellator, WorldRenderer ren, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        // top
		QuickRenderer worldrenderer = new QuickRenderer(ren);
		worldrenderer.normal(0, +1, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
        worldrenderer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
        // bottom
        worldrenderer.normal(0, -1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.normal(0, 0, -1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        // positive z | south
        worldrenderer.normal(0, 0, +1);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        // positive x | east
        worldrenderer.normal(+1, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        // negative x | west
        worldrenderer.normal(-1, 0, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
	}
	
	
	public static final void renderBox(Tessellator tessellator, WorldRenderer ren, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        // top
		QuickRenderer worldrenderer = new QuickRenderer(ren);
        worldrenderer.startDrawingQuads();
        worldrenderer.setBrightness(0xEE);
        
        worldrenderer.setNormal(0, 1, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
        worldrenderer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
        // bottom
        worldrenderer.setNormal(0, -1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.setNormal(0, 0, -1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        // positive z | south
        worldrenderer.setNormal(0, 0, 1);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        // positive x | east
        worldrenderer.setNormal(1, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        // negative x | west
        worldrenderer.setNormal(-1, 0, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        tessellator.draw();
	}
	
	public static final void renderBox(Tessellator tessellator, WorldRenderer ren, AxisAlignedBB aabb, float r, float g, float b, float a) {
		float minX = (float) aabb.minX;
		float minY = (float) aabb.minY;
		float minZ = (float) aabb.minZ;
		float maxX = (float) aabb.maxX;
		float maxY = (float) aabb.maxY;
		float maxZ = (float) aabb.maxZ;
		QuickRenderer worldrenderer = new QuickRenderer(ren);
		// top
        worldrenderer.startDrawingQuads();
        worldrenderer.setBrightness(0xEE);
        worldrenderer.setColorRGBA_F(r, g, b, a);
        
        // top
        worldrenderer.setNormal(0, 1, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
        worldrenderer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
        // bottom
        worldrenderer.setNormal(0, -1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.setNormal(0, 0, -1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        // positive z | south
        worldrenderer.setNormal(0, 0, 1);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        // positive x | east
        worldrenderer.setNormal(1, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        // negative x | west
        worldrenderer.setNormal(-1, 0, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        tessellator.draw();
	}
	
	public static final void renderBox(
			Tessellator tessellator, WorldRenderer ren,
			float minX, float minY, float minZ,
			float maxX, float maxY, float maxZ,
			float r, float g, float b, float a
	) {
		QuickRenderer worldrenderer = new QuickRenderer(ren);
        GlStateManager.color(r, g, b, a);
        
        worldrenderer.startDrawingQuads();
        
        worldrenderer.setTranslation(0, 0, 0);
        worldrenderer.setColorRGBA_F(r, g, b, a);
        // worldrenderer.setBrightness(0xEE);
        
        // top
        worldrenderer.setNormal(0, 1, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
        worldrenderer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
        // bottom
        worldrenderer.setNormal(0, -1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.setNormal(0, 0, -1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        // positive z | south
        worldrenderer.setNormal(0, 0, 1);
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        // positive x | east
        worldrenderer.setNormal(1, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        // negative x | west
        worldrenderer.setNormal(-1, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        
        tessellator.draw();
	}
	
	public static final void renderBox(
			Tessellator tessellator, WorldRenderer ren,
			double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ,
			float r, float g, float b, float a
	) {
		QuickRenderer worldrenderer = new QuickRenderer(ren);
        worldrenderer.startDrawingQuads();
        
        worldrenderer.setColorRGBA_F(r, g, b, a);
        worldrenderer.setBrightness(0xFF);
        
        // top
        worldrenderer.setNormal(0, 1, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
        worldrenderer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
        // bottom
        worldrenderer.setNormal(0, -1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.setNormal(0, 0, -1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        // positive z | south
        worldrenderer.setNormal(0, 0, 1);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        // positive x | east
        worldrenderer.setNormal(1, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.pos(maxX, maxY, maxZ).tex(1, 0).endVertex();
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        // negative x | west
        worldrenderer.setNormal(-1, 0, 0);
        worldrenderer.pos(minX, maxY, maxZ).tex(0, 0).endVertex();
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        tessellator.draw();
	}
	
	public static final void renderSelectionBox(
			Tessellator tessellator, WorldRenderer ren,
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
            
			float f4 = (float)(Minecraft.getSystemTime() % speedL) / speedF;
            float ADD = f4;
            
            u0 += ADD;
            u1 += ADD;
            v0 += ADD;
            v1 += ADD;
            w0 += ADD;
            w1 += ADD;
		}
		
		float funkyTime = (Minecraft.getSystemTime() / 1000.0f);
		float r = 1;
		float g = 0.5f;
		float b = 0;
		
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
		QuickRenderer worldrenderer = new QuickRenderer(ren);
        // top
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA_F(r, g, b, a);
        //worldrenderer.setBrightness(0xEE); //TODO FIX
        
        worldrenderer.putNormal(0, 1, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, u0, w0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, u1, w0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, u1, w1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, u0, w1);
        // bottom
        worldrenderer.putNormal(0, -1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, u0, w0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, u1, w0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, u1, w1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, u0, w1);
        // negative z | north
        worldrenderer.putNormal(0, 0, -1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, u0, v0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, u1, v0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, u1, v1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, u0, v1);
        // positive z | south
        worldrenderer.putNormal(0, 0, 1);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, u0, v0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, u1, v0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, u1, v1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, u0, v1);
        // positive x | east
        worldrenderer.putNormal(1, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, w0, v0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, w1, v0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, w1, v1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, w0, v1);
        // negative x | west
        worldrenderer.putNormal(-1, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, w0, v0);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, w1, v0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, w1, v1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, w0, v1);
        tessellator.draw();
	}
	
	//tiffit got lazy, he used a cheap way
	private static class QuickRenderer{
		private WorldRenderer rend;
		public QuickRenderer(WorldRenderer rend){
			this.rend = rend;
		}
		
		public WorldRenderer pos(double x, double y, double z) {
			return rend.pos(x, y, z);
		}

		public void setTranslation(int i, int j, int k) {
			rend.setTranslation(i, j, k);
			
		}

		public void addVertexWithUV(double x, double y, double z, float u, float v){
			rend.pos(x, y, z).tex(u, v).endVertex();
		}
		
		public void putNormal(float x, float y, float z){
			rend.normal(x, y, z);
		}
		
		public void normal(float x, float y, float z){
			rend.normal(x, y, z);
		}
		
		public void setNormal(float x, float y, float z){
			rend.normal(x, y, z);
		}
		
		public WorldRenderer pos(float x, float y, float z){
			return rend.pos(x, y, z);
		}
		
		public void startDrawingQuads(){
			rend.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		}
		
		public void setBrightness(int arg){
			//TODO FIX
		}
		
		public void setColorRGBA_F(float r, float g, float b, float a){
			rend.color(r, g, b, a);
		}
	}
	
	
}
