package de.longor.talecraft;

import com.google.common.eventbus.Subscribe;

import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.managers.TCWorldManager;
import de.longor.talecraft.server.ServerHandler;
import net.minecraft.server.ServerEula;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TaleCraftForgeEventHandler
{
	private TaleCraft taleCraft;
	
	public TaleCraftForgeEventHandler(TaleCraft taleCraft)
	{
		this.taleCraft = taleCraft;
	}
	
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
		taleCraft.worldsmanager.registerWorld(event.world);
	}
	
	@SubscribeEvent
	public void worldUnload(WorldEvent.Unload event)
	{
		taleCraft.worldsmanager.unregisterWorld(event.world);
	}
	
//	@SubscribeEvent
//	public void guiRenderPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
//		 event.gui.drawString(event.gui.mc.fontRendererObj, "Hello, World!", 1, 1, 0xFFFFFF);
//	}
	
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if(!event.world.isRemote) {
			ServerHandler.handleEntityJoin(event.world, event.entity);
			taleCraft.worldsmanager.fetchManager(event.world).joinWorld(event.entity);
		}
	}
	
	@SubscribeEvent
	public void onLivingAttacked(LivingAttackEvent event) {
		World world = event.entity.worldObj;
		
		if(world.isRemote) return;
		
		if(world.getGameRules().getBoolean("disable.damage.*")) {
			event.setCanceled(true);
			return;
		}
		
		if(event.source == DamageSource.fall && world.getGameRules().getBoolean("disable.damage.fall")) {
			event.setCanceled(true);
			return;
		}
		
		if(event.source == DamageSource.drown && world.getGameRules().getBoolean("disable.damage.drown")) {
			event.setCanceled(true);
			return;
		}
		
		if(event.source == DamageSource.lava && world.getGameRules().getBoolean("disable.damage.lava")) {
			event.setCanceled(true);
			return;
		}
		
		if(event.source == DamageSource.magic && world.getGameRules().getBoolean("disable.damage.magic")) {
			event.setCanceled(true);
			return;
		}
		
		if(event.source == DamageSource.inFire && world.getGameRules().getBoolean("disable.damage.fire")) {
			event.setCanceled(true);
			return;
		}
		
		if(event.source == DamageSource.inWall && world.getGameRules().getBoolean("disable.damage.suffocate")) {
			event.setCanceled(true);
			return;
		}
		
	}
	
	// CommandEvent
	
	/*
	@SubscribeEvent
	public void playerJoin(PlayerUseItemEvent event)
	{
		System.out.println("[TALECRAFT INFO] Player used item! " + event);
	}
	//*/
}
