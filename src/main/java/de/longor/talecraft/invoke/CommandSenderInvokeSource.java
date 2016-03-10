package de.longor.talecraft.invoke;

import java.util.List;

import jdk.nashorn.internal.objects.NativeObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;

public class CommandSenderInvokeSource implements IInvokeSource {
	Scriptable scope;
	ICommandSender sender;
	
	public CommandSenderInvokeSource(ICommandSender sender) {
		this.scope = TaleCraft.globalScriptManager.createNewScope();
		this.sender = sender;
	}
	
	@Override
	public Scriptable getInvokeScriptScope() {
		return scope;
	}

	@Override
	public ICommandSender getInvokeAsCommandSender() {
		return sender;
	}

	@Override
	public BlockPos getInvokePosition() {
		return sender.getPosition();
	}

	@Override
	public World getInvokeWorld() {
		return sender.getEntityWorld();
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// nope
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 1.0f;
		color[2] = 1.0f;
	}

}
