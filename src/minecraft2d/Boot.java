package minecraft2d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

public class Boot {

	// Initialization for everything
	BlockGrid grid;
	private BlockType selection = BlockType.STONE;
	private int selector_x = 0, selector_y = 0;
	private boolean mouseEnabled = true;
	
	// Constructor
	public Boot() {

		try {
			Display.setDisplayMode(new DisplayMode(640, 480)); // Width, height
			Display.setTitle("Minecraft 2D"); // Self-explanatory
			Display.create(); // Also self-explanatory, throws LWJGLException
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		grid = new BlockGrid();

		// Setup for OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 480, 0, 1, -1); // Amount of units between each side. In this case, each pixel is one unit.
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D); // Enables the use of 2D textures on the Display
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		while (!Display.isCloseRequested()) { // Called 60 times per second
			// Render Code here
			glClear(GL_COLOR_BUFFER_BIT); // Clears the Display from the previous frame's clutter
			input(); // Takes all input defined below.
			grid.draw();
			drawSelectionBox(); 

			Display.update();
			Display.sync(60); // FPS = 60
		}
		Display.destroy();
		System.exit(0); // Mac safety code
	}

	private void drawSelectionBox() { // The semi-transparent box that shows what you're about to place and where
		int x = selector_x * World.BLOCK_SIZE;
		int y = selector_y * World.BLOCK_SIZE;
		int x2 = x + World.BLOCK_SIZE;
		int y2 = y + World.BLOCK_SIZE;
		if (grid.getAt(selector_x, selector_y).getType() != BlockType.AIR || selection == BlockType.AIR) {
			glBindTexture(GL_TEXTURE_2D, 0);
			glColor4f(1f, 1f, 1f, 0.5f);
			glBegin(GL_QUADS); // Personally, I like half-indenting OpenGL code for readability.
			    glVertex2i(x, y);
			    glVertex2i(x2, y);
			    glVertex2i(x2, y2);
			    glVertex2i(x, y2);
			glEnd();
			glColor4f(1, 1, 1, 1);
		} else {
			glColor4f(1, 1, 1, 0.5f);
			new Block(selection, selector_x * World.BLOCK_SIZE, selector_y * World.BLOCK_SIZE).draw();;
			glColor4f(1, 1, 1, 1);
		}
	}

	private void input() {
		if (mouseEnabled || Mouse.isButtonDown(0)) { // If the mouse is disabled by the keyboard and we want to turn it back on
			mouseEnabled = true;
			int mousex = Mouse.getX();
			int mousey = 480 - Mouse.getY() - 1; // Mouse.getY() doesn't return the actual y-position of the mouse
			boolean mouseClicked = Mouse.isButtonDown(0); // 0 = left mouse button, 1 = right mouse button, 2 = middle button
			selector_x = Math.round(mousex / World.BLOCK_SIZE); // No funky decimal values
			selector_y = Math.round(mousey / World.BLOCK_SIZE);
			if (mouseClicked) {
				grid.setAt(selector_x, selector_y, selection); // puts the block down, drawing it on-screen
			}
		}
		
		// This section is for the Keyboard to be used as your selector instead of the mouse.
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) { // Limits it to one movement per button press
				if (!(selector_x + 1 > World.BLOCKS_WIDTH - 2)) {
					mouseEnabled = false; // Mouse interferes with Keyboard control
					selector_x++;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				if (!(selector_x - 1 < 0)) {
					mouseEnabled = false; // This program's not big enough for the two of us...
					selector_x--;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState()) {
				if (!(selector_y - 1 < 0)) {
					mouseEnabled = false;
					selector_y--;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState()) {
				if (!(selector_y + 1 > World.BLOCKS_HEIGHT - 2)) {
					mouseEnabled = false;
					selector_y++;
				}
			}
			
			if (Keyboard.getEventKey() == Keyboard.KEY_S) { // saves the file when S is pressed.
				grid.save(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_L) { // loads the file when L is pressed.
				grid.load(new File("save.xml"));
			}
			// When a number is pressed, the block that you're about to place changes, corresponding to your selection.
			if (Keyboard.getEventKey() == Keyboard.KEY_1) {
				selection = BlockType.STONE;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_2) {
				selection = BlockType.DIRT;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_3) {
				selection = BlockType.GRASS;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_4) {
				selection = BlockType.AIR;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_5) {
				selection = BlockType.WOOD;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_6) {
				selection = BlockType.BRICK;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_7) {
				selection = BlockType.WATER;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_8) {
				selection = BlockType.LAVA;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_9) {
				selection = BlockType.LEAF;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_0) {
				selection = BlockType.SQUIDWOOD;
			}
			
			if (Keyboard.getEventKey() == Keyboard.KEY_C) {
				grid.clear(); // sets all blocks to AIR
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
				grid.setAt(selector_x, selector_y, selection); // Does the same thing as a mouse click, except it's for keyboard control.
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) { // Kills the display when Esc is pressed.
				Display.destroy();
				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		new Boot(); // Finally! A way to run things without getting an unused warning!
	}
}
