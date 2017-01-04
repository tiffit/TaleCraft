package talecraft.client.render.metaworld;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderPainting;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class CustomPaintingRender implements IMetadataRender{

	private final RenderPainting RENDER;
	private final Minecraft mc;
	
	public CustomPaintingRender() {
		mc = Minecraft.getMinecraft();
		RENDER = new RenderPainting(mc.getRenderManager());
	}
	
	@Override
	public void render(Item item, ItemStack stack, Tessellator tessellator, VertexBuffer buffer, double partialTick, BlockPos playerPos, EntityPlayerSP player, WorldClient world) {
		EnumArt painting = EnumArt.valueOf(stack.getTagCompound().getString("art"));
		RayTraceResult result = player.rayTrace(5, (float)partialTick);
		if(result.typeOfHit == Type.BLOCK){
			BlockPos pos = result.getBlockPos();
			EnumFacing facing = result.sideHit;
			if(!facing.getAxis().isHorizontal())return;
			EntityPainting ent = new EntityPainting(world, pos.offset(facing.getOpposite(), 1), facing);
			ent.art = painting;
			GlStateManager.enableBlend();
			GL11.glColor4f(1f, 1f, 1f, 0.5f);
			double xMove = 0;
			if(facing.getFrontOffsetX() == 0)xMove = (painting.sizeX != 32 ?  painting.sizeX >= 48 ? -painting.sizeX/16/8 : 0.5 : 0);
			double zMove = 0;
			if(facing.getFrontOffsetZ() == 0)zMove = (painting.sizeX != 32 ?  painting.sizeX >= 48 ? -painting.sizeX/16/8 : 0.5 : 0);
			if(painting.sizeX == 16){
				if(facing.getOpposite() == EnumFacing.EAST)zMove -= 1;
				if(facing.getOpposite() == EnumFacing.NORTH)xMove -= 1;
			}
			double yMove = (painting.sizeY == 16  || painting.sizeY == 16*3 ? 0 : 0.5);
			RENDER.doRender(ent, ent.posX + facing.getFrontOffsetX()*2 + xMove, ent.posY + yMove, ent.posZ + facing.getFrontOffsetZ()*2 + zMove, ent.rotationYaw, (float)partialTick);
		}
	}

}
