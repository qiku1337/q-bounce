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
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.physics.box2d.Contact;
import main.Game;
import scene.Actor;
import screens.StageScreen;
import system.Physics;
/**
 *
 * @author Qiku
 */
public class BounceActor extends Actor {
    
    public static float xvel,yvel;
    
    public static float x,y;
    
    public static boolean respawn=false;
    
    private final Body body;
    
    private final Fixture fixture;
    //protected Fixture fixture;
    private Sprite binspr;
    //private float velocity;
    private float speed;
        
    //private SpriteBatch spriteBatch;
    
    private Animation animation;    

    private final Texture machineTexture;
    
    private final TextureRegion[] ballguy;
    
    private TextureRegion currentFrame;
    
    private BitmapFont font;
    
    private static final int        FRAME_COLS = 28;
    
    private static final int        FRAME_ROWS = 1; 
    
    private float frame = 0.f;
    
    private Sound sound;
    
    private boolean allowjump=false;
    
    private SpriteBatch batch;
    Vector2 vel;
    
    public BounceActor(int id) {
		super(id);
		
		// body shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10.f * Physics.SCALE, 10.f * Physics.SCALE);
		
		// create physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
                
		bodyDef.position.set(0.f, 0.f);
		body = Game.physics.world.createBody(bodyDef);
		fixture = body.createFixture(shape, 2.f);
                //fixture.setRestitution(.1f);
		fixture.setFriction(.4f);
                fixture.setDensity(1.5f);                
                fixture.setUserData(this);
                
                body.setFixedRotation(true);
 
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
                animation = new Animation(0.03f, ballguy);      
                batch = new SpriteBatch();
    }
    
    @Override
    public void create() {
        super.create();
        
        System.out.println("Bounce Created");        
    }
    @Override
    public void update(float delta){  
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            respawn=true;
            Game.scene.ACTION_2.add(new BounceActor(id));
            this.remove();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            jump();
        }            
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //frame += body.getLinearVelocity().len()*delta;
            if(body.getLinearVelocity().len()<2.1f) 
            moveright();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //frame += body.getLinearVelocity().len()*delta;
            if(body.getLinearVelocity().len()<2.1f) 
            moveleft();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
                Game.mainCamera.zoom+=0.01f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
                Game.mainCamera.zoom-=0.01f;
        }
                       
        xvel=body.getLinearVelocity().x*1.65f;        
        yvel=body.getLinearVelocity().y*1.65f;
        x=getX();        
        y=getY();       
        frame += delta;
    }
    @Override
    public void draw(SpriteBatch batch) {                        
             
        currentFrame = animation.getKeyFrame(frame, true);  
        batch.begin();        
        batch.setProjectionMatrix(Game.mainCamera.projection);
        batch.draw(currentFrame,                                 
                getX()-20, 
                getY()-70,                       //miejsce rysowania
                Gdx.graphics.getWidth()/20.f,Gdx.graphics.getHeight()/10.f);//wielkosc  
        batch.end(); 
    }
    @Override
    public void dispose(){
        Game.physics.world.destroyBody(body);       
    }
    public void jump() {
        if(allowjump){
		body.applyForceToCenter(0.f, 15.f, true);
                sound = Gdx.audio.newSound(Gdx.files.internal("assets/sound/jump_01.wav"));
                long idsound = sound.play(1.0f);
                allowjump=false;
        }
    }
    public void moveright() {
		body.applyForceToCenter(1.2f, 0f, true);
            
    }
    public void moveleft() {        
		body.applyForceToCenter(-1.2f, 0f, true);                
    }
    public float getY() {
                return body.getPosition().y;
    }
    public float getX() {         
                return body.getPosition().x;      
                
    }
    @Override
    public void onHit(Actor actor, Contact contact) {
	if(actor instanceof GroundActor) {
            allowjump=true;
            sound = Gdx.audio.newSound(Gdx.files.internal("assets/sound/jumpland.mp3"));
            long idsound = sound.play(1.0f);        
            //sound.setLooping(idsound, true);
	}
    }
}