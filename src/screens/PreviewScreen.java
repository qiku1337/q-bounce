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
import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.graphics.GL20;
import main.Game;
import actors.BounceActor;
import actors.GroundActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
/**
 *
 * @author Qiku
 */
public class PreviewScreen extends GameScreen{
    float frame = 0;
    
    @Override
    public void prepare() {
        //Game.assets.load("assets/steamangel.png", Texture.class);
    }
    
    @Override
    public void show() {
        Game.world.add(new GroundActor(1,100,250)); 
        Game.world.add(new GroundActor(1,250,350)); 
        Game.world.add(new GroundActor(1,140,350)); 
        Game.world.add(new BounceActor(3));        
    }

    @Override
    public void render(float delta) {
        // clear target buffer
        gl.glClearColor(0.f, 0.f, 0.f, 1.f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Game.performSystemsJob(delta);
        if(BounceActor.kul==true){        
            
            Game.world.add(new BounceActor(3));  
        }
    }
    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void hide() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void dispose() {
    }
}