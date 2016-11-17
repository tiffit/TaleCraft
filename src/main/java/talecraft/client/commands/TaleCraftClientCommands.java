package talecraft.client.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;

public class TaleCraftClientCommands {
	private static final List<CommandBase> commands = new ArrayList<CommandBase>();
	private static ClientCommandHandler instance = ClientCommandHandler.instance;

	public static void init() {
		register(new ResourceReloadCommand());
		register(new ToggleEntityBoundsCommand());
		register(new ClearChatCommand());
		register(new VisualizationModeCommand());
		register(new TeleportToOriginCommand());
		register(new InfobarCommand());
		register(new PasteReachCommand());
		register(new PasteSnapCommand());
		register(new InvokeTrackerCommand());
		register(new InvokeVisualizationCommand());
		register(new FullDebugPrintCommand());
		register(new RightClickCommand());
	}

	private static void register(CommandBase command) {
		instance.registerCommand(command);
		commands.add(command);
	}

	public static Collection<? extends CommandBase> getCommandList() {
		return commands;
	}

}
