package minecraft2d;

import static minecraft2d.World.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Block {
	private BlockType type = BlockType.AIR;
	private Texture texture;
	private float x, y;
	
	public Block(BlockType type, float x, float y) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;
		try {
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(type.location)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void draw() {
		texture.bind();
		glLoadIdentity();
		glTranslatef(x, y, 0);
		// Draws each block with its set texture
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0); // Tex = texture
			glVertex2f(0, 0);
			glTexCoord2f(1, 0);
			glVertex2f(BLOCK_SIZE, 0);
			glTexCoord2f(1, 1);
			glVertex2f(BLOCK_SIZE, BLOCK_SIZE);
			glTexCoord2f(0,1);
			glVertex2f(0, BLOCK_SIZE);
		glEnd();
		glLoadIdentity(); // Loading it twice, just to be safe.
	}
	public BlockType getType() {
		return type;
	}
	public void setType(BlockType type) {
		this.type = type;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void bind() {
		texture.bind();
	}
}
