package de.longor.talecraft.proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.longor.talecraft.TaleCraft;
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
import de.longor.talecraft.client.ClientKeyboardHandler;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.ClientRenderer;
import de.longor.talecraft.client.ClientSettings;
import de.longor.talecraft.client.InfoBar;
import de.longor.talecraft.client.InvokeTracker;
import de.longor.talecraft.client.commands.TaleCraftClientCommands;
import de.longor.talecraft.client.gui.misc.GuiConfirmOpenLinkTCOverride;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor.RemoteEntityDataLink;
import de.longor.talecraft.client.gui.misc.GuiMapControl;
import de.longor.talecraft.client.network.PlayerDataMergeMessageHandler;
import de.longor.talecraft.client.network.StringNBTCommandMessageHandler;
import de.longor.talecraft.client.render.IRenderable;
import de.longor.talecraft.client.render.ITemporaryRenderable;
import de.longor.talecraft.client.render.PushRenderableFactory;
import de.longor.talecraft.client.render.RenderModeHelper;
import de.longor.talecraft.client.render.entity.PointEntityRenderer;
import de.longor.talecraft.client.render.renderables.SelectionBoxRenderer;
import de.longor.talecraft.client.render.renderers.CustomSkyRenderer;
import de.longor.talecraft.client.render.renderers.EXTFontRenderer;
import de.longor.talecraft.client.render.renderers.ItemMetaWorldRenderer;
import de.longor.talecraft.client.render.tileentity.GenericTileEntityRenderer;
import de.longor.talecraft.client.render.tileentity.ImageHologramBlockTileEntityEXTRenderer;
import de.longor.talecraft.client.render.tileentity.StorageBlockTileEntityEXTRenderer;
import de.longor.talecraft.client.render.tileentity.SummonBlockTileEntityEXTRenderer;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.entities.EntityPoint;
import de.longor.talecraft.items.CopyItem;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.network.StringNBTCommand;

public class ClientProxy extends CommonProxy {
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
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		settings.init();
		
		MinecraftForge.EVENT_BUS.register(this);
		
		clientKeyboardHandler = new ClientKeyboardHandler(this);
		
    	TaleCraftClientCommands.init();
    	
    	clientTickQeue = new ConcurrentLinkedDeque<Runnable>();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		// create client network'er
		clientNetworkHandler = new ClientNetworkHandler(this);
		clientNetworkHandler.init();
		
		// create client renderer
		clientRenderer = new ClientRenderer(this);
    	clientRenderer.init();
		// add all static renderers
		clientRenderer.addStaticRenderer(new SelectionBoxRenderer());
		
	} // init(..){}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		// Create the InfoBar Instance
		infoBarInstance = new InfoBar();
		
		// Create the invoke tracker instance
		invokeTracker = new InvokeTracker();
	}

	@SubscribeEvent
	public void worldPass(RenderWorldLastEvent event) {
		clientRenderer.on_render_world_post(event);
	}

	@SubscribeEvent
	public void worldPostRenderHand(RenderHandEvent event) {
		clientRenderer.on_render_world_hand_post(event);
	}
	
	@SubscribeEvent
	public void worldPassPre(RenderWorldEvent.Pre event) {
		clientRenderer.on_render_world_pre(event);
	}
	
	/**
	 * This method is called when the world is unloaded.
	 **/
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
	public boolean isBuildMode() {
		return mc.playerController != null && mc.playerController.isInCreativeMode();
	}

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
		mc.thePlayer.sendChatMessage(message);
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
