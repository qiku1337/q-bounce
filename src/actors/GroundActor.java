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
package actors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import main.Game;
import scene.Actor;
import system.Physics;
/**
 *
 * @author Qiku
 */
public class GroundActor extends Actor {
	private final Body body;
	private final Fixture fixture;
	private Sprite sprGround;
        static public final String GROUND_TEXTURE = "assets/Tile.png";
        
	public GroundActor(int id) {
		super(id);
		
		// shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1000.f * Physics.SCALE, 20.f * Physics.SCALE);
		
		// body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(1.f,-2.f);
		body = Game.physics.world.createBody(bodyDef);
		fixture = body.createFixture(shape, 2.f);
		fixture.setUserData(this);
                
                sprGround = new Sprite(Game.assets.get("assets/Tile.png", Texture.class));
                sprGround.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		float region = 80.f * Physics.SCALE_INV;
		sprGround.setBounds(0.f, 0.f, region/4, sprGround.getHeight()/4);
		sprGround.setRegionWidth((int)region);
                
		shape.dispose();
	}
        @Override
        public void draw(SpriteBatch batch){
                sprGround.setCenter(
			(body.getPosition().x * Physics.SCALE_INV) ,
			(body.getPosition().y * Physics.SCALE_INV)
		);
		batch.begin();
		sprGround.draw(batch);
		batch.end();        	
        }
        public static void preload() {
            Game.assets.load(GROUND_TEXTURE, Texture.class);
        }
        @Override
            public void dispose(){
        Game.physics.world.destroyBody(body);       
    }
 }

