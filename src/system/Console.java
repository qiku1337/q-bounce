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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Qiku
 */
public class Console implements System, InputProcessor {
	/**
	 * Console visibility.
	 */
	public boolean visible = false;
	
	/**
	 * Logs array.
	 */
	public final Array<String> logs = new Array<>();
	
	/**
	 * Commands list.
	 */
	public final HashMap<String, ConsoleAction> commands = new HashMap<>();
	
	/**
	 * Input string.
	 */
	public String input = "";
	
	/**
	 * Height of the console.
	 */
	private float height = 50.f;
	
	/**
	 * Color of the console background.
	 */
	private Color color = new Color(0.f, 0.f, 0.f, 1.f);
	
	/**
	 * Process the console input.
	 */
	private boolean process = false;
	
	/**
	 * Sprite batch.
	 */
	private final SpriteBatch batch;
	
	/**
	 * Shape renderer.
	 */
	private final ShapeRenderer renderer;
	
	/**
	 * Font renderer.
	 */
	private final BitmapFont font;

	/**
	 * Ctor.
	 */
	public Console() {
		// create the renderers
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		font = new BitmapFont();
		
		// welcome log
		logs.add(">");
	}
	
	/**
	 * Perform console input and actions proceed.
	 * @see System#perform() 
	 */
	@Override
	public void perform() {
		if(!visible) {
			return;
		}
		
		// process console input
		if(input.length() > 0 && process) {
			processCommand();
			process = false;
			input = "";
		}
	}

	/**
	 * Render the console.
	 * @see System#postPerform() 
	 */
	@Override
	public void postPerform() {
		if(!visible) {
			return;
		}
		
		float scwidth = (float)Gdx.graphics.getWidth(),
			scheight = (float)Gdx.graphics.getHeight();
		
		// assign projection to the renderers
		Matrix4 ortho = new Matrix4().setToOrtho2D(0.f, 0.f, scwidth, scheight);
		
		renderer.setProjectionMatrix(ortho);
		batch.setProjectionMatrix(ortho);
		
		// draw the console background
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(color);
		renderer.rect(0.f, scheight-height, scwidth, height);
		renderer.end();
		
		// crop the logs list
		while(logs.size > (int)(height / 19.f) - 1) {
			logs.removeIndex(0);
		}
		
		// draw the console logs
		batch.begin();
		for(int i = 0; i < logs.size; i++) {
			font.draw(batch, logs.get(logs.size - i - 1), 5.f, scheight - height + 25.f + 19.f * (i+1));
		}
		font.draw(batch, input, 5.f, scheight - height + 25.f);
		batch.end();
	}

	/**
	 * Release all resources used by the console.
	 * @see System#dispose() 
	 */
	@Override
	public void dispose() {
		batch.dispose();
		renderer.dispose();
		font.dispose();
	}
	
	/**
	 * Toggle console visibility.
	 */
	public void toggle() {
		visible = !visible;
	}

	/**
	 * Process the console command.
	 */
	private void processCommand() {
		// split string by white space
		String[] params = input.split(" ");
		
		if(params.length == 0) {
			return;
		}
		
		// process the console
		if(commands.containsKey(params[0])) {
			logs.add(commands.get(params[0]).perform(params));
		} else {
			logs.add("Unknown command '" + params[0] + "'");
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return !visible;
	}

	@Override
	public boolean keyUp(int keycode) {
		return !visible;
	}

	@Override
	public boolean keyTyped(char character) {
		if(visible && (int)character > 0) {
			if((int)character == 8) {
				if(input.length() > 0) {
					input = input.substring(0, input.length() - 1);
				}
			} else if((int)character == 13) {
				process = true;
			} else if((int)character != 96) {
				input += character;
			}
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return !visible;
	}

	@Override
	public boolean scrolled(int amount) {
		return true;
	}
}
