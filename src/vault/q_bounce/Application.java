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
package vault.q_bounce;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import static vault.q_bounce.Game.CONFIG_FILENAME;

/**
 *
 * @author Qiku
 */
public class Application {
    /**
     * Window title.
     */
    static public final String WINDOW_TITLE = "Q-Bounce 1.5 (alpha)";
    
    /**
     * Window's canvas width in pixels.
     */
    static public final int WINDOW_WIDTH = 800;
    
    /**
     * Window's canvas height in pixels.
     */
    static public final int WINDOW_HEIGHT = 600;
    
    /**
     * Program entry point.
     * Performs general LWJGL application initialization.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
		// create application wrapper
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.vSyncEnabled = false;   // disable vertical sync with the monitor
        cfg.resizable = false;      // disable client space resizing
        cfg.forceExit = false;      // disable exit force
        cfg.title = WINDOW_TITLE;   // window title on titlebar
        cfg.width = WINDOW_WIDTH;   // canvas width in pixels
        cfg.height = WINDOW_HEIGHT; // canvas height in pixels
        cfg.x = -1;                 // place window at the center of the screen
        cfg.y = -1;
        
        // create and run application listeners
        LwjglApplication app = new LwjglApplication(Game.app, cfg);
    }
}
