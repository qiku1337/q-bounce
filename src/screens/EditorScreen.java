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
import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Gdx.gl;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import java.util.Iterator;
import com.badlogic.gdx.InputMultiplexer;
import main.Game;

import actors.TileActor;
import com.badlogic.gdx.InputProcessor;
import controllers.CameraController;
import controllers.EditorController;
import editor.PropSerialized;
import editor.TileProp;
/**
 *
 * @author Qiku
 */
public class EditorScreen implements GameScreen {
	/**
	 * Serializable class as the props holder.
	 */
	static public class PropsHolder implements Iterable<PropSerialized> {
		/**
		 * All props on the scene.
		 */
		public final Array<PropSerialized> props = new Array<>();

		/**
		 * Allow itterate;
		 * @return 
		 */
		@Override
		public Iterator<PropSerialized> iterator() {
			return props.iterator();
		}
	}
	
	/**
	 * Currently editing level file.
	 */
	public final String filename;
	
	/**
	 * Kontroluje kamere, tj. zoom.
	 */
	public final CameraController camera = new CameraController();
	
	/**
	 * Props array wrapped.
	 */
	public Array<PropSerialized> props;
	
	/**
	 * Props holder.
	 */
	private PropsHolder propsHolder = new PropsHolder();
	
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
		this.filename = Game.LEVELS_PATH + filename;
	}

	/**
	 * @see GameScreen#show() 
	 */
	@Override
	public void show() {
                reConfigure();
		// add the editor controller
		Game.scene.controllers.add(camera);
		Game.scene.controllers.add(new EditorController(this));
		
		// register input processors
		//Game.inputMultiplexer.addProcessor(camera);
		
		if(Gdx.files.internal(filename).exists()) {
			this.load();
		} else {
			// add general prop
			props = propsHolder.props;
			props.add(new TileProp());
			props.add(new TileProp());
			props.peek().position.set(10.f, 100.f);
		}
		
		// add editor actors
		//Game.scene.BACKGROUND.add(new GridBackgroundActor(-1));
	}

	/**
	 * @see GameScreen#render(float) 
	 * @param delta 
	 */
	@Override
	public void render(float delta) {
        // clear target buffer
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// perform game systems
		Game.performSystems();
	}
	
	/**
	 * Load editable scene.
	 */
	public void load() {
		propsHolder = EditorScreen.load(filename);
		props = propsHolder.props;
	}
	
	/**
	 * Save the edited scene.
	 */
	public void save() {
		Json json = new Json(JsonWriter.OutputType.javascript);
		json.setUsePrototypes(false);
		
		// save the scene file
		Gdx.files.local(filename).writeString(json.prettyPrint(propsHolder), false);
	}
	
	/**
	 * Load PropsHolder class.
	 * @param filename
	 * @return 
	 */
	static public PropsHolder load(String filename) {
		Json json = new Json(JsonWriter.OutputType.javascript);

		// console logging
		Game.console.logs.add("Parsing level file... '" + filename + "'");
		
		// load the scene file
		return json.fromJson(PropsHolder.class, Gdx.files.internal(filename));
	}
}
