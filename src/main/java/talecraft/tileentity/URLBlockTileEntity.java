package talecraft.tileentity;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import talecraft.TaleCraft;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.network.packets.StringNBTCommandPacketClient;
import talecraft.util.NBTHelper;

public class URLBlockTileEntity extends TCTileEntity {
	String url;
	String selector;

	public URLBlockTileEntity() {
		url = "https://www.reddit.com/r/talecraft/";
		selector = "@a";
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// none
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 1.0f;
		color[2] = 0.8f;
	}

	@Override
	public String getName() {
		return "URLBlock@"+this.getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		url = comp.getString("url");
		selector = comp.getString("selector");
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setString("url", url);
		comp.setString("selector", selector);
		return comp;
	}

	public String getURL() {
		return url;
	}

	public String getSelector() {
		return selector;
	}

	public void trigger(EnumTriggerState triggerState) {
		if(triggerState.getBooleanValue()) {
			List<EntityPlayerMP> players = null;
			try {
				players = EntitySelector.matchEntities(this, selector, EntityPlayerMP.class);
			} catch (CommandException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringNBTCommandPacketClient command = new StringNBTCommandPacketClient();
			command.command = "client.gui.openurl";
			command.data = NBTHelper.newSingleStringCompound("url",url);

			for(EntityPlayerMP player : players) {
				TaleCraft.network.sendTo(command, player);
			}
		}
	}

}
