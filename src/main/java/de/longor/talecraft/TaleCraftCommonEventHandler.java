package de.longor.talecraft;

import com.google.common.eventbus.Subscribe;

import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.managers.TCWorldManager;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.server.ServerHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TaleCraftCommonEventHandler
{
	private TaleCraft taleCraft;
	
	public TaleCraftCommonEventHandler(TaleCraft taleCraft)
	{
		this.taleCraft = taleCraft;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void keyEvent(KeyInputEvent event) {
		if(FMLCommonHandler.instance().getSide().isClient()) {
			if(taleCraft.proxy instanceof de.longor.talecraft.proxy.ClientProxy) {
				taleCraft.proxy.asClient().getKeyboardHandler().on_key(event);
			}
		}
	}
	
	@SubscribeEvent
	public void tick(TickEvent event) {
		taleCraft.proxy.tick(event);
	}
	
	@SubscribeEvent
	public void tickWorld(WorldTickEvent event) {
		taleCraft.proxy.tickWorld(event);
	}
	
	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			ServerHandler.getServerMirror(null).playerList().playerJoin((EntityPlayerMP) event.player);
			TaleCraft.network.sendTo(new StringNBTCommand("client.network.join"), (EntityPlayerMP) event.player);
		}
	}
	
	@SubscribeEvent
	public void playerLoggedOut(PlayerLoggedOutEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			ServerHandler.getServerMirror(null).playerList().playerLeave((EntityPlayerMP) event.player);
		}
	}
	
}
