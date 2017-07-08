package minecraft2d;

public enum BlockType {
	STONE("res/stone.png"), AIR("res/air.png"), GRASS("res/grass.png"), DIRT("res/dirt.png"), 
	WOOD("res/wood2.png"), BRICK("res/brick.png"), WATER("res/water.png"), LAVA("res/lava.png"),
	LEAF("res/leaf.png"), SQUIDWOOD("res/slime.png");
	public final String location;
	// location as in file location
	BlockType(String location) {
		this.location = location;
	}
}
