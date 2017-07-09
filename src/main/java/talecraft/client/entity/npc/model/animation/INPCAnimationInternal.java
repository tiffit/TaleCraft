package talecraft.client.entity.npc.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import talecraft.client.entity.npc.model.NPCModel;
import talecraft.client.entity.npc.model.NPCModelData;
import talecraft.entity.NPC.EntityNPC;

public interface INPCAnimationInternal {

	public void animate(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, NPCModel model, NPCModelData data);

	public static class ZombieAnimation extends BipedAnimation {

		@Override
		public void animate(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, NPCModel model, NPCModelData data) {
			super.animate(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn, model, data);
			ModelRenderer bipedRightArm = data.animation.get("rightarm");
			ModelRenderer bipedLeftArm = data.animation.get("leftarm");

			boolean flag = false; // Raise arms
			float f = MathHelper.sin(model.swingProgress * (float) Math.PI);
			float f1 = MathHelper.sin((1.0F - (1.0F - model.swingProgress) * (1.0F - model.swingProgress)) * (float) Math.PI);

			bipedRightArm.rotateAngleZ = 0.0F;
			bipedLeftArm.rotateAngleZ = 0.0F;
			bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
			bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
			float f2 = -(float) Math.PI / (flag ? 1.5F : 2.25F);
			bipedRightArm.rotateAngleX = f2;
			bipedLeftArm.rotateAngleX = f2;
			bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
			bipedLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
			bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
			bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		}

	}

	public static class PlayerAnimation extends BipedAnimation {

		@Override
		public void animate(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, NPCModel model, NPCModelData data) {
			super.animate(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn, model, data);
			ModelBiped.copyModelAngles(data.animation.get("leftleg"), data.animation.get("leftlegwear"));
			ModelBiped.copyModelAngles(data.animation.get("rightleg"), data.animation.get("rightlegwear"));
			ModelBiped.copyModelAngles(data.animation.get("leftarm"), data.animation.get("leftarmwear"));
			ModelBiped.copyModelAngles(data.animation.get("rightarm"), data.animation.get("rightarmwear"));
			ModelBiped.copyModelAngles(data.animation.get("body"), data.animation.get("bodywear"));

		}

	}

	public static class BipedAnimation implements INPCAnimationInternal {

		@Override
		public void animate(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, NPCModel model, NPCModelData data) {

			EntityNPC npc = (EntityNPC) entityIn;

			boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;

			ModelRenderer bipedRightArm = data.animation.get("rightarm");
			ModelRenderer bipedLeftArm = data.animation.get("leftarm");
			ModelRenderer bipedHead = data.animation.get("head");
			ModelRenderer bipedBody = data.animation.get("body");
			ModelRenderer bipedRightLeg = data.animation.get("rightleg");
			ModelRenderer bipedLeftLeg = data.animation.get("leftleg");
			ModelRenderer bipedHeadwear = data.animation.get("headwear");

			bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;

			if (flag) {
				bipedHead.rotateAngleX = -((float) Math.PI / 4F);
			} else {
				bipedHead.rotateAngleX = headPitch * 0.017453292F;
			}

			bipedBody.rotateAngleY = 0.0F;
			bipedRightArm.rotationPointZ = 0.0F;
			bipedRightArm.rotationPointX = -5.0F;
			bipedLeftArm.rotationPointZ = 0.0F;
			bipedLeftArm.rotationPointX = 5.0F;
			float f = 1.0F;

			if (flag) {
				f = (float) (entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
				f = f / 0.2F;
				f = f * f * f;
			}

			if (f < 1.0F) {
				f = 1.0F;
			}

			bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
			bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
			bipedRightArm.rotateAngleZ = 0.0F;
			bipedLeftArm.rotateAngleZ = 0.0F;
			bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
			bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
			bipedRightLeg.rotateAngleY = 0.0F;
			bipedLeftLeg.rotateAngleY = 0.0F;
			bipedRightLeg.rotateAngleZ = 0.0F;
			bipedLeftLeg.rotateAngleZ = 0.0F;

			if (model.isRiding) {
				bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
				bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
				bipedRightLeg.rotateAngleX = -1.4137167F;
				bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
				bipedRightLeg.rotateAngleZ = 0.07853982F;
				bipedLeftLeg.rotateAngleX = -1.4137167F;
				bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
				bipedLeftLeg.rotateAngleZ = -0.07853982F;
			}

			bipedRightArm.rotateAngleY = 0.0F;
			bipedRightArm.rotateAngleZ = 0.0F;

			switch (model.leftArmPose) {
			case EMPTY:
				bipedLeftArm.rotateAngleY = 0.0F;
				break;
			case BLOCK:
				bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
				bipedLeftArm.rotateAngleY = 0.5235988F;
				break;
			case ITEM:
				bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
				bipedLeftArm.rotateAngleY = 0.0F;
			}

			switch (model.rightArmPose) {
			case EMPTY:
				bipedRightArm.rotateAngleY = 0.0F;
				break;
			case BLOCK:
				bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
				bipedRightArm.rotateAngleY = -0.5235988F;
				break;
			case ITEM:
				bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
				bipedRightArm.rotateAngleY = 0.0F;
			}

			if (model.swingProgress > 0.0F) {
				EnumHandSide enumhandside = model.getMainHand(entityIn);
				ModelRenderer modelrenderer = model.getArmForSide(enumhandside, data);
				float f1 = model.swingProgress;
				bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

				if (enumhandside == EnumHandSide.LEFT) {
					bipedBody.rotateAngleY *= -1.0F;
				}

				bipedRightArm.rotationPointZ = MathHelper.sin(bipedBody.rotateAngleY) * 5.0F;
				bipedRightArm.rotationPointX = -MathHelper.cos(bipedBody.rotateAngleY) * 5.0F;
				bipedLeftArm.rotationPointZ = -MathHelper.sin(bipedBody.rotateAngleY) * 5.0F;
				bipedLeftArm.rotationPointX = MathHelper.cos(bipedBody.rotateAngleY) * 5.0F;
				bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
				bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
				bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
				f1 = 1.0F - model.swingProgress;
				f1 = f1 * f1;
				f1 = f1 * f1;
				f1 = 1.0F - f1;
				float f2 = MathHelper.sin(f1 * (float) Math.PI);
				float f3 = MathHelper.sin(model.swingProgress * (float) Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				modelrenderer.rotateAngleX = (float) ((double) modelrenderer.rotateAngleX - ((double) f2 * 1.2D + (double) f3));
				modelrenderer.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
				modelrenderer.rotateAngleZ += MathHelper.sin(model.swingProgress * (float) Math.PI) * -0.4F;
			}

			if (model.isSneak) {
				bipedBody.rotateAngleX = 0.5F;
				bipedRightArm.rotateAngleX += 0.4F;
				bipedLeftArm.rotateAngleX += 0.4F;
				bipedRightLeg.rotationPointZ = 4.0F;
				bipedLeftLeg.rotationPointZ = 4.0F;
				bipedRightLeg.rotationPointY = 9.0F;
				bipedLeftLeg.rotationPointY = 9.0F;
				bipedHead.rotationPointY = 1.0F;
			} else {
				bipedBody.rotateAngleX = 0.0F;
				bipedRightLeg.rotationPointZ = 0.1F;
				bipedLeftLeg.rotationPointZ = 0.1F;
				bipedRightLeg.rotationPointY = 12.0F;
				bipedLeftLeg.rotationPointY = 12.0F;
				bipedHead.rotationPointY = 0.0F;
			}

			bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
			bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

			if (model.rightArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
				bipedRightArm.rotateAngleY = -0.1F + bipedHead.rotateAngleY;
				bipedLeftArm.rotateAngleY = 0.1F + bipedHead.rotateAngleY + 0.4F;
				bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + bipedHead.rotateAngleX;
				bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + bipedHead.rotateAngleX;
			} else if (model.leftArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
				bipedRightArm.rotateAngleY = -0.1F + bipedHead.rotateAngleY - 0.4F;
				bipedLeftArm.rotateAngleY = 0.1F + bipedHead.rotateAngleY;
				bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + bipedHead.rotateAngleX;
				bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + bipedHead.rotateAngleX;
			}

			ModelBiped.copyModelAngles(bipedHead, bipedHeadwear);
		}

	}

}
