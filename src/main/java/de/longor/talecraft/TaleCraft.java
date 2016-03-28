package de.longor.talecraft;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.network.StringNBTCommandPacket;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.proxy.CommonProxy;
import de.longor.talecraft.script.GlobalScriptManager;
import de.longor.talecraft.server.ServerHandler;
import de.longor.talecraft.util.TimedExecutor;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
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
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tiffit.talecraft.util.ConfigurationManager;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY)
public class TaleCraft {
	@Mod.Instance(Reference.MOD_ID)
	public static TaleCraft instance;
	public static ModContainer container;
	public static TCWorldsManager worldsManager;
	public static TaleCraftEventHandler eventHandler;
	public static GlobalScriptManager globalScriptManager;
	public static SimpleNetworkWrapper network;
	public static TimedExecutor timedExecutor;
	public static Logger logger;
	public static Random random;
	public static Configuration config;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY, modId = Reference.MOD_ID)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		container = FMLCommonHandler.instance().findContainerFor(instance);
		logger.info("TaleCraft initialization...");
		logger.info("TaleCraft Version: " + Reference.MOD_VERSION);
		logger.info("TaleCraft ModContainer: " + container);
		logger.info("Creating/Reading configuration file!");
		config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigurationManager.init(config);
		logger.info("Configuration loaded!");
		MinecraftForge.EVENT_BUS.register(this);

		random = new Random(42);

		worldsManager = new TCWorldsManager(this);
		timedExecutor = new TimedExecutor();
		globalScriptManager = new GlobalScriptManager();
		globalScriptManager.init(this, proxy);
		network = NetworkRegistry.INSTANCE.newSimpleChannel("TaleCraftNet");

		// Register the handler for server-side StringNBT-commands.
		network.registerMessage(StringNBTCommandPacket.Handler.class, StringNBTCommandPacket.class, 0, Side.SERVER);

		// Print debug information
		logger.info("TaleCraft CoreManager @" + worldsManager.hashCode());
		logger.info("TaleCraft TimedExecutor @" + timedExecutor.hashCode());
		logger.info("TaleCraft NET SimpleNetworkWrapper @" + network.hashCode());

		// Create and register the event handler
		eventHandler = new TaleCraftEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		logger.info("TaleCraft Event Handler @" + eventHandler.hashCode());

		// Initialize all the Tabs/Blocks/Items/Commands etc.
		logger.info("Loading Tabs, Blocks, Items, Entities and Commands (In that order)");
		TaleCraftTabs.init();
		TaleCraftBlocks.init();
		TaleCraftItems.init();
		TaleCraftEntities.init();
		TaleCraftCommands.init();

		// Initialize the Proxy
		logger.info("Initializing Proxy...");
		proxy.taleCraft = this;
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
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
		// logger.info("Server started: " + event + " [TCINFO]");
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
	{
		// logger.info("Server stopped: " + event + " [TCINFO]");
	}

	@SideOnly(Side.CLIENT)
	public static ClientProxy asClient() {
		return proxy.asClient();
	}

	public static NBTTagCompound getSettings(EntityPlayer player) {
		return proxy.getSettings(player);
	}

}
