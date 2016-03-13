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
package system;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import main.Game;
/**
 *
 * @author Qiku
 */
public class PhysicsWorldSystem {
    /**
     * World scaling factor.
     */
    static public final float SCALE_FACTOR = 1.f/60.f;
    
    /**
     * Inverted world scaling factor.
     */
    static public final float SCALE_FACTOR_INV = 1.f/SCALE_FACTOR;
    
    /**
     * World speed.
     */
    static public final float WORLD_STEP = 1.f/50.f;
    
    /**
     * Velocity iterations resolver per step.
     */
    static public final int VELOCITY_ITERATIONS = 6;
    
    /**
     * Position iterations resolver per step.
     */
    static public final int POSITION_ITERATIONS = 2;
    
    /**
     * Box2D world.
     */
    public final World world;
    
    /**
     * Physics debug renderer.
     */
    private Box2DDebugRenderer debugRenderer;
    
    /**
     * Step accumulator.
     */
    private float accumulator = 0.f;
    
    /**
     * Static action.
     * Performed before any main program execution.
     * This one performs the initialization of Box2D physics engine.
     */
    static {
        Box2D.init();
    }
    
    /**
     * Physics world system constructor.
     */
    public PhysicsWorldSystem() {
        this.world = new World(new Vector2(0.f, 10.f), true);
    }
    
    /**
     * Simulate world step.
     * Updates physics objects in fixed mode.
     * @param delta Frames delta time for update ticks perform.
     */
    public void update(float delta) {
        accumulator += Math.min(0.25f, delta);
        while(accumulator >= WORLD_STEP) {
            this.world.step(WORLD_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= WORLD_STEP;
        }
    }
    
    /**
     * Render physics debugging scene.
     */
    public void debug() {
        if(debugRenderer == null) {
            debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
        }
        
        Matrix4 scaleUp = new Matrix4().scale(SCALE_FACTOR_INV, SCALE_FACTOR_INV, 1.f);
        debugRenderer.render(world, Game.mainCamera.combined.cpy().mul(scaleUp));
    }
}
