package de.longor.talecraft.client.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import de.longor.talecraft.proxy.ClientProxy;

public final class InvokeTrackerCommand extends CommandBase {
	@Override public String getCommandName() {
		return "tcc_invtrk";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "<true/false>";
	}

	@Override public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			return;
		}
		
		boolean flag = this.parseBoolean(args[0]);
		ClientProxy.settings.setBoolean("invoke.tracker", flag);
		ClientProxy.settings.send();
	}
}