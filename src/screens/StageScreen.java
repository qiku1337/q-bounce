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
import actors.BackgroundActor;
import actors.BounceActor;
import actors.DebugScreenActor;
import actors.GroundActor;
import actors.SpikesActor;
import actors.TileActor;
import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import main.Game;
import system.Physics;
/**
 *
 * @author Qiku
 */
public class StageScreen implements GameScreen {
	Body body;
	Fixture fixture;
	
	@Override
	public void prepare() {
		Game.assets.load("assets/dragonball.png", Texture.class);
                Game.assets.load("assets/layer-1.png", Texture.class);
                Game.assets.load("assets/layer-2.png", Texture.class);
                Game.assets.load("assets/layer-3.png", Texture.class);
                Game.assets.load("assets/layer-5.png", Texture.class);
                Game.assets.load("assets/spike_A.png", Texture.class);
                Game.assets.load("assets/Tile.png", Texture.class); 
                Game.assets.load("assets/sound/jump_01.wav", Sound.class);
                
                //Game.assets.load("assets/sound/level1.mp3", Sound.class);
	}

	@Override
	public void show() {
		// prepare scene camera
		Game.mainCamera.setToOrtho(false);
		Game.mainCamera.translate(-400.f, -250.f);
                Game.mainCamera.zoom =0.5f;
		Game.mainCamera.update();
		
		// create turret actor
		Game.scene.DEBUG.add(new DebugScreenActor());   
                Game.scene.BACKGROUND.add(new BackgroundActor(0));
                Game.scene.ACTION_2.add(new GroundActor(0));
                Game.scene.ACTION_2.add(new SpikesActor(0));
                Game.scene.ACTION_2.add(new TileActor(0));
                Game.scene.ACTION_3.add(new BounceActor(0));
                
            //Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/sound/level1.mp3"));
           // long idsound = sound.play(1.0f);        
           //sound.setLooping(idsound, true);
	}

	@Override
	public void render(float delta) {
        // clear target buffer
       
        gl.glClearColor(0.f, 0.f, 0.f, 0.f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
                        if(!BounceActor.respawn){
                            Game.mainCamera.translate(
                                    BounceActor.xvel,
                                    BounceActor.yvel
                            );
                            Game.mainCamera.update();
                        }else{
                            Game.mainCamera.translate(
                                    -BounceActor.x,
                                    -BounceActor.y
                            );
                            Game.mainCamera.update();
                            BounceActor.respawn=false;
                        }
		
		
		// perform game systems
		Game.performSystems();
	}

	@Override
	public void dispose() {
	}
}
