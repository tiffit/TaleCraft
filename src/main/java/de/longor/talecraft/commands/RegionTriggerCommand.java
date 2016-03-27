package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.invoke.CommandSenderInvokeSource;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class RegionTriggerCommand extends TCCommandBase {

	@Override
	public String getCommandName() {
		return "tc_triggerregion";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<min-x> <min-y> <min-z> <max-x> <max-y> <max-z>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 6) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 6 needed.");
		}

		BlockPos originPos = sender.getPosition();

		CoordinateArg minX = CommandBase.parseCoordinate(originPos.getX(), args[0], false);
		CoordinateArg minY = CommandBase.parseCoordinate(originPos.getY(), args[1], false);
		CoordinateArg minZ = CommandBase.parseCoordinate(originPos.getZ(), args[2], false);
		CoordinateArg maxX = CommandBase.parseCoordinate(originPos.getX(), args[3], false);
		CoordinateArg maxY = CommandBase.parseCoordinate(originPos.getY(), args[4], false);
		CoordinateArg maxZ = CommandBase.parseCoordinate(originPos.getZ(), args[5], false);

		int ix = Math.min((int)minX.func_179628_a(), (int)maxX.func_179628_a());
		int iy = Math.min((int)minY.func_179628_a(), (int)maxY.func_179628_a());
		int iz = Math.min((int)minZ.func_179628_a(), (int)maxZ.func_179628_a());
		int ax = Math.max((int)minX.func_179628_a(), (int)maxX.func_179628_a());
		int ay = Math.max((int)minY.func_179628_a(), (int)maxY.func_179628_a());
		int az = Math.max((int)minZ.func_179628_a(), (int)maxZ.func_179628_a());

		Invoke.trigger(new CommandSenderInvokeSource(sender), ix, iy, iz, ax, ay, az, EnumTriggerState.ON);
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
	}

}
