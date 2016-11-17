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
import talecraft.entity.projectile.EntityKnife;

public class RenderKnife extends Render<EntityKnife> {

	public RenderKnife(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRender(EntityKnife entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        bindEntityTexture(entity);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 35, 0.0F, 0.0F, 1.0F);
        float scale = 1.3F;
        GlStateManager.scale(scale, scale, scale);
        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(TaleCraftItems.knife), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityKnife entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
