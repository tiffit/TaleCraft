package tiffit.talecraft.packet;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tiffit.talecraft.ingamescripting.InGameScriptTransport;
import tiffit.talecraft.ingamescripting.InGameScriptingGui;

public class InGameScripterRequestResponsePacket implements IMessage {

	List<InGameScriptTransport> scripts;

	public InGameScripterRequestResponsePacket() {}
	
	public InGameScripterRequestResponsePacket(List<InGameScriptTransport> scripts) {
		this.scripts = scripts;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		List<InGameScriptTransport> scripts = new ArrayList<InGameScriptTransport>();
		for(int i = 0; i < tag.getInteger("size"); i++){
			scripts.add(InGameScriptTransport.fromNBT(tag.getCompoundTag("script_" + i)));
		}
		this.scripts = scripts;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("size", scripts.size());
		for(int i = 0; i < scripts.size(); i++){
			tag.setTag("script_" + i, scripts.get(i).getNBT());
		}
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class Handler implements IMessageHandler<InGameScripterRequestResponsePacket, IMessage> {

		@Override
		public IMessage onMessage(InGameScripterRequestResponsePacket message, MessageContext ctx) {
			Minecraft.getMinecraft().displayGuiScreen(new InGameScriptingGui(message.scripts));
			return null;
		}
	}
}
