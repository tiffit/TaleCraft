package de.longor.talecraft.commands;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.util.CommandArgumentParser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TriggerCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_triggerblock";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<x> <y> <z>";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 3) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 3 needed.");
		}
		
		CommandArgumentParser parser = new CommandArgumentParser(args);
		parser.commandSenderPosition = sender.getPositionVector();
		BlockPos triggerPos = parser.consume_blockpos("Failed to parse block position: " + Arrays.toString(args));
		
		World world = sender.getEntityWorld();
		IBlockState state = world.getBlockState(triggerPos);
		Invoke.trigger(world, triggerPos, state, EnumTriggerState.ON);
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
    	if(args.length == 0) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
    	}
    	if(args.length == 1) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
    	}
    	if(args.length == 2) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
    	}
    	
    	return null;
    }

}
