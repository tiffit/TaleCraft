package de.longor.talecraft.script;

import org.mozilla.javascript.ClassShutter;

import de.longor.talecraft.TaleCraft;

public class GlobalClassShutter implements ClassShutter {
	
	@Override
	public boolean visibleToScripts(String fullClassName) {
		// adapter<N> classes MUST be accesible.
		if(fullClassName.startsWith("adapter"))
			return true;
		
		// this is kinda obvious
		if(fullClassName.startsWith("de.longor.talecraft.script."))
			return true;
		
		// basic security measures
		if(fullClassName.startsWith("java.lang.reflect")) return false;
		if(fullClassName.startsWith("java.awt")) return false;
		if(fullClassName.startsWith("java.io")) return false;
		if(fullClassName.startsWith("java.nio")) return false;
		if(fullClassName.startsWith("java.rmi")) return false;
		if(fullClassName.startsWith("java.security")) return false;
		if(fullClassName.startsWith("javax.")) return false;
		
		// further blocking of java.lang.* classes.
		if(fullClassName.equals("java.lang.Thread")) return false;
		if(fullClassName.equals("java.lang.Class")) return false;
		if(fullClassName.equals("java.lang.ClassLoader")) return false;
		if(fullClassName.equals("java.lang.Process")) return false;
		if(fullClassName.equals("java.lang.ProcessBuilder")) return false;
		
		// there is a whole bunch of stuff missing
		
		TaleCraft.logger.info("Class passed shutter: " + fullClassName);
		return true;
	}
	
}
