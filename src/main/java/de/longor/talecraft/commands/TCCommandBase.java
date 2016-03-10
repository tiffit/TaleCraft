package de.longor.talecraft.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public abstract class TCCommandBase extends CommandBase {
	
    public int getRequiredPermissionLevel() {
        return 2;
    }
	
	/*
		UP:    0; +y
		DOWN:  1; -y
		NORTH: 2; -z
		EAST:  3; +x
		SOUTH: 4; +z
		WEST:  5; -x
	//*/
	public static final int parseDirection(String directionStr, EntityPlayer player) throws CommandException {
		int direction = -1;
		
		if(directionStr.equalsIgnoreCase("east")  || directionStr.equalsIgnoreCase("+x") || directionStr.equalsIgnoreCase("x")) direction = 3; // +x
		if(directionStr.equalsIgnoreCase("up")    || directionStr.equalsIgnoreCase("+y") || directionStr.equalsIgnoreCase("y")) direction = 0; // +y
		if(directionStr.equalsIgnoreCase("south") || directionStr.equalsIgnoreCase("+z") || directionStr.equalsIgnoreCase("z")) direction = 4; // +z
		
		if(directionStr.equalsIgnoreCase("down")  || directionStr.equalsIgnoreCase("-y")) direction = 1; // -y
		if(directionStr.equalsIgnoreCase("north") || directionStr.equalsIgnoreCase("-z")) direction = 2; // -z
		if(directionStr.equalsIgnoreCase("west")  || directionStr.equalsIgnoreCase("-x")) direction = 5; // -x
		
		if(directionStr.equalsIgnoreCase("me")) {
        	EnumFacing directionSky = player.getHorizontalFacing();
        	EnumFacing directionFull = null;
        	
    		if(player.rotationPitch > 45) {
    			directionFull = EnumFacing.DOWN;
    		} else if(player.rotationPitch < -45) {
    			directionFull = EnumFacing.UP;
    		} else {
    			directionFull = player.getHorizontalFacing();
    		}
			
    		switch(directionFull) {
	    		case UP:    direction = 0; break; // +y
	    		case DOWN:  direction = 1; break; // -y
	    		case NORTH: direction = 2; break; // -z
	    		case EAST:  direction = 3; break; // +x
	    		case SOUTH: direction = 4; break; // +z
	    		case WEST:  direction = 5; break; // -x
	    		default: throw new WrongUsageException("WHAT THE FUCK?!");
    		}
		}
		
		return direction;
	}
    
    public static final int parseColor(String color) throws CommandException {
    	if(color.equalsIgnoreCase("0")) return 0xFF000000;
    	if(color.equalsIgnoreCase("1")) return 0xFFFFFFFF;
    	
    	if(color.startsWith("#")) {
    		// Is a HEX color!
    		color = color.substring(1);
    		
    		if(color.length() == 3) {
    			int r = (hexcharToInt(color.charAt(0)) * 0xF) & 0xFF;
    			int g = (hexcharToInt(color.charAt(1)) * 0xF) & 0xFF;
    			int b = (hexcharToInt(color.charAt(2)) * 0xF) & 0xFF;
    			return (r<<16) | (g<<8) | (b);
    		} else if(color.length() == 6) {
    			int r = hexcharToInt(color.charAt(0), color.charAt(1)) & 0xFF;
    			int g = hexcharToInt(color.charAt(2), color.charAt(3)) & 0xFF;
    			int b = hexcharToInt(color.charAt(4), color.charAt(5)) & 0xFF;
    			return (r<<16) | (g<<8) | (b);
    		} else if(color.length() == 8) {
    			int a = hexcharToInt(color.charAt(0), color.charAt(1)) & 0xFF;
    			int r = hexcharToInt(color.charAt(2), color.charAt(3)) & 0xFF;
    			int g = hexcharToInt(color.charAt(4), color.charAt(5)) & 0xFF;
    			int b = hexcharToInt(color.charAt(6), color.charAt(7)) & 0xFF;
    			return (a<<24) | (r<<16) | (g<<8) | (b);
    		}
    	}
    	
    	if(color.equalsIgnoreCase("white")) return 0xFFFFFFFF;
    	if(color.equalsIgnoreCase("black")) return 0xFF000000;
    	if(color.equalsIgnoreCase("red")) return 0xFFFF0000;
    	if(color.equalsIgnoreCase("green")) return 0xFF00FF00;
    	if(color.equalsIgnoreCase("blue")) return 0xFF0000FF;
    	if(color.equalsIgnoreCase("yellow")) return 0xFFFFFF00;
    	if(color.equalsIgnoreCase("purple")) return 0xFFFF00FF;
    	if(color.equalsIgnoreCase("orange")) return 0xFFFF7F00;
    	
    	throw new CommandException("Could not parse color: " + color);
    }
    
	private static final int hexcharToInt(char charAt) {
		return Integer.parseInt(String.valueOf(charAt).toLowerCase(), 16);
	}
	
	private static final int hexcharToInt(char charA, char charB) {
		return Integer.parseInt((charA+""+charB).toLowerCase(), 16);
	}
	
}
