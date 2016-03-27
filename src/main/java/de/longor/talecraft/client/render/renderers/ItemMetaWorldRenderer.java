package de.longor.talecraft.client.render.renderers;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.clipboard.ClipboardTagNames;
import de.longor.talecraft.items.PasteItem;
import de.longor.talecraft.items.VoxelBrushItem;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.util.NBTHelper;
import de.longor.talecraft.voxelbrush_old.IShape;
import de.longor.talecraft.voxelbrush_old.ShapeFactory;
import de.longor.talecraft.voxelbrush_old.shapes.CylinderShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class ItemMetaWorldRenderer {
	// CLIENT
	public static ClientProxy clientProxy;
	public static Tessellator tessellator;
	public static VertexBuffer vertexbuffer;
	public static double partialTicks;
	public static float partialTicksF;
	// CLIENT.PLAYER
	public static Vec3d playerPosition;
	public static EntityPlayerSP player;
	public static WorldClient world;

	// RENDER
	public static void render(Item itemType, ItemStack itemStack) {

		if(itemType instanceof VoxelBrushItem) {
			renderVoxelBrushItem(itemStack);
		}

		if(itemType instanceof PasteItem) {
			renderPasteItem(itemStack);
		}

	}

	private static void renderPasteItem(ItemStack itemStack) {
		ClipboardItem clip = TaleCraft.asClient().getClipboard();

		if(clip == null)
			return;

		float lenMul = ClientProxy.settings.getInteger("item.paste.reach");
		Vec3d plantPos = player.getLook(partialTicksF);
		plantPos = new Vec3d(
				plantPos.xCoord*lenMul,
				plantPos.yCoord*lenMul,
				plantPos.zCoord*lenMul
				).add(player.getPositionEyes(partialTicksF));

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
		BoxRenderer.renderSelectionBox(tessellator, vertexbuffer, minX, minY, minZ, maxX, maxY, maxZ, color);

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

			vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			vertexbuffer.color(1, 1, 1, 1);
			//vertexbuffer.setBrightness(0xEE); //TODO FIX

			for(int y = startY; y <= endY; y++) {
				for(int z = startZ; z <= endZ; z++) {
					for(int x = startX; x <= endX; x++) {
						if(x%snap==0&&y%snap==0&&z%snap==0)
							BoxRenderer.renderBoxEmb(tessellator, vertexbuffer, x+bsi, y+bsi, z+bsi, x+bsa, y+bsa, z+bsa);
					}
				}
			}
			tessellator.draw();
		}
	}

	private static void renderVoxelBrushItem(ItemStack stack) {
		float lerp = 1F;
		float dist = 256;

		Vec3d start = player.getPositionEyes(lerp);
		Vec3d direction = player.getLook(lerp);
		Vec3d end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);

		RayTraceResult result = world.rayTraceBlocks(start, end, false, false, false);

		if(result == null)
			return;

		if(result.typeOfHit != RayTraceResult.Type.BLOCK)
			return;

		if(Minecraft.getMinecraft().objectMouseOver != null) {
			if(Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
				return;
			}
			// System.out.println("WAT");
		}

		if(!stack.hasTagCompound())
			return;

		NBTTagCompound vbData = stack.getTagCompound().getCompoundTag("vbData");

		if(vbData.hasNoTags())
			return;

		renderVoxelBrushItem_do(vbData, result.getBlockPos());
	}

	private static void renderVoxelBrushItem_do(NBTTagCompound vbData, BlockPos position) {
		NBTTagCompound shapeTag = vbData.getCompoundTag("shape");

		if(shapeTag == null)
			return;
		if(shapeTag.hasNoTags())
			return;

		IShape shape = ShapeFactory.create(shapeTag.getString("type"), shapeTag, position);

		if(shape == null)
			return;

		GL11.glLineWidth(2f);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

		int[] bounds = shape.getBounds();
		boolean intersectsWorldBoundaries = bounds[1] <= 0 || bounds[1] >= 255;

		// XXX: Find out why the brush box is rendered incorrectly when the shape is offset.

		Minecraft.getMinecraft().getTextureManager().bindTexture(
				intersectsWorldBoundaries ? ClientResources.texColorYellow : ClientResources.texColorWhite
				);
		BoxRenderer.renderBox(
				tessellator, vertexbuffer,
				bounds[0], bounds[1], bounds[2], bounds[3]+1, bounds[4]+1, bounds[5]+1,
				1, 1, 1, 1
				);

		Minecraft.getMinecraft().getTextureManager().bindTexture(ClientResources.texColorBlack);
		BoxRenderer.renderBox(
				tessellator, vertexbuffer,
				position.getX(), position.getY(), position.getZ(),
				position.getX()+1, position.getY()+1, position.getZ()+1,
				0, 0, 0, 1
				);

		if(shape instanceof CylinderShape) {
			CylinderShape cyl = (CylinderShape) shape;
			final float d = 0.0125f;
			final float ey = cyl.ey -1;
			final float rad = (float) cyl.radius -1;

			int segments = 8;

			if(rad < 64) {
				segments = 128;
			} if(rad < 32) {
				segments = 128;
			} if(rad < 16) {
				segments = 64;
			} if(rad < 8) {
				segments = 32;
			}

			vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			vertexbuffer.color(1, 1, 0, 1);
			vertexbuffer.setTranslation(
					position.getX() +.5f,
					position.getY() +.5f,
					position.getZ() +.5f
					);

			for(int i = 0; i < segments; i++) {
				float t = (float)i / (float)segments;

				float ox = (float) Math.sin(t * Math.PI * 2) * rad;
				float oz = (float) Math.cos(t * Math.PI * 2) * rad;

				BoxRenderer.renderBoxEmb(tessellator, vertexbuffer, ox-d, -d -ey, oz-d, ox+d, d +ey, oz+d);
			}
			tessellator.draw();
			vertexbuffer.setTranslation(0,0,0);
		}

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glLineWidth(.5f);
		Minecraft.getMinecraft().getItemRenderer().renderItem(player, new ItemStack(Blocks.quartz_block), TransformType.NONE);
	}

}
