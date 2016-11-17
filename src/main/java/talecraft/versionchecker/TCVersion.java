package talecraft.versionchecker;

public class TCVersion {
	String name;
	
	public TCVersion(String name){
		this.name = name;
	}
	
	public String getVersion(){
		return name.replace("TaleCraft-v", "");
	}
	
	private String[] getVersionNumbers(){
		return getVersion().split("\\.");
	}
	
	public int getMajor(){
		return Integer.parseInt(getVersionNumbers()[0]);
	}
	
	public int getMiddle(){
		return Integer.parseInt(getVersionNumbers()[1]);
	}
	
	public int getMinor(){
		return Integer.parseInt(getVersionNumbers()[2]);
	}
	
	public boolean isGreaterVersion(String currentVer){
		String[] current = currentVer.split("\\.");
		int major = Integer.parseInt(current[0]);
		int middle = Integer.parseInt(current[1]);
		int minor = Integer.parseInt(current[2]);
		if(getMajor() > major) return true;
		if(getMiddle() > middle) return true;
		if(getMinor() > minor) return true;
		return false;
	}
}
