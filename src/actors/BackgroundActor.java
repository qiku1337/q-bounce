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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import main.Game;
import scene.Actor;
import system.Physics;

/**
 *
 * @author Qiku
 */
public class BackgroundActor extends Actor {	
    
    private SpriteBatch batch;
            
    private final Sprite spritecloud;
    private Body body;
    
    private final Sprite spritelayer1;
    private Body bodylayer;
    
    private float frame;
    
    private final float velocity=100.f;
            
    public BackgroundActor(int id) {
        super(id);
        frame = -10.f;
                    
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;		
	bodyDef.position.set(0.f, -1.5f);
        bodylayer = Game.physics.world.createBody(bodyDef);
        body = Game.physics.world.createBody(bodyDef);

        spritelayer1 = new Sprite(Game.assets.get("assets/layer-1.png", Texture.class));
        spritelayer1.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spritelayer1.setBounds(0.f, 0.f, 
                spritelayer1.getOriginX()*4, 
                spritelayer1.getOriginY()
        );
	spritelayer1.setOriginCenter(); 
        
        spritecloud = new Sprite(Game.assets.get("assets/layer-5.png", Texture.class));
        spritecloud.setBounds(1.f, 1.f, 
                spritecloud.getOriginX(), 
                spritecloud.getOriginY()
        );
	spritecloud.setOriginCenter(); 
        
    }
    @Override
    public void update(float delta){ 
        body.setTransform(
            frame*Physics.SCALE*velocity,
            -1.5f,
            0.f
	); 
        frame+=delta;
        if(frame>10.f) 
            frame=-10.f;
        //draw();
    }
    @Override
    public void draw(SpriteBatch batch) {            
            spritelayer1.setCenter(
		bodylayer.getPosition().x * Physics.SCALE_INV,
		bodylayer.getPosition().y * Physics.SCALE_INV
            );
            batch.begin();                
		spritelayer1.draw(batch);
            batch.end();
		
            spritecloud.setCenter(
                body.getPosition().x * Physics.SCALE_INV,
                body.getPosition().y * Physics.SCALE_INV
            );
            batch.begin();
            spritecloud.draw(batch);
            batch.end();
	}
 }
