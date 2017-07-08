package minecraft2d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static minecraft2d.BlockType.*;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

public class AltBoot {
	
	BlockGrid grid;
	BlockType bt = DIRT;
	
    public AltBoot() {
        
    	try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setTitle("Minecraft 2D");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        
    	grid = new BlockGrid();
    	grid.setAt(10, 10, BlockType.STONE);
    	
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        
        while (!Display.isCloseRequested()) {
            // Render Code here
            glClear(GL_COLOR_BUFFER_BIT);
            input();
            grid.draw();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
        System.exit(0);
    }
    
    private void input() {
    	int mousex = Mouse.getX();
    	int mousey = 480 - Mouse.getY() - 1;
    	boolean mouseClicked = Mouse.isButtonDown(0);
    	
    	
    	if (mouseClicked) {
    		int grid_x = Math.round(mousex / World.BLOCK_SIZE);
    		int grid_y = Math.round(mousey / World.BLOCK_SIZE);
    		bt = getBT(bt);
    		grid.setAt(grid_x, grid_y, bt);
    	}
    	while (Keyboard.next()) {
    		if (Keyboard.getEventKey() == Keyboard.KEY_S) {
    			grid.save(new File("save.xml"));
    		}
    		if (Keyboard.getEventKey() == Keyboard.KEY_L) {
    			grid.load(new File("save.xml"));
    		}
    		if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
    			if (bt == DIRT) {
    				setBT(GRASS);
    			} else if (bt == GRASS) {
    				setBT(STONE);
    			} else if (bt == STONE) {
    				setBT(AIR);
    			} else if (bt == AIR) {
    				setBT(DIRT);
    			}
    		}
    	}
    }
    
    private void setBT(BlockType blocktype) {
    	bt = blocktype;
    }
    
    private BlockType getBT(BlockType bt) {
		return bt;
	}

	/**
     * @param args
     */
    public static void main(String[] args) {
		new AltBoot();
	}
}