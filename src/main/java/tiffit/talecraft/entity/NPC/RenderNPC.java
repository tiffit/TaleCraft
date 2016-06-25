package tiffit.talecraft.entity.NPC;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderNPC extends RenderLivingBase<EntityNPC>{

	public RenderNPC(RenderManager manager) {
		super(manager, new ModelPlayer(0.0F, false), 0.5F);
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
