package de.longor.talecraft.proxy;

import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.managers.TCWorldManager;
import de.longor.talecraft.server.ServerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOps;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ServerProxy extends CommonProxy
{

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
	
	// XXX: THIS METHOD IS NEVER CALLED
	@Override
	public void tick(TickEvent event) {
		super.tick(event);
	}
	
	// XXX: THIS METHOD IS NEVER CALLED
	@Override
	public void tickWorld(WorldTickEvent event) {
		super.tickWorld(event);
	}
	
	public NBTTagCompound getSettings(EntityPlayer playerIn) {
		return ServerHandler.getServerMirror(null).playerList().getPlayer((EntityPlayerMP) playerIn).settings;
	}
	
}
