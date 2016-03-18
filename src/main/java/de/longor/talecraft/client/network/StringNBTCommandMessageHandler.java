package de.longor.talecraft.client.network;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.network.StringNBTCommand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StringNBTCommandMessageHandler implements IMessageHandler {

	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if(message instanceof StringNBTCommand) {
			StringNBTCommand cmd = (StringNBTCommand) message;
			TaleCraft.proxy.asClient().getNetworkHandler().handleClientCommand(cmd.command, cmd.data);
		}
		return null;
	}

}
