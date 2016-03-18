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
package main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 *
 * @author Qiku
 */
public class Config {
	public int width = 800;
	public int height = 600;
	
	/**
	 * Load configuration file.
	 * @param filename File to read.
	 * @param recreate Recreate the configuration file on reading fail.
	 * @return Newly loaded configuration.
	 */
	public static Config load(String filename, boolean recreate) {
		Json json = new Json(JsonWriter.OutputType.javascript);
		FileHandle file = Gdx.files.local(filename);
		
		json.setUsePrototypes(false);
		
		// load json configuration file
		Config cfg = file.exists() ? json.fromJson(Config.class, file) : null;
		
		if(cfg == null) {
			cfg = new Config();
			
			if(recreate) {
				System.err.println("Config file recreating...");
				Config.save(cfg, filename);
			}
		}
		
		return cfg;
	}
	
	/**
	 * Save the configuration to the file.
	 * @param cfg
	 * @param filename 
	 */
	public static void save(Config cfg, String filename) {
		Json json = new Json(JsonWriter.OutputType.javascript);
		FileHandle file = Gdx.files.local(filename);
		
		json.setUsePrototypes(false);
		
		// save json configuration file
		file.writeString(json.prettyPrint(cfg), false);
	}
}
