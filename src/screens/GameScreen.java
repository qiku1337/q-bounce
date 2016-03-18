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
package screens;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import main.Game;

/**
 * Base game screen.
 * Provides `prepare` method to be performed by the loader screen.
 * @author Konrad Nowakowski https://github.com/konrad92
 */
public interface GameScreen extends Screen {
    /**
     * Prepare the game screen on the load process.
     * Used to fillup the game assets loader.
     */
    public void prepare();
	
	/**
	 * Dispatched on game reconfiguration process.
	 * For example via console durning the game.
	 */
	public default void reConfigure() {
		// dummy method
	}
    
	/**
	 * @see ApplicationListener#resize(int, int)
	 */
    @Override
    public default void resize(int width, int height) {
        // dummy method
    }

	/**
	 * @see ApplicationListener#pause()
	 */
    @Override
	public default void pause() {
        // dummy method
    }

	/** 
	 * @see ApplicationListener#resume()
	 */
    @Override
	public default void resume() {
        // dummy method
    }
	
	/**
	 * Clear the scene on screen hiding.
	 * Also release the assets set.
	 * @see Screen#hide() 
	 */
	@Override
	public default void hide() {
		Game.scene.clear();
		Game.assets.clear();
	}
	
	/**
	 * @see Screen#dispose() 
	 */
	@Override
	public default void dispose() {
		// dummy method
	}
}