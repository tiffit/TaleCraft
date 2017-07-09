package talecraft.client.entity.npc.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import talecraft.client.entity.npc.model.animation.INPCAnimationInternal;
import talecraft.entity.NPC.EntityNPC;

public class NPCModel extends ModelBiped {

	@Override
	public void render(Entity entity, float swingTime, float swingAmunt, float tick, float yaw, float pitch, float scale) {
		EntityNPC npc = (EntityNPC) entity;
		NPCModelData model_data = npc.model_data;
		if (model_data == null) {
			model_data = new NPCModelData(this);
			npc.model_data = model_data;
		}
		setRotationAngles(swingTime, swingAmunt, tick, yaw, pitch, scale, entity);
		GlStateManager.pushMatrix();
		GlStateManager.popMatrix();
		int index = 0;
		textureWidth = model_data.textureWidth;
		textureHeight = model_data.textureHeight;
		for (ModelRenderer part : model_data.animation.values()) {
			part.render(1 / 16F);
			index++;
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		EntityNPC npc = (EntityNPC) entityIn;
		NPCModelData model_data = npc.model_data;
		if (model_data == null) {
			model_data = new NPCModelData(this);
			npc.model_data = model_data;
		}
		if (model_data.currentModel.animation.equals("internal:zombie")) {
			new INPCAnimationInternal.ZombieAnimation().animate(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn, this, model_data);
		}
		if (model_data.currentModel.animation.equals("internal:player")) {
			new INPCAnimationInternal.PlayerAnimation().animate(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn, this, model_data);
		}
	}

	@Override
	public EnumHandSide getMainHand(Entity entityIn) {
		return super.getMainHand(entityIn);
	}

	public ModelRenderer getArmForSide(EnumHandSide side, NPCModelData data) {
		return side == EnumHandSide.LEFT ? data.animation.get("leftarm") : data.animation.get("leftarm");
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
	}

}
