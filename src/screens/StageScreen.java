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
import actors.BounceActor;
import actors.DebugScreenActor;
import actors.GroundActor;
import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import main.Game;
/**
 *
 * @author Qiku
 */
public class StageScreen implements GameScreen {
	Body body;
	Fixture fixture;
	
	@Override
	public void prepare() {
		//Game.assets.load("assets/dragonball.png", Texture.class);
                //BounceActor.preload();
	}

	@Override
	public void show() {
		// prepare scene camera
		Game.mainCamera.setToOrtho(false);
		Game.mainCamera.translate(-400.f, -300.f);
		Game.mainCamera.update();
		
		// create turret actor
		Game.scene.DEBUG.add(new DebugScreenActor());
		Game.scene.ACTION_2.add(new BounceActor(0));
                Game.scene.ACTION_2.add(new GroundActor(0));
	}

	@Override
	public void render(float delta) {
        // clear target buffer
        gl.glClearColor(0.f, 0.f, 0.f, 0.f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// perform game systems
		Game.performSystems();
	}

	@Override
	public void dispose() {
	}
}
