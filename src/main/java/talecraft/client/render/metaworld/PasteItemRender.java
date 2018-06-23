package talecraft.client.render.metaworld;

import static talecraft.clipboard.ClipboardTagNames.$REGION_PALLET;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import talecraft.TaleCraft;
import talecraft.client.ClientResources;
import talecraft.client.render.renderers.BoxRenderer;
import talecraft.clipboard.ClipboardItem;
import talecraft.clipboard.ClipboardTagNames;
import talecraft.proxy.ClientProxy;
import talecraft.util.GObjectTypeHelper;
import talecraft.util.NBTHelper;

public class PasteItemRender implements IMetadataRender {

	@Override
	public void render(Item item, ItemStack stack, Tessellator tessellator, BufferBuilder buffer, double partialTick, BlockPos playerPos, EntityPlayerSP player, WorldClient world) {
		ClipboardItem clip = TaleCraft.asClient().getClipboard();

		if(clip == null)
			return;

		float lenMul = ClientProxy.settings.getInteger("item.paste.reach");
		Vec3d plantPos = player.getLook((float) partialTick);
		plantPos = new Vec3d(
				plantPos.x*lenMul,
				plantPos.y*lenMul,
				plantPos.z*lenMul
				).add(player.getPositionEyes((float) partialTick));

		float dimX = 0;
		float dimY = 0;
		float dimZ = 0;

		NBTTagCompound blocks = NBTHelper.getOrNull(clip.getData(), ClipboardTagNames.$REGION);
		NBTTagCompound entity = NBTHelper.getOrNull(clip.getData(), ClipboardTagNames.$ENTITY);

		if(clip.getData().hasKey(ClipboardTagNames.$OFFSET, clip.getData().getId())) {
			NBTTagCompound offset = clip.getData().getCompoundTag(ClipboardTagNames.$OFFSET);
			plantPos = new Vec3d(
					plantPos.x + offset.getFloat("x"),
					plantPos.y + offset.getFloat("y"),
					plantPos.z + offset.getFloat("z")
					);
		}

		float snap = ClientProxy.settings.getInteger("item.paste.snap");
		if(snap > 1) {
			plantPos = new Vec3d(
					Math.floor(plantPos.x / snap) * snap,
					Math.floor(plantPos.y / snap) * snap,
					Math.floor(plantPos.z / snap) * snap
					);
		}

		float color = 0;
		
		if(blocks != null) {
			color = -2;

			dimX = blocks.getInteger(ClipboardTagNames.$REGION_WIDTH);
			dimY = blocks.getInteger(ClipboardTagNames.$REGION_HEIGHT);
			dimZ = blocks.getInteger(ClipboardTagNames.$REGION_LENGTH);
			
			plantPos = new Vec3d(
					Math.floor(plantPos.x),
					Math.floor(plantPos.y),
					Math.floor(plantPos.z)
					);
		}

		if(entity != null) {
			color = -3;

			float width = entity.getFloat("tc_width");
			float height = entity.getFloat("tc_height");

			dimX = width;
			dimY = height;
			dimZ = width;

			// float shift = 0.5f;
			plantPos = plantPos.subtract(width/2, 0, width/2);
		}

		float minX = (float) plantPos.x;
		float minY = (float) plantPos.y;
		float minZ = (float) plantPos.z;
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
		BoxRenderer.renderSelectionBox(tessellator, buffer, minX, minY, minZ, maxX, maxY, maxZ, color);
		if(blocks != null){
			NBTTagList pallet = blocks.getTagList($REGION_PALLET, new NBTTagString().getId());
			IBlockState[] palletRaw = new IBlockState[pallet.tagCount()];
			for(int i = 0; i < pallet.tagCount(); i++) {
				String typeString = pallet.getStringTagAt(i);
				IBlockState state = palletRaw[i] = GObjectTypeHelper.findBlockState(typeString);
				
				if(state != null) {
					// Dont do a thing.
				} else {
					System.out.println("Could not locate block type: " + typeString + " -> " + i);
				}
			}
			
			/*
			int regionWidth = blocks.getInteger(ClipboardTagNames.$REGION_WIDTH);
			int regionHeight = blocks.getInteger(ClipboardTagNames.$REGION_HEIGHT);
			int regionLength = blocks.getInteger(ClipboardTagNames.$REGION_LENGTH);
			int[] blockData = blocks.getIntArray($REGION_DATA);
			for(int Yx = 0; Yx < regionHeight; Yx++) {
				for(int Zx = 0; Zx < regionLength; Zx++) {
					for(int Xx = 0; Xx < regionWidth; Xx++) {
						int index = (Yx*regionWidth*regionLength) + (Zx*regionWidth) + (Xx);
						int type = blockData[index];
						IBlockState state = palletRaw[type];
						int blockY = Yx + (int)minY + 1;
						int blockZ = Zx + (int)minZ;
						int blockX = Xx + (int)minX;
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						IBakedModel model = ClientProxy.mc.getBlockRendererDispatcher().getModelForState(state);
					}
				}
			}
			*/
		}
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
		}
	}
	

}
