package talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import talecraft.TaleCraft;
import talecraft.TaleCraftItems;
import talecraft.clipboard.ClipboardItem;
import talecraft.invoke.CommandSenderInvokeSource;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.Invoke;
import talecraft.items.CopyItem;
import talecraft.items.WandItem;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.server.ServerMirror;
import talecraft.util.GObjectTypeHelper;
import talecraft.util.WorldHelper;

public class WandCommand extends TCCommandBase {
	private static final String[] BOUNDS = new String[] {"x0","y0","z0","x1","y1","z1"};
	private static final String[] COORDINATES = new String[] {"~", "0", "1"};
	private static final String[] DIRECTIONS = new String[] {
			"me",
			"up","down",
			"north","east","south","west",
			"x","y","z",
			"+x","+y","+z",
			"-x","-y","-z"
	};

	@Override
	public String getName() {
		return "tc_wand";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "? (Use tab-completion!)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP player = CommandBase.getCommandSenderAsPlayer(sender);

		if(args.length == 0) {
			throw new WrongUsageException("No parameters given!");
		}

		if(args[0].equals(".")) {
			BlockPos position = player.getPosition();

			int ix = position.getX();
			int iy = position.getY();
			int iz = position.getZ();
			int ax = position.getX();
			int ay = position.getY();
			int az = position.getZ();

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		}

		if(args[0].equals("..")) {
			BlockPos position = player.getPosition();

			int ix = position.getX();
			int iy = position.getY();
			int iz = position.getZ();
			int ax = position.getX();
			int ay = position.getY()+1;
			int az = position.getZ();

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		}

		if(args[0].equals(".o")) {
			WandItem.setBounds(player, 0, 0, 0, 0, 0, 0);
			return;
		}

		if(args[0].equals(".c")) {
			// BlockPos position = player.getPosition();

			int ix = player.chunkCoordX * 16;
			int iy = 0;
			int iz = player.chunkCoordZ * 16;
			int ax = player.chunkCoordX * 16 + 16;
			int ay = 255;
			int az = player.chunkCoordZ * 16 + 16;

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		}

		if(args[0].equals("set")) {
			execute_set(args, player, sender);
			return;
		}

		if(args[0].equals("expand_to") || args[0].equals("exto")) {
			execute_expand_to(args, player, sender);
			return;
		}

		if(args[0].equals("expand_into") || args[0].equals("exito")) {
			execute_expand_into(args, player, sender);
			return;
		}

		if(args[0].equals("expand")) {
			execute_expand(args, player, sender);
			return;
		}

		if(args[0].equals("expand_h")) {
			execute_expand_h(args, player, sender);
			return;
		}

		if(args[0].equals("expand_v")) {
			execute_expand_v(args, player, sender);
			return;
		}

		if(args[0].equals("region")) {
			execute_region(args, player, sender);
			return;
		}

	}

	private void execute_set(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {

		if(args.length == 3) {
			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			String varStr = args[1];

			int ix = bounds[0];
			int iy = bounds[1];
			int iz = bounds[2];
			int ax = bounds[3];
			int ay = bounds[4];
			int az = bounds[5];

			if(varStr.equalsIgnoreCase("x0")) {
				ix = (int) CommandBase.parseDouble(sender.getPosition().getX(), varStr, false);
			} else if(varStr.equalsIgnoreCase("y0")) {
				iy = (int) CommandBase.parseDouble(sender.getPosition().getY(), varStr, false);
			} else if(varStr.equalsIgnoreCase("z0")) {
				iz = (int) CommandBase.parseDouble(sender.getPosition().getZ(), varStr, false);
			} else if(varStr.equalsIgnoreCase("x1")) {
				ax = (int) CommandBase.parseDouble(sender.getPosition().getX(), varStr, false);
			} else if(varStr.equalsIgnoreCase("y1")) {
				ay = (int) CommandBase.parseDouble(sender.getPosition().getY(), varStr, false);
			} else if(varStr.equalsIgnoreCase("z1")) {
				az = (int) CommandBase.parseDouble(sender.getPosition().getZ(), varStr, false);
			} else {
				throw new WrongUsageException("Wrong parameter 'bound'! /tc_wand set <bound> <value>");
			}

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
		} else if(args.length == 7) {
			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int ix = (int) CommandBase.parseDouble(sender.getPosition().getX(), args[1], false);//bounds[0];
			int iy = (int) CommandBase.parseDouble(sender.getPosition().getY(), args[2], false);;//bounds[1];
			int iz = (int) CommandBase.parseDouble(sender.getPosition().getZ(), args[3], false);;//bounds[2];
			int ax = (int) CommandBase.parseDouble(sender.getPosition().getX(), args[4], false);;//bounds[3];
			int ay = (int) CommandBase.parseDouble(sender.getPosition().getY(), args[5], false);;//bounds[4];
			int az = (int) CommandBase.parseDouble(sender.getPosition().getZ(), args[6], false);;//bounds[5];

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand set < ? >");
		}
	}

	private void execute_expand(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 2) {
			int value = CommandBase.parseInt(args[1]);

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int ix = bounds[0] - value;
			int iy = bounds[1] - value;
			int iz = bounds[2] - value;
			int ax = bounds[3] + value;
			int ay = bounds[4] + value;
			int az = bounds[5] + value;

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand expand <val>");
		}
	}

	private void execute_expand_h(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 2) {
			int value = CommandBase.parseInt(args[1]);

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int ix = bounds[0] - value;
			int iy = bounds[1];
			int iz = bounds[2] - value;
			int ax = bounds[3] + value;
			int ay = bounds[4];
			int az = bounds[5] + value;

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand expand <val>");
		}
	}

	private void execute_expand_v(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 2) {
			int value = CommandBase.parseInt(args[1], 0, 128);

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int ix = bounds[0];
			int iy = bounds[1] - value;
			int iz = bounds[2];
			int ax = bounds[3];
			int ay = bounds[4] + value;
			int az = bounds[5];

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand expand <val>");
		}
	}

	private void execute_expand_to(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 4) {
			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int posX = (int) CommandBase.parseDouble(sender.getPosition().getX(), args[1], false);
			int posY = (int) CommandBase.parseDouble(sender.getPosition().getY(), args[2], false);
			int posZ = (int) CommandBase.parseDouble(sender.getPosition().getZ(), args[3], false);

			int ix = bounds[0];
			int iy = bounds[1];
			int iz = bounds[2];
			int ax = bounds[3];
			int ay = bounds[4];
			int az = bounds[5];

			if(ix > posX) ix = posX;
			if(iy > posY) iy = posY;
			if(iz > posZ) iz = posZ;
			if(ax < posX) ax = posX;
			if(ay < posY) ay = posY;
			if(az < posZ) az = posZ;

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand expand_to <x> <y> <z>");
		}
	}

	private void execute_expand_into(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 3) {
			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int ix = bounds[0];
			int iy = bounds[1];
			int iz = bounds[2];
			int ax = bounds[3];
			int ay = bounds[4];
			int az = bounds[5];

			int direction = parseDirection(args[1], player);
			int value = CommandBase.parseInt(args[2], 1, 128);

			switch (direction) {
			case 0: ay += value; break; // +y
			case 1: iy -= value; break; // -y
			case 2: iz -= value; break; // -z
			case 3: ax += value; break; // +x
			case 4: az += value; break; // +z
			case 5: ix -= value; break; // -x
			default: throw new WrongUsageException("Direction unknown: " + args[2]);
			}

			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand expand <direction> <value>");
		}
	}

	private void execute_region(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length >= 2) {

			if(args[1].equals("erase")) {
				execute_region_erase(args, player, sender);
				return;
			}

			if(args[1].equals("copy")) {
				execute_region_copy(args, player, sender);
				return;
			}

			if(args[1].equals("paste")) {
				execute_region_paste(args, player, sender);
				return;
			}

			if(args[1].equals("trigger")) {
				execute_region_trigger(args, player, sender);
				return;
			}

			if(args[1].equals("fill")) {
				execute_region_fill(args, player, sender);
				return;
			}

			if(args[1].equals("replace")) {
				execute_region_replace(args, player, sender);
				return;
			}

			if(args[1].equals("room")) {
				execute_region_room(args, player, sender);
				return;
			}

			if(args[1].equals("repeat")) {
				execute_region_repeat(args,player,sender);
				return;
			}

			if(args[1].equals("butcher")) {
				int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand.");
				}

				AxisAlignedBB aabb = new AxisAlignedBB(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
				aabb = aabb.expand(1, 1, 1);
				List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABB(Entity.class, aabb);

				for(Entity entity : entities) {
					if(entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer))
						entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 10000f);
					else if(entity instanceof EntityItem)
						entity.setDead();
				}

				return;
			}
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand region ...?");
		}

	}

	/****/
	private void execute_region_erase(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
		if(bounds == null) {
			throw new WrongUsageException("You didn't select a region with your wand.");
		}

		WorldHelper.fill(player.getEntityWorld(), bounds, Blocks.AIR.getDefaultState());
	}

	/****/
	private void execute_region_copy(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length > 2) {
			String name = args[2];

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			ClipboardItem item = ClipboardItem.copyRegion(bounds, player.getEntityWorld(), name, player);

			if(item != null) {
				ServerMirror.instance().getClipboard().put(name, item);
				player.sendMessage(new TextComponentString(TextFormatting.GREEN+"Copied region to clipboard as '"+name+"'!"));
			}
		} else {
			CopyItem copy = TaleCraftItems.copy;
			// ItemStack stack = new ItemStack(copy);
			copy.onItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
			TaleCraft.network.sendTo(new StringNBTCommandPacket("item.copy.trigger"), player);
		}
	}

	/****/
	private void execute_region_paste(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 2) {
			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			String name = "player."+player.getGameProfile().getId().toString();
			ClipboardItem item = ServerMirror.instance().getClipboard().get(name);

			if(item != null) {
				ClipboardItem.pasteRegion(item, new BlockPos(bounds[0], bounds[1], bounds[2]), player.getEntityWorld(), player);
				player.sendMessage(new TextComponentString(TextFormatting.GREEN+"Copied region to world: '"+name+"'."));
			} else {
				throw new CommandException("There is no record with the name '"+name+"' in the clipboard.");
			}
			return;
		}

		if(args.length > 2) {
			String name = args[2];

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			ClipboardItem item = ServerMirror.instance().getClipboard().get(name);

			if(item != null) {
				ClipboardItem.pasteRegion(item, new BlockPos(bounds[0], bounds[1], bounds[2]), player.getEntityWorld(), player);
				player.sendMessage(new TextComponentString(TextFormatting.GREEN+"Copied region to world: '"+name+"'."));
			} else {
				throw new CommandException("There is no record with the name '"+name+"' in the clipboard.");
			}
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand region paste <name>");
		}
	}

	/****/
	private void execute_region_trigger(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
		if(bounds == null) {
			throw new WrongUsageException("You didn't select a region with your wand.");
		}

		int ix = bounds[0];
		int iy = bounds[1];
		int iz = bounds[2];
		int ax = bounds[3];
		int ay = bounds[4];
		int az = bounds[5];

		Invoke.trigger(new CommandSenderInvokeSource(player), ix, iy, iz, ax, ay, az, EnumTriggerState.ON);
	}

	/****/
	private void execute_region_fill(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 3) {
			IBlockState replace = GObjectTypeHelper.findBlockState(args[2]);

			if(replace == null) {
				throw new CommandException("Could not find block type: " + args[2]);
			}

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			WorldHelper.fill(player.getEntityWorld(), bounds, replace);
			return;
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand region fill <block>");
		}
	}

	/****/
	private void execute_region_replace(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 4) {
			IBlockState replace = GObjectTypeHelper.findBlockState(args[2]);
			IBlockState mask = GObjectTypeHelper.findBlockState(args[3]);

			if(replace == null) {
				throw new CommandException("Could not find block type: " + args[2]);
			}

			if(mask == null) {
				throw new CommandException("Could not find block type: " + args[3]);
			}

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			WorldHelper.replace(player.getEntityWorld(), bounds, replace, mask);
			return;
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand region fill <block>");
		}
	}

	/****/
	private void execute_region_room(String[] args, EntityPlayerMP player, ICommandSender sender) throws CommandException {
		if(args.length == 4) {
			IBlockState replace = GObjectTypeHelper.findBlockState(args[3]);

			if(replace == null) {
				throw new CommandException("Could not find block type: " + args[3]);
			}

			boolean f_up = false;
			boolean f_down = false;
			boolean f_north = false;
			boolean f_east = false;
			boolean f_south = false;
			boolean f_west = false;

			String flagsStr = args[2];

			{ // Parse Wall Flags
				if(containsCharFlagIgnoreCase(flagsStr, 'U')) f_up = true;
				if(containsCharFlagIgnoreCase(flagsStr, 'D')) f_down = true;
				if(containsCharFlagIgnoreCase(flagsStr, 'N')) f_north = true;
				if(containsCharFlagIgnoreCase(flagsStr, 'E')) f_east = true;
				if(containsCharFlagIgnoreCase(flagsStr, 'S')) f_south = true;
				if(containsCharFlagIgnoreCase(flagsStr, 'W')) f_west = true;

				if(containsCharFlagIgnoreCase(flagsStr, 'B')) {
					f_north = true;
					f_east = true;
					f_south = true;
					f_west = true;
				}

				if(containsCharFlagIgnoreCase(flagsStr, 'A')) {
					f_up = true;
					f_down = true;
					f_north = true;
					f_east = true;
					f_south = true;
					f_west = true;
				}
			}

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int ix = bounds[0];
			int iy = bounds[1];
			int iz = bounds[2];
			int ax = bounds[3];
			int ay = bounds[4];
			int az = bounds[5];

			if(f_down)  WorldHelper.fill(player.getEntityWorld(), ix, iy, iz, ax, iy, az, replace);
			if(f_up)    WorldHelper.fill(player.getEntityWorld(), ix, ay, iz, ax, ay, az, replace);
			if(f_north) WorldHelper.fill(player.getEntityWorld(), ix, iy, iz, ax, ay, iz, replace);
			if(f_east)  WorldHelper.fill(player.getEntityWorld(), ax, iy, iz, ax, ay, az, replace);
			if(f_south) WorldHelper.fill(player.getEntityWorld(), ix, iy, az, ax, ay, az, replace);
			if(f_west)  WorldHelper.fill(player.getEntityWorld(), ix, iy, iz, ix, ay, az, replace);

			// WorldHelper.fill(player.getEntityWorld(), bounds, replace);
			return;
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand region room <UDNESW BA> <block>");
		}
	}

	/****/
	private void execute_region_repeat(String[] args, EntityPlayerMP player,ICommandSender sender) throws CommandException {
		if(args.length >= 3) {
			int count = parseInt(args[2], 1, 128);

			String directionStr = args.length == 4 ? args[3] : "me";
			int direction = parseDirection(directionStr, player);

			int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
			if(bounds == null) {
				throw new WrongUsageException("You didn't select a region with your wand.");
			}

			int sizeX = bounds[3] - bounds[0] + 1;
			int sizeY = bounds[4] - bounds[1] + 1;
			int sizeZ = bounds[5] - bounds[2] + 1;

			ClipboardItem item = ClipboardItem.copyRegion(bounds, player.getEntityWorld(), null, player);
			int originX = bounds[0];
			int originY = bounds[1];
			int originZ = bounds[2];

			int addX = 0;
			int addY = 0;
			int addZ = 0;

			switch(direction) {
			case 0: {addY = sizeY;} break;
			case 1: {addY = -sizeY;} break;
			case 2: {addZ = -sizeZ;} break;
			case 3: {addX = sizeX;} break;
			case 4: {addZ = sizeZ;} break;
			case 5: {addX = -sizeX;} break;
			default: throw new CommandException("Direction unknown: " + directionStr);
			}

			int curX = originX;
			int curY = originY;
			int curZ = originZ;
			for(int i = 0; i < count; i++) {
				curX += addX;
				curY += addY;
				curZ += addZ;
				ClipboardItem.pasteRegion(item, new BlockPos(curX, curY, curZ), player.getEntityWorld(), player);
			}
		} else {
			throw new WrongUsageException("Missing parameters! /tc_wand region repeat <count> [direction]");
		}
	}

	private boolean containsCharFlagIgnoreCase(String flagsStr, char c) {
		char cLow = Character.toLowerCase(c);
		char cUp  = Character.toUpperCase(c);
		return flagsStr.indexOf(cUp) != -1 || flagsStr.indexOf(cLow) != -1;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length <= 1) {
			return getListOfStringsMatchingLastWord(args, new String[] {
					"region",
					"expand",
					"expand_h",
					"expand_v",
					"expand_to",
					"expand_into",
					".", "..", ".o", ".c",
					"set"
			});
		}

		if(args.length >= 2 && args[0].equals("expand_to")) {
			return getListOfStringsMatchingLastWord(args, COORDINATES);
		}

		if(args.length == 2 && args[0].equals("expand_into")) {
			return getListOfStringsMatchingLastWord(args, DIRECTIONS);
		}

		if(args.length >= 2 && args[0].equals("exto")) {
			return getListOfStringsMatchingLastWord(args, COORDINATES);
		}

		if(args.length == 2 && args[0].equals("exito")) {
			return getListOfStringsMatchingLastWord(args, DIRECTIONS);
		}

		if(args.length == 2 && args[0].equals("set")) {
			return getListOfStringsMatchingLastWord(args, BOUNDS);
		}

		if(args.length > 2 && args[0].equals("set")) {
			return getListOfStringsMatchingLastWord(args, COORDINATES);
		}

		if(args[0].equals("region")) {
			if(args.length == 2) {
				return getListOfStringsMatchingLastWord(args, new String[] {"erase", "fill", "replace","room","repeat","copy","paste","trigger","butcher"});
			}
			if(args.length == 3 && args[1].equals("fill")) {
				return CommandBase.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
			}
			if(args.length >= 3 && args[1].equals("replace")) {
				return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
			}
			if(args.length >= 4 && args[1].equals("room")) {
				return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
			}
		}

		return Collections.emptyList();
	}

}
