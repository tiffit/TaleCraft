package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.invoke.CommandSenderInvokeSource;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 6) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 6 needed.");
		}
		
		BlockPos originPos = sender.getPosition();
		
		CoordinateArg minX = this.parseCoordinate(originPos.getX(), args[0], false);
		CoordinateArg minY = this.parseCoordinate(originPos.getY(), args[1], false);
		CoordinateArg minZ = this.parseCoordinate(originPos.getZ(), args[2], false);
		CoordinateArg maxX = this.parseCoordinate(originPos.getX(), args[3], false);
		CoordinateArg maxY = this.parseCoordinate(originPos.getY(), args[4], false);
		CoordinateArg maxZ = this.parseCoordinate(originPos.getZ(), args[5], false);
		
		int ix = Math.min((int)minX.func_179628_a(), (int)maxX.func_179628_a());
		int iy = Math.min((int)minY.func_179628_a(), (int)maxY.func_179628_a());
		int iz = Math.min((int)minZ.func_179628_a(), (int)maxZ.func_179628_a());
		int ax = Math.max((int)minX.func_179628_a(), (int)maxX.func_179628_a());
		int ay = Math.max((int)minY.func_179628_a(), (int)maxY.func_179628_a());
		int az = Math.max((int)minZ.func_179628_a(), (int)maxZ.func_179628_a());
		
		Invoke.trigger(new CommandSenderInvokeSource(sender), ix, iy, iz, ax, ay, az, EnumTriggerState.ON);
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
    }
    
}
