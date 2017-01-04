package talecraft.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import talecraft.proxy.ClientProxy;

public final class ResourceReloadCommand extends CommandBase {
	@Override
	public String getName() {
		return "tcc_resreload";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		ClientProxy.shedule(new Runnable() {
			@Override
			public void run() {
				Minecraft.getMinecraft().refreshResources();
			}
		});
	}
}