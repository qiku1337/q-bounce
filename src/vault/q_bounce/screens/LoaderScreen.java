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
package vault.q_bounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static javafx.scene.text.Font.font;
import com.badlogic.gdx.graphics.Color;
import vault.q_bounce.Game;

/**
 *
 * @author Qiku
 */

public class LoaderScreen implements Screen {
    
    private Sound sound;

    private SpriteBatch spriteBatch;
    
    private Animation animation;    

    private Texture machineTexture;
    
    private TextureRegion[] ballguy;
    
    private TextureRegion currentFrame;
    
    private BitmapFont font;
    
    private static final int        FRAME_COLS = 4;
    
    private static final int        FRAME_ROWS = 2; 
       
    /**
     * Tick frame.
     */
    private float frame = 0.f;
    
    public final GameScreen next;
    
    public boolean clearAssets;

    public LoaderScreen(GameScreen next) {
        this(next, true);
    }
    public LoaderScreen(GameScreen next, boolean clearAssets) {
        this.next = next;
        this.clearAssets = clearAssets;
    }
    
    @Override
    public void show() {        
        machineTexture = new Texture(Gdx.files.internal("assets/load.png"));        
        TextureRegion[][] tmp = TextureRegion.split(machineTexture, machineTexture.getWidth()/FRAME_COLS, machineTexture.getHeight()/FRAME_ROWS);              // #10
        ballguy = new TextureRegion[FRAME_COLS * FRAME_ROWS];        
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                ballguy[index++] = tmp[i][j];
            }
        }
        animation = new Animation(0.04f, ballguy);      
        spriteBatch = new SpriteBatch(); 
        
        sound = Gdx.audio.newSound(Gdx.files.internal("assets/sound/menuloop.wav"));
        long idsound = sound.play(1.0f);        
        sound.setLooping(idsound, true);
        
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        
        if(this.clearAssets) {
            Game.assets.clear();
        }
        
        // prepare next screen to load
        if(this.next != null) {
            this.next.prepare();
        }
    }   
    /**
     * Frame update.
     * Update asset manager and render some loader screen stuff.
     * @param delta 
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        // #14
        //frame += Gdx.graphics.getDeltaTime();          
        currentFrame = animation.getKeyFrame(frame, true);  
        spriteBatch.begin();        
        spriteBatch.draw(currentFrame, 150, 150,                        //miejsce rysowania
                Gdx.graphics.getWidth()/4.f,Gdx.graphics.getHeight()/2.f);  //wielkosc  
        
        font.setScale(2);       
        font.draw(spriteBatch, "Please wait...", 400, 150);        
        spriteBatch.end();

        frame += 1.f*delta;
        //((Game)Gdx.app.getApplicationListener()).setScreen(this.next);
        if(Game.assets.update()) {
			Game.app.setScreen(next);
        }
        
		// perform game systems
		Game.performSystems();
    }       
    
   /**
     * Handle window resizing.
     * Performed when window client resized.
     * @param width
     * @param height 
     */
    @Override
    public void resize(int width, int height) {
    }

    /**
     * Handle pause event.
     * Performed on window focus lost.
     */
    @Override
    public void pause() {
    }

    /**
     * Handle resume event.
     * Performed on window focus gain.
     */
    @Override
    public void resume() {
    }

    /**
     * Performed when screen were changed.
     * Dispose all resources of the loader.
     */
    @Override
    public void hide() {
        this.dispose();
    }

    /**
     * Dispsoe resources.
     * Release all unused resources to bypass the memoryleaks.
     */
    @Override
    public void dispose() {        
        machineTexture.dispose();
        spriteBatch.dispose();
        font.dispose();
        sound.dispose();
        System.out.println("Loading screen disposed");
    }
}