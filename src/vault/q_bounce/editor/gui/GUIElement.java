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
package vault.q_bounce.editor.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Qiku
 */
public interface GUIElement {
	/**
	 * Position anchor of the element.
	 */
	public static enum Anchor {
		Center,
		TopLeft,
		TopRight,
		BottomRight,
		BottomLeft,
	}
	
	/**
	 * Position over the screen space.
	 */
	public static enum ScreenSpace {
		Center,
		TopLeft,
		TopRight,
		BottomRight,
		BottomLeft,
	}
	
	/**
	 * Dispatched on cursor enters over this element.
	 */
	public void enter();
	
	/**
	 * Dispatched on cursor leaves this element.
	 */
	public void leave();
	
	/**
	 * Dispatched on element gain focus.
	 */
	public void focus();
	
	/**
	 * Dispatched on element lost focus.
	 */
	public void blur();
	
	/**
	 * Dispatched on action performed.
	 * Basic befaviour means single mouse click over this element.
	 */
	public void action();
	
	/**
	 * Dispatched on focused keyboard typing.
	 * @param input
	 */
	public void typed(char input);
	
	/**
	 * Draw the GUI element onto the screen.
	 * @param shape
	 * @param batch
	 */
	public void draw(ShapeRenderer shape, SpriteBatch batch);
	
	/**
	 * Returns GUI element screen bounds.
	 * @return Bounds rectangle.
	 */
	public Rectangle getBounds();
	
	/**
	 * Calculates the element bounds.
	 * @param anchor
	 * @param space
	 * @param position
	 * @param width
	 * @param height
	 * @return 
	 */
	public static Rectangle calculateBounds(Anchor anchor, ScreenSpace space, Vector2 position, float width, float height) {
		final Vector2 screen = position.cpy();
		
		// calculate screen spacing position
		switch(space) {
			case TopRight:
				screen.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				screen.x -= position.x;
				screen.y -= position.y;
				break;
				
			case TopLeft:
				screen.set(0, Gdx.graphics.getHeight());
				screen.x += position.x;
				screen.y -= position.y;
				break;
				
			case BottomRight:
				screen.set(Gdx.graphics.getWidth(), 0);
				screen.x -= position.x;
				screen.y += position.y;
				break;
				
			case Center:
				screen.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
				screen.x += position.x;
				screen.y += position.y;
				break;
		}
		
		// calculate screen bounds over the anchor
		switch(anchor) {
			case TopRight:
				return new Rectangle(screen.x - width, screen.y - height, width, height);
				
			case TopLeft:
				return new Rectangle(screen.x, screen.y - height, width, height);
				
			case BottomRight:
				return new Rectangle(screen.x - width, screen.y, width, height);
				
			case Center:
				return new Rectangle(screen.x - width/2, screen.y - height/2, width, height);
				
			default:
				return new Rectangle(screen.x, screen.y, width, height);
		}
	}
}
