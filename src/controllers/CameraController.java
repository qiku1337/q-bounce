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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import main.Game;
import actors.BounceActor;
import scene.Actor;
import system.SceneController;
/**
 *
 * @author Qiku
 */
public class CameraController implements SceneController {
	/**
	 * Rodzaje podazania kamery za aktorami.
	 * FOLLOW_STATIC - statycznie podaza za aktorem.
	 */
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
        
	/**
	 * Wykonuje sie przed jakakolwiek aktualizacja sceny.
	 * @see SceneController#prePerform() 
	 */
        public CameraController(){
            	Game.mainCamera.setToOrtho(false);
                //Game.mainCamera.translate(0.f, 0.f);
                Game.mainCamera.zoom =0.6f;
                Game.mainCamera.update();
        }
	@Override
	public void prePerform() {
            
	}

	/**
	 * Wkonuje sie gdy wykonano wszystkie akcje sceny.
	 * @see SceneController#postPerform() 
	 */
	@Override
	public void postPerform() {
	}

	/**
	 * Wykonuje sie przed aktualizacja sceny.
	 * @see SceneController#preUpdate(float) 
	 * @param delta 
	 */
	@Override
	public void preUpdate(float delta) {            
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

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
		if(follow == null || followType == FOLLOW_STATIC) {
                            Game.mainCamera.position.set(
                                    BounceActor.x,
                                    BounceActor.y,
                                    0
                            );
                            Game.mainCamera.update();
		} else {

		}
	}

	/**
	 * Wykonuje sie przed rysowaniem sceny.
	 * @see SceneController#preDraw(com.badlogic.gdx.graphics.g2d.SpriteBatch) 
	 * @param batch 
	 */
	@Override
	public void preDraw(SpriteBatch batch) {
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
	 * Podczas zwalniania kontrollera ze sceny.
	 * @see Disposable#dispose() 
	 */
	public void dispose() {
	}
}
