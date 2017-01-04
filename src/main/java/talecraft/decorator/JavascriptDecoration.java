package talecraft.decorator;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import talecraft.TaleCraft;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.FileScriptInvoke;
import talecraft.invoke.IInvoke;
import talecraft.invoke.IInvokeSource;
import talecraft.invoke.Invoke;

public class JavascriptDecoration implements Decoration{

	private final FileScriptInvoke invoke;
	private Scriptable scope;
	private final String name;
	
	public JavascriptDecoration(FileScriptInvoke invoke, String name) {
		this.invoke = invoke;
		this.name = name;
	}

	@Override
	public int plant(World world, BlockPos[] positions, NBTTagCompound options) {
		scope = TaleCraft.globalScriptManager.createNewDecorationScope(world, positions, options);
		Invoke.invoke(invoke, new InvokeSource(world, positions), null, EnumTriggerState.IGNORE);
		return 0;
	}

	@Override
	public String name() {
		return name;
	}
	
	private class InvokeSource implements IInvokeSource, ICommandSender{

		World world;
		BlockPos[] positions;
		
		InvokeSource(World world, BlockPos[] positions){
			this.world = world;
			this.positions = positions;
		}
		
		@Override
		public Scriptable getInvokeScriptScope() {
			return scope;
		}

		@Override
		public ICommandSender getInvokeAsCommandSender() {
			return this;
		}

		@Override
		public BlockPos getInvokePosition() {
			return positions[0];
		}

		@Override
		public World getInvokeWorld() {
			return world;
		}

		@Override
		public void getInvokes(List<IInvoke> invokes) {
			invokes.add(invoke);
		}

		@Override
		public void getInvokeColor(float[] color) {
			color[0] = 1.0f;
			color[1] = 0.5f;
			color[2] = 0.0f;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public ITextComponent getDisplayName() {
			return new TextComponentString(getName());
		}

		@Override
		public void sendMessage(ITextComponent component) {}

		@Override
		public boolean canUseCommand(int permLevel, String commandName) {
			return false;
		}

		@Override
		public BlockPos getPosition() {
			return positions[0];
		}

		@Override
		public Vec3d getPositionVector() {
			return new Vec3d(0, 0, 0);
		}

		@Override
		public World getEntityWorld() {
			return world;
		}

		@Override
		public Entity getCommandSenderEntity() {
			return null;
		}

		@Override
		public boolean sendCommandFeedback() {
			return false;
		}

		@Override
		public void setCommandStat(Type type, int amount) {
		}

		@Override
		public MinecraftServer getServer() {
			return FMLCommonHandler.instance().getMinecraftServerInstance();
		}
	}

}
