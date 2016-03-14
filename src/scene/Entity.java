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
package scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
/**
 *
 * @author Qiku
 */
public interface Entity extends Disposable {
	/**
	 * Create method performed when the entity were created from the scene.
	 */
	public default void create() {
		// dummy method
	}
	
	/**
	 * Destroy method performed when the entity were disposed from the scene.
	 */
	public default boolean destroy() {
		// dummy method
		return true;
	}
	
	/**
	 * Entity update method.
	 * @param delta Delta time for smoother update performing.
	 */
	public default void update(float delta) {
		// dummy method
	}
	
	/**
	 * Entity drawing method.
	 * @param batch Sprite batch for instance draw performing.
	 */
	public default void draw(SpriteBatch batch) {
		// dummy method
	}
	
	/**
	 * Debugging information rendering.
	 * @param gizmo 
	 */
	public default void debug(ShapeRenderer gizmo) {
		// dummy method
		gizmo.begin(ShapeRenderer.ShapeType.Line);
		gizmo.rect(-2.f, -2.f, 4.f, 4.f);
		gizmo.end();
	}
	
	/**
	 * @see Disposable#dispose() 
	 */
	@Override
	public default void dispose() {
		// dummy method
	}
}