package de.longor.talecraft.commands;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.invoke.Invoke;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class VelocityCommand extends TCCommandBase {
	
	@Override
	public String getCommandName() {
		return "tc_velocity";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<set|add|multiply> <this|entity> <x> <y> <z>";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 5) {
            throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
		}
		
		// Parse action
		int action = 0;
		
		if(args[0].equalsIgnoreCase("set"))
			action = 1;
		if(args[0].equalsIgnoreCase("add"))
			action = 2;
		if(args[0].equalsIgnoreCase("multiply"))
			action = 3;
		if(args[0].equalsIgnoreCase("viewadd"))
			action = 4;
		
		if(action == 0) {
			throw new CommandException("Invalid action: " + args[0]);
		}
		
		// Parse target argument
		List<Entity> entities = null;
		
		if(args[1].equalsIgnoreCase("this")) {
			if(sender.getCommandSenderEntity() == null) {
				throw new CommandException("Command-Sender is not an entity! 'this'-option cannot be used.");
			}
			
			entities = new ArrayList<Entity>(1);
			entities.add(sender.getCommandSenderEntity());
		} else {
			entities = PlayerSelector.matchEntities(sender, args[1], Entity.class);
		}
		
		if(entities.isEmpty()) {
			throw new CommandException("No entities found for selector: " + args[1]);
		}
		
		final double min = -10;
		final double max = +10;
		
		double vX = this.parseDouble(args[2], min, max);
		double vY = this.parseDouble(args[3], min, max);
		double vZ = this.parseDouble(args[4], min, max);
		
		if(action == 1) { // ACTION::set
			for(Entity entity : entities) {
				entity.motionX = vX;
				entity.motionY = vY;
				entity.motionZ = vZ;
				entity.velocityChanged = true;
			}
		}
		
		if(action == 2) { // ACTION::add
			for(Entity entity : entities) {
				entity.motionX += vX;
				entity.motionY += vY;
				entity.motionZ += vZ;
				entity.velocityChanged = true;
			}
		}
		
		if(action == 3) { // ACTION::multiply
			for(Entity entity : entities) {
				entity.motionX *= vX;
				entity.motionY *= vY;
				entity.motionZ *= vZ;
				entity.velocityChanged = true;
			}
		}
		
		if(action == 4) { // ACTION::multiply
			for(Entity entity : entities) {
				float yaw = 90 - entity.rotationYaw;
				float pitch = entity.rotationPitch;
				
		        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
		        float f5 = MathHelper.sin(-pitch * 0.017453292F);
				
				entity.motionX += f2 * vX;
				entity.motionY += vY;
				entity.motionZ += f3 * vX;
				entity.velocityChanged = true;
			}
		}
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length == 1) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"set", "add", "multiply", "viewadd"});
    	}
    	
    	if(args.length == 2) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"this", "@a", "@p", "@r", "@e"});
    	}
    	
    	if(args.length >= 3) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"1","0","-1"});
    	}
    	
        return null;
    }
    
}
