package de.longor.talecraft.commands;

import java.util.Arrays;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.server.ServerFileSystem;
import de.longor.talecraft.server.ServerMirror;
import de.longor.talecraft.util.CommandArgumentParser;
import de.longor.talecraft.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class FileCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_file";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<action> <directory|   >";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(!(sender instanceof EntityPlayerMP)) {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"This command can only be used by players."));
			return;
		}
		
		ServerFileSystem fileSystem = ServerMirror.instance().getFileSystem();
		
		if(args.length == 0) {
			throw new SyntaxErrorException("No arguments given; /tc_file <action> <path>");
		}
		
		String action = args[0];
		
		if(action.equalsIgnoreCase("open")) {
			String path = args.length > 1 ? args[1] : "/";
			path = path.replace("%20", " ");
			
			NBTTagCompound data = null;
			Either<NBTTagCompound, String> either = fileSystem.getFileData(path, true);
			if(either.issetA()) {
				data = either.getA();
			} else {
				data = new NBTTagCompound();
				data.setString("error", either.getB());
			}
			
			String type = data.getString("type");
			
			if(type.equalsIgnoreCase("file")) {
				StringNBTCommand command = new StringNBTCommand("client.gui.file.edit", data);
				TaleCraft.network.sendTo(command, (EntityPlayerMP) sender);
			} else if(type.equalsIgnoreCase("dir")) {
				StringNBTCommand command = new StringNBTCommand("client.gui.file.browse", data);
				TaleCraft.network.sendTo(command, (EntityPlayerMP) sender);
			} else {
				// ...?
			}
		} else {
			// ...?
		}
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	return null;
    }
	
}
