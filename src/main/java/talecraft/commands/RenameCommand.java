package talecraft.commands;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class RenameCommand extends TCCommandBase {

	@Override
	public String getName(){
		return "tc_rename";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/tc_rename <name>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP player = CommandBase.getCommandSenderAsPlayer(sender);
		
		if(args.length == 0) {
			throw new WrongUsageException("No parameters given!");
		}
		
		String newName = Strings.join(args, " ").trim();
		
		if(newName.isEmpty()) {
			return;
		}
		
		ItemStack itemStack = player.getHeldItemMainhand();
		
		if(itemStack != null) {
			itemStack.setStackDisplayName(newName);
		}
	}

}
