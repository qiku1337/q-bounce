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
package systems;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import scene.Entity;
import scene.Transform;
/**
 *
 * @author Qiku
 */
public class SceneRendererSystem {
    /**
     * Root entities for rendering perform.
     */
    public final Transform root;
    
    /**
     * Sprite batch renderer.
     */
    public final SpriteBatch spriteBatch = new SpriteBatch();
    
    /**
     * Gizmos shape renderer.
     */
    public final ShapeRenderer shapeRenderer = new ShapeRenderer();
    
    /**
     * Scene renderer system constructor.
     * @param root 
     */
    public SceneRendererSystem(Transform root) {
        this.root = root;
    }
    
    /**
     * Render scene.
     */
    public void render() {
        this.render(root, true, spriteBatch);
    }
    
    /**
     * Perform entity rendering.
     * @param root Rendering perform target.
     * @param childrenToo Render children if <b>TRUE</b>.
     * @param batch Sprite batch renderer.
     */
    public void render(Transform root, boolean childrenToo, Batch batch) {
        if(root instanceof Entity) {
            ((Entity)root).render(batch);
        }
        
        if(childrenToo) {
            for(Transform child : root) {
                this.render(child, childrenToo, batch);
            }
        }
    }
    
    /**
     * Render debug information of the scene.
     * Performs rendering entities' debugging gizmos.
     */
    public void debug() {
        this.debug(root, true, shapeRenderer);
    }
    
    /**
     * Perform entity debug rendering.
     * @param root Debug rendering perform target.
     * @param childrenToo Render children if <b>TRUE</b>.
     * @param gizmos Gizmos shape renderer.
     */
    public void debug(Transform root, boolean childrenToo, ShapeRenderer gizmos) {
        if(root instanceof Entity) {
            ((Entity)root).debug(gizmos);
        }
        
        if(childrenToo) {
            for(Transform child : root) {
                this.debug(child, childrenToo, gizmos);
            }
        }
    }
}
