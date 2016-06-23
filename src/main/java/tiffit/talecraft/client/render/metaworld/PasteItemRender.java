package tiffit.talecraft.client.render.metaworld;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.clipboard.ClipboardTagNames;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.util.NBTHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PasteItemRender implements IMetadataRender {

	@Override
	public void render(Item item, ItemStack stack, Tessellator tessellator, VertexBuffer buffer, double partialTick, BlockPos playerPos, EntityPlayerSP player, WorldClient world) {
		ClipboardItem clip = TaleCraft.asClient().getClipboard();

		if(clip == null)
			return;

		float lenMul = ClientProxy.settings.getInteger("item.paste.reach");
		Vec3d plantPos = player.getLook((float) partialTick);
		plantPos = new Vec3d(
				plantPos.xCoord*lenMul,
				plantPos.yCoord*lenMul,
				plantPos.zCoord*lenMul
				).add(player.getPositionEyes((float) partialTick));

		float dimX = 0;
		float dimY = 0;
		float dimZ = 0;

		NBTTagCompound blocks = NBTHelper.getOrNull(clip.getData(), ClipboardTagNames.$REGION);
		NBTTagCompound entity = NBTHelper.getOrNull(clip.getData(), ClipboardTagNames.$ENTITY);

		if(clip.getData().hasKey(ClipboardTagNames.$OFFSET, clip.getData().getId())) {
			NBTTagCompound offset = clip.getData().getCompoundTag(ClipboardTagNames.$OFFSET);
			plantPos = new Vec3d(
					plantPos.xCoord + offset.getFloat("x"),
					plantPos.yCoord + offset.getFloat("y"),
					plantPos.zCoord + offset.getFloat("z")
					);
		}

		float snap = ClientProxy.settings.getInteger("item.paste.snap");
		if(snap > 1) {
			plantPos = new Vec3d(
					Math.floor(plantPos.xCoord / snap) * snap,
					Math.floor(plantPos.yCoord / snap) * snap,
					Math.floor(plantPos.zCoord / snap) * snap
					);
		}

		float color = 0;

		if(blocks != null) {
			color = -2;

			dimX = blocks.getInteger(ClipboardTagNames.$REGION_WIDTH);
			dimY = blocks.getInteger(ClipboardTagNames.$REGION_HEIGHT);
			dimZ = blocks.getInteger(ClipboardTagNames.$REGION_LENGTH);

			plantPos = new Vec3d(
					Math.floor(plantPos.xCoord),
					Math.floor(plantPos.yCoord),
					Math.floor(plantPos.zCoord)
					);
		}

		if(entity != null) {
			color = -3;

			float width = entity.getFloat("tc_width");
			float height = entity.getFloat("tc_height");

			dimX = width;
			dimY = height;
			dimZ = width;

			float shift = 0.5f;
			plantPos = plantPos.subtract(width/2, 0, width/2);
		}

		float minX = (float) plantPos.xCoord;
		float minY = (float) plantPos.yCoord;
		float minZ = (float) plantPos.zCoord;
		float maxX = minX + dimX;
		float maxY = minY + dimY;
		float maxZ = minZ + dimZ;

		float error = 1f / 16f;
		minX -= error;
		minY -= error;
		minZ -= error;
		maxX += error;
		maxY += error;
		maxZ += error;

		ClientProxy.mc.renderEngine.bindTexture(ClientResources.textureSelectionBoxFF);
		// BoxRenderer.renderBox(tessellator, worldrenderer, minX, minY, minZ, maxX, maxY, maxZ, 0, 1, 0, 1);
		BoxRenderer.renderSelectionBox(tessellator, buffer, minX, minY, minZ, maxX, maxY, maxZ, color);

		if(snap > 1) {
			final int s = (int) snap;
			final int r = 1 * s;
			final float bsi = 0.5f - 0.05f;
			final float bsa = 0.5f + 0.05f;

			int midX = (int) Math.floor(minX);
			int midY = (int) Math.floor(minY);
			int midZ = (int) Math.floor(minZ);

			int startX = midX - r;
			int startY = midY - r;
			int startZ = midZ - r;

			int endX = midX + r + 1;
			int endY = midY + r + 1;
			int endZ = midZ + r + 1;

			ClientProxy.mc.renderEngine.bindTexture(ClientResources.texColorWhite);

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			buffer.color(1, 1, 1, 1);
			//vertexbuffer.setBrightness(0xEE); //TODO FIX

			for(int y = startY; y <= endY; y++) {
				for(int z = startZ; z <= endZ; z++) {
					for(int x = startX; x <= endX; x++) {
						if(x%snap==0&&y%snap==0&&z%snap==0)
							BoxRenderer.renderBoxEmb(tessellator, buffer, x+bsi, y+bsi, z+bsi, x+bsa, y+bsa, z+bsa);
					}
				}
			}
			tessellator.draw();
		}
	}

}
