package talecraft.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import talecraft.TaleCraft;
import talecraft.network.packets.UndoGuiPacket;
import talecraft.util.UndoTask;

public class UndoCommand extends TCCommandBase {

	@Override
	public String getName(){
		return "tc_undo";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/tc_undo";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayerMP){
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for(UndoTask task : UndoTask.TASKS){
				NBTTagCompound taskTag = new NBTTagCompound();
				taskTag.setString("tool", task.tool);
				taskTag.setString("user", task.user);
				taskTag.setInteger("changes", task.getChangeSize());
				taskTag.setString("time", task.time.toString());
				list.appendTag(taskTag);
			}
			tag.setTag("list", list);
			TaleCraft.network.sendTo(new UndoGuiPacket(tag), (EntityPlayerMP) sender);
		}
//		UndoTask task = UndoTask.getLastJob();
//		int changes = task.getChangeSize();
//		task.undo();
//		UndoTask.TASKS.remove(task);
//		sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Undid " + TextFormatting.GOLD + changes + TextFormatting.WHITE + " changes."));
	}

}
