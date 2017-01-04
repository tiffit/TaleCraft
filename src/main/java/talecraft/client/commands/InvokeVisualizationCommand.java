package talecraft.client.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import talecraft.proxy.ClientProxy;

public final class InvokeVisualizationCommand extends CommandBase {
	@Override
	public String getName() {
		return "tcc_invokeviz";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<true/false>";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			sender.sendMessage(new TextComponentString("Enables/Disables the invoke visualization."));
			return;
		}

		boolean flag = CommandBase.parseBoolean(args[0]);
		ClientProxy.settings.setBoolean("client.render.invokeVisualize", flag);
	}
}