package de.longor.talecraft.client.commands;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class InvokeTrackerCommand extends CommandBase {
	@Override
	public String getCommandName() {
		return "tcc_invtrk";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<true/false>";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			return;
		}

		boolean flag = CommandBase.parseBoolean(args[0]);
		ClientProxy.settings.setBoolean("invoke.tracker", flag);
		ClientProxy.settings.send();
	}
}