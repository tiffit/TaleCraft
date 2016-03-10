package de.longor.talecraft.client.commands;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public final class TeleportToOriginCommand extends CommandBase {
	@Override public String getCommandName() {
		return "tcc_tpo";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		ClientProxy.shedule(new Runnable() {
			@Override public void run() {
				EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
				if(thePlayer != null) {
					thePlayer.sendChatMessage("/tp 0 255 0");
				}
			}
		});
	}
	
}