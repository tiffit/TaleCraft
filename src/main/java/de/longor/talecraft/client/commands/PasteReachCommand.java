package de.longor.talecraft.client.commands;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public final class PasteReachCommand extends CommandBase {
	@Override
	public String getCommandName() {
		return "tcc_pastereach";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<0..64>";
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

		int reach = CommandBase.parseInt(args[0], 1, 64);
		ClientProxy.settings.setInteger("item.paste.reach", reach);
		ClientProxy.settings.send();
	}
}