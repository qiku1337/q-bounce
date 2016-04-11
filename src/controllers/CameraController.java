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
import actors.BounceActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import main.Game;
import scene.Actor;
import system.Debug;
import system.SceneController;
/**
 *
 * @author Qiku
 */
public class CameraController extends InputAdapter implements SceneController {
	/**
	 * Rodzaje podazania kamery za aktorami.
	 * FOLLOW_STATIC - statycznie podaza za aktorem.
	 */
        public static final Rectangle SCREEN_BOUNDS = new Rectangle(-1536.f, 0.f, 3072.f, 2048.f);
	static public final int
		FOLLOW_STATIC = 0, // "twarde" przypisanie do aktora
		FOLLOW_TRACING = 1, // sledzi aktora
		FOLLOW_DISTANT = 2, // staje sie "okiem" aktora
		FOLLOW_FREE = 3; // wolna kamera, kontrolowana przez myszke

	/**
	 * Rodzaj sledzenia aktora.
	 * Wartosc jedna ze stalych, tj.
	 *	FOLLOW_STATIC,
	 *	FOLLOW_TRACING,
	 *	FOLLOW_DISTANT,
	 *	FOLLOW_FREE
	 */
	public int followType = FOLLOW_STATIC;

	/**
	 * Aktor do sledzenia.
	 * Aktor musi miec nadpisana metode Actor#getPosition()
	 */
	public Actor follow = null;
        private boolean changecam = false;
        public final OrthographicCamera camera = new OrthographicCamera();
	/**
	 * Wykonuje sie przed jakakolwiek aktualizacja sceny.
	 * @see SceneController#prePerform() 
	 */
        public CameraController(){
		camera.setToOrtho(false);
		camera.translate(
			-(float)(Gdx.graphics.getWidth()/2),
			-(float)(Gdx.graphics.getHeight()/2)
		);
        }
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
            	Vector2 rounded = new Vector2(
			(float)Math.floor(camera.position.x),
			(float)Math.floor(camera.position.y)
		);
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
		if(!changecam) {
                            Game.mainCamera.position.set(
                                    BounceActor.x,
                                    BounceActor.y,
                                    0
                            );
                            Game.mainCamera.update();
		} else {
                     if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				Game.mainCamera.translate(
					-(float)Gdx.input.getDeltaX() * 2.f *
                                                Game.mainCamera.zoom,
					(float)Gdx.input.getDeltaY() * 2.f *
                                                Game.mainCamera.zoom
				);
                                Game.mainCamera.update();
                    }
                    if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
                                Game.mainCamera.zoom+=0.01f;
                    }
                    if(Gdx.input.isKeyPressed(Input.Keys.X)) {
                                Game.mainCamera.zoom-=0.01f;
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
	public void preDebug(ShapeRenderer gizmo) {
		gizmo.setProjectionMatrix(camera.combined);
	}
	public void postDebug(ShapeRenderer gizmo) {
	}
	public void dispose() {
		Game.mainCamera = null;
		
		// restart scene camera
		camera.setToOrtho(false);
		camera.translate(
			-(float)(Gdx.graphics.getWidth()/2),
			-(float)(Gdx.graphics.getHeight()/2)
		);
	}
}
