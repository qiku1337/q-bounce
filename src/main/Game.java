package main;
import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.Input;
import system.Scene;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import screens.GameScreen;
import screens.LoaderScreen;
import screens.StageScreen;
//import screens.StageScreen;
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
	 * Game instance.
	 */
	static public final Game app = new Game();
	
    /**
     * Game main assets manager.
     * Manage game resources such as textures, sounds, musics etc. globally.
     */
    static public AssetManager assets;
	
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
		
		// post performing
		Game.physics.postPerform();
		Game.scene.postPerform();
	}
	
    /**
     * Performed after application succeed creation.
     * Initialize game global resources, loaders and scenes.
     */
    @Override
    public void create() {
		// initialize game resources
		Game.assets = new AssetManager();
		Game.physics = new Physics();
		Game.scene = new Scene();
		
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