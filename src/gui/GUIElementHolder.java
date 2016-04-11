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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;
/**
 *
 * @author Qiku
 */
public class GUIElementHolder implements Iterable<GUIElement> {
	/**
	 * Unordered stack of the GUI elements.
	 */
	public final Array<GUIElement> elements = new Array<>();
	
	/**
	 * Returns element that bounds overlaps the point.
	 * @param point 2D screen point representation.
	 * @return GUI element from the gui stack. NULL on nothing todo.
	 */
	public GUIElement getOverPoint(Vector2 point) {
		for(GUIElement element : this) {
			if(element.getBounds().contains(point)) {
				return element;
			}
		}
		
		return null;
	}
	
	/**
	 * Draws the GUI elements.
	 * @param shape
	 * @param batch 
	 */
	public void draw(ShapeRenderer shape, SpriteBatch batch) {
		// draw the shapes
		for(GUIElement element : this) {
			element.draw(shape, batch);
		}
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public Iterator<GUIElement> iterator() {
		return elements.iterator();
	}
}
