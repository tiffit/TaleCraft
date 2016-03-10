package de.longor.talecraft.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.core.net.DatagramOutputStream;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerNBTDataMerge implements IMessage {
	public NBTTagCompound data;
	
	public PlayerNBTDataMerge() {
		data = new NBTTagCompound();
	}
	
	public PlayerNBTDataMerge(NBTTagCompound in) {
		data = in;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int binlen = buf.readShort();
		byte[] bindata = new byte[binlen];
		buf.readBytes(bindata);
		
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bindata);
			DataInputStream inputDataStream = new DataInputStream(inputStream);
			data = CompressedStreamTools.read(inputDataStream);
			inputDataStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		byte[] bindata = null;
		try {
			ByteArrayOutputStream oStream = new ByteArrayOutputStream(1024);
			DataOutputStream o2 = new DataOutputStream(oStream);
			CompressedStreamTools.write(data, o2);
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
