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
package scene;
import static system.PhysicsWorldSystem.SCALE_FACTOR;
import static system.PhysicsWorldSystem.SCALE_FACTOR_INV;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
/**
 *
 * @author Qiku
 */
public abstract class PhysicsActor extends Actor {
    /**
     * World assigned to.
     */
    public final World world;
    
    /**
     * Body definition.
     */
    public final BodyDef bodyDef = new BodyDef();
    
    /**
     * Fixture definition.
     */
    public final FixtureDef fixtureDef = new FixtureDef();
    
    /**
     * Body instance.
     */
    public Body body;
    
    /**
     * Shape instance.
     * Specified for the default usage.
     */
    public Shape shape;
    
    /**
     * Fixture instance.
     */
    public Fixture fixture;
    
    /**
     * Physics actor constructor.
     * @param id Unique ID of the actor.
     * @param world Physics world assigned to.
     */
    public PhysicsActor(int id, World world) {
        this(id, null, null, world);
    }
    
    /**
     * Physics actor constructor.
     * @param id Unique ID of the actor.
     * @param tag Grouping tag of the actor.
     * @param world Physics world assigned to.
     */
    public PhysicsActor(int id, ActorTag tag, World world) {
        this(id, tag, null, world);
    }
    
    /**
     * Physics actor constructor.
     * @param id Unique ID of the actor.
     * @param tag Grouping tag of the actor.
     * @param parent Parent transform to assign with the actor.
     * @param world Physics world assigned to.
     */
    public PhysicsActor(int id, ActorTag tag, Transform parent, World world) {
        super(id, tag, parent);
        this.world = world;
    }

    /**
     * Create physics actor's body and fixture.
     */
    @Override
    public void create() {
        // prepare body position
        bodyDef.position
            .set(this.position.x, this.position.y)
            .scl(SCALE_FACTOR);
        
        // create body and fixtures
        this.body = this.world.createBody(bodyDef);
        this.fixture = this.body.createFixture(fixtureDef);
    }

    /**
     * Update scene.
     * @param delta 
     */
    @Override
    public void update(float delta) {
        Vector2 bodyPos = this.body.getPosition();
        this.position.set(bodyPos.x * SCALE_FACTOR_INV, bodyPos.y * SCALE_FACTOR_INV, 0.f);
        this.rotate.setFromAxisRad(0.f, 0.f, 1.f, this.body.getAngle());
    }
    
    /**
     * Dispose resources.
     */
    @Override
    public void dispose() {
        if(this.shape != null) {
            this.shape.dispose();
        }
    }
}
