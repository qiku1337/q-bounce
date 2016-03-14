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
package actors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import main.Game;
import scene.Actor;
/**
 *
 * @author Qiku
 */
public class DebugScreenActor extends Actor {
	/**
	 * Debug information builder.
	 */
	public static final StringBuilder info = new StringBuilder();
	
	/**
	 * Font used to render the bitmap.
	 */
	private final BitmapFont font;
	
	/**
	 * Ctor.
	 */
	public DebugScreenActor() {
		super(0, 0);
		
		this.font = new BitmapFont();
	}
	
	/**
	 * Draw layer debug information such as actors each layer.
	 * @see Actor#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch) 
	 * @param batch 
	 */
	@Override
	public void draw(SpriteBatch batch) {
		
		String fps = "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n";
                
		
		batch.begin();
		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0.f, 0.f, 800.f, 600.f));
		font.drawMultiLine(batch, fps + info.toString(), 5.f, 595.f);
		batch.end();
		
		// clear up debug information
		info.setLength(0);
	}
}
