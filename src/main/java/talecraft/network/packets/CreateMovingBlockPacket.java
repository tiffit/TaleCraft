package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.entity.EntityMovingBlock;

public class CreateMovingBlockPacket implements IMessage{

	private double x;
	private double y;
	private double z;
	private int world;
	private IBlockState STATE;
	private boolean INVISIBLE, PUSHABLE, COLLISION, NO_GRAVITY;
	private float MOUNT_Y_OFFSET;
	private String[] SCRIPTS;

	public CreateMovingBlockPacket() {}
	
	public CreateMovingBlockPacket(double x, double y, double z, int world, IBlockState state, boolean invisible, boolean pushable, boolean collision, boolean no_gravity, float mount_y_offset, String[] scripts){
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		STATE = state;
		INVISIBLE = invisible;
		PUSHABLE = pushable;
		COLLISION = collision;
		NO_GRAVITY = no_gravity;
		MOUNT_Y_OFFSET = mount_y_offset;
		SCRIPTS = scripts;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		world = buf.readInt();
		INVISIBLE = buf.readBoolean();
		PUSHABLE = buf.readBoolean();
		COLLISION = buf.readBoolean();
		NO_GRAVITY = buf.readBoolean();
		MOUNT_Y_OFFSET = buf.readFloat();
		STATE = Block.getStateById(buf.readInt());
		SCRIPTS = new String[4];
		for(int i = 0; i < 4; i++){
			SCRIPTS[i] = ByteBufUtils.readUTF8String(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeInt(world);
		buf.writeBoolean(INVISIBLE);
		buf.writeBoolean(PUSHABLE);
		buf.writeBoolean(COLLISION);
		buf.writeBoolean(NO_GRAVITY);
		buf.writeFloat(MOUNT_Y_OFFSET);
		buf.writeInt(Block.getStateId(STATE));
		for(int i = 0; i < 4; i++){
			ByteBufUtils.writeUTF8String(buf, SCRIPTS[i]);
		}
	}

	public static class Handler implements IMessageHandler<CreateMovingBlockPacket, IMessage> {

		@Override
		public IMessage onMessage(CreateMovingBlockPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			WorldServer world = server.getWorld(message.world);
			EntityMovingBlock ent = new EntityMovingBlock(world);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Block", message.STATE.getBlock().getRegistryName().toString());
			tag.setBoolean("invisible", message.INVISIBLE);
			tag.setBoolean("pushable", message.PUSHABLE);
			tag.setBoolean("collision", message.COLLISION);
			tag.setBoolean("no_gravity", message.NO_GRAVITY);
			tag.setFloat("mount_y_offset", message.MOUNT_Y_OFFSET);
			tag.setByte("Data", (byte) message.STATE.getBlock().getMetaFromState(message.STATE));
			tag.setString("onTick", message.SCRIPTS[0]);
			tag.setString("onInteract", message.SCRIPTS[1]);
			tag.setString("onCollide", message.SCRIPTS[2]);
			tag.setString("onDeath", message.SCRIPTS[3]);
			ent.updateData(tag);
			ent.setPosition(message.x, message.y, message.z);
			world.spawnEntity(ent);
			return null;
		}
	}
	
	
}
