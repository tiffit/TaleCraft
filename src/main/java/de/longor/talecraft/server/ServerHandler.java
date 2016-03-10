package de.longor.talecraft.server;

import java.util.HashMap;
import java.util.UUID;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCIBlockCommandReceiver;
import de.longor.talecraft.network.*;
import de.longor.talecraft.util.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ServerHandler {
	
	public static void handleEntityJoin(World world, Entity entity) {
		// If this is a player, send the player the persistent EntityData.
		if(entity instanceof EntityPlayerMP) {
			TaleCraft.network.sendTo(new PlayerNBTDataMerge(entity.getEntityData()), (EntityPlayerMP) entity);
			// PlayerList.playerJoin((EntityPlayerMP)entity);
		}
	}
	
	public static void handleSNBTCommand(NetHandlerPlayServer serverHandler, StringNBTCommandPacket message) {
		EntityPlayerMP playerEntity = serverHandler.playerEntity;
		WorldServer worldServer = playerEntity.getServerForPlayer();
		handleSNBTCommand(playerEntity, worldServer, message);
	}
	
	/** This method actually handles the SNBT-command. **/
	private static void handleSNBTCommand(EntityPlayerMP player, World world, StringNBTCommandPacket message) {
		if(world.isRemote){
			TaleCraft.logger.error("FATAL ERROR: ServerHandler method was called on client-side!");
			return;
		}
		
		if(message.command.equals("server.client.connection.state.change:join_acknowledged")) {
			TaleCraft.logger.info("join acknowledged : " + message.data);
			getServerMirror(null).playerList().getPlayer(player).construct(message.data);
			return;
		}
		
		if(message.command.equals("server.client.settings.update")) {
			TaleCraft.logger.info("updating settings " + message.data);
			getServerMirror(null).playerList().getPlayer(player).updateSettings(message.data);
			return;
		}
		
		if(message.command.equals("server.data.entity.merge")) {
			if(!PlayerHelper.isOp(player)) {
				player.addChatMessage(new ChatComponentText("Error: 'server.data.entity.merge' is a operator only command."));
				return;
			}
			
			String uuidStr = message.data.getString("entityUUID");
			UUID uuid = UUID.fromString(uuidStr);
			
			Entity theEntity = null;
			
			for(Object entityObject : world.loadedEntityList) {
				Entity entity = (Entity) entityObject;
				
				if(entity.getUniqueID().equals(uuid)) {
					theEntity = entity;
					break;
				}
			}
			
			if(theEntity == null) {
				player.addChatMessage(new ChatComponentText("Error: Entity not found. (Possibly dead)"));
				return;
			}
			
			// Clean data
			NBTTagCompound entityData = new NBTTagCompound();
			NBTTagCompound mergeData = message.data.getCompoundTag("entityData");
			
			mergeData.removeTag("UUIDMost");
			mergeData.removeTag("UUIDLeast");
			mergeData.removeTag("Dimension");
			mergeData.removeTag("Pos");
	        
			theEntity.writeToNBT(entityData);
			entityData.merge(mergeData);
			theEntity.readFromNBT(entityData);
			
			if(entityData.hasKey("TC_Width")) theEntity.width = entityData.getFloat("TC_Width");
			if(entityData.hasKey("TC_Height")) theEntity.height = entityData.getFloat("TC_Height");
			if(entityData.hasKey("TC_StepHeight")) theEntity.stepHeight = entityData.getFloat("TC_StepHeight");
			if(entityData.hasKey("TC_NoClip")) theEntity.noClip = entityData.getBoolean("TC_NoClip");
			
			// Done!
			return;
		}
		
		if(message.command.startsWith("server.data.block.merge:")) {
			if(!PlayerHelper.isOp(player)) {
				player.addChatMessage(new ChatComponentText("Error: 'blockdatamerge' is a operator only command."));
				return;
			}
			
			String positionString = message.command.substring(24);
			String[] posStrings = positionString.split(" ");
			BlockPos position = new BlockPos(Integer.valueOf(posStrings[0]), Integer.valueOf(posStrings[1]), Integer.valueOf(posStrings[2]));
			
			TileEntity entity = world.getTileEntity(position);
			
			if(entity != null) {
				TaleCraft.logger.info("(datamerge) " + position + " -> " + message.data);
				mergeTileEntityData(entity, message.data);
				return;
			} else {
				player.addChatMessage(new ChatComponentText("Error: Failed to merge block data: TileEntity does not exist."));
				return;
			}
		}
		
		if(message.command.startsWith("server.data.block.command:")) {
			if(!PlayerHelper.isOp(player)) {
				player.addChatMessage(new ChatComponentText("Error: 'blockcommand' is a operator only command."));
				return;
			}
			
			String positionString = message.command.substring(26);
			String[] posStrings = positionString.split(" ");
			BlockPos position = new BlockPos(Integer.valueOf(posStrings[0]), Integer.valueOf(posStrings[1]), Integer.valueOf(posStrings[2]));
			
			TileEntity entity = world.getTileEntity(position);
			
			if(entity != null) {
				// TaleCraft.logger.info("(block command) " + position + " -> " + commandPacket.data);
				
				if(entity instanceof TCIBlockCommandReceiver) {
					((TCIBlockCommandReceiver) entity).commandReceived(message.data.getString("command"), message.data);
					return;
				}
			} else {
				player.addChatMessage(new ChatComponentText("Error: Failed to run block-command: TileEntity does not exist."));
				return;
			}
		}
		
		TaleCraft.logger.error("Received unknown StringNBTCommand from client: "+message.command+" : "+message.data);
	}
	
	/** Merges the given {@link NBTTagCompound} into the given {@link TileEntity} data. **/
	private static void mergeTileEntityData(TileEntity entity, NBTTagCompound data) {
		BlockPos blockpos = entity.getPos();
		NBTTagCompound compound = new NBTTagCompound();
		entity.writeToNBT(compound);
		
		compound.merge(data);
		compound.setInteger("x", blockpos.getX());
		compound.setInteger("y", blockpos.getY());
		compound.setInteger("z", blockpos.getZ());
		
		entity.readFromNBT(compound);
		entity.markDirty();
		entity.getWorld().markBlockForUpdate(blockpos);
	}
	
	private static HashMap<MinecraftServer, ServerMirror> serverMirrorsMap = new HashMap<MinecraftServer, ServerMirror>();
	public static ServerMirror getServerMirror(MinecraftServer server) {
		if(server == null) {
			server = MinecraftServer.getServer();
		}
		
		ServerMirror mirror = serverMirrorsMap.get(server);
		
		if(mirror == null) {
			mirror = new ServerMirror();
			mirror.create(server);
			serverMirrorsMap.put(server, mirror);
		}
		
		return mirror;
	}
	
	public static void destroyServerMirror(MinecraftServer server) {
		if(server == null) {
			for(ServerMirror mirror : serverMirrorsMap.values()) {
				mirror.destroy();
			}
			serverMirrorsMap.clear();
			return;
		}
		
		ServerMirror mirror = serverMirrorsMap.remove(server);
		
		if(mirror != null) {
			mirror.destroy();
		}
	}
	
}
