package de.longor.talecraft;

import de.longor.talecraft.network.StringNBTCommandPacket;
import de.longor.talecraft.server.ServerHandler;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TaleCraftEventHandler {
	public TaleCraftEventHandler() {}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void keyEvent(KeyInputEvent event) {
		if(FMLCommonHandler.instance().getSide().isClient()) {
			if(TaleCraft.proxy instanceof de.longor.talecraft.proxy.ClientProxy) {
				TaleCraft.proxy.asClient().getKeyboardHandler().on_key(event);
			}
		}
	}

	@SubscribeEvent
	public void tick(TickEvent event) {
		TaleCraft.proxy.tick(event);
	}

	@SubscribeEvent
	public void tickWorld(WorldTickEvent event) {
		TaleCraft.proxy.tickWorld(event);
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			ServerHandler.getServerMirror(null).playerList().playerJoin((EntityPlayerMP) event.player);
			TaleCraft.network.sendTo(new StringNBTCommandPacket("client.network.join"), (EntityPlayerMP) event.player);
		}
		
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerLoggedOutEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			ServerHandler.getServerMirror(null).playerList().playerLeave((EntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event) {
		TaleCraft.worldsManager.registerWorld(event.getWorld());
	}

	@SubscribeEvent
	public void worldUnload(WorldEvent.Unload event) {
		TaleCraft.worldsManager.unregisterWorld(event.getWorld());
	}

	/*
	@SubscribeEvent
	public void guiRenderPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
		event.getGui().drawString(event.getGui().mc.fontRendererObj, "Hello, World!", 1, 1, 0xFFFFFF);
	}
	 */

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if(!event.getWorld().isRemote) {
			ServerHandler.handleEntityJoin(event.getWorld(), event.getEntity());
			TaleCraft.worldsManager.fetchManager(event.getWorld()).joinWorld(event.getEntity());
		}
	}

	@SubscribeEvent
	public void onLivingAttacked(LivingAttackEvent event) {
		World world = event.getEntity().worldObj;

		if(world.isRemote) return;

		if(world.getGameRules().getBoolean("disable.damage.*")) {
			event.setCanceled(true);
			return;
		}

		if(event.getSource() == DamageSource.fall && world.getGameRules().getBoolean("disable.damage.fall")) {
			event.setCanceled(true);
			return;
		}

		if(event.getSource() == DamageSource.drown && world.getGameRules().getBoolean("disable.damage.drown")) {
			event.setCanceled(true);
			return;
		}

		if(event.getSource() == DamageSource.lava && world.getGameRules().getBoolean("disable.damage.lava")) {
			event.setCanceled(true);
			return;
		}

		if(event.getSource() == DamageSource.magic && world.getGameRules().getBoolean("disable.damage.magic")) {
			event.setCanceled(true);
			return;
		}

		if(event.getSource() == DamageSource.inFire && world.getGameRules().getBoolean("disable.damage.fire")) {
			event.setCanceled(true);
			return;
		}

		if(event.getSource() == DamageSource.inWall && world.getGameRules().getBoolean("disable.damage.suffocate")) {
			event.setCanceled(true);
			return;
		}

	}

	@SubscribeEvent
	public void playerUseItem(final PlayerInteractEvent.RightClickItem event) {
		if(event.getSide() == Side.CLIENT) return;
		ItemStack stack = event.getItemStack();
		final EntityPlayer player = event.getEntityPlayer();
		boolean hasCommandTag = stack.hasTagCompound() ? stack.getTagCompound().hasKey("command") : false;
		if(hasCommandTag){
			String command = stack.getTagCompound().getString("command");
			FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(new ICommandSender() {
				@Override
				public void setCommandStat(Type type, int amount) {
					player.setCommandStat(type, amount);
				}
				@Override
				public boolean sendCommandFeedback() {
					return false;
				}
				@Override
				public MinecraftServer getServer() {
					return FMLCommonHandler.instance().getMinecraftServerInstance();
				}
				@Override
				public Vec3d getPositionVector() {
					return player.getPositionVector();
				}
				
				@Override
				public BlockPos getPosition() {
					return player.getPosition();
				}
				
				@Override
				public String getName() {
					return player.getName();
				}
				
				@Override
				public World getEntityWorld() {
					return player.getEntityWorld();
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return player.getDisplayName();
				}
				@Override
				public Entity getCommandSenderEntity() {
					return player;
				}
				@Override
				public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
					return true;
				}
				@Override
				public void addChatMessage(ITextComponent component) {
					event.getEntityPlayer().addChatMessage(component);
				}
			}, command);
		}
	}
	 

}
