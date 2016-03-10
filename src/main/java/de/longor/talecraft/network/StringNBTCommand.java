package de.longor.talecraft.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.core.net.DatagramOutputStream;

import de.longor.talecraft.TaleCraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class StringNBTCommand implements IMessage {
	public NBTTagCompound data;
	public String command;
	
	public StringNBTCommand() {
		data = new NBTTagCompound();
		command = "";
	}
	
	public StringNBTCommand(String cmdIN, NBTTagCompound dataIN) {
		this.data = dataIN != null ? dataIN : new NBTTagCompound();
		this.command = cmdIN;
	}
	
	public StringNBTCommand(String cmd) {
		data = new NBTTagCompound();
		command = cmd;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int cmdlen = buf.readShort();
		byte[] cmddata = new byte[cmdlen];
		buf.readBytes(cmddata);
		command = new String(cmddata, StandardCharsets.ISO_8859_1);
		
		int binlen = buf.readShort();
		byte[] bindata = new byte[binlen];
		buf.readBytes(bindata);
		
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bindata);
			DataInputStream inputDataStream = new DataInputStream(inputStream);
			data = CompressedStreamTools.readCompressed(inputDataStream);
			inputDataStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		byte[] cmdbin = command.getBytes(StandardCharsets.ISO_8859_1);
		buf.writeShort(cmdbin.length);
		buf.writeBytes(cmdbin);
		
		byte[] bindata = null;
		try {
			ByteArrayOutputStream oStream = new ByteArrayOutputStream(1024);
			DataOutputStream o2 = new DataOutputStream(oStream);
			
			if(data != null) {
				CompressedStreamTools.writeCompressed(data, o2);
			} else {
				CompressedStreamTools.writeCompressed(new NBTTagCompound(), o2);
				TaleCraft.logger.error("StringNBTCommand was sent without payload: " + command + " #"+this.hashCode());
			}
			
			o2.close();
			oStream.close();
			bindata = oStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buf.writeShort(bindata.length);
		buf.writeBytes(bindata);
	}
	
}
