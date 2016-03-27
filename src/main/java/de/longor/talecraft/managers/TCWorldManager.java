package de.longor.talecraft.managers;

import de.longor.talecraft.TaleCraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldEventListener;
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

	@Override
	public String toString() {
		return "TCWorldManager#"+world.hashCode();
	}

	public void init() {
		world.addEventListener(new TCWorldManagerAccess());

		TaleCraft.logger.info("Initializing new TCWorldManager -> " + this + " @" + world.hashCode());
	}

	public void dispose() {
		TaleCraft.logger.info("Disposing of TCWorldManager -> " + this + " @" + world.getWorldInfo());
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

	private class TCWorldManagerAccess implements IWorldEventListener {
		@Override public void notifyBlockUpdate(World world, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {}
		@Override public void notifyLightSet(BlockPos pos) {}
		@Override public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {}
		@Override public void playSoundToAllNearExcept(EntityPlayer player, SoundEvent sound, SoundCategory category, double x, double y, double z, float volume, float pitch) {}
		@Override public void playRecord(SoundEvent sound, BlockPos pos) {}
		@Override public void spawnParticle(int particleId, boolean ignoreRange, double x, double y, double z, double xOffset, double yOffset, double zOffset, int... parameters) {}
		@Override public void onEntityAdded(Entity entity) {}
		@Override public void onEntityRemoved(Entity entity) {}
		@Override public void broadcastSound(int soundID, BlockPos pos, int data) {}
		@Override public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos pos, int data) {}
		@Override public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {}
	}

}
