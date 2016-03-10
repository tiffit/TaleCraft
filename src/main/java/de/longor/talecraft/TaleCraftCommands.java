package de.longor.talecraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.longor.talecraft.commands.AttackCommand;
import de.longor.talecraft.commands.ButcherCommand;
import de.longor.talecraft.commands.EditEntityCommand;
import de.longor.talecraft.commands.FileCommand;
import de.longor.talecraft.commands.HighlightCommand;
import de.longor.talecraft.commands.MountCommand;
import de.longor.talecraft.commands.RegionTriggerCommand;
import de.longor.talecraft.commands.ScriptCommand;
import de.longor.talecraft.commands.SwitchShaderCommand;
import de.longor.talecraft.commands.TargetedTeleportCommand;
import de.longor.talecraft.commands.TriggerCommand;
import de.longor.talecraft.commands.ValidateBlockCommand;
import de.longor.talecraft.commands.VelocityCommand;
import de.longor.talecraft.commands.VisualizeCommand;
import de.longor.talecraft.commands.VoxelBrushCommand;
import de.longor.talecraft.commands.WandCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;

public class TaleCraftCommands {
	private static final List<CommandBase> commands = new ArrayList<CommandBase>();
	
	public static void init() {
		// just add commands here and they automagically get registered!
		
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
