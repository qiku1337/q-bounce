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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
public class BounceActor extends Actor {
    private final Body body;
    private final Fixture fixture;
    //protected Fixture fixture;
    private Sprite binspr;
    //private float velocity;
    private float speed;
    public static boolean kul=false;
    private SpriteBatch spriteBatch;
    
    private Animation animation;    

    private Texture machineTexture;
    
    private TextureRegion[] ballguy;
    
    private TextureRegion currentFrame;
    
    private BitmapFont font;
    
    private static final int        FRAME_COLS = 28;
    
    private static final int        FRAME_ROWS = 1; 
    
    private float frame = 0.f;
    
    Vector2 vel;
    
    public BounceActor(int id) {
		super(id);
		
		// body shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10.f * Physics.SCALE, 10.f * Physics.SCALE);
		
		// create physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
                
		bodyDef.position.set((float)Math.random()*1.f, (float)Math.random()*1.f);
		body = Game.physics.world.createBody(bodyDef);
		fixture = body.createFixture(shape, 2.f);
                fixture.setRestitution(.1f);
		fixture.setFriction(.4f);
                fixture.setDensity(1.5f);
 
                shape.dispose();
                
        machineTexture = new Texture(Gdx.files.internal("assets/ballguy.png"));        
        TextureRegion[][] tmp = TextureRegion.split(machineTexture, machineTexture.getWidth()/FRAME_COLS, machineTexture.getHeight()/FRAME_ROWS);              // #10
        ballguy = new TextureRegion[FRAME_COLS * FRAME_ROWS];        
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                ballguy[index++] = tmp[i][j];
            }
        }
        animation = new Animation(0.04f, ballguy);      
        spriteBatch = new SpriteBatch(); 	  
    }
    
    @Override
    public void create() {
        super.create();
        System.out.println("Bounce Created"); 
       
    }
    @Override
    public void update(float delta){
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {				
                        Game.scene.ACTION_2.add(new BounceActor(id));
			//this.remove();
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                jump();
            }            
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                frame += body.getLinearVelocity().len()*delta;
                if(body.getLinearVelocity().len()<2.f) 
                    moveright();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                frame += body.getLinearVelocity().len()*delta;
                if(body.getLinearVelocity().len()<2.f) 
                moveleft();
            }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        // #14
                
        currentFrame = animation.getKeyFrame(frame, true);  
        spriteBatch.begin();        
        spriteBatch.draw(currentFrame,                                 
                380.f+getX()*100.f, 
                290.f+getY()*100.f,                       //miejsce rysowania
                Gdx.graphics.getWidth()/20.f,Gdx.graphics.getHeight()/10.f);  //wielkosc  
        spriteBatch.end();        
    }  
    @Override
    public void dispose(){
        Game.physics.world.destroyBody(body);
    }
    public void jump() {
		body.applyForceToCenter(0.f, 20.f, true);
    }
    public void moveright() {
		body.applyForceToCenter(1.5f, 0f, true);
                
    }
    public void moveleft() {
		body.applyForceToCenter(-1.5f, 0f, true);                
    }
    public float getY() {
                return body.getPosition().y;
    }
    public float getX() {
                System.out.println(body.getPosition().x);
                return body.getPosition().x;
                
    }
}