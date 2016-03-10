package de.longor.talecraft.client.render.tileentity;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.ClockBlockTileEntity;
import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.render.RenderHelper;
import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GenericTileEntityRenderer<T extends TileEntity> extends TileEntitySpecialRenderer {
    private static final List<IInvoke> invokes = Lists.newArrayList();
	private final ResourceLocation texture;
    private final IEXTTileEntityRenderer<T> extRenderer;
	
    public GenericTileEntityRenderer(String texturePath, IEXTTileEntityRenderer<T> exr) {
    	texture = new ResourceLocation(texturePath);
    	extRenderer = exr;
    }
	
    public GenericTileEntityRenderer(String texturePath) {
    	texture = new ResourceLocation(texturePath);
    	extRenderer = null;
    }
    
	@Override
	public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posY, double posZ, float partialTicks, int p_180535_9_) {
		T tile = (T) p_180535_1_;
        
        // render states
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.resetColor();
        
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        
        // get tessellator
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		
        if(ClientProxy.isInBuildMode()) {
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float)posX, (float)posY, (float)posZ);
	        
	        // bounds
	        final float D = 2f / 64f;
	        final float I = D;
	        final float A = 1f - D;
	        
	        // bind texture
	        this.bindTexture(texture);
	        
	        // time to render
	        
	        // top
	        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
	        //worldrenderer.setBrightness(0xEE); //TODO: FIX
	        
	        worldrenderer.pos(I, A, A).tex(1, 0).endVertex();
	        worldrenderer.pos(A, A, A).tex(0, 0).endVertex();
	        worldrenderer.pos(A, A, I).tex(0, 1).endVertex();
	        worldrenderer.pos(I, A, I).tex(1, 1).endVertex();
	        // bottom
	        worldrenderer.pos(I, I, I).tex(1, 0).endVertex();
	        worldrenderer.pos(A, I, I).tex(0, 0).endVertex();
	        worldrenderer.pos(A, I, A).tex(0, 1).endVertex();
	        worldrenderer.pos(I, I, A).tex(1, 1).endVertex();
	        // negative z | north
	        worldrenderer.pos(I, A, I).tex(1, 0).endVertex();
	        worldrenderer.pos(A, A, I).tex(0, 0).endVertex();
	        worldrenderer.pos(A, I, I).tex(0, 1).endVertex();
	        worldrenderer.pos(I, I, I).tex(1, 1).endVertex();
	        // positive z | south
	        worldrenderer.pos(A, A, A).tex(1, 0).endVertex();
	        worldrenderer.pos(I, A, A).tex(0, 0).endVertex();
	        worldrenderer.pos(I, I, A).tex(0, 1).endVertex();
	        worldrenderer.pos(A, I, A).tex(1, 1).endVertex();
	        // positive x | east
	        worldrenderer.pos(A, A, I).tex(1, 0).endVertex();
	        worldrenderer.pos(A, A, A).tex(0, 0).endVertex();
	        worldrenderer.pos(A, I, A).tex(0, 1).endVertex();
	        worldrenderer.pos(A, I, I).tex(1, 1).endVertex();
	        // negative x | west
	        worldrenderer.pos(I, A, A).tex(1, 0).endVertex();
	        worldrenderer.pos(I, A, I).tex(0, 0).endVertex();
	        worldrenderer.pos(I, I, I).tex(0, 1).endVertex();
	        worldrenderer.pos(I, I, A).tex(1, 1).endVertex();
	        tessellator.draw();
	        
	        GlStateManager.popMatrix();
		}
        
        /*
        final String TEXT = tile.getBlockType().getLocalizedName(); // tile.getStateAsString();
        if(TEXT != null || Boolean.FALSE) {
        	final int TEXT_W = this.getFontRenderer().getStringWidth(TEXT);
        	final float HEX = 1f / 32f;
        	GlStateManager.translate(0.5f, 1.75f, 0.5f);
        	GlStateManager.rotate(180, 1, 0, 0);
        	GlStateManager.scale(HEX, HEX, HEX);
        	GlStateManager.rotate((float)(Minecraft.getMinecraft().thePlayer.rotationYawHead + 180), 0, 1, 0);
        	this.getFontRenderer().drawString(TEXT, -TEXT_W/2, 0, 0xFFFFFFFF);
        }
        //*/
        
        if(extRenderer != null) {
        	GlStateManager.pushMatrix();
            GlStateManager.translate(
            		(float) -this.rendererDispatcher.entityX,
            		(float) -this.rendererDispatcher.entityY,
            		(float) -this.rendererDispatcher.entityZ
            );
            
            extRenderer.render(tile, posX, posY, posZ, partialTicks);
            GlStateManager.popMatrix();
        }
        
        if(ClientProxy.isInBuildMode() && tile instanceof IInvokeSource && ClientProxy.settings.getBoolean("client.render.invokeVisualize")) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(
            		(float) -this.rendererDispatcher.entityX,
            		(float) -this.rendererDispatcher.entityY,
            		(float) -this.rendererDispatcher.entityZ
            );
            
        	invokes.clear();
        	((IInvokeSource) tile).getInvokes(invokes);
        	
        	float minX = 0;
        	float minY = 0;
        	float minZ = 0;
        	float maxX = 0;
        	float maxY = 0;
        	float maxZ = 0;
        	
        	float r = 1.0f;
        	float g = 0.5f;
        	float b = 0.0f;
        	float a = 1.0f;
        	
        	float x0 = tile.getPos().getX() + 0.5f;
        	float y0 = tile.getPos().getY() + 0.5f;
        	float z0 = tile.getPos().getZ() + 0.5f;
        	float x1 = 0;
        	float y1 = 0;
        	float z1 = 0;
        	
        	final float error = 1f / 128f;
        	
        	if(!invokes.isEmpty()) {
        		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                this.bindTexture(ClientResources.texColorWhite);
                GlStateManager.disableCull();
                GlStateManager.resetColor();
                GL11.glShadeModel(GL11.GL_SMOOTH);
                GL11.glLineWidth(2);
                
            	float[] color = new float[3];
            	((IInvokeSource) tile).getInvokeColor(color);
            	r = color[0];
            	g = color[1];
            	b = color[2];
            	
        		for(IInvoke invoke : invokes) {
        			if(invoke instanceof BlockTriggerInvoke) {
        				int[] bounds = ((BlockTriggerInvoke) invoke).getBounds();
        				
        				if(bounds != null) {
        					minX = bounds[0]   - error;
            				minY = bounds[1]   - error;
            				minZ = bounds[2]   - error;
            				maxX = bounds[3]+1 + error;
            				maxY = bounds[4]+1 + error;
            				maxZ = bounds[5]+1 + error;
            				
            				x1 = ((bounds[3] + bounds[0]) / 2f) + 0.5f;
            				y1 = ((bounds[4] + bounds[1]) / 2f) + 0.5f;
            				z1 = ((bounds[5] + bounds[2]) / 2f) + 0.5f;
            				
        					BoxRenderer.renderBox(tessellator, worldrenderer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
        					
        					
        					
        					GL11.glBegin(GL11.GL_LINES);
        					GL11.glColor4f(color[0], color[1], color[2], 1f);
        					GL11.glTexCoord2f(0, 0);
        					GL11.glVertex3f(x0, y0, z0);
        					{
        						// reuse minX minY minZ
        						minX = (x0 + x1) / 2f;
        						minY = (y0 + y1) / 2f;
        						minZ = (z0 + z1) / 2f;
            					GL11.glVertex3f(minX, minY, minZ);
            					GL11.glVertex3f(minX, minY, minZ);
        					}
        					GL11.glColor4f(1,1,1,1f);
        					GL11.glVertex3f(x1, y1, z1);
        					GL11.glEnd();
        					
        					// BoxRenderer.renderBoxLine(tessellator, worldrenderer, x0, y0, z0, x1, y1, z1, r, g, b, a);
        				}
        			}
        		}
        		
                GL11.glLineWidth(1f);
        		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        	}
        	
            GlStateManager.popMatrix();
        }
        
        
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        
	}
	
	
	
}
