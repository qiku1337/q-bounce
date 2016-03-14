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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import main.Game;
/**
 *
 * @author Qiku
 */
public class Physics implements System {
	/**
	 * Sceen to world scale.
	 */
	static public final float SCALE = 0.01f;
	
	/**
	 * World to screen scale (inverted SCALE).
	 */
	static public final float SCALE_INV = 1.f/SCALE;
	
	/**
	 * Box2D physics world.
	 */
	public final World world;
	
	/**
	 * Debug renderer.
	 */
	private final Box2DDebugRenderer debugRenderer;
	
	/**
	 * Step performing accumulator.
	 */
	private float accumulator;
	
	/**
	 * Initialize Box2D system.
	 */
	static {
		Box2D.init();
	}
	
	/**
	 * Ctor.
	 */
	public Physics() {
		this.world = new World(new Vector2(0.f, -10.f), true);
		
		if(Game.DEBUG) {
			this.debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
		}
	}
	
	/**
	 * Perform physics step.
	 */
	@Override
	public void perform() {
		accumulator += Math.min(0.25f, Gdx.graphics.getDeltaTime());
		
		// perform accumulated step
		while(accumulator >= 1.f/60.f) {
			this.world.step(1/60.f, 6, 2);
			accumulator -= 1.f/60.f;
		}
	}
	
	/**
	 * Perform debug information render if enabled.
	 */
	@Override
	public void postPerform() {
		if(Game.DEBUG) {
			// scale `world to screen`
			Matrix4 projMatrix = Game.mainCamera.combined.cpy();
			projMatrix.mul(new Matrix4().scl(SCALE_INV));
			
			// perform debug rendering
			this.debugRenderer.render(this.world, projMatrix);
		}
	}

	/**
	 * @see Disposable#dispose() 
	 */
	@Override
	public void dispose() {
		if(Game.DEBUG) {
			this.debugRenderer.dispose();
		}
		
		this.world.dispose();
	}
}
