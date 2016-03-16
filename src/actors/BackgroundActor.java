/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import main.Game;
import scene.Actor;

/**
 *
 * @author Konrad Nowakowski https://github.com/konrad92
 */
public class BackgroundActor extends Actor {	
	/**
	 * The background tilling sprite.
	 */
	private final Sprite background;
	
	private final float pixelSize;
	
	/**
	 * Ctor.
	 * @see Actor#Actor(int) 
	 * @param id Unique actor identifier.
	 */
	public BackgroundActor(int id) {
		super(id);
		
		// create the blueprint sprite
		this.background = new Sprite(
			Game.assets.get("assets/dragonball.png", Texture.class));
		this.background.getTexture().setWrap(
			Texture.TextureWrap.Repeat,
			Texture.TextureWrap.Repeat
		);
		
		// change size to identity screen-coords
		this.background.setBounds(-1.f, -1.f, 2.f, 2.f);
		this.pixelSize = 1.f/(float)this.background.getTexture().getWidth();
	}
	
	/**
	 * Draw the background grid.
	 * @see Actor#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch) 
	 * @param batch Sprite batching
	 */
	@Override
	public void draw(SpriteBatch batch) {
		// change drawing projection to identity
		batch.setProjectionMatrix(new Matrix4());
		
		float w = (float)Gdx.graphics.getWidth() * pixelSize, 
			h = (float)Gdx.graphics.getHeight() * pixelSize,
			x = (float)Game.mainCamera.position.x * pixelSize,
			y = (float)-Game.mainCamera.position.y * pixelSize;
		
		background.setU(x);
		background.setU2(x + w);
		background.setV(y);
		background.setV2(y + h);
		
		// fill-up the screen
		batch.begin();
		background.draw(batch);
		batch.end();
		
		// reverse camera projection
		batch.setProjectionMatrix(Game.mainCamera.combined);
	}
}