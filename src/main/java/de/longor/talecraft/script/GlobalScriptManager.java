package de.longor.talecraft.script;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Interpreter;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.ast.Scope;

import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.proxy.CommonProxy;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.world.WorldObjectWrapper;
import de.longor.talecraft.util.MutableBlockPos;

public class GlobalScriptManager {
	private NativeObject globalScope;
	private ClassShutter globalClassShutter;
	private ContextFactory globalContextFactory;
	private GlobalScriptObject globalScriptObject;
	private ConsoleOutput consoleOutput;
	
	public void init(TaleCraft taleCraft, CommonProxy proxy) {
		taleCraft.logger.info("Initializing Rhino Script Engine...");
		globalScope = new NativeObject();
		globalClassShutter = new GlobalClassShutter();
		
		globalContextFactory = new GlobalContextFactory();
		ContextFactory.initGlobal(globalContextFactory);
		
		Context cx = Context.enter();
		try {
			ScriptRuntime.initStandardObjects(cx, globalScope, true);
			
			globalScriptObject = new GlobalScriptObject(this);
			ScriptableObject.putProperty(globalScope, "system", Context.javaToJS(globalScriptObject, globalScope));
			
			consoleOutput = new ConsoleOutput();
			ScriptableObject.putProperty(globalScope, "out", Context.javaToJS(new ConsoleOutput(), globalScope));
			
			// String loadMe = "RegExp; getClass; java; Packages; JavaAdapter;";
			// cx.evaluateString(globalScope , loadMe, "lazyLoad", 0, null);
			
			// Startup Script Test
			String startupTestScript = "msg = \"Rhino Time!\"; msg;";
			Object startupTestScriptResult = cx.evaluateString(globalScope, startupTestScript, "<cmd>", 0, null);
			TaleCraft.logger.info("Startup Script Test: " + startupTestScriptResult);
		} finally {
			cx.exit();
		}
		
		taleCraft.logger.info("Script Engine initialized!");
	}
	
	public void contextCreation(Context cx) {
		cx.setClassShutter(globalClassShutter);
	}
	
	public Scriptable createNewScope() {
		Context cx = Context.enter();
		Scriptable newScope = cx.newObject(globalScope);
		newScope.setPrototype(globalScope);
		newScope.setParentScope(null);
		Context.exit();
		return newScope;
	}
	
	public Scriptable createNewBlockScope(World world, BlockPos blockpos) {
		Context cx = Context.enter();
		Scriptable newScope = cx.newObject(globalScope);
		newScope.setPrototype(globalScope);
		newScope.setParentScope(null);
		
		ScriptableObject.putProperty(newScope, "position", Context.javaToJS(new MutableBlockPos(blockpos), newScope));
		ScriptableObject.putProperty(newScope, "world", Context.javaToJS(new WorldObjectWrapper(world), newScope));
		
		Context.exit();
		
		return newScope;
	}
	
	public Scriptable createNewWorldScope(World world) {
		Context cx = Context.enter();
		Scriptable newScope = cx.newObject(globalScope);
		newScope.setPrototype(globalScope);
		newScope.setParentScope(null);
		
		ScriptableObject.putProperty(newScope, "world", Context.javaToJS(new WorldObjectWrapper(world), newScope));
		
		Context.exit();
		
		return newScope;
	}
	
	public Object interpret(String script, String fileName, Scriptable scope) {
		Object rvalue = null;
		Context cx = Context.enter();
		
		if(scope == null) {
			Scriptable newScope = cx.newObject(globalScope);
			newScope.setPrototype(globalScope);
			newScope.setParentScope(null);
			scope = newScope;
		}
		
		try {
			rvalue = cx.evaluateString(scope, script, fileName, 0, null);
		} catch(Throwable e) {
			e.printStackTrace();
			ChatComponentText text = new ChatComponentText("Script Error: " + e.getMessage());
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(text);
		} finally {
			cx.exit();
		}
		
		return rvalue;
	}
	
	/**
	 * @return The script, or a short block-comment saying that an error ocurred.
	 **/
	public String loadScript(World world, String fileName) {
		if(fileName.isEmpty()) {
			return "";
		}
		
		File worldDirectory = world.getSaveHandler().getWorldDirectory();
		
		File dataDir = worldDirectory;
		File scriptDir = new File(dataDir, "scripts");
		
		if(!scriptDir.exists()) {
			scriptDir.mkdir();
		}
		
		File scriptFile = new File(scriptDir, fileName+".js");
		
		if(!scriptFile.exists()) {
			String message = "Script does not exist: " + scriptFile;
			TaleCraft.logger.error(message);
            return "/*Failed to load script: "+fileName+". Reason: "+message+"*/";
		}
		
		TaleCraft.logger.info("Loading script: " + scriptFile);
		
		try {
			String script = FileUtils.readFileToString(scriptFile);
			// TaleCraft.logger.info("Script successfully loaded: " + scriptFile + " (~"+script.length()+" chars)");
			return script;
		} catch (IOException e) {
			TaleCraft.logger.error("Failed to load Script: " + scriptFile);
			e.printStackTrace();
			return "/*Failed to load script: "+fileName+". Reason: "+e.getMessage()+"*/";
		}
	}
	
	public void saveScript(World world, String fileContent, String fileName) throws IOException {
		File worldDirectory = world.getSaveHandler().getWorldDirectory();
		
		File dataDir = worldDirectory;
		File scriptDir = new File(dataDir, "scripts");
		
		if(!scriptDir.exists()) {
			scriptDir.mkdir();
		}
		
		File scriptFile = new File(scriptDir, fileName+".js");
		FileUtils.writeStringToFile(scriptFile, fileContent);
	}
	
	public List<String> getOwnPropertyNames(IObjectWrapper wrapper) {
		Class<?> clazz = wrapper.getClass();
		
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		
		String[] props = new String[fields.length+methods.length];
		int ix = 0;
		
		for(Field field : fields) {
			props[ix++] = field.getName();
		}
		
		for(Method method : methods) {
			props[ix++] = method.getName();
		}
		
		return Lists.newArrayList(props);
	}
	
	public NativeObject getGlobalScope() {
		return globalScope;
	}
	
}
