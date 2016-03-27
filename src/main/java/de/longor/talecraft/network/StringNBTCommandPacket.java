package de.longor.talecraft.network;

import de.longor.talecraft.server.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StringNBTCommandPacket implements IMessage {

	public NBTTagCompound data;
	public String command;

	public StringNBTCommandPacket() {
		data = new NBTTagCompound();
		command = "";
	}

	public StringNBTCommandPacket(String cmdIN, NBTTagCompound dataIN) {
		this.data = dataIN != null ? dataIN : new NBTTagCompound();
		this.command = cmdIN;
	}

	public StringNBTCommandPacket(String cmd) {
		data = new NBTTagCompound();
		command = cmd;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		//		int cmdlen = buf.readShort();
		//		byte[] cmddata = new byte[cmdlen];
		//		buf.readBytes(cmddata);
		//		command = new String(cmddata, StandardCharsets.ISO_8859_1);
		command = ByteBufUtils.readUTF8String(buf);
		if(!buf.readBoolean()){
			data = ByteBufUtils.readTag(buf);
		}
		//		int binlen = buf.readShort();
		//		byte[] bindata = new byte[binlen];
		//		buf.readBytes(bindata);
		//		
		//		try {
		//			ByteArrayInputStream inputStream = new ByteArrayInputStream(bindata);
		//			DataInputStream inputDataStream = new DataInputStream(inputStream);
		//			data = CompressedStreamTools.readCompressed(inputDataStream);
		//			inputDataStream.close();
		//			inputStream.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//		byte[] cmdbin = command.getBytes(StandardCharsets.ISO_8859_1);
		//		buf.writeShort(cmdbin.length);
		//		buf.writeBytes(cmdbin);
		ByteBufUtils.writeUTF8String(buf, command);

		//		byte[] bindata = null;
		//		try {
		//			ByteArrayOutputStream oStream = new ByteArrayOutputStream(1024);
		//			DataOutputStream o2 = new DataOutputStream(oStream);
		//			
		//			if(data != null) {
		//				CompressedStreamTools.writeCompressed(data, o2);
		//			} else {
		//				CompressedStreamTools.writeCompressed(new NBTTagCompound(), o2);
		//				TaleCraft.logger.error("StringNBTCommand was sent without payload: " + command + " #"+this.hashCode());
		//			}
		//			
		//			o2.close();
		//			oStream.close();
		//			bindata = oStream.toByteArray();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//		
		//		buf.writeShort(bindata.length);
		//		buf.writeBytes(bindata);
		buf.writeBoolean(data == null);
		ByteBufUtils.writeTag(buf, data);
	}

	public static class Handler implements IMessageHandler<StringNBTCommandPacket, IMessage> {

		@Override
		public IMessage onMessage(StringNBTCommandPacket message, MessageContext ctx) {
			if(message.data != null){
				ServerHandler.handleSNBTCommand(ctx.getServerHandler(), message);
			}
			return null;
		}
	}
}
