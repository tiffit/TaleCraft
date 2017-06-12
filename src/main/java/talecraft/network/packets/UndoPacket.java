package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.util.UndoTask;

public class UndoPacket implements IMessage {
	
	public int index;
	
	public UndoPacket() {
	}
	
	public UndoPacket(int index){
		this.index = index;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(index);
	}
	
	public static class Handler implements IMessageHandler<UndoPacket, IMessage> {
		@Override
		public IMessage onMessage(UndoPacket message, MessageContext ctx) {
			UndoTask task = UndoTask.TASKS.get(message.index);
			task.undo();
			UndoTask.TASKS.remove(task);
			return null;
		}
	}


}
