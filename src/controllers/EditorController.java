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
package controllers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import main.Game;
import editor.PropSerialized;
import screens.EditorScreen;
import system.ConsoleAction;
import system.Debug;
import system.SceneController;
/**
 *
 * @author Qiku
 */
public class EditorController implements SceneController {
	/**
	 * Editor screen assigned with the controller.
	 */
	private final EditorScreen editor;
	
	/**
	 * Font using for drawing named scene elements.
	 */
	private final BitmapFont font = new BitmapFont();
	
	/**
	 * Property selection.
	 */
	private PropSerialized nearly, selected;
	
	/**
	 * State flag.
	 */
	private boolean singleClicked = false,
					propDragged = false;
	
	/**
	 * Ctor.
	 * @param editor 
	 */
	public EditorController(EditorScreen editor) {
		this.editor = editor;
		
		/**
		 * Allow edited scnee processing.
		 */
		Game.console.commands.put("edit", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				// edit save|load
				if(params.length == 2) {
					if(params[1].equals("save")) {
						editor.save();
						return "Saving scene... '" + editor.filename + "'";
					} else if(params[1].equals("reload")) {
						editor.load();
						return "Reloading scene... '" + editor.filename + "'";
					}
				}
				
				return "edit save|reload";
			}
		});
	}
	
	@Override
	public void prePerform() {
	}

	@Override
	public void postPerform() {
		// rounded coords
		Vector2 rounded = new Vector2(
			(float)Math.floor(Game.mainCamera.position.x),
			(float)Math.floor(Game.mainCamera.position.y)
		);
		
		// append log information
		Debug.info.append("World coord: " + rounded + " \n");
	}

	@Override
	public void preUpdate(float delta) {
		// get world cursor (in 3D coords)
		Vector3 origin = Game.mainCamera.getPickRay(
			Gdx.input.getX(),
			Gdx.input.getY()
		).origin;
		
		// transform to 2D origin
		Vector2 origin2D = new Vector2(origin.x, origin.y);
		
		// allow editor object selecting
		nearly = selected;
		
		for(PropSerialized prop : editor.props) {
			if(origin2D.dst(prop.position) < 16.f && selected != prop) {
				nearly = prop;
			}
		}
		
		// move the prop (position)
		if(selected != null && propDragged) {
			selected.position.add(
				(float)Gdx.input.getDeltaX() * Game.mainCamera.zoom * 2.f,
				(float)Gdx.input.getDeltaY() * -Game.mainCamera.zoom * 2.f
			);
		}
		
		// select the prop
		if(!singleClicked && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			// draw the prop
			if(selected != null && selected.position.dst(origin2D) < 16.f) {
				propDragged = true;
			} else {
				selected = nearly;
				propDragged = true;
			}
			
			singleClicked = true;
		} else if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			singleClicked = false;
			propDragged = false;
		}
	}

	@Override
	public void postUpdate(float delta) {
	}

	@Override
	public void preDraw(SpriteBatch batch) {
	}

	@Override
	public void postDraw(SpriteBatch batch) {
		// get the shape renderer
		ShapeRenderer gizmo = Game.scene.gizmo;
		
		// draw-up the editor props
		gizmo.begin(ShapeRenderer.ShapeType.Line);
		for(PropSerialized prop : editor.props) {
			prop.draw(gizmo);
			if(prop == selected) {
				gizmo.setColor(Color.GREEN);
			} else if(prop == nearly) {
				gizmo.setColor(Color.PINK);
			} else {
				gizmo.setColor(Color.RED);
			}
			gizmo.circle(prop.position.x, prop.position.y, 16.f);
		}
		gizmo.end();
		
		// draw-up the named props
		batch.begin();
		font.setScale(Game.mainCamera.zoom);
		for(PropSerialized prop : editor.props) {
			font.draw(batch, prop.getClass().toString(),
				prop.position.x,
				prop.position.y
			);
		}
		batch.end();
	}

	public void preDebug(ShapeRenderer gizmo) {
	}

	public void postDebug(ShapeRenderer gizmo) {
	}

	public void dispose() {
		Game.console.commands.remove("edit");
	}
}
