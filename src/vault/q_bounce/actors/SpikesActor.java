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
package vault.q_bounce.actors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import vault.q_bounce.Game;
import vault.q_bounce.scene.Actor;
import vault.q_bounce.system.Physics;
/**
 *
 * @author Qiku
 */
public class SpikesActor extends Actor {
	private final Body body;
	private final Fixture fixture;
        private Sprite sprite;
        static public final String SPIKES_TEXTURE = "assets/spike_A.png";
	
	public SpikesActor(int id) {
		super(id);
		
		// shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(17.f * Physics.SCALE, 17.f * Physics.SCALE);
		
		// body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(1.f,-1.5f);
		body = Game.physics.world.createBody(bodyDef);
		fixture = body.createFixture(shape, 2.f);
		fixture.setUserData(this);
                
                sprite = new Sprite(Game.assets.get("assets/spike_A.png", Texture.class));
		sprite.setBounds(1.f, 1.f, 40.f, 40.f);
		sprite.setOriginCenter(); 
                
		shape.dispose();
        }
        @Override
	public void draw(SpriteBatch batch) {
		sprite.setCenter(
			(body.getPosition().x * Physics.SCALE_INV) ,
			(body.getPosition().y * Physics.SCALE_INV)
		);
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
        public static void preload() {
            Game.assets.load(SPIKES_TEXTURE, Texture.class);
        }
        @Override
            public void dispose(){
        Game.physics.world.destroyBody(body);       
    }
}
