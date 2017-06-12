package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.entity.EntityMovingBlock;

public class MovingBlockDataUpdatePacket implements IMessage {

	NBTTagCompound data;
	int id;
	

	public MovingBlockDataUpdatePacket() {
	}

	public MovingBlockDataUpdatePacket(int id, NBTTagCompound tag) {
		data = tag;
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
		buf.writeInt(id);
	}

	public static class Handler implements IMessageHandler<MovingBlockDataUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(MovingBlockDataUpdatePacket message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			
			EntityMovingBlock moving = (EntityMovingBlock) mc.world.getEntityByID(message.id);
			if(moving != null)moving.updateData(message.data);
			return null;
		}
	}
}
