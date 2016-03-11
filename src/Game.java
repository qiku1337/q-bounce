import screens.LoaderScreen;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import screens.GameScreen;
import screens.MenuScreen;

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
    static public AssetManager assets;
    static public void setGameScreen(GameScreen next) {
        ((Game)Gdx.app.getApplicationListener()).setScreen(new LoaderScreen(next));
    }
    
    @Override
    public void create() {
        // create asset manager
        assets = new AssetManager();
        
        // prepare startup screen
        this.setScreen(new LoaderScreen(new MenuScreen()));
    }
    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
        super.dispose();
        
        assets.dispose();
    }
}
