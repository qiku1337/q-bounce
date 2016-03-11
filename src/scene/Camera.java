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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
/**
 *
 * @author Qiku
 */
public class Camera extends Transform implements Entity {
    /**
     * The camera projection matrix.
     */
    public final Matrix4 projection = new Matrix4();
    
    /**
     * Camera ortho width dimmension.
     */
    public float width = 800.f;
    
    /**
     * Camera ortho height dimmension.
     */
    public float height = 600.f;
    
    /**
     * Prepare camera before use.
     * Setups camera as ortho.
     */
    @Override
    public void create() {
        this.projection.setToOrtho2D(0.f, 0.f,
            (float)Gdx.graphics.getWidth(),
            (float)Gdx.graphics.getHeight()
        );
    }
    
    /**
     * Combined camera projection.
     * @return New and combined matrix instance.
     */
    public Matrix4 combined() {
        return this.world().trn(
            this.width*.5f, this.height*.5f, 0.f
        ).mulLeft(this.projection);
    }

    /**
     * Update camera.
     * @param delta Update delta time.
     */
    @Override
    public void update(float delta) {
    }

    /**
     * Prepare rendering batch witch the camera.
     * 
     * @param batch 
     */
    @Override
    public void render(Batch batch) {
        batch.setProjectionMatrix(this.combined());
    }
    
    /**
     * Render actor coords.
     * @param gizmos Enabled gizmo.
     */
    @Override
    public void debug(ShapeRenderer gizmos) {
        gizmos.setProjectionMatrix(new Matrix4());
        gizmos.setTransformMatrix(new Matrix4());
        gizmos.begin(ShapeRenderer.ShapeType.Line);
        gizmos.setColor(Color.DARK_GRAY);
        gizmos.rect(-1.f, -1.f, 2.f, 2.f);
        gizmos.end();
        
        // children debug projection
        gizmos.setProjectionMatrix(this.combined());
    }

    /**
     * Dispose camera from scene.
     */
    @Override
    public void dispose() {
    }
}
