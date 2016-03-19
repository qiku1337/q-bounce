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
package system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import main.Game;

/**
 *
 * @author Qiku
 */
public class Debug implements System {
	/**
	 * Debug information builder.
	 */
	public static final StringBuilder info = new StringBuilder();
	
	/**
	 * Font used to render the bitmap.
	 */
	private final BitmapFont font;
	
	/**
	 * Debug sprite batch.
	 */
	private final SpriteBatch batch;
	
	/**
	 * Ctor.
	 */
	public Debug() {
		this.font = new BitmapFont();
		this.batch = new SpriteBatch();
	}
	
	/**
	 * Nothing todo.
	 * @see System#perform() 
	 */
	@Override
	public void perform() {
	}

	/**
	 * Render the debug informations.
	 * @see System#postPerform() 
	 */
	@Override
	public void postPerform() {
		String fps = "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n";
	
	
		batch.begin();
		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0.f, 0.f,
			(float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()
		));
		font.drawMultiLine(batch, Game.DEBUG_INFO || Game.DEBUG_ADDITIONAL ?
			fps + info.toString() : fps, 5.f, (float)Gdx.graphics.getHeight() - 5.f);
		batch.end();
		
		// clear up debug information
		info.setLength(0);
	}

	/**
	 * Release all resources used by the debugging screen.
	 * @see System#dispose() 
	 */
	@Override
	public void dispose() {
		this.batch.dispose();
		this.font.dispose();
	}
}
