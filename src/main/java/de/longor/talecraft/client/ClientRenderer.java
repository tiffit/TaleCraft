package de.longor.talecraft.client;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.blocks.util.tileentity.BlockUpdateDetectorTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ClockBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.CollisionTriggerBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.DelayBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.EmitterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ImageHologramBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.InverterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.LightBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.MemoryBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.MessageBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RelayBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ScriptBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.StorageBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.SummonBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.TriggerFilterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.URLBlockTileEntity;
import de.longor.talecraft.client.render.IRenderable;
import de.longor.talecraft.client.render.ITemporaryRenderable;
import de.longor.talecraft.client.render.RenderModeHelper;
import de.longor.talecraft.client.render.entity.PointEntityRenderer;
import de.longor.talecraft.client.render.renderables.SelectionBoxRenderer;
import de.longor.talecraft.client.render.renderers.CustomSkyRenderer;
import de.longor.talecraft.client.render.renderers.ItemMetaWorldRenderer;
import de.longor.talecraft.client.render.tileentity.GenericTileEntityRenderer;
import de.longor.talecraft.client.render.tileentity.ImageHologramBlockTileEntityEXTRenderer;
import de.longor.talecraft.client.render.tileentity.StorageBlockTileEntityEXTRenderer;
import de.longor.talecraft.client.render.tileentity.SummonBlockTileEntityEXTRenderer;
import de.longor.talecraft.entities.EntityPoint;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import tiffit.talecraft.client.entity.RenderNPC;
import tiffit.talecraft.client.render.specialrender.LockedDoorRenderer;
import tiffit.talecraft.entity.NPC.EntityNPC;
import tiffit.talecraft.entity.projectile.EntityBomb;
import tiffit.talecraft.entity.projectile.EntityBombArrow;
import tiffit.talecraft.entity.projectile.EntityBoomerang;
import tiffit.talecraft.entity.projectile.EntityBullet;
import tiffit.talecraft.tileentity.LockedDoorTileEntity;
import tiffit.talecraft.tileentity.MusicBlockTileEntity;

public class ClientRenderer {
	private final ClientProxy proxy;
	private final Minecraft mc;

	private VisualMode visualizationMode;
	private float partialTicks;
	
	public static enum VisualMode{
		Default, Wireframe, Backface, Lighting, Nightvision;
		
		public VisualMode next(){
			int current = ordinal();
			current++;
			if(current >= values().length) current = 0;
			return values()[current];
		}
	}

	private final ConcurrentLinkedDeque<ITemporaryRenderable> temporaryRenderers;;
	private final ConcurrentLinkedDeque<IRenderable> staticRenderers;

	public ClientRenderer(ClientProxy clientProxy) {
		proxy = clientProxy;
		mc = Minecraft.getMinecraft();

		visualizationMode = VisualMode.Default;
		partialTicks = 1f;

		temporaryRenderers = new ConcurrentLinkedDeque<ITemporaryRenderable>();
		staticRenderers = new ConcurrentLinkedDeque<IRenderable>();
	}

	public void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityPoint.class, PointEntityRenderer.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new EntityBullet.EntityBulletRenderFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, new EntityBomb.EntityBombRenderFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC.NPCRenderFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityBombArrow.class, new EntityBombArrow.EntityBombArrowRenderFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityBoomerang.class, new EntityBoomerang.EntityBoomerangFactory());
	}
	
	public void init() {
		// Get the ModelMesher and register ALL item-models
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		init_render_item(mesher);
		init_render_block(mesher);

		init_render_entity();
		init_render_tilentity();
	}

	private void init_render_tilentity() {
		ClientRegistry.bindTileEntitySpecialRenderer(ClockBlockTileEntity.class,
				new GenericTileEntityRenderer<ClockBlockTileEntity>("talecraft:textures/blocks/util/timer.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(RedstoneTriggerBlockTileEntity.class,
				new GenericTileEntityRenderer<RedstoneTriggerBlockTileEntity>("talecraft:textures/blocks/util/redstoneTrigger.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(RelayBlockTileEntity.class,
				new GenericTileEntityRenderer<RelayBlockTileEntity>("talecraft:textures/blocks/util/relay.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(ScriptBlockTileEntity.class,
				new GenericTileEntityRenderer<ScriptBlockTileEntity>("talecraft:textures/blocks/util/script.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(BlockUpdateDetectorTileEntity.class,
				new GenericTileEntityRenderer<BlockUpdateDetectorTileEntity>("talecraft:textures/blocks/util/bud.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(StorageBlockTileEntity.class,
				new GenericTileEntityRenderer<StorageBlockTileEntity>("talecraft:textures/blocks/util/storage.png",
						new StorageBlockTileEntityEXTRenderer()));

		ClientRegistry.bindTileEntitySpecialRenderer(EmitterBlockTileEntity.class,
				new GenericTileEntityRenderer<EmitterBlockTileEntity>("talecraft:textures/blocks/util/emitter.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(ImageHologramBlockTileEntity.class,
				new GenericTileEntityRenderer<ImageHologramBlockTileEntity>("talecraft:textures/blocks/util/texture.png",
						new ImageHologramBlockTileEntityEXTRenderer()));

		ClientRegistry.bindTileEntitySpecialRenderer(CollisionTriggerBlockTileEntity.class,
				new GenericTileEntityRenderer<CollisionTriggerBlockTileEntity>("talecraft:textures/blocks/util/trigger.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(LightBlockTileEntity.class,
				new GenericTileEntityRenderer<LightBlockTileEntity>("talecraft:textures/blocks/util/light.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(MessageBlockTileEntity.class,
				new GenericTileEntityRenderer<MessageBlockTileEntity>("talecraft:textures/blocks/util/message.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(InverterBlockTileEntity.class,
				new GenericTileEntityRenderer<InverterBlockTileEntity>("talecraft:textures/blocks/util/inverter.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(MemoryBlockTileEntity.class,
				new GenericTileEntityRenderer<MemoryBlockTileEntity>("talecraft:textures/blocks/util/memory.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(TriggerFilterBlockTileEntity.class,
				new GenericTileEntityRenderer<TriggerFilterBlockTileEntity>("talecraft:textures/blocks/util/filter.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(DelayBlockTileEntity.class,
				new GenericTileEntityRenderer<DelayBlockTileEntity>("talecraft:textures/blocks/util/delay.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(URLBlockTileEntity.class,
				new GenericTileEntityRenderer<URLBlockTileEntity>("talecraft:textures/blocks/util/url.png"));

		ClientRegistry.bindTileEntitySpecialRenderer(SummonBlockTileEntity.class,
				new GenericTileEntityRenderer<SummonBlockTileEntity>("talecraft:textures/blocks/util/spawner.png",
						new SummonBlockTileEntityEXTRenderer()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(LockedDoorTileEntity.class, new LockedDoorRenderer());
		
		ClientRegistry.bindTileEntitySpecialRenderer(URLBlockTileEntity.class,
				new GenericTileEntityRenderer<URLBlockTileEntity>("talecraft:textures/blocks/util/url.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(MusicBlockTileEntity.class,
				new GenericTileEntityRenderer<MusicBlockTileEntity>("talecraft:textures/blocks/util/music.png"));
		
		
	}

	private void init_render_item(ItemModelMesher mesher) {
		// items
		
		for(Item item : TaleCraftItems.ALL_TC_ITEMS){
			if(!(item instanceof ItemBlock))mesher.register(item, 0, new ModelResourceLocation("talecraft:" + item.getUnlocalizedName().replace("item.", ""), "inventory"));
		}
	}

	private void init_render_block(ItemModelMesher mesher) {
		for(String name : TaleCraftBlocks.blocksMap.keySet()){
			mesher.register(Item.getItemFromBlock(TaleCraftBlocks.blocksMap.get(name)), 0, new ModelResourceLocation("talecraft:" + name, "inventory"));
		}
		
		// killblock (why?!)
		for(int i = 0; i < 7; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.killBlock), i, new ModelResourceLocation("talecraft:killblock", "inventory"));

		// decoration blocks
		// blank block
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.blankBlock), i, new ModelResourceLocation("talecraft:blankblock", "inventory"));

		// stone block A
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_a), i, new ModelResourceLocation("talecraft:deco_stone/block"+i, "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_a), mkstrlfint("talecraft:deco_stone/block", 0));
		// stone block B
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_b), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+16), "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_b), mkstrlfint("talecraft:deco_stone/block", 16));
		// stone block C
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_c), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+32), "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_c), mkstrlfint("talecraft:deco_stone/block", 32));
		// stone block D
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_d), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+48), "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_d), mkstrlfint("talecraft:deco_stone/block", 48));
		// stone block E
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_e), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+64), "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_e), mkstrlfint("talecraft:deco_stone/block", 64));
		// stone block E
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_f), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+80), "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_f), mkstrlfint("talecraft:deco_stone/block", 80));

		// wood block A
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_wood_a), i, new ModelResourceLocation("talecraft:deco_wood/block"+i, "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_wood_a), mkstrlfint("talecraft:deco_wood/block", 0));

		// glass block A
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_glass_a), i, new ModelResourceLocation("talecraft:deco_glass/block"+i, "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_glass_a), mkstrlfint("talecraft:deco_glass/block", 0));

		// cage block A
		for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_cage_a), i, new ModelResourceLocation("talecraft:deco_cage/block"+i, "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.deco_cage_a), mkstrlfint("talecraft:deco_cage/block", 0));
		
		//Locked Door Block
		for(int i = 0; i < 8; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.lockedDoorBlock), i, new ModelResourceLocation("talecraft:lockeddoorblock", "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.lockedDoorBlock), new ResourceLocation("talecraft:lockeddoorblock"));
		
		for(int i = 0; i < 12; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.spikeBlock), i, new ModelResourceLocation("talecraft:spikeblock", "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(TaleCraftBlocks.spikeBlock), new ResourceLocation("talecraft:spikeblock"));

	}

	private ResourceLocation[] mkstrlfint(String string, int j) {
		String[] ary = new String[16];

		for(int i = 0; i < 16; i++)
			ary[i] = string + (j+i);
		ResourceLocation[] res = new ResourceLocation[16];
		for(int i = 0; i < 16; i++){
			res[i] = new ResourceLocation(ary[i]);
		}
		return res;
	}

	private void init_render_entity() {

	}

	public void addStaticRenderer(SelectionBoxRenderer selectionBoxRenderer) {
		staticRenderers.offer(selectionBoxRenderer);
	}

	/****/
	public void addTemporaryRenderer(ITemporaryRenderable renderable) {
		temporaryRenderers.offer(renderable);
	}

	public void clearTemporaryRenderers() {
		temporaryRenderers.clear();
	}

	public VisualMode getVisualizationMode() {
		return visualizationMode;
	}

	/****/
	public void setVisualizationMode(VisualMode mode) {
		visualizationMode = mode;
	}

	/****/
	public int getTemporablesCount() {
		return temporaryRenderers.size();
	}

	/****/
	public int getStaticCount() {
		return staticRenderers.size();
	}

	/****/
	public float getLastPartialTicks() {
		return partialTicks;
	}




	// some empty space here





	public void on_render_world_post(RenderWorldLastEvent event) {
		RenderModeHelper.DISABLE();
		partialTicks = event.getPartialTicks();

		// Iterate trough all ITemporaryRenderables and remove the ones that can be removed.
		Iterator<ITemporaryRenderable> iterator = temporaryRenderers.iterator();
		while(iterator.hasNext()) {
			ITemporaryRenderable itr = iterator.next();
			if(itr.canRemove()) {
				iterator.remove();
			}
		}

		// If the world and the player exist, call the worldPostRender-method.
		if(mc.theWorld != null && mc.thePlayer != null) {
			GlStateManager.pushMatrix();

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();

			on_render_world_post_sub(partialTicks, tessellator, vertexbuffer);

			GlStateManager.popMatrix();
		}

		// Enable textures again, since the GUI-prerender doesn't enable it again by itself.
		GlStateManager.enableTexture2D();
	}

	private void on_render_world_post_sub(double partialTicks, Tessellator tessellator, VertexBuffer vertexbuffer) {

		// Translate into World-Space
		double px = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * partialTicks;
		double py = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * partialTicks;
		double pz = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * partialTicks;
		GL11.glTranslated(-px, -py, -pz);

		GlStateManager.disableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.enableStandardItemLighting();

		// Render all the renderables
		for(IRenderable renderable : staticRenderers) {
			renderable.render(mc, proxy, tessellator, vertexbuffer, partialTicks);
		}

		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		// Render all the temporary renderables
		for(ITemporaryRenderable renderable : temporaryRenderers) {
			renderable.render(mc, proxy, tessellator, vertexbuffer, partialTicks);
		}

		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		// Render Item Meta Renderables
		if(mc.thePlayer != null && mc.thePlayer.getHeldItemMainhand() != null) {
			ItemStack stack = mc.thePlayer.getHeldItemMainhand();
			Item item = stack.getItem();

			ItemMetaWorldRenderer.tessellator = tessellator;
			ItemMetaWorldRenderer.vertexbuffer = vertexbuffer;
			ItemMetaWorldRenderer.partialTicks = partialTicks;
			ItemMetaWorldRenderer.partialTicksF = (float) partialTicks;
			ItemMetaWorldRenderer.clientProxy = proxy;
			ItemMetaWorldRenderer.world = mc.theWorld;
			ItemMetaWorldRenderer.player = mc.thePlayer;
			ItemMetaWorldRenderer.playerPosition = new BlockPos(px, py, pz);
			ItemMetaWorldRenderer.render(item, stack);
		}

		if(mc.thePlayer != null && mc.thePlayer.getHeldItemOffhand() != null) {
			ItemStack stack = mc.thePlayer.getHeldItemOffhand();
			Item item = stack.getItem();

			ItemMetaWorldRenderer.tessellator = tessellator;
			ItemMetaWorldRenderer.vertexbuffer = vertexbuffer;
			ItemMetaWorldRenderer.partialTicks = partialTicks;
			ItemMetaWorldRenderer.partialTicksF = (float) partialTicks;
			ItemMetaWorldRenderer.clientProxy = proxy;
			ItemMetaWorldRenderer.world = mc.theWorld;
			ItemMetaWorldRenderer.player = mc.thePlayer;
			ItemMetaWorldRenderer.playerPosition = new BlockPos(px, py, pz);
			ItemMetaWorldRenderer.render(item, stack);
		}

		GlStateManager.enableCull();;
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.enableStandardItemLighting();
	}

	public void on_world_unload() {
		temporaryRenderers.clear();
		visualizationMode = VisualMode.Default;
	}
	
	public void on_render_world_terrain_pre(RenderTickEvent revt) {
		// this takes care of the CUSTOM SKY RENDERING
		if(mc.theWorld != null && mc.theWorld.provider != null) {
			boolean wireframeModeActive = proxy.isBuildMode() ? (visualizationMode != VisualMode.Default) : false;

			if(wireframeModeActive) {
				CustomSkyRenderer.instance.setDebugSky(true);
				mc.theWorld.provider.setSkyRenderer(CustomSkyRenderer.instance);
			} else {
				CustomSkyRenderer.instance.setDebugSky(false);
				mc.theWorld.provider.setSkyRenderer(null);
			}
		}

		// this enables the WIREFRAME-MODE if we are ingame
		if(mc.theWorld != null && mc.thePlayer != null) {
			RenderModeHelper.ENABLE(mc.thePlayer.capabilities.isCreativeMode ? visualizationMode : VisualMode.Default);

			if(visualizationMode == VisualMode.Default) {
				mc.gameSettings.clouds = 1;
			} else {
				mc.gameSettings.clouds = 0;
			}

			// this is part of the LIGHTING visualization mode
			if(visualizationMode == VisualMode.Lighting) {
				GlStateManager.disableTexture2D();
			}

			if(visualizationMode == VisualMode.Nightvision) {
				GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
				GlStateManager.disableTexture2D();
				GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
				GlStateManager.disableFog();
			}
		}
	}

	public void on_render_world_terrain_post(RenderTickEvent revt) {
		if(mc.ingameGUI != null && mc.theWorld != null) {
			if(proxy.getInfoBar().canDisplayInfoBar(mc, proxy)) {
				proxy.getInfoBar().display(mc, mc.theWorld, proxy);

				// XXX: Move this to its own IF
				proxy.getInvokeTracker().display(mc, proxy);
			}
		}
	}

	public static ClientFadeEffect fadeEffect = null;
	
	public void on_render_world_hand_post(RenderHandEvent event) {
		if(fadeEffect != null && mc.ingameGUI != null){
			fadeEffect.render();

			// Do NOT draw the hand!
			event.setCanceled(true);
		}
		GlStateManager.enableTexture2D();
//		// If active, render a fade-effect (this makes the screen go dark).
//		// This overlays everything except the hand and the GUI, which is wrong.
//		double fade = 0.5f;
//		int color = 0xff0000;
//		if(fade > 0 && mc.ingameGUI != null) {
//			// Draw Overlay
//			GL11.glMatrixMode(GL11.GL_PROJECTION);
//			GL11.glLoadIdentity();
//			GLU.gluOrtho2D(0, 2, 2, 0);
//			GL11.glMatrixMode(GL11.GL_MODELVIEW);
//			GL11.glLoadIdentity();
//
//			{
//				int alpha = MathHelper.clamp_int((int) (fade * 255), 0, 255);
//				int mixed = ((alpha & 0xFF) << 24) | (color);
//				Gui.drawRect(-1, -1, 4, 4, mixed);
//			}
//
//			RenderHelper.disableStandardItemLighting();
//
//			// Do NOT draw the hand!
//			event.setCanceled(true);
//		}
//
//		// Enable for reasons stated in:
//		// ClientProxy..worldPass() -> Last line of code.
//		GlStateManager.enableTexture2D();
	}



}
