package de.longor.talecraft.commands;

import de.longor.talecraft.invoke.Invoke;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

// XXX: Finish Implementation for ExplosionCommand
public class ExplosionCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_explode";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return ">this< OR <entity> OR <x> <y> <z>";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
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
			CoordinateArg posX = this.parseCoordinate(originPos.getX(), args[0], false);
			CoordinateArg posY = this.parseCoordinate(originPos.getY(), args[1], false);
			CoordinateArg posZ = this.parseCoordinate(originPos.getZ(), args[2], false);
			BlockPos triggerPos = new BlockPos(posX.func_179628_a(), posY.func_179628_a(), posZ.func_179628_a());
		}
	}

}
