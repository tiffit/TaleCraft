package tiffit.talecraft.client.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tiffit.talecraft.entity.NPC.EntityNPC;

public class RenderNPC extends RenderLivingBase<EntityNPC>{

	public RenderNPC(RenderManager manager) {
		super(manager, new ModelNPC(), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityNPC entity) {
		return entity.getNPCData().getSkin().getResourceLocation();
	}
	
    protected boolean canRenderName(EntityNPC entity){
        return entity.getNPCData().shouldShowName();
    }

	public static class NPCRenderFactory implements IRenderFactory{
		@Override
		public Render createRenderFor(RenderManager manager) {
			return new RenderNPC(manager);
			
		}
	}

	

}
