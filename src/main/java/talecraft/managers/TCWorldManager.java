package talecraft.managers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import talecraft.TaleCraft;
import talecraft.network.packets.GameruleSyncPacket;

public class TCWorldManager {
	private final World world;

	public TCWorldManager(TaleCraft tc, World w) {
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

	private NBTTagCompound gamerules = null;
	
	public void tickWorld(WorldTickEvent event) {
		if(!(event.world instanceof WorldServer))
			return;

		// System.out.println("TICKING WORLD -> @" + event.world);
		// TaleCraft.proxy.tick(event);

		GameRules rules = event.world.getGameRules();
		//TODO: Fix this
		if(rules.getBoolean("tc_disableWeather")) {
			// Clear the weather for 5 seconds.
			event.world.getWorldInfo().setCleanWeatherTime(20*5);
		}
		NBTTagCompound current = rules.writeToNBT();
		if(!current.equals(gamerules)){
			gamerules = current;
			TaleCraft.network.sendToAll(new GameruleSyncPacket(current));
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
		@Override public void playEvent(EntityPlayer player, int type, BlockPos pos, int data) {}
		@Override public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {}
		@Override public void spawnParticle(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, double p_190570_4_,double p_190570_6_, double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_,int... p_190570_16_) {}
	}

}
