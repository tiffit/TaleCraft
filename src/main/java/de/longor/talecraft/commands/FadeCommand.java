package de.longor.talecraft.commands;

import java.util.Collections;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import tiffit.talecraft.packet.FadePacket;

public class FadeCommand extends TCCommandBase {

	@Override
	public String getCommandName() {
		return "tc_fade";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<player> [color] [time]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		int color = 0x000000;
		int time = 2;
		List<EntityPlayerMP> players = Collections.emptyList();
		if(args.length > 0) {
			players = EntitySelector.matchEntities(sender, args[0], EntityPlayerMP.class);
			if(args.length > 1){
				try{
					color = Integer.parseInt(args[1]);
					if(args.length > 2){
						time = Integer.parseInt(args[2]);
					}
				}catch(NumberFormatException ex){
					throw new NumberInvalidException("Invalid integer!", new Object[0]);
				}
			}
		}else{
			throw new CommandException("Incorrect usage!", new Object[0]);
		}
		for(EntityPlayerMP player : players){
			TaleCraft.network.sendTo(new FadePacket(color, time), player);
		}
		
	}

}
