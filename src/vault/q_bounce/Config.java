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
package vault.q_bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import java.lang.reflect.Field;
import vault.q_bounce.system.ConsoleAction;

/**
 *
 * @author Qiku
 */
public class Config {
	/**
	 * Determine game window width.
	 */
	public int width = 800;
	
	/**
	 * Determine game window height.
	 */
	public int height = 600;
	
	/**
	 * Enable fullscreen video mode.
	 */
	public boolean fullscreen = false;
	
	/**
	 * Enable shaders using.
	 */
	public boolean shaders = true;
	
	/**
	 * Play sounds.
	 */
	public boolean sounds = true;
	
	/**
	 * Register configuration commands.
	 */
	static public void registerConfigCommands() {
		Game.console.commands.put("cfg", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				// cfg save|update
				if(params.length == 2) {
					if(params[1].equals("save")) {
						Config.save(Game.config, Game.CONFIG_FILENAME);
						return "Saving configuration... " + Game.CONFIG_FILENAME;
					} else if(params[1].equals("update")) {
						Game.reConfigure();
						return "Updating configuration...";
					}
				}
				
				// cfg get(name)
				if(params.length == 3 && params[1].equals("get")) {
					try {
						Field cfgField = Config.class.getField(params[2]);
						Class<?> type = cfgField.getType();
						
						try {
							return "Config field " + params[2] + " = " + cfgField.get(Game.config);
						} catch (IllegalArgumentException | IllegalAccessException ex) {
							return "Unaccessible config field '" + params[2] + "'";
						}
					} catch (NoSuchFieldException | SecurityException ex) {
						return "Unknown config field '" + params[2] + "'";
					}
				}
				
				// cfg set(name value)
				if(params.length == 4 && params[1].equals("set")) {
					try {
						Field cfgField = Config.class.getField(params[2]);
						Class<?> type = cfgField.getType();
						
						try {
							// set the config value
							if(type.getName().equals("int")) {
								cfgField.setInt(Game.config, Integer.parseInt(params[3]));
							} else if(type.getName().equals("boolean")) {
								cfgField.setBoolean(Game.config, Boolean.parseBoolean(params[3]));
							} else {
								cfgField.set(Game.config, type.cast(params[3]));
							}
							
							return "Config field " + params[2] + " = " + params[3];
						} catch (IllegalArgumentException | IllegalAccessException ex) {
							return "Unaccessible config field '" + params[2] + "'";
						}
					} catch (NoSuchFieldException | SecurityException ex) {
						return "Unknown config field '" + params[2] + "'";
					}
				}
				
				// show help
				return "cfg save|update|set(name value)";
			}
		});
	}
	
	/**
	 * Load configuration file.
	 * @param filename File to read.
	 * @param recreate Recreate the configuration file on reading fail.
	 * @return Newly loaded configuration.
	 */
	static public Config load(String filename, boolean recreate) {
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
	static public void save(Config cfg, String filename) {
		Json json = new Json(JsonWriter.OutputType.javascript);
		FileHandle file = Gdx.files.local(filename);
		
		json.setUsePrototypes(false);
		
		// save json configuration file
		file.writeString(json.prettyPrint(cfg), false);
	}
}
