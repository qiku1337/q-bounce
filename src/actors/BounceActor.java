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
import com.badlogic.gdx.Input;
import static system.PhysicsWorldSystem.SCALE_FACTOR;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import main.Game;
import scene.PhysicsActor;
/**
 *
 * @author Qiku
 */
public class BounceActor extends PhysicsActor {
    public static boolean createkul;
    public static boolean kul=false;
    
    Vector2 force = Vector2.Y.rotate(180).scl(1000.f);
    
    public BounceActor(int id) {
        super(id, Game.physics.world);
        
        this.position.set(105.f, 100.f, 0.f);
        
        this.shape = new CircleShape();
        this.shape.setRadius(32.f * SCALE_FACTOR);
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.fixtureDef.shape = this.shape;
        this.fixtureDef.density = 2.5f;
        this.fixtureDef.friction = 0.4f;
        this.fixtureDef.restitution = 0.6f;       
    }
    
    @Override
    public void create() {
        super.create();
        System.out.println("Bounce Created"); 
       
    }
    @Override
    public void update(float delta){
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {				
		System.out.println("Space pressed");  
                
                kul=true;
            }else{
                kul=false;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                System.out.println("UP pressed"); 
                jump(force);
            }                   
    }

    @Override
    public void render(Batch batch) {
    }   
    @Override
    public void dispose(){
        Game.physics.world.destroyBody(body);
    }
    public void jump(Vector2 newForce) {
		//body.setTransform(body.getPosition(), (float)(Math.random()*Math.PI*2));
		body.applyForceToCenter(newForce, true);
    }        
}