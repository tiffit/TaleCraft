package tiffit.talecraft.versionchecker;

import de.longor.talecraft.Reference;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.network.PlayerNBTDataMergePacket;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class SendMessage {

	@SubscribeEvent
	public void onClientConnection(ClientConnectedToServerEvent event){
		TaleCraft.logger.info("Checking version...");
		final TCVersion latest = VersionParser.getLatestVersion();
		final String current = Reference.MOD_VERSION; //to be easily changeable for debugging
		if(latest.isGreaterVersion(current)){
			final ClientProxy cproxy = TaleCraft.proxy.asClient();
			cproxy.sheduleClientTickTask(new Runnable(){
				Minecraft mc = cproxy.mc;
				@Override 
				public void run() {
					while(mc.thePlayer == null){}
					String message = TextFormatting.YELLOW + "TaleCraft version is outdated! Your version is " + TextFormatting.GOLD + current + TextFormatting.YELLOW + ". The latest is " + TextFormatting.GOLD + latest.getVersion() + TextFormatting.YELLOW + ".";
					mc.thePlayer.addChatMessage(new TextComponentString(message));
					TaleCraft.logger.warn(TextFormatting.getTextWithoutFormattingCodes(message));
				}
			});
			
		}
	}
	
}
