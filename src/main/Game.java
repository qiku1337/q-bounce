package main;
import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.Input;
import system.Scene;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import screens.EditorScreen;
import screens.GameScreen;
import screens.LoaderScreen;
import screens.StageScreen;
import system.Console;
import system.ConsoleAction;

import system.Physics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
	 * Game instance.
	 */
	static public final Game app = new Game();
	
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
        public static String LEVELS_PATH="assets/levels/";
	
	/**
	 * Perform systems.
	 */
	static public void performSystems() {
		Game.physics.perform();
		Game.scene.perform();
		Game.console.perform();
		
		// post performing for rendering process
		Game.physics.postPerform();
		Game.scene.postPerform();
		Game.console.postPerform();
	}
	
	/**
	 * Reconfigure the game settups from the config.
	 */
	static public void reConfigure() {
		Gdx.graphics.setDisplayMode(
			Game.config.width,
			Game.config.height,
			false
		);
		
		// game screen reconfigure
		if(Game.app.screen instanceof GameScreen) {
			((GameScreen)Game.app.screen).reConfigure();
		}
	}
	
    /**
     * Performed after application succeed creation.
     * Initialize game global resources, loaders and scenes.
     */
    @Override
    public void create() {
		// load configuration file
		Game.config = Config.load(CONFIG_FILENAME, true);
		Game.reConfigure();
		
		// initialize game resources
		Game.assets = new AssetManager();
		Game.console = new Console();
		Game.physics = new Physics();
		Game.scene = new Scene();
		
		// assign input processor with the console
		Gdx.input.setInputProcessor(Game.console);
		
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
                Game.console.commands.put("edit", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				if(params.length == 2) {
					Game.app.setNextScreen(new EditorScreen(params[1]));
					return "Open scene editor for '" + params[1] + "'";
				}
				return "level name";
			}
		});
                
		
		//Game.console.commands.put("cfg", new ConsoleAction() {
			//@Override			
		//});
		
		// wrap the main camera
		Game.mainCamera = Game.scene.camera;
		
		// startup screen
		this.setNextScreen(new StageScreen());
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
		
		// render-up the game screen
		super.render();
    }
    
    /**
     * Dispose resources.
     * Release all game resources and assets.
     */
    @Override
    public void dispose() {
		// dispose game resources
		Game.scene.dispose();
        Game.assets.dispose();
    }
	
	/**
	 * Prepare new game screen.
	 * @param next 
	 */
	public void setNextScreen(GameScreen next) {
		this.setScreen(new LoaderScreen(next));
	}
}