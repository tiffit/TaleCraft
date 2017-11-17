package talecraft.client.render.renderables;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import talecraft.TaleCraft;
import talecraft.TaleCraftItems;
import talecraft.client.ClientResources;
import talecraft.client.render.IRenderable;
import talecraft.client.render.renderers.BoxRenderer;
import talecraft.proxy.ClientProxy;
import talecraft.util.WorldHelper;
import talecraft.util.WorldHelper.BlockRegionIterator;

public class SelectionBoxRenderer implements IRenderable {

	@Override
	public void render(Minecraft mc, ClientProxy clientProxy,
			Tessellator tessellator, final BufferBuilder vertexbuffer,
			double partialTicks) {

		// Don't show the selection if we are not in BuildMode!
		if(!ClientProxy.isInBuildMode()) return;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);

		// Wand Selection Rendering
		NBTTagCompound playerData = mc.player.getEntityData();
		if(playerData.hasKey("tcWand")) {
			NBTTagCompound tcWand = playerData.getCompoundTag("tcWand");

			if(tcWand.hasKey("cursor")) {
				final float E = -1f / 64f;
				int[] cursor = tcWand.getIntArray("cursor");

				GlStateManager.glPolygonMode(GL11.GL_FRONT, GL11.GL_POINT);
				GlStateManager.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
				GlStateManager.enableTexture2D();
				mc.getTextureManager().bindTexture(ClientResources.texColorWhite);
				BoxRenderer.renderBox(tessellator, vertexbuffer, cursor[0]-E, cursor[1]-E, cursor[2]-E, cursor[0]+1+E, cursor[1]+1+E, cursor[2]+1+E, 1f,1f,1f,1f);
				GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}

			// If not null, render the cursor selections boundaries.
			if(tcWand.hasKey("boundsA") && tcWand.hasKey("boundsB") && tcWand.getBoolean("enabled")) {
				// get bounds
				int[] ba = tcWand.getIntArray("boundsA");
				int[] bb = tcWand.getIntArray("boundsB");
				
				// make sure its correctly sorted
				int ix = Math.min(ba[0], bb[0]);
				int iy = Math.min(ba[1], bb[1]);
				int iz = Math.min(ba[2], bb[2]);
				int ax = Math.max(ba[0], bb[0]);
				int ay = Math.max(ba[1], bb[1]);
				int az = Math.max(ba[2], bb[2]);
				
				// Calculate Size & Volume
				int sx = ax-ix+1;
				int sy = ay-iy+1;
				int sz = az-iz+1;
				int sv = sx*sy*sz;
				
				if(sv < 1024 && sv > 0) {
					boolean showVoid = false;
					
					ItemStack IS = mc.player.getHeldItemMainhand();
					Item I = IS!=null?IS.getItem():null;
					
					if(I!=null) {
						/* TODO: Replace the getBlockFromName with the actual Block instance if possible. */
						showVoid |= I.equals(Item.getItemFromBlock(Block.getBlockFromName("minecraft:structure_void")));
						showVoid |= I.equals(Item.getItemFromBlock(Blocks.STRUCTURE_BLOCK));
						showVoid |= I.equals(TaleCraftItems.paste);
						showVoid |= I.equals(TaleCraftItems.copy);
						showVoid |= I.equals(TaleCraftItems.cut);
					}
					
					if(showVoid) {
						final IBlockState stvoid = Block.getBlockFromName("minecraft:structure_void").getDefaultState();
						
						GlStateManager.disableBlend();
						GlStateManager.disableTexture2D();
						GlStateManager.glLineWidth(3f);
						vertexbuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
						final float m = 0.45f;
						final float M = 1-m;
						
						final int r = 0;
						final int g = 255;
						final int b = 255;
						final int a = 255;
						
						WorldHelper.foreach(mc.world, ix, iy, iz, ax, ay, az, new BlockRegionIterator() {
							@Override public void $(World world, IBlockState state, BlockPos pos) {
								if(!state.equals(stvoid))
									return;
								
								float x = pos.getX();
								float y = pos.getY();
								float z = pos.getZ();
								
								vertexbuffer.pos(x+m, y+m, z+m).color(r, g, b, a).endVertex();
								vertexbuffer.pos(x+M, y+M, z+M).color(r, g, b, a).endVertex();
								vertexbuffer.pos(x+M, y+m, z+m).color(r, g, b, a).endVertex();
								vertexbuffer.pos(x+m, y+M, z+M).color(r, g, b, a).endVertex();
								
								vertexbuffer.pos(x+m, y+m, z+M).color(r, g, b, a).endVertex();
								vertexbuffer.pos(x+M, y+M, z+m).color(r, g, b, a).endVertex();
								vertexbuffer.pos(x+M, y+m, z+M).color(r, g, b, a).endVertex();
								vertexbuffer.pos(x+m, y+M, z+m).color(r, g, b, a).endVertex();
							}
						});
						tessellator.draw();
						GlStateManager.glLineWidth(1f);
						GlStateManager.enableTexture2D();
						GlStateManager.enableBlend();
					}
				}

				// 'error' offset
				final float E = 1f / 32f;

				// Prepare state
				GlStateManager.enableNormalize();
				GlStateManager.enableTexture2D();
				RenderHelper.disableStandardItemLighting();
				GlStateManager.glLineWidth(2.5f);

				ResourceLocation texture = null;

				TaleCraft.proxy.asClient();
				if(!ClientProxy.settings.getBoolean("client.render.useAlternateSelectionTexture")) {
					texture = ClientResources.textureSelectionBoxWS;
				} else {
					texture = ClientResources.textureSelectionBoxFF;
				}

				GlStateManager.enableTexture2D();
				
				// Render primary (with-depth) box
				{
					mc.getTextureManager().bindTexture(texture);
					GlStateManager.enableDepth();
					BoxRenderer.renderSelectionBox(tessellator, vertexbuffer, ix-E, iy-E, iz-E, ax+1+E, ay+1+E, az+1+E, 1);
				}
				
				// Render secondary (no-depth) box
				/// XXX: Temporarily disabled the 'xray selection' feature since it isnt working as intended.
				if(Boolean.FALSE.booleanValue())
				{
					mc.getTextureManager().bindTexture(ClientResources.texColorWhite);
					GlStateManager.disableTexture2D();
					GlStateManager.disableDepth();
					GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
					BoxRenderer.renderSelectionBox(tessellator, vertexbuffer, ix-E, iy-E, iz-E, ax+1+E, ay+1+E, az+1+E, -1);
					GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
					GlStateManager.enableDepth();
					GlStateManager.enableTexture2D();
				}
				
				mc.getTextureManager().bindTexture(ClientResources.texColorWhite);
				GlStateManager.glBegin(GL11.GL_LINES);
				GlStateManager.color(1, 0, 0, 1);
				GlStateManager.glVertex3f(ix-E, iy-E, iz-E);
				GlStateManager.glVertex3f(ax+1+E, iy-E, iz-E);
				GlStateManager.color(0, 1, 0, 1);
				GlStateManager.glVertex3f(ix-E, iy-E, iz-E);
				GlStateManager.glVertex3f(ix-E, ay+1+E, iz-E);
				GlStateManager.color(0, 0, 1, 1);
				GlStateManager.glVertex3f(ix-E, iy-E, iz-E);
				GlStateManager.glVertex3f(ix-E, iy-E, az+1+E);
				GlStateManager.glEnd();

				GlStateManager.glLineWidth(1.0f);
			}
		}

		GlStateManager.disableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	}

}
