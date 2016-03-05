import screens.LoaderScreen;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;

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
    
    @Override
    public void create() {
        
        assets = new AssetManager();        
        this.setScreen(new LoaderScreen());
    }
}
