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
package vault.q_bounce.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import vault.q_bounce.Game;
import vault.q_bounce.Vault;
import vault.q_bounce.editor.PropHolder;
import vault.q_bounce.editor.PropSerialized;
import vault.q_bounce.editor.gui.ButtonAction;
import vault.q_bounce.editor.gui.GUIButtonElement;
import vault.q_bounce.editor.gui.GUIElement;
import vault.q_bounce.editor.gui.GUIFieldElement;
import vault.q_bounce.editor.gui.GUILabelElement;
import vault.q_bounce.editor.props.TileProp;
import vault.q_bounce.system.ConsoleAction;
import vault.q_bounce.system.SceneController;

/**
 *
 * @author Qiku
 */
public class EditorController extends InputAdapter implements SceneController {
	/**
	 * Currently editing level file.
	 */
	public final String filename;
	
	/**
	 * Editor's GUI controller.
	 */
	public final GUIController gui = new GUIController();
	
	/**
	 * Font using for drawing named scene elements.
	 */
	private final BitmapFont font = new BitmapFont();
	
	/**
	 * Gizmo renderer.
	 */
	private final ShapeRenderer gizmo = Game.scene.gizmo;
	
	/**
	 * Property selection.
	 */
	private PropSerialized nearly, selected, drag;
	
	/**
	 * Editor property holder.
	 */
	private PropHolder propHolder;
	
	/**
	 * Dynamic editor GUI elements.
	 */
	private final Array<GUIElement> dynamicElements = new Array<>();
	
	/**
	 * Ctor.
	 * @param filename Propsfile to edit.
	 */
	public EditorController(String filename) {
		// editing filename
		this.filename = filename;
		
		// load the propsfile
		this.loadPropHolder();
		
		// add editor buttons
		Array<Class<? extends PropSerialized>> propClasses = Vault.PROP_CLASSES;
		
		// create button action
		ButtonAction createAction = new ButtonAction() {
			@Override
			public void actionPerformed(GUIButtonElement button, Object userData) {
				CreatePropAction pair = (CreatePropAction)userData;
				try {
					PropSerialized serialized = pair.propClass.newInstance();
					serialized.position.set(
						Game.mainCamera.position.x,
						Game.mainCamera.position.y
					);
					
					pair.ctrl.propHolder.props.add(serialized);
				} catch (InstantiationException | IllegalAccessException ex) {
					Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		};
		
		ButtonAction destroyAction = new ButtonAction() {
			@Override
			public void actionPerformed(GUIButtonElement button, Object userData) {
				DestroyPropAction action = (DestroyPropAction)userData;
				if(action != null) {
					action.ctrl.propHolder.props.removeValue(
						action.ctrl.selected, true
					);
				}
			}
		};
		
		// destroy prop button
		Vector2 position = new Vector2(5.f, 5.f);
		GUIButtonElement button = new GUIButtonElement("Delete", position, font);
		button.screenSpace = GUIElement.ScreenSpace.BottomRight;
		button.anchor = GUIElement.Anchor.BottomRight;
		button.actionListener = destroyAction;
		button.userData = new DestroyPropAction(this);

		// add the button to the GUI
		gui.guiElements.elements.add(button);
		position.y += 20.f;
		
		// build editor buttons
		for(Class<? extends PropSerialized> propClass : propClasses) {
			button = new GUIButtonElement(propClass.getSimpleName(), position, font);
			button.screenSpace = GUIElement.ScreenSpace.BottomRight;
			button.anchor = GUIElement.Anchor.BottomRight;
			button.actionListener = createAction;
			button.userData = new CreatePropAction(this, propClass);
			
			// add the button to the GUI
			gui.guiElements.elements.add(button);
			position.y += 20.f;
		}
		
		// add last information
		GUILabelElement label = new GUILabelElement("---Create prop---", position, font);
		label.screenSpace = GUIElement.ScreenSpace.BottomRight;
		label.anchor = GUIElement.Anchor.BottomRight;
		gui.guiElements.elements.add(label);
		
		// add the editor console commands.
		Game.console.commands.put("edit", new ConsoleAction() {
			@Override
			public String perform(String[] params) {
				EditorController editor = null;
				
				// lookup for editor controller
				for(SceneController ctrl : Game.scene.controllers) {
					if(ctrl instanceof EditorController) {
						editor = (EditorController)ctrl;
						break;
					}
				}
				
				// edit save|load
				if(editor != null && params.length == 2) {
					switch (params[1]) {
					case "save":
						editor.savePropHolder();
						return "Props scene saved";
					case "reload":
						editor.loadPropHolder();
						return "Props scene reloaded";
					case "exit":
						return "Unsupported yet...";
					}
				}
				
				return "edit save|reload|exit";
			}
		});
	}
	
	/**
	 * Loads propfile from editor filename.
	 */
	public void loadPropHolder() {
		Game.console.logs.add("Loading propsfile... '" + filename + "'");
		
		// load props holder instance
		propHolder = PropHolder.load(filename);
		
		if(propHolder == null) {
			Game.console.logs.add("Propsfile not exists");
			propHolder = new PropHolder();
		} else {
			Game.console.logs.add("Propsfile loaded successfuly");
		}
	}
	
	/**
	 * Saves editor prop holder to file.
	 */
	public void savePropHolder() {
		Game.console.logs.add("Saving propsfile... '" + filename + "'");
		if(PropHolder.save(propHolder, filename)) {
			Game.console.logs.add("Propsfile saved successfuly!");
		} else {
			Game.console.logs.add("Cannot save propsfile!");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prePerform() {
		Game.DEBUG_INFO = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postPerform() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preUpdate(float delta) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postUpdate(float delta) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preDraw(SpriteBatch batch) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postDraw(SpriteBatch batch) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preDebug(ShapeRenderer gizmo) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postDebug(ShapeRenderer gizmo) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		// flush the editor commands...
		Game.console.commands.remove("edit");
		
		// dispose editor resources
		font.dispose();
	}
	
	/**
	 * Draw the editor elements.
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		// draw editor simple shapes
		gizmo.begin(ShapeRenderer.ShapeType.Line);
		
		// draw-up the editor props
		for(PropSerialized prop : propHolder.props) {
			// draw-up the prop bounds
			prop.draw(gizmo);
			
			// draw the prop indicator
			if(nearly == prop || selected == prop) {
				gizmo.setColor(Color.RED);
			} else {
				gizmo.setColor(Color.PINK);
			}
			gizmo.circle(prop.position.x, prop.position.y, 16.f);
		}
		gizmo.end();
		
		// draw-up the props names
		batch.begin();
		font.setColor(Color.WHITE);
		font.setScale(Game.mainCamera.zoom);
		for(PropSerialized prop : propHolder.props) {
			// fetch for prop name
			String name = prop.getClass().getSimpleName() + ": " + prop.id;
			
			// layer name
			switch(prop.layer) {
				case 1: name += " (ACTION_1)"; break;
				case 2: name += " (ACTION_2)"; break;
				case 3: name += " (ACTION_3)"; break;
				case 4: name += " (FOREGROUND)"; break;
				case 5: name += " (GUI)"; break;
				case 6: name += " (DEBUG)"; break;
				default: name += " (BACKGROUND)";
			}
			
			// text bounds
			BitmapFont.TextBounds bounds = font.getBounds(name);
			
			// draw-up the prop name
			font.draw(batch, name,
				prop.position.x - bounds.width/2,
				prop.position.y + bounds.height + 24.f
			);
		}
		font.setScale(1);
		batch.end();
	}
	
	/**
	 * Focus given element.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 * @return 
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == 0) {
			selected = nearly;
			drag = selected;
			
			// remove old dynamic elements
			gui.guiElements.elements.removeAll(dynamicElements, true);
			dynamicElements.clear();
			
			// recreate fields
			if(selected != null) {
				Vector2 position = new Vector2(5.f, 5.f);
				for(Field field : selected.getClass().getFields()) {
					switch(field.getType().getTypeName()) {
						case "float":
						case "int":
						case "boolean":
						case "java.lang.String":
							dynamicElements.add(new GUIFieldElement(field, selected, position, font));
							position.y += 20.f;
					}
				}
				
				// add dynamic elements to GUI stack
				gui.guiElements.elements.addAll(dynamicElements);
			}
		}
		
		return drag != null;
	}
	
	/**
	 * Makes props draggable.
	 * @param screenX
	 * @param screenY
	 * @param button
	 * @return 
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int button) {
		if(drag != null) {
			Vector3 worldCursor = Game.mainCamera.unproject(
				new Vector3(screenX, screenY, 0)
			);
			
			drag.position.set(new Vector2(
				worldCursor.x,
				worldCursor.y
			));
		}
		
		return drag != null;
	}
	/**
	 * Drop active prop.
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 * @return 
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		drag = null;
		return false;
	}
	
	/**
	 * Hover curosr over the elements.
	 * @param screenX
	 * @param screenY
	 * @return 
	 */
	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		Vector3 worldCursor = Game.mainCamera.unproject(
			new Vector3(screenX, screenY, 0)
		);
		
		// dragging
		if(drag == null) {
			// lookup for near prop
			nearly = propHolder.getPropAt(new Vector2(
				worldCursor.x,
				worldCursor.y
			), 16.f);
		}
		
		return nearly != null;
	}
	
	/**
	 * Create prop pair for the button action.
	 */
	private static class CreatePropAction {
		public final EditorController ctrl;
		public final Class<? extends PropSerialized> propClass;
		
		public CreatePropAction(EditorController ctrl, Class<? extends PropSerialized> propClass) {
			this.ctrl = ctrl;
			this.propClass = propClass;
		}
	}
	
	/**
	 * Destroy prop pair for the button action.
	 */
	private static class DestroyPropAction {
		public final EditorController ctrl;
		
		public DestroyPropAction(EditorController ctrl) {
			this.ctrl = ctrl;
		}
	}
}
