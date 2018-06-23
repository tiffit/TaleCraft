package talecraft.proxy;

import java.util.concurrent.ConcurrentLinkedDeque;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import talecraft.TaleCraftSounds;
import talecraft.TaleCraft;
import talecraft.TaleCraftItems;
import talecraft.client.ClientKeyboardHandler;
import talecraft.client.ClientNetworkHandler;
import talecraft.client.ClientRenderer;
import talecraft.client.ClientSettings;
import talecraft.client.InfoBar;
import talecraft.client.InvokeTracker;
import talecraft.client.commands.TaleCraftClientCommands;
import talecraft.client.environment.Environments;
import talecraft.client.gui.entity.npc.GuiNPCMerchant;
import talecraft.client.render.metaworld.CustomPaintingRender;
import talecraft.client.render.metaworld.PasteItemRender;
import talecraft.client.render.renderables.SelectionBoxRenderer;
import talecraft.client.render.renderers.ItemMetaWorldRenderer;
import talecraft.clipboard.ClipboardItem;
import talecraft.entity.NPC.NPCShop;
import talecraft.util.ReflectionUtil;

public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener {
	// All the singletons!
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static final ClientSettings settings = new ClientSettings();
	public static ClientProxy proxy = (ClientProxy) TaleCraft.proxy;

	// tc internals
	private ClipboardItem currentClipboardItem;
	private InfoBar infoBarInstance;
	private InvokeTracker invokeTracker;
	private ClientNetworkHandler clientNetworkHandler;
	private ClientKeyboardHandler clientKeyboardHandler;
	private ClientRenderer clientRenderer;
	private ConcurrentLinkedDeque<Runnable> clientTickQeue;
	
	//Client settings
	public GameRules gamerules;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		settings.init();

		MinecraftForge.EVENT_BUS.register(this);

		clientKeyboardHandler = new ClientKeyboardHandler(this);
		TaleCraftClientCommands.init();

		IReloadableResourceManager resManager = (IReloadableResourceManager) mc.getResourceManager();
		resManager.registerReloadListener(this);
		
		clientTickQeue = new ConcurrentLinkedDeque<Runnable>();
		clientRenderer = new ClientRenderer(this);
		clientRenderer.preInit();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		// create client network'er
		clientNetworkHandler = new ClientNetworkHandler(this);

		// add all static renderers
		clientRenderer.addStaticRenderer(new SelectionBoxRenderer());
		
		ReflectionUtil.replaceMusicTicker();
		
	} // init(..){}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);

		// Create the InfoBar Instance
		infoBarInstance = new InfoBar();

		// Create the invoke tracker instance
		invokeTracker = new InvokeTracker();
		
		ItemMetaWorldRenderer.ITEM_RENDERS.put(TaleCraftItems.paste, new PasteItemRender());
		ItemMetaWorldRenderer.ITEM_RENDERS.put(TaleCraftItems.custompainting, new CustomPaintingRender());
		
		gamerules = new GameRules();
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		Environments.reload(resourceManager);
	}

	@SubscribeEvent
	public void worldPass(RenderWorldLastEvent event) {
		clientRenderer.on_render_world_post(event);
	}
	
	private NPCShop lastOpened;
	
	
	@SubscribeEvent
	public void npcTradeOpen(GuiOpenEvent event){
		if(event.getGui() instanceof GuiMerchant){
			if(((GuiMerchant) event.getGui()).getMerchant() instanceof NPCShop){
				lastOpened = (NPCShop) ((GuiMerchant) event.getGui()).getMerchant();
			}else{
				Minecraft mc = Minecraft.getMinecraft();
				event.setGui(new GuiNPCMerchant(mc.player.inventory, lastOpened, mc.world));
			}
		}
	}

	@SubscribeEvent
	public void worldPostRenderHand(RenderHandEvent event) {
		clientRenderer.on_render_world_hand_post(event);
	}

	/**
	 * This method is called when the world is unloaded.
	 **/
	@Override
	public void unloadWorld(World world) {
		if(world instanceof WorldClient) {
			// the client is either changing dimensions or leaving the server.
			// reset all temporary world related settings here
			// delete all temporary world related objects here

			clientRenderer.on_world_unload();

			// This is stupid but,
			// Save the TaleCraft settings on World unload.
			// Just to be sure...
			settings.save();
		}
	}

	/**
	 * @return TRUE, if the client is in build-mode (aka: creative-mode), FALSE if not.
	 **/
	@Override
	public boolean isBuildMode() {
		return mc.playerController != null && mc.playerController.isInCreativeMode();
	}
	
	@Override
	public void tick(TickEvent event) {
		super.tick(event);

		if(event instanceof ClientTickEvent) {
			while(!clientTickQeue.isEmpty())
				clientTickQeue.poll().run();
		}
		if(event instanceof RenderTickEvent) {
			RenderTickEvent revt = (RenderTickEvent) event;

			// Pre-Scene Render
			if(revt.phase == Phase.START) {
				clientRenderer.on_render_world_terrain_pre(revt);
			} else
				// Post-World >> Pre-HUD Render
				if(revt.phase == Phase.END) {
					clientRenderer.on_render_world_terrain_post(revt);
				}
		}
	}

	/***********************************/
	/**                               **/
	/**                               **/
	/**                               **/
	/***********************************/

	/****/
	@Override
	public NBTTagCompound getSettings(EntityPlayer playerIn) {
		return getSettings().getNBT();
	}

	/****/
	public ClientSettings getSettings() {
		return settings;
	}

	public InfoBar getInfoBar() {
		return infoBarInstance;
	}

	public InvokeTracker getInvokeTracker() {
		return invokeTracker;
	}

	public ClientNetworkHandler getNetworkHandler() {
		return clientNetworkHandler;
	}

	public ClientRenderer getRenderer() {
		return clientRenderer;
	}

	/****/
	public void setClipboard(ClipboardItem item) {
		currentClipboardItem = item;
	}

	/****/
	public ClipboardItem getClipboard() {
		return currentClipboardItem;
	}

	/****/
	public static final boolean isInBuildMode() {
		if(proxy == null)
			proxy = TaleCraft.proxy.asClient();

		return proxy.isBuildMode();
	}

	/****/
	public void sendChatMessage(String message) {
		mc.player.sendChatMessage(message);
	}

	public void sheduleClientTickTask(Runnable runnable) {
		this.clientTickQeue.push(runnable);
	}

	public static void shedule(Runnable runnable) {
		proxy.sheduleClientTickTask(runnable);
	}

	public ClientKeyboardHandler getKeyboardHandler() {
		return clientKeyboardHandler;
	}

}
