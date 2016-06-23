package tiffit.talecraft.entity.NPC;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkin;

public class QADSkinButton extends QADButton {

	NPCSkin skin;
	int entityRotation = 0;
	
	public QADSkinButton(String text, NPCSkin skin) {
		super(text);
		this.skin = skin;
	}
	
	@Override
	public void draw(int x, int y, float partial, VCUIRenderer renderer){
		super.draw(x, y, partial, renderer);
		entityRotation++;
		if(entityRotation >= 360) entityRotation = 0;
		GlStateManager.color(1, 1, 1);
		EntityNPC npc = new EntityNPC(Minecraft.getMinecraft().theWorld);
		npc.getNPCData().setSkin(skin);
		if(hovered)drawEntityOnScreen(400, 200, 60, npc);
	}
	
	
	//Taken from minecraft code
	 public void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase ent){
	        GlStateManager.enableColorMaterial();
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float)posX, (float)posY, 50.0F);
	        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
	        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
	        int mouseX = 0;
	        int mouseY = 0;
	        float f = ent.renderYawOffset;
	        float f1 = ent.rotationYaw;
	        float f2 = ent.rotationPitch;
	        float f3 = ent.prevRotationYawHead;
	        float f4 = ent.rotationYawHead;
	        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
	        RenderHelper.enableStandardItemLighting();
	        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
	        ent.renderYawOffset = entityRotation;
	        ent.rotationYaw = entityRotation;
	        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
	        ent.rotationYawHead = ent.rotationYaw;
	        ent.prevRotationYawHead = ent.rotationYaw;
	        GlStateManager.translate(0.0F, 0.0F, 0.0F);
	        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
	        rendermanager.setPlayerViewY(180.0F);
	        rendermanager.setRenderShadow(false);
	        rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
	        rendermanager.setRenderShadow(true);
	        ent.renderYawOffset = f;
	        ent.rotationYaw = f1;
	        ent.rotationPitch = f2;
	        ent.prevRotationYawHead = f3;
	        ent.rotationYawHead = f4;
	        GlStateManager.popMatrix();
	        RenderHelper.disableStandardItemLighting();
	        GlStateManager.disableRescaleNormal();
	        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	        GlStateManager.disableTexture2D();
	        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	    }

}
