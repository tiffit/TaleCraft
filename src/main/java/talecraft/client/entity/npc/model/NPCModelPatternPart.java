package talecraft.client.entity.npc.model;

public class NPCModelPatternPart {

	public String name;
	public String type;
	public String parent;
	public boolean mirror;
	public float scale;
	public PartTexture texture;
	public PartTransformation translation;
	public PartTransformation size;
	public PartRotation rotation;

	public static class PartTransformation {
		public int x;
		public int y;
		public int z;
	}

	public static class PartRotation {
		public float x;
		public float y;
		public float z;
	}

	public static class PartTexture {
		public int x;
		public int y;
	}

}
