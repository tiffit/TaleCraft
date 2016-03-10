package de.longor.talecraft.managers;

import de.longor.talecraft.TaleCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TCWorldManager {
	private final TaleCraft taleCraft;
	private final World world;
	
	public TCWorldManager(TaleCraft tc, World w) {
		taleCraft = tc;
		world = w;
	}
	
	public String toString() {
		return "TCWorldManager#"+world.hashCode();
	}
	
	public void init() {
		world.addWorldAccess(new TCWorldManagerAccess());
		
		taleCraft.logger.info("Initializing new TCWorldManager -> " + this + " @" + world.hashCode());
	}
	
	public void dispose() {
		taleCraft.logger.info("Disposing of TCWorldManager -> " + this + " @" + world.getWorldInfo());
	}
	
	public void tickWorld(WorldTickEvent event) {
		if(!(event.world instanceof WorldServer))
			return;
		
		// System.out.println("TICKING WORLD -> @" + event.world);
		// TaleCraft.proxy.tick(event);
		
		GameRules rules = event.world.getGameRules();
		if(rules.getBoolean("disableWeather")) {
			// Clear the weather for 5 seconds.
			event.world.getWorldInfo().setCleanWeatherTime(20*5);
		}
		
	}
	
	public void joinWorld(Entity entity) {
		
	}
	
	private class TCWorldManagerAccess implements IWorldAccess {
		@Override public void markBlockForUpdate(BlockPos pos) {}
		@Override public void notifyLightSet(BlockPos pos) {}
		@Override public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {}
		@Override public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
			// TaleCraft.logger.info("PlaySound " + TCWorldManager.this + " : " + soundName + " @["+x+"/"+y+"/"+z+"]");
		}
		@Override public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {}
		@Override public void spawnParticle(int p_180442_1_, boolean p_180442_2_, double p_180442_3_, double p_180442_5_, double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int... p_180442_15_) {}
		@Override public void onEntityAdded(Entity entityIn) {
			// TaleCraft.logger.info("EntityAdded " + TCWorldManager.this + " : " + entityIn);
		}
		@Override public void onEntityRemoved(Entity entityIn) {
			// TaleCraft.logger.info("EntityRemoved " + TCWorldManager.this + " : " + entityIn);
		}
		@Override public void playRecord(String recordName, BlockPos blockPosIn) {}
		@Override public void broadcastSound(int soundID, BlockPos p_180440_2_, int p_180440_3_) {
			// TaleCraft.logger.info("BroadcastSound " + TCWorldManager.this + " : " + soundID);
		}
		@Override public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int p_180439_4_) {}
		@Override public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {}
	}
	
}
