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
import com.badlogic.gdx.graphics.Texture;
import main.Game;
import actors.TileActor;
import editor.EditorActor;
//import editor.GUIActor;
import controllers.CameraController;
import controllers.EditorController;
/**
 *
 * @author Qiku
 */
public class EditorScreen implements GameScreen {
	/**
	 * Kontroluje kamere, tj. zoom.
	 */
	public final CameraController camera;
	
	/**
	 * Kontroler edytora.
	 * Zarzadza ladowaniem, zapisywaniem, modyfikowaniem propow.
	 */
	public final EditorController editor;
	
	/**
	 * @see GameScreen#prepare() 
	 */
	@Override
	public void prepare() {
		// load editor assets
		//Game.assets.load("assets/blueprint.png", Texture.class);
		
		// preload actor resources
		TileActor.preload();
	}

	/**
	 * Ctor.
	 * @param filename Stage filename to edit, from assets/levels/ catalogue.
	 */
	public EditorScreen(String filename) {
		// setup the editor controllers
		camera = new CameraController();
		editor = new EditorController(Game.LEVELS_PATH + filename);
	}

	/**
	 * @see GameScreen#show() 
	 */
	@Override
	public void show() {
		// add the editor controller
		Game.scene.controllers.add(camera);
		Game.scene.controllers.add(editor.gui);
		Game.scene.controllers.add(editor);
		
		// register input processors
		Game.inputMultiplexer.addProcessor(camera);
		Game.inputMultiplexer.addProcessor(editor.gui);
		Game.inputMultiplexer.addProcessor(editor);
		
		// add editor actors
		Game.scene.ACTION_1.add(new EditorActor(-1, editor));
		//Game.scene.FOREGROUND.add(new GUIActor(-1, editor.gui));
	}

	/**
	 * @see GameScreen#render(float) 
	 * @param delta 
	 */
	@Override
	public void render(float delta) {
        // clear target buffer
        gl.glClearColor(0.1f, 0.2f, 0.1f, 1.f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// perform game systems
		Game.performSystems();
	}
}
