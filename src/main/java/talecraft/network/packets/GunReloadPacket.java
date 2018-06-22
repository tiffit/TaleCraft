package talecraft.network.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.items.weapon.TCGunItem;

public class GunReloadPacket implements IMessage {

	UUID uuid;

	public GunReloadPacket() {
	}

	public GunReloadPacket(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
	}

	public static class Handler implements IMessageHandler<GunReloadPacket, IMessage> {

		@Override
		public IMessage onMessage(GunReloadPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(message.uuid);
			ItemStack item = player.inventory.getCurrentItem();
			if(item == null) return null;
			if(item.getItem() instanceof TCGunItem){
				TCGunItem gun = (TCGunItem) item.getItem();
				int index = gun.getClipInInventory(player.inventory);
				if(index != -1){
					if(!item.hasTagCompound())item.setTagCompound(new NBTTagCompound());
					ItemStack clipStack = player.inventory.getStackInSlot(index);
					if(item.getItemDamage() == 0){
						return null;
					}
					item.setItemDamage(0);
					item.getTagCompound().setLong("reloading", player.world.getTotalWorldTime() + 45);
					clipStack.shrink(1);
					if(clipStack.getCount() <= 0) clipStack = ItemStack.EMPTY;
					player.inventory.setInventorySlotContents(index, clipStack);
				}
			}
			return null;
		}
	}
}
