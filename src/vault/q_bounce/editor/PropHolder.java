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
package vault.q_bounce.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import java.util.Iterator;

/**
 *
 * @author Qiku
 */
public class PropHolder implements Iterable<PropSerialized> {
	/**
	 * All props on the scene.
	 */
	public final Array<PropSerialized> props = new Array<>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<PropSerialized> iterator() {
		return props.iterator();
	}
	
	/**
	 * Returns editor prop near the radius.
	 * @param worldPos
	 * @param radius
	 * @return 
	 */
	public PropSerialized getPropAt(Vector2 worldPos, float radius) {
		for(int i = props.size - 1; i >= 0; i--) {
			PropSerialized prop = props.get(i);
			if(prop.position.dst(worldPos) < radius) {
				return prop;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the prop holder instance from the file.
	 * @param filename Editor props filename to read.
	 * @return Newly instanced PropHolder. Or NULL on loading fails.
	 */
	public static PropHolder load(String filename) {
		FileHandle fileHandle = Gdx.files.internal(filename);
		
		// load the prop holder if able
		if(fileHandle.exists() && !fileHandle.isDirectory()) {
			return getJson().fromJson(PropHolder.class, Gdx.files.internal(filename));
		} else {
			return null;
		}
	}
	
	/**
	 * Save the editor props to the file.
	 * @param propHolder Prop holder to save.
	 * @param filename Editor props filename to write.
	 * @return TRUE on successfuly save, FALSE otherwise.
	 */
	public static boolean save(PropHolder propHolder, String filename) {
		FileHandle fileHandle = Gdx.files.local(filename);
		
		// save the prop holder if able
		if(!fileHandle.isDirectory()) {
			fileHandle.writeString(getJson().prettyPrint(propHolder), false);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the json parser instance.
	 * @return Newly instanced json coder/decoder.
	 */
	private static Json getJson() {
		Json json = new Json(JsonWriter.OutputType.javascript);
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		return json;
	}
}
