package talecraft.client.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.NPC.EnumNPCSkin;

public class RenderNPC extends RenderLivingBase<EntityNPC> {

	public RenderNPC(RenderManager manager) {
		super(manager, new ModelNPC(), 0.5F);
		this.addLayer(new LayerBipedArmor(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityNPC entity) {
		if (entity.getNPCData().getSkin() == EnumNPCSkin.Custom)
			return new ResourceLocation("talecraft:textures/entity/" + entity.getNPCData().getCustomSkin());
		return entity.getNPCData().getSkin().getResourceLocation();
	}

	@Override
	protected boolean canRenderName(EntityNPC entity) {
		return entity.getNPCData().shouldShowName();
	}

	@SuppressWarnings("rawtypes")
	public static class NPCRenderFactory implements IRenderFactory {
		@Override
		public Render createRenderFor(RenderManager manager) {
			return new RenderNPC(manager);

		}
	}

}
