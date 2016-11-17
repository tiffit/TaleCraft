package talecraft.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import talecraft.TaleCraftItems;
import talecraft.entity.projectile.EntityBoomerang;

public class RenderBoomerang extends Render<EntityBoomerang> {

	public RenderBoomerang(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRender(EntityBoomerang entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        bindEntityTexture(entity);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 1.0F);
        GlStateManager.rotate(entity.getRotation(), 0.0F, 0.0F, 1.0F);
        //GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, -0.0F);
        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(TaleCraftItems.boomerang), ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBoomerang entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
