package talecraft.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import talecraft.invoke.CommandSenderInvokeSource;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.Invoke;

public class RegionTriggerCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_triggerregion";
	}

	@Override
	public String getUsage(ICommandSender sender) {
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

		int ix = Math.min((int)minX.getAmount(), (int)maxX.getAmount());
		int iy = Math.min((int)minY.getAmount(), (int)maxY.getAmount());
		int iz = Math.min((int)minZ.getAmount(), (int)maxZ.getAmount());
		int ax = Math.max((int)minX.getAmount(), (int)maxX.getAmount());
		int ay = Math.max((int)minY.getAmount(), (int)maxY.getAmount());
		int az = Math.max((int)minZ.getAmount(), (int)maxZ.getAmount());

		Invoke.trigger(new CommandSenderInvokeSource(sender), ix, iy, iz, ax, ay, az, EnumTriggerState.ON);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
	}

}
