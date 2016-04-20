package tiffit.talecraft.packet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tiffit.talecraft.ingamescripting.InGameScriptTransport;

public class InGameScripterRequestPacket implements IMessage {

	

	public InGameScripterRequestPacket() {}

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<InGameScripterRequestPacket, IMessage> {

		@Override
		public IMessage onMessage(InGameScripterRequestPacket message, MessageContext ctx) {
			File world = DimensionManager.getCurrentSaveRootDirectory();
			File scriptFolder = new File(world, "scripts");
			scriptFolder.mkdir();
			File[] scripts = scriptFolder.listFiles();
			List<InGameScriptTransport> responses = new ArrayList<InGameScriptTransport>();
			for(File script : scripts){
				if(script.getName().endsWith(".js") && !script.isDirectory()){
					List<String> content = new ArrayList<String>();
					try {
						FileReader freader = new FileReader(script);
						BufferedReader reader = new BufferedReader(freader);
						String line;
						while((line = reader.readLine()) != null){
							content.add(line);
						}
						freader.close();
						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					responses.add(new InGameScriptTransport(script.getName(), content));
				}
			}
			return new InGameScripterRequestResponsePacket(responses);
		}
	}
}
