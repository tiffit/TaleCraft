package tiffit.talecraft.packet;

import java.util.UUID;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;
import tiffit.talecraft.items.weapon.TCGunClipItem;
import tiffit.talecraft.items.weapon.TCGunItem;

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
					if(!item.getTagCompound().hasKey("reloading") || item.getTagCompound().getLong("reloading") <= 0){
						return null;
					}
					TCGunClipItem clip = (TCGunClipItem) player.inventory.getStackInSlot(index).getItem();
					item.getTagCompound().setInteger("amount", clip.clipSize());
					item.getTagCompound().setLong("reloading", player.worldObj.getTotalWorldTime() + 45);
					player.inventory.setInventorySlotContents(index, null);
				}
			}
			return null;
		}
	}
}
