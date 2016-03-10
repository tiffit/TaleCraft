package de.longor.talecraft.client.commands;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public final class VisualizationModeCommand extends CommandBase {
	@Override public String getCommandName() {
		return "tcc_vismode";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		final int mode = args.length > 0 ? super.parseInt(args[0]) : 0;
		
		ClientProxy.shedule(new Runnable() {
			@Override public void run() {
				ClientProxy.proxy.getRenderer().setVisualizationMode(mode);
			}
		});
	}
}