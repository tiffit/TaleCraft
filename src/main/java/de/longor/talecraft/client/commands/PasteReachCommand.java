package de.longor.talecraft.client.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import de.longor.talecraft.proxy.ClientProxy;

public final class PasteReachCommand extends CommandBase {
	@Override public String getCommandName() {
		return "tcc_pastereach";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "<0..64>";
	}

	@Override public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			return;
		}
		
		int reach = this.parseInt(args[0], 1, 64);
		ClientProxy.settings.setInteger("item.paste.reach", reach);
		ClientProxy.settings.send();
	}
}