package de.longor.talecraft.managers;

import java.util.HashMap;
import java.util.TreeMap;

import de.longor.talecraft.TaleCraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;

public class TCWorldsManager {
	private TaleCraft taleCraft;
	public TCWorldsManager(TaleCraft tc) {
		worldMap = new HashMap<World, TCWorldManager>();
		taleCraft = tc;
	}
	
	private HashMap<World, TCWorldManager> worldMap;
	
	public synchronized void registerWorld(World world) {
		if(worldMap.containsKey(world)) {
			taleCraft.logger.error("WorldManager for THIS world is already registered -> " + world.toString());
			return;
		}
		
		TCWorldManager mng = new TCWorldManager(taleCraft, world);
		mng.init();
		worldMap.put(world, mng);
		
		taleCraft.proxy.loadWorld(world);
	}
	
	public synchronized void unregisterWorld(World world) {
		taleCraft.proxy.unloadWorld(world);
		
		if(!worldMap.containsKey(world)) {
			taleCraft.logger.error("There is no WorldManager associated with THIS -> " + world.toString());
			return;
		}
		
		TCWorldManager mng = worldMap.remove(world);
		mng.dispose();
	}
	
	public TCWorldManager fetchManager(World world) {
		TCWorldManager worldManager = worldMap.get(world);
		
		if(worldManager == null) {
			registerWorld(world);
			worldManager = worldMap.get(world);
		}
		
		return worldManager;
	}
	
}
