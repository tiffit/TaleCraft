package de.longor.talecraft;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import sun.net.NetworkClient;
import de.longor.talecraft.client.commands.TaleCraftClientCommands;
import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.network.StringNBTCommandPacket;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.proxy.CommonProxy;
import de.longor.talecraft.script.GlobalScriptManager;
import de.longor.talecraft.server.ServerHandler;
import de.longor.talecraft.server.ServerMirror;
import de.longor.talecraft.util.TimedExecutor;
import net.minecraft.block.Block;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkSystem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistrySimple;
import net.minecraft.world.World;
import net.minecraftforge.client.gui.ForgeGuiFactory.ForgeConfigGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class TaleCraft
{
    @Mod.Instance(Reference.MOD_ID)
    public static TaleCraft instance;
    public static ModContainer container;
    public static TCWorldsManager worldsmanager;
    public static TaleCraftCommonEventHandler fmlEventHandler;
    public static TaleCraftForgeEventHandler forgeEventHandler;
    public static GlobalScriptManager globalScriptManager;
    public static SimpleNetworkWrapper network;
    public static TimedExecutor timedExecutor;
    public static Logger logger;
    public static Random random;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY, modId = Reference.MOD_ID)
    public static CommonProxy proxy;
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	container = FMLCommonHandler.instance().findContainerFor(instance);
    	
    	logger.info("TaleCraft initialization...");
    	logger.info("TaleCraft Version: " + Reference.MOD_VERSION);
    	logger.info("TaleCraft ModContainer: " + container);
    	
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	random = new Random(42);
    	
    	worldsmanager = new TCWorldsManager(this);
    	timedExecutor = new TimedExecutor();
    	globalScriptManager = new GlobalScriptManager();
    	globalScriptManager.init(this, proxy);
    	network = NetworkRegistry.INSTANCE.newSimpleChannel("TaleCraftNet");
		
    	// Register the handler for server-side StringNBT-commands.
		network.registerMessage(StringNBTCommandPacket.Handler.class, StringNBTCommandPacket.class, 0, Side.SERVER);
    	
		/// print debug information
    	logger.info("TaleCraft CoreManager @" + worldsmanager.hashCode());
    	logger.info("TaleCraft TimedExecutor @" + timedExecutor.hashCode());
    	logger.info("TaleCraft NET SimpleNetworkWrapper @" + network.hashCode());
    	
    	// create and register the event handlers for COMMON
    	fmlEventHandler = new TaleCraftCommonEventHandler(this);
    	MinecraftForge.EVENT_BUS.register(fmlEventHandler);
    	logger.info("TaleCraft CME Handler @" + fmlEventHandler.hashCode());
    	
    	// create and register the event handlers for FORGE
    	forgeEventHandler = new TaleCraftForgeEventHandler(this);
    	MinecraftForge.EVENT_BUS.register(forgeEventHandler);
    	logger.info("TaleCraft EVTB Handler @" + forgeEventHandler.hashCode());
    	
    	
    	// Initialize all the Tabs/Blocks/Items/Commands etc.etc.
    	logger.info("Loading Tabs, Blocks, Items (In that order)");
    	TaleCraftTabs.init();
    	TaleCraftBlocks.init();
    	TaleCraftItems.init();
    	logger.info("Loading Entities and Commands (In that order)");
    	TaleCraftEntities.init();
    	TaleCraftCommands.init();
    	
    	// Initialize the Proxy(s)
    	logger.info("Initializing Proxy...");
    	proxy.taleCraft = this;
    	proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    	logger.info("TaleCraft initialized, all systems ready.");
    }
	
	@Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event){
		MinecraftServer server = event.getServer();
		
		ICommandManager cmdmng = server.getCommandManager();
		if (cmdmng instanceof ServerCommandManager && cmdmng instanceof CommandHandler) {
			CommandHandler cmdhnd = (CommandHandler) cmdmng;
			TaleCraftCommands.register(cmdhnd);
		}
		
		// By calling this method, we create the ServerMirror for the given server.
		ServerHandler.getServerMirror(server);
	}
	
	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		// Calling this method destroys all server instances that exist,
		// because the 'event' given above does NOT give us the server-instance that is being stopped.
		ServerHandler.destroyServerMirror(null);
	}
	
	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
		// TaleCraft.logger.info("Server started: " + event + " [TCINFO]");
	}
	
	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
	{
		// TaleCraft.logger.info("Server stopped: " + event + " [TCINFO]");
	}
	
	@SideOnly(Side.CLIENT)
	public static ClientProxy asClient() {
		return proxy.asClient();
	}
	
	public static NBTTagCompound getSettings(EntityPlayer playerIn) {
		return proxy.getSettings(playerIn);
	}
	
}
