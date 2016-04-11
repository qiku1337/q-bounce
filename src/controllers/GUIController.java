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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import main.Game;
import gui.GUIElement;
import gui.GUIElementHolder;
import gui.GUILabelElement;
import system.SceneController;

/**
 *
 * @author Qiku
 */
public class GUIController extends InputAdapter implements SceneController {
	/**
	 * Font using for drawing text in gui elements.
	 */
	private final BitmapFont font = new BitmapFont();
	
	/**
	 * Gizmo renderer.
	 */
	private final ShapeRenderer gizmo = Game.scene.gizmo;
	
	/**
	 * Holds GUI elements.
	 */
	public final GUIElementHolder guiElements = new GUIElementHolder();
	
	/**
	 * Ortho camera of the GUI.
	 */
	public final OrthographicCamera camera = new OrthographicCamera();
	
	/**
	 * Focused GUI element holder.
	 */
	public GUIElement focused, mouseOver;
	
	/**
	 * Ctor.
	 */
	public GUIController() {
		GUILabelElement label = new GUILabelElement("Editor GUI v0.1", new Vector2(5.f, 5.f), font);
		label.screenSpace = GUIElement.ScreenSpace.TopRight;
		label.anchor = GUIElement.Anchor.TopRight;
		
		guiElements.elements.add(label);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prePerform() {
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
	public void preDebug(ShapeRenderer gizmo) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void postDebug(ShapeRenderer gizmo) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
	}
	
	/**
	 * Input for focused GUI elements.
	 * @param character
	 * @return 
	 */
	@Override
	public boolean keyTyped (char character) {
		if(focused != null) {
			focused.typed(character);
		}
		
		return focused != null;
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
		// make element focused
		if(button == 0 && mouseOver != focused) {
			// element blurs
			if(focused != null) {
				focused.blur();
			}
			
			// element gains the focus
			focused = mouseOver;
			if(focused != null) {
				focused.focus();
			}
		}
		
		if(button == 0 && focused != null) {
			focused.action();
		}
		
		return mouseOver != null;
	}
	
	/**
	 * Hover curosr over the elements.
	 * @param screenX
	 * @param screenY
	 * @return 
	 */
	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		// lookup for over the cursor element
		GUIElement newMouseOver = guiElements.getOverPoint(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
		
		// make element overed by cursor
		if(mouseOver != newMouseOver) {
			// cursor leaves the element bounds
			if(mouseOver != null) {
				mouseOver.leave();
			}
			
			// cursor enters the element bounds
			mouseOver = newMouseOver;
			if(mouseOver != null) {
				mouseOver.enter();
			}
		}
		
		return mouseOver != null;
	}
	
	/**
	 * Draw the GUI elements.
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		// calculate new camera coords
		camera.setToOrtho(false);
		camera.update();
		
		// save old projection
		Matrix4 gizmoProj = gizmo.getProjectionMatrix().cpy();
		Matrix4 batchProj = batch.getProjectionMatrix().cpy();
		
		// draw the GUI elements
		gizmo.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
		guiElements.draw(gizmo, batch);
		
		// reset projection matrix
		gizmo.setProjectionMatrix(gizmoProj);
		batch.setProjectionMatrix(batchProj);
	}
}
