package talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.BlockCommandBlock;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import talecraft.TaleCraft;
import talecraft.invoke.CommandSenderInvokeSource;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.FileScriptInvoke;
import talecraft.invoke.Invoke;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.util.PlayerHelper;

public class ScriptCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_script";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<[?]> (use tab completion)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			throw new WrongUsageException("Not enough parameters for meaningful action. ("+args.length+")");
		}

		if(sender.getCommandSenderEntity() == null) {
			throw new WrongUsageException("ICommandSender does not have a entity assigned! Bug?");
		}
		boolean cmd = sender instanceof BlockCommandBlock;
		if(!(sender.getCommandSenderEntity() instanceof EntityPlayerMP || cmd)) {
			throw new WrongUsageException("This command can only be executed by a opped player or command block.");
		}
		EntityPlayerMP player = null;
		if(!cmd){
			player = (EntityPlayerMP) sender.getCommandSenderEntity();

			if(!PlayerHelper.isOp(player)) {
				throw new WrongUsageException("This command can only be executed by a opped player.");
			}
		}

		if(args[0].equals("run")) {
			if(args.length == 2) {
				// Runs a script
				String script = args[1];

				if(script != null && !script.isEmpty()) {
					Invoke.invoke(new FileScriptInvoke(script), new CommandSenderInvokeSource(sender), null, EnumTriggerState.ON);
				}
			} else {
				throw new WrongUsageException("Wrong parameter count: /tc_script run <scriptname>");
			}
		}

		if(args[0].equals("edit")) {
			if(cmd) throw new WrongUsageException("Edit can only be run by a player.");
			if(args.length == 2) {
				// Get Script name
				String fileName = args[1];

				// Load Script
				String fileContent = TaleCraft.globalScriptManager.loadScript(sender.getEntityWorld(), fileName);

				// Send Command-Packet with script to sender!
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("scriptname", fileName);
				nbt.setString("script", fileContent);
				TaleCraft.network.sendTo(new StringNBTCommandPacket("script_edit", nbt), player);
			} else {
				throw new WrongUsageException("Wrong parameter count: /tc_script edit <scriptname>");
			}
		}

	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length == 0) {
			return getListOfStringsMatchingLastWord(args, new String[]{"run","edit"});
		}

		return Collections.emptyList();
	}

}
