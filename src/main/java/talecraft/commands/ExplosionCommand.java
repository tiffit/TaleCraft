package talecraft.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

// XXX: Finish Implementation for ExplosionCommand
public class ExplosionCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_explode";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return ">this< OR <entity> OR <x> <y> <z>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// Pattern Matching?
		// 0 1 -> [strength]
		// 1 2 -> <entity> [strength]
		// 3 4 -> <x> <y> <z> [strength]


		// entities = PlayerSelector.matchEntities(sender, args[0], Entity.class);

		//		if(args.length != 3) {
		//			throw new CommandException("Wrong number of parameters: " + args.length + " given, 3 needed.");
		//		}

		{
			BlockPos originPos = sender.getPosition();
			CoordinateArg posX = CommandBase.parseCoordinate(originPos.getX(), args[0], false);
			CoordinateArg posY = CommandBase.parseCoordinate(originPos.getY(), args[1], false);
			CoordinateArg posZ = CommandBase.parseCoordinate(originPos.getZ(), args[2], false);
			BlockPos pos = new BlockPos(posX.getAmount(), posY.getAmount(), posZ.getAmount());
			
			sender.getEntityWorld().createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 10f, false);
		}
	}

}
