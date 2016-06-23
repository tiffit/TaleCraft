package de.longor.talecraft.client.commands;

import de.longor.talecraft.client.ClientRenderer.VisualMode;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class VisualizationModeCommand extends CommandBase {
	@Override
	public String getCommandName() {
		return "tcc_vismode";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		final VisualMode mode = VisualMode.valueOf(args[0].toUpperCase());

		ClientProxy.shedule(new Runnable() {
			@Override
			public void run() {
				ClientProxy.proxy.getRenderer().setVisualizationMode(mode);
			}
		});
	}
}