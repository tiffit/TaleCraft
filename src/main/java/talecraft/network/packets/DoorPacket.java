package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.tileentity.LockedDoorTileEntity;

public class DoorPacket implements IMessage {

	int x;
	int y;
	int z;
	boolean type;

	public DoorPacket() {}

	public DoorPacket(BlockPos pos, boolean type) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
		this.type = type;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		type = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeBoolean(type);
	}

	public static class Handler implements IMessageHandler<DoorPacket, IMessage> {

		@Override
		public IMessage onMessage(DoorPacket message, MessageContext ctx) {
			TileEntity te = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if(te == null) return null;
			((LockedDoorTileEntity) te).useSilverKey = message.type;
			return null;
		}
	}
}
