package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.network.handlers.client.VoxelatorGuiPacketHandler;

public class VoxelatorGuiPacket implements IMessage {
	
	public NBTTagCompound tag;
	
	public VoxelatorGuiPacket() {
	}
	
	public VoxelatorGuiPacket(NBTTagCompound tag){
		this.tag = tag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}
	
	public static class Handler implements IMessageHandler<VoxelatorGuiPacket, IMessage> {
		@Override
		public IMessage onMessage(VoxelatorGuiPacket message, MessageContext ctx) {
			VoxelatorGuiPacketHandler.handle(message);
			return null;
		}
	}


}
