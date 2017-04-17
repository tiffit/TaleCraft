package talecraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import talecraft.commands.AttackCommand;
import talecraft.commands.ButcherCommand;
import talecraft.commands.EditEntityCommand;
import talecraft.commands.FadeCommand;
import talecraft.commands.FileCommand;
import talecraft.commands.HighlightCommand;
import talecraft.commands.MountCommand;
import talecraft.commands.PlayerDataCommand;
import talecraft.commands.RegionTriggerCommand;
import talecraft.commands.RenameCommand;
import talecraft.commands.ScriptCommand;
import talecraft.commands.TargetedTeleportCommand;
import talecraft.commands.TriggerCommand;
import talecraft.commands.UndoCommand;
import talecraft.commands.ValidateBlockCommand;
import talecraft.commands.VelocityCommand;
import talecraft.commands.VisualizeCommand;
import talecraft.commands.VoxelBrushCommand;
import talecraft.commands.WandCommand;

public class TaleCraftCommands {
	private static final List<CommandBase> commands = Lists.newArrayList();

	public static void init() {
		// Just add commands here and they automatically get registered!
		commands.add(new WandCommand());
		commands.add(new MountCommand());
		commands.add(new TriggerCommand());
		commands.add(new RegionTriggerCommand());
		commands.add(new VelocityCommand());
		// commands.add(new ExplosionCommand());
		// commands.add(new SwitchShaderCommand());
		commands.add(new VoxelBrushCommand());
		commands.add(new ButcherCommand());
		commands.add(new ScriptCommand());
		commands.add(new VisualizeCommand());
		commands.add(new ValidateBlockCommand());
		commands.add(new EditEntityCommand());
		commands.add(new AttackCommand());
		commands.add(new TargetedTeleportCommand());
		commands.add(new HighlightCommand());
		commands.add(new FileCommand());
		commands.add(new FadeCommand());
		commands.add(new RenameCommand());
		commands.add(new PlayerDataCommand());
		commands.add(new UndoCommand());
	}

	public static void register(CommandHandler registry) {
		for(ICommand cmd : commands) {
			registry.registerCommand(cmd);
		}
	}

	public static Collection<? extends CommandBase> getCommandList() {
		return Collections.unmodifiableList(commands);
	}

}
