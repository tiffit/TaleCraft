package talecraft.client.entity;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import talecraft.client.entity.npc.models.NPCModelToModelBase;
import talecraft.entity.NPC.EntityNPC;

public class ModelNPC extends ModelBiped {
	
	private ModelBase lastUsedModel;
	
	@Override
	public void render(Entity entity, float swing, float swing_amount, float age, float netHeadYaw, float headPitch, float scale) {
		getCurrentModel(entity).render(entity, swing, swing_amount, age, netHeadYaw, headPitch, scale);
	}
	
    @Override
		public void setRotationAngles(float swing, float swing_amount, float age, float netHeadYaw, float headPitch, float scaleFactor, Entity entity){
    	getCurrentModel(entity).setRotationAngles(swing, swing_amount, age, netHeadYaw, headPitch, headPitch, entity);
    }
    
    @Override
		public void setLivingAnimations(EntityLivingBase entityLiving, float swing_amount, float age, float partialTickTime){
    	getCurrentModel(entityLiving).setLivingAnimations(entityLiving, swing_amount, age, partialTickTime);
    }
    
    @Override
    public ModelRenderer getRandomModelBox(Random rand) {
    	return lastUsedModel.getRandomModelBox(rand);
    }
    
    @Override
    public TextureOffset getTextureOffset(String partName) {
    	return lastUsedModel.getTextureOffset(partName);
    }
    
    @Override
    public void setModelAttributes(ModelBase model) {
    	lastUsedModel.setModelAttributes(model);
    }
    
    @Override
    public void postRenderArm(float scale, EnumHandSide side) {
    	if(lastUsedModel instanceof ModelBiped){
    		ModelBiped biped = (ModelBiped) lastUsedModel;
    		biped.postRenderArm(scale, side);
    	}
    }
    
    private ModelBase getCurrentModel(Entity entity){
    	EntityNPC npc = (EntityNPC) entity;
    	ModelBase model = getModelForEntity(npc);
    	this.lastUsedModel = model;
    	return model;
    }
    
    public static ModelBase getModelForEntity(EntityNPC npc){
    	return NPCModelToModelBase.getModel(npc.getNPCData().getModel());
    }

}
