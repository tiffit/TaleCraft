package talecraft.client.entity.npc.model;

public class NPCModelPattern {

	public String name;
	public String file;
	public String animation;
	public TextureDimension texture;
	public NPCModelPatternPart[] parts;

	public static class TextureDimension {
		public int width;
		public int height;
	}

}
