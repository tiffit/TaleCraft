package de.longor.talecraft.commands;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.util.GObjectTypeHelper;
import de.longor.talecraft.util.CommandArgumentParser;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ValidateBlockCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_isblock";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<block>";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 1 needed.");
		}
		
		IBlockState state = GObjectTypeHelper.findBlockState(args[0]);
		
		if(state == null) {
			throw new CommandException("Block type deos not exist: " + args[0]);
		}
		
		sender.addChatMessage(new ChatComponentText("Block type exists: " + args[0]));
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length == 0) {
    		return getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys());
    	}
    	
    	return null;
    }
    
}
