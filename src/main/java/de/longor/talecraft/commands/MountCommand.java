package de.longor.talecraft.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;

public class MountCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_mount";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<rider> <mount>";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 2) {
			throw new WrongUsageException(getCommandUsage(sender), sender, args);
		}
		
		Entity rider = fetchEntity(sender, args[0]);
		Entity mount = fetchEntity(sender, args[1]);
		
		if(rider.ridingEntity != null) {
			throw new CommandException("Rider is already mounting another entity.", sender, rider, rider.ridingEntity);
		}
		
		if(mount.riddenByEntity != null) {
			throw new CommandException("Mount is already mounted by another entity.", sender, mount, mount.riddenByEntity);
		}
		
		rider.mountEntity(mount);
	}
	
    private Entity fetchEntity(ICommandSender sender, String string) throws CommandException {
		if(string.equalsIgnoreCase("this")) {
			if(sender.getCommandSenderEntity() != null) {
				return sender.getCommandSenderEntity();
			} else {
				throw new CommandException("CommandSender does not have a entity assigned.", sender, string);
			}
		}
    	
		List<Entity> list = PlayerSelector.matchEntities(sender, string, Entity.class);
		
		if(list.size() == 0) {
			throw new CommandException("Matched zero entitires: " + string, sender, string);
		}
		if(list.size() > 1) {
			throw new CommandException("Matched more than one ("+list.size()+") entity: " + string, sender, string);
		}
		
		Entity ent = list.get(0);
		
		if(ent == null) {
			throw new CommandException("Could not match one entity: " + string, sender, string);
		}
		
    	return ent;
	}
    
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
    	if(args.length <= 1) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"this", "@a", "@p", "@r", "@e"});
    	}
    	
    	if(args.length == 2) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"this", "@a", "@p", "@r", "@e"});
    	}
    	
    	return null;
    }

}
