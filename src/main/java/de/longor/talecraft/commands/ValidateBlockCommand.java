package de.longor.talecraft.commands;

import java.util.Collections;
import java.util.List;

import de.longor.talecraft.util.GObjectTypeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 1 needed.");
		}

		IBlockState state = GObjectTypeHelper.findBlockState(args[0]);

		if(state == null) {
			throw new CommandException("Block type does not exist: " + args[0]);
		}

		sender.addChatMessage(new TextComponentString("Block type exists: " + args[0]));
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length == 0) {
			return getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys());
		}

		return Collections.emptyList();
	}

}
