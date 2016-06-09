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

import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import vault.q_bounce.Game;
import vault.q_bounce.Vault;
import vault.q_bounce.actors.BounceActor;
import vault.q_bounce.actors.TileActor;
import vault.q_bounce.controllers.CameraController;
import vault.q_bounce.controllers.MenuController;
import vault.q_bounce.editor.PropHolder;
import vault.q_bounce.editor.PropSerialized;
import vault.q_bounce.scene.Actor;
import vault.q_bounce.system.Scene;

/**
 *
 * @author Qiku
 */
public class StageScreen implements GameScreen {
	/**
	 * Sciezka do pliku poziomu.
	 */
	private final String filename;
	
	/**
	 * Kontroluje kamere, tj. podazanie za aktorem.
	 */
	private final CameraController camera = new CameraController();
	
	/**
	 * Ctor.
	 * @param filename 
	 */
	public StageScreen(String filename) {
		this.filename = Game.LEVELS_PATH + filename;
	}
	
	/**
	 * Preload all screen resources here.
	 * @see GameScreen#prepare() 
	 */
	@Override
	public void prepare() {
		Game.assets.load("assets/Tile.png", Texture.class);
                Game.assets.load("assets/spike_A.png", Texture.class);
                Game.assets.load("assets/ballmove.png", Texture.class);

		
		
		
		// preload resources
		TileActor.preload();
                BounceActor.preload();
	}

	/**
	 * Prepare the scene to show-up.
	 */
	@Override
	public void show() {
		reConfigure();
		
		// add scene controllers
		Game.scene.controllers.add(camera);
		Game.scene.controllers.add(new MenuController());
		
		// register input processors
		Game.inputMultiplexer.addProcessor(camera);
		
		// wczytaj scene
		this.load(filename);
	}

	/**
	 * Update screen logic and perform the systems.
	 * @see Screen#render(float) 
	 */
	@Override
	public void render(float delta) {
        // clear target buffer
        gl.glClearColor(0.1f, 0.2f, 0.1f, 1.f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
						
		// perform game systems
		Game.performSystems();
	}
	
	/**
	 * Wczytaj scene z pliku.
	 * @param filename Sciezka do poziomu.
	 */
	public void load(String filename) {
		PropHolder props = PropHolder.load(filename);
		
		// instance props onto the scene
		for(PropSerialized prop : props) {
			Actor actor = (Actor)prop.instance();
			
			// place the instanced actor
			if(actor != null) {
				Scene.Layer layer = Game.scene.BACKGROUND;
				
				// select layer
				switch(prop.layer) {
					case 1: layer = Game.scene.ACTION_1; break;
					case 2: layer = Game.scene.ACTION_2; break;
					case 3: layer = Game.scene.ACTION_3; break;
					case 4: layer = Game.scene.FOREGROUND; break;
					case 5: layer = Game.scene.GUI;break;
					case 6: layer = Game.scene.DEBUG; break;
				}
				
				// place the actor
				layer.add(actor);
			}
		}
	}
}
