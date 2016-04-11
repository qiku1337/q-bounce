/*
 * The MIT License
 *
 * Copyright 2016 Qiku.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package gui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 *
 * @author Qiku
 */
public class GUILabelElement implements GUIElement {
	/**
	 * Position of the element on the screen.
	 */
	public final Vector2 position = new Vector2();
	
	/**
	 * Label text.
	 */
	public String text;
	
	/**
	 * Font used for the label.
	 */
	public BitmapFont font;
	
	/**
	 * Position anchor of the element.
	 */
	public Anchor anchor = Anchor.BottomLeft;
	
	/**
	 * Position over the screen.
	 */
	public ScreenSpace screenSpace = ScreenSpace.BottomLeft;
	
	/**
	 * Flags that determines element state.
	 */
	protected boolean focused, overed;
	
	/**
	 * Ctor.
	 * @param text
	 * @param position
	 * @param font
	 */
	public GUILabelElement(String text, Vector2 position, BitmapFont font) {
		this.text = text;
		this.position.set(position);
		this.font = font;
	}
	
	/**
	 * Nothing todo, it's a text element.
	 */
	@Override
	public void enter() {
		overed = true;
	}

	/**
	 * Nothing todo, it's a text element.
	 */
	@Override
	public void leave() {
		overed = false;
	}

	/**
	 * Nothing todo, it's a text element.
	 */
	@Override
	public void focus() {
		focused = true;
	}

	/**
	 * Nothing todo, it's a text element.
	 */
	@Override
	public void blur() {
		focused = false;
	}

	/**
	 * Nothing todo, it's a text element.
	 */
	@Override
	public void action() {
	}

	/**
	 * Nothing todo, it's a text element.
	 */
	@Override
	public void typed(char input) {
	}

	/**
	 * Draw the label.
	 * @param shape
	 * @param batch 
	 */
	@Override
	public void draw(ShapeRenderer shape, SpriteBatch batch) {
		// calculate label text bounds
		Rectangle bounds = getBounds();
		
		// enable alpha channel usage
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// draw the shape
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setColor(0, 0, 0, .5f);
		shape.rect(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4);
		shape.end();
		
		// draw the label
		batch.begin(); {
			font.setColor(!focused ? (overed ? Color.PINK : Color.WHITE) : Color.RED);
			font.draw(batch, text, bounds.x, bounds.y + bounds.height);
		}
		batch.end();
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public Rectangle getBounds() {
		BitmapFont.TextBounds bounds = font.getBounds(text);
		final Vector2 screen = new Vector2();
		
		// height fix
		bounds.height += 3;
		
		// calculate element bounds
		return GUIElement.calculateBounds(anchor, screenSpace, position, bounds.width, bounds.height);
	}
}
