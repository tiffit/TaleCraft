package tiffit.talecraft.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tiffit.talecraft.tileentity.LockedDoorTileEntity;
import tiffit.talecraft.tileentity.SpikeBlockTileEntity;

public class SpikePacket implements IMessage {

	int x;
	int y;
	int z;
	boolean active;

	public SpikePacket() {}

	public SpikePacket(BlockPos pos, boolean active) {
		System.out.println("Changing to: " + active);
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
		this.active = active;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		active = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeBoolean(active);
	}

	public static class Handler implements IMessageHandler<SpikePacket, IMessage> {

		@Override
		public IMessage onMessage(SpikePacket message, MessageContext ctx) {
			TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if(te == null) return null;
			((SpikeBlockTileEntity) te).setActive(message.active);
			return null;
		}
	}
}
