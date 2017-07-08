package minecraft2d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

public class Boot {

	BlockGrid grid;
	private BlockType selection = BlockType.STONE;
	private int selector_x = 0, selector_y = 0;
	private boolean mouseEnabled = true;

	public Boot() {

		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("Minecraft 2D");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		grid = new BlockGrid();
		grid.setAt(10, 10, selection);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		while (!Display.isCloseRequested()) {
			// Render Code here
			glClear(GL_COLOR_BUFFER_BIT);
			input();
			grid.draw();
			drawSelectionBox();

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}

	private void drawSelectionBox() {
		int x = selector_x * World.BLOCK_SIZE;
		int y = selector_y * World.BLOCK_SIZE;
		int x2 = x + World.BLOCK_SIZE;
		int y2 = y + World.BLOCK_SIZE;
		if (grid.getAt(selector_x, selector_y).getType() != BlockType.AIR || selection == BlockType.AIR) {
			glBindTexture(GL_TEXTURE_2D, 0);
			glColor4f(1f, 1f, 1f, 0.5f);
			glBegin(GL_QUADS);
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
		if (mouseEnabled || Mouse.isButtonDown(0)) {
			mouseEnabled = true;
			int mousex = Mouse.getX();
			int mousey = 480 - Mouse.getY() - 1;
			boolean mouseClicked = Mouse.isButtonDown(0);
			selector_x = Math.round(mousex / World.BLOCK_SIZE);
			selector_y = Math.round(mousey / World.BLOCK_SIZE);
			if (mouseClicked) {
				grid.setAt(selector_x, selector_y, selection);
			}
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
				if (!(selector_x + 1 > World.BLOCKS_WIDTH - 2)) {
					mouseEnabled = false;
					selector_x++;
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				if (!(selector_x - 1 < 0)) {
					mouseEnabled = false;
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
			
			if (Keyboard.getEventKey() == Keyboard.KEY_S) {
				grid.save(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_L) {
				grid.load(new File("save.xml"));
			}
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
				grid.clear();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
				grid.setAt(selector_x, selector_y, selection);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				Display.destroy();
				System.exit(0);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Boot();
	}
}
