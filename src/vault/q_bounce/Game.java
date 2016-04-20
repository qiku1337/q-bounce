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

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import vault.q_bounce.system.Scene;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import vault.q_bounce.actors.BounceActor;
import vault.q_bounce.screens.EditorScreen;
import vault.q_bounce.screens.GameScreen;
import vault.q_bounce.screens.LoaderScreen;
import vault.q_bounce.screens.StageScreen;
import vault.q_bounce.system.Console;
import vault.q_bounce.system.ConsoleAction;
import vault.q_bounce.system.Debug;
import vault.q_bounce.system.Physics;

/**
 *
 * @author Qiku
 */
public class Game extends com.badlogic.gdx.Game {
	/**
	 * Use debug information depolying.
	 */
	static public boolean DEBUG_INFO = true;
	static public boolean DEBUG_ADDITIONAL = false;
	
	/**
	 * Game configuration filename.
	 */
	static public final String CONFIG_FILENAME = "config.cfg";
	
	/**
	 * Path to the game levels catalogue.
	 */
	static public final String LEVELS_PATH = "assets/levels/";
	
	/**
	 * Game instance.
	 */
	static public final Game app = new Game();
	
	/**
	 * Multiplexer for input processors.
	 */
	static public final InputMultiplexer inputMultiplexer =  new InputMultiplexer();
	
	/**
	 * Game configuration object.
	 */
	static public Config config;
	
    /**
     * Game main assets manager.
     * Manage game resources such as textures, sounds, musics etc. globally.
     */
    static public AssetManager assets;
	
	/**
	 * Ingame console.
	 */
	static public Console console;
	
	/**
	 * Debugging on-screen system.
	 */
	static public Debug debug;
	
	/**
	 * Scene system.
	 */
	static public Scene scene;
	
	/**
	 * Physics world system.
	 */
	static public Physics physics;
	
	/**
	 * Main camera wrapper.
	 */
	static public OrthographicCamera mainCamera;
	
	/**
	 * Perform systems.
	 */
	static public void performSystems() {
		Game.physics.perform();
		Game.scene.perform();
		Game.debug.perform();
		Game.console.perform();
		
		// post performing for rendering process
		Game.physics.postPerform();
		Game.scene.postPerform();
		Game.debug.postPerform();
		Game.console.postPerform();
	}
	
	/**
	 * Reconfigure the game settups from the config.
	 */
	static public void reConfigure() {
		Gdx.graphics.setDisplayMode(
			Game.config.width,
			Game.config.height,
			Game.config.fullscreen
		);
		
		// game screen reconfigure
		if(Game.app.screen instanceof GameScreen) {
			((GameScreen)Game.app.screen).reConfigure();
		}
	}
	
	// Nastepna scena
	private GameScreen nextScreen;
	
    /**
     * Performed after application succeed creation.
     * Initialize game global resources, loaders and scenes.
     */
    @Override
    public void create() {
		// load configuration file
		Game.config = Config.load(CONFIG_FILENAME, true);
		Game.reConfigure();
		
		// assign the input multiplexer to the mani input processor
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		// initialize game resources
		Game.assets = new AssetManager();
		Game.console = new Console();
		Game.debug = new Debug();
		Game.physics = new Physics();
		Game.scene = new Scene();
		
		// add generic console commands
		Game.console.commands.put("exit", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				Gdx.app.exit();
				return "Bye";
			}
		});
		
		Game.console.commands.put("restart", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				if(screen instanceof GameScreen) {
					Game.app.setNextScreen((GameScreen)screen);
					return "Scene restarting...";
				}
				
				return "Nothing to restart";
			}
		});
		
		Game.console.commands.put("editor", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				if(params.length == 2) {
					Game.app.setNextScreen(new EditorScreen(params[1]));
					return "Open scene editor for '" + params[1] + "'";
				}
				return "editor filename";
			}
		});
		
		Game.console.commands.put("play", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				if(params.length == 2) {
					Game.app.setNextScreen(new StageScreen(params[1]));
					return "Open scene for '" + params[1] + "'";
				}
				return "stage filename";
			}
		});
                Game.console.commands.put("spawn", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				Game.scene.ACTION_1.add(new BounceActor(0));
				return "respawn";
			}
		});
		
		// register configuration commands
		Config.registerConfigCommands();
		
		// vault instances
		Vault.preload();
		
		// startup screen
		//this.setNextScreen(new StageScreen("a"));
		this.setNextScreen(new EditorScreen("a"));
    }
    
    /**
     * Perform game screen rendering.
     */
    @Override
    public void render() {
		// allow debug info toggling
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
			Game.DEBUG_INFO = !Game.DEBUG_INFO;
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
			Game.DEBUG_ADDITIONAL = !Game.DEBUG_ADDITIONAL;
		}
		
		// allow console access
		if(Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) {
			Game.console.toggle();
		}
		
		// change debug lines width
		gl.glLineWidth(1.5f);
		
		// open-up the 'next' screen loader
		if(nextScreen != null) {
			this.setScreen(new LoaderScreen(nextScreen));
			nextScreen = null;
		}
		
		// render-up the game screen
		super.render();
    }
    
    /**
     * Dispose resources.
     * Release all game resources and assets.
     */
    @Override
    public void dispose() {
		// vault unloading
		Vault.unload();
		
		// clear up input multiplexer
		inputMultiplexer.clear();
		
		// dispose game resources
		Game.scene.dispose();
		Game.physics.dispose();
		Game.debug.dispose();
		Game.console.dispose();
        Game.assets.dispose();
    }
	
	/**
	 * Prepare new game screen.
	 * @param next 
	 */
	public void setNextScreen(GameScreen next) {
		// re-assign console input processor
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(Game.console);
		
		nextScreen = next;
	}
	
	/**
	 * Cast the current screen as given type.
	 * @param <T> Scene type to cast.
	 * @param sceneType Type of the screen.
	 * @return Screen instance as sceneType, or <b>NULL</b>.
	 */
	public <T> T getScreenAs(Class<T> sceneType) {
		return sceneType.cast(screen);
	}
}
