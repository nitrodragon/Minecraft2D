package minecraft2d;

import java.io.File; // For saving
import java.io.FileOutputStream; // For saving
import java.io.IOException; // For saving and loading

import org.jdom.Document; // For both saving and loading
import org.jdom.Element; // For both saving and loading, mostly saving
import org.jdom.JDOMException; // Exception Handling
import org.jdom.input.SAXBuilder; // For loading
import org.jdom.output.XMLOutputter; // For saving

import static minecraft2d.World.*; // Get the constants from World without typing "World." for everything
import static minecraft2d.BlockType.*; // Get the constants for BlockType for easier access

public class BlockGrid {
	// An array of the blocks that will be rendered onscreen
	private final Block[][] blocks = new Block[BLOCKS_WIDTH][BLOCKS_HEIGHT];

	public BlockGrid() {
		for (int x = 0; x < BLOCKS_WIDTH; x++) {
			for (int y = 0; y < BLOCKS_HEIGHT; y++) { // Iteration for all blocks
				blocks[x][y] = new Block(AIR, x * BLOCK_SIZE, y * BLOCK_SIZE); // Same as clear(), sets all to AIR.
			}
		}
	}
	public void load(File load) {
		SAXBuilder render = new SAXBuilder();
		// Parses the save data and builds each block from its contents.
		try {
			Document doc = render.build(load); // "load" means the Filepath
			Element root = doc.getRootElement();
			for (Object block : root.getChildren()) {
				Element e = (Element) block;
				int x = Integer.parseInt(e.getAttributeValue("x")); // Gets the value of the x and y attributes...
				int y = Integer.parseInt(e.getAttributeValue("y")); // ...and saves it to x and y variables.
				// Notice that the type is loaded directly and not stored to a variable because it doesn't need to change
				blocks[x][y] = new Block(BlockType.valueOf(e.getAttributeValue("type")), x * BLOCK_SIZE, y * BLOCK_SIZE); // Actual size
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void save(File sav) {
		// Outputs the x, y, and BlockType of each block to an external XML file.
		Document document = new Document();
		Element root = new Element("blocks");
		document.setRootElement(root);
		for (int x = 0; x < BLOCKS_WIDTH; x++) {
			for (int y = 0; y < BLOCKS_HEIGHT; y++) { // Makes each block an Element with three Attributes; x, y, and type.
				Element block = new Element("block");
				block.setAttribute("x", String.valueOf((int) (blocks[x][y].getX() / BLOCK_SIZE))); // Poisition on grid
				block.setAttribute("y", String.valueOf((int) (blocks[x][y].getY() / BLOCK_SIZE))); // NOT actual size
				block.setAttribute("type", String.valueOf(blocks[x][y].getType())); // Type of each block
				root.addContent(block); // Adds it all to the Root Element
			}
		}
		XMLOutputter output = new XMLOutputter();
		try {
			output.output(document, new FileOutputStream(sav)); // "sav" is the filepath
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public void setAt(int x, int y, BlockType b) {
        blocks[x][y] = new Block(b, x * BLOCK_SIZE, y * BLOCK_SIZE); // Sets position and BlockType
    }

    public Block getAt(int x, int y) {
        return blocks[x][y];
    }

    public void draw() {
        for (int x = 0; x < BLOCKS_WIDTH - 1; x++) {
            for (int y = 0; y < BLOCKS_HEIGHT - 1; y++) { // For each block...
                blocks[x][y].draw(); // call draw() on blocks[x][y]
            }
        }
    }
	public void clear() {
		for (int x = 0; x < BLOCKS_WIDTH - 1; x++) {
        		for (int y = 0; y < BLOCKS_HEIGHT - 1; y++) { // For every single block...
        	        	blocks[x][y] = new Block(BlockType.AIR, x * BLOCK_SIZE, y * BLOCK_SIZE); // Set the block at the index to AIR.
		    	}
		}		
	}
}
