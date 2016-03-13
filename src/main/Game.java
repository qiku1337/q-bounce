package main;
import screens.LoaderScreen;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import screens.GameScreen;
import screens.MenuScreen;
import screens.PreviewScreen;
import system.SceneRendererSystem;
import system.SceneWorldSystem;
import system.PhysicsWorldSystem;

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
     * Game main assets manager.
     * Manage game resources such as textures, sounds, musics etc. globally.
     */
    static public AssetManager assets;
    static public Camera mainCamera;
    
     /**
     * Physics world system.
     * Simulates physics world.
     */
    static public PhysicsWorldSystem physics;
    /**
     * Scene world system.
     */
    static public SceneWorldSystem world;
    
    /**
     * Scene renderer system.
     */
    static public SceneRendererSystem renderer;
    
    /**
     * Initialize loader for the new game screen.
     * Automate the loader initialization for next game screen.
     * @param next Screen to go.
     */
    static public void setGameScreen(GameScreen next) {
        ((Game)Gdx.app.getApplicationListener()).setScreen(new LoaderScreen(next));
    }
    static public void performSystemsJob(float delta) {
        // update frame
        Game.physics.update(delta);
        Game.world.update(delta);
        
        // render frame
        Game.renderer.prerender();
        Game.renderer.render();
        Game.renderer.debug();
        Game.physics.debug();
    }
    
    /**
     * Performed after application succeed creation.
     * Initialize game global resources, loaders and scenes.
     */
    @Override
    public void create() {
        // create asset manager
        assets = new AssetManager();
        
        // create scene systems
        world = new SceneWorldSystem();
        renderer = new SceneRendererSystem(world.root);
        physics = new PhysicsWorldSystem();
        
        // fetch for main camera
        mainCamera = renderer.camera;
        
        // prepare startup screen
        //this.setScreen();
        this.setScreen(new LoaderScreen(new PreviewScreen()));
    }
    
    /**
     * Perform game screen rendering.
     */
    @Override
    public void render() {
        super.render();
    }
    
    /**
     * Dispose resources.
     * Release all game resources and assets.
     */
    @Override
    public void dispose() {
        super.dispose();
        
        assets.dispose();
    }
}