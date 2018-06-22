package talecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import talecraft.TaleCraft;

public class TCMusicTicker extends MusicTicker {

	private final Minecraft mc;
	
	public TCMusicTicker(Minecraft mc) {
		super(mc);
		this.mc = mc;
	}
	
	@Override
	public void playMusic(MusicType song) {
		if(mc.world != null && mc.player != null && (mc.player.getEntityData().getBoolean("no-music")  || !TaleCraft.proxy.asClient().gamerules.getBoolean("tc_playDefaultMusic"))){
			return;
		}
		super.playMusic(song);
	}
	
	@Override
	public void update() {
		if(mc.world != null && mc.player != null && (mc.player.getEntityData().getBoolean("no-music")  || !TaleCraft.proxy.asClient().gamerules.getBoolean("tc_playDefaultMusic"))){
			// stopMusic(); TODO
		}
		super.update();
	}

}
