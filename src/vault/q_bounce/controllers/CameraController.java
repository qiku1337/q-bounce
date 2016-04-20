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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import vault.q_bounce.Game;
import vault.q_bounce.actors.BounceActor;
import vault.q_bounce.scene.Actor;
import vault.q_bounce.system.Debug;
import vault.q_bounce.system.SceneController;

/**
 *
 * @author Qiku
 */
public class CameraController extends InputAdapter implements SceneController {
	/**
	 * Game screen bounds.
	 */
	public static final Rectangle SCREEN_BOUNDS = new Rectangle(-1536.f, 0.f, 3072.f, 2048.f);
	private boolean changecam = false;
	/**
	 * Rodzaje podazania kamery za aktorami.
	 * FOLLOW_STATIC - statycznie podaza za aktorem.
	 */
	static public final int
		FOLLOW_STATIC = 0, // "twarde" przypisanie do aktora
		FOLLOW_WALKAROUND = 1, // "oglada" scene
		FOLLOW_TRACING = 2, // sledzi aktora
		FOLLOW_DISTANT = 3, // staje sie "okiem" aktora (NIEZAIMPLEMENTOWANO)
		FOLLOW_FREE = 4; // wolna kamera, kontrolowana przez myszke
	
	/**
	 * Macierz kamery 2D (ortho).
	 */
	public final OrthographicCamera camera = new OrthographicCamera();
	
	/**
	 * Rodzaj sledzenia aktora.
	 * Wartosc jedna ze stalych, tj.
	 *	FOLLOW_STATIC,
	 *	FOLLOW_TRACING,
	 *	FOLLOW_DISTANT,
	 *	FOLLOW_FREE
	 */
	public int followType = FOLLOW_FREE;

	/**
	 * Aktor do sledzenia.
	 * Aktor musi miec nadpisana metode Actor#getPosition()
	 * Tylko dla stanow:
	 *		FOLLOW_WALKAROUND,
	 *		FOLLOW_TRACING
	 */
	public Actor follow = null;
	
	/**
	 * Drugi aktor dzialania.
	 * Tylko dla stanow:
	 *		FOLLOW_WALKAROUND
	 */
	public Actor from = null;
	
	/**
	 * Ctor.
	 */
	public CameraController() {
		// prepare scene camera
		camera.setToOrtho(false);
		camera.translate(
			-(float)(Gdx.graphics.getWidth()/2),
			-(float)(Gdx.graphics.getHeight()/2)
		);
	}

	/**
	 * Wykonuje sie przed jakakolwiek aktualizacja sceny.
	 * @see SceneController#prePerform() 
	 */
	@Override
	public void prePerform() {
		Game.mainCamera = camera;
	}

	/**
	 * Wkonuje sie gdy wykonano wszystkie akcje sceny.
	 * @see SceneController#postPerform() 
	 */
	@Override
	public void postPerform() {
		// rounded coords
		Vector2 rounded = new Vector2(
			(float)Math.floor(camera.position.x),
			(float)Math.floor(camera.position.y)
		);
		
		// append log information
		Debug.info.append("World coord: ").append(rounded).append(" \n");
	}

	/**
	 * Wykonuje sie przed aktualizacja sceny.
	 * @see SceneController#preUpdate(float) 
	 * @param delta 
	 */
	@Override
	public void preUpdate(float delta) {
            	if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                    changecam = !changecam;
		}
	}

	/**
	 * Wykonuje sie po aktualizacji sceny.
	 * @see SceneController#postUpdate(float) 
	 * @param delta 
	 */
	@Override
	public void postUpdate(float delta) {
		// wolna kamera
		if(changecam) {
			if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				camera.translate(
					-(float)Gdx.input.getDeltaX() * 2.f * camera.zoom,
					(float)Gdx.input.getDeltaY() * 2.f * camera.zoom
				);
			}
		} else {
                            Game.mainCamera.position.set(
                                    BounceActor.x,
                                    BounceActor.y,
                                    0
                            );
                            Game.mainCamera.update();	// podazaj za danym aktorem
		}
		
		// granica kamery
		if(!SCREEN_BOUNDS.contains(camera.position.x, camera.position.y)) {
			// vertical edges
			if(camera.position.x < SCREEN_BOUNDS.x) {
				camera.translate(-(camera.position.x - SCREEN_BOUNDS.x), 0.f);
			} else if(camera.position.x > SCREEN_BOUNDS.x + SCREEN_BOUNDS.width) {
				camera.translate(-(camera.position.x - (SCREEN_BOUNDS.x + SCREEN_BOUNDS.width)), 0.f);
			}
			
			// horizontal edges
			if(camera.position.y < SCREEN_BOUNDS.y) {
				camera.translate(0.f, -(camera.position.y - SCREEN_BOUNDS.y));
			} else if(camera.position.y > SCREEN_BOUNDS.y + SCREEN_BOUNDS.height) {
				camera.translate(0.f, -(camera.position.y - (SCREEN_BOUNDS.y + SCREEN_BOUNDS.height)));
			}
		}
	}

	/**
	 * Wykonuje sie przed rysowaniem sceny.
	 * @see SceneController#preDraw(com.badlogic.gdx.graphics.g2d.SpriteBatch) 
	 * @param batch 
	 */
	@Override
	public void preDraw(SpriteBatch batch) {
		// update camera projection matrix
		camera.update();
		
		// setup camera projection
		batch.setProjectionMatrix(camera.combined);
	}

	/**
	 * Wykonuje się po rysowaniu sceny.
	 * @see SceneController#postDraw(com.badlogic.gdx.graphics.g2d.SpriteBatch) 
	 * @param batch 
	 */
	@Override
	public void postDraw(SpriteBatch batch) {
	}

	/**
	 * Wykonuje sie przed rysowaniem debug screena.
	 * @param gizmo 
	 */
	@Override
	public void preDebug(ShapeRenderer gizmo) {
		gizmo.setProjectionMatrix(camera.combined);
		
		// enable alpha channel usage
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// draw scene regions
		gizmo.begin(ShapeRenderer.ShapeType.Line);
		gizmo.setColor(1.f, 0.1f, 0.f, 0.5f);
		gizmo.rect(
			CameraController.SCREEN_BOUNDS.x,
			CameraController.SCREEN_BOUNDS.y,
			CameraController.SCREEN_BOUNDS.width,
			CameraController.SCREEN_BOUNDS.height
		);
		gizmo.end();
	}

	/**
	 * Wykonuje sie po rysowaniu debug screena.
	 * @param gizmo 
	 */
	@Override
	public void postDebug(ShapeRenderer gizmo) {
	}

	/**
	 * Podczas zwalniania kontrollera ze sceny.
	 * @see Disposable#dispose() 
	 */
	@Override
	public void dispose() {
		Game.mainCamera = null;
		
		// restart scene camera
		camera.setToOrtho(false);
		camera.translate(
			-(float)(Gdx.graphics.getWidth()/2),
			-(float)(Gdx.graphics.getHeight()/2)
		);
	}
	
	/**
	 * Zoom-in/out kamery.
	 * @see InputProcessor#scrolled(int) 
	 * @param amount
	 * @return 
	 */
	@Override
	public boolean scrolled(int amount) {
		camera.zoom = Math.min(Math.max(camera.zoom + 0.1f * (float)amount, 1.f), 2.0f);
		return false;
	}
}