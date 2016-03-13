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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/**
 *
 * @author Qiku
 */
public abstract class Actor extends Transform implements Entity {
    /**
     * Actor tag.
     * Group actors type by the tag.
     */
    private ActorTag tag;
    
    /**
     * Actor ID on the scene.
     * Should be unique ID.
     */
    public final int id;
    
    /**
     * Active actor can be updated and rendered by the actors group.
     */
    public boolean active = true;
    
    /**
     * Visible actor can be rendered by the actors group.
     * To render visible actor they must be also active.
     */
    public boolean visible = true;
    
    /**
     * Actor constructor.
     * @param id Unique ID of the actor.
     */
    public Actor(int id) {
        this(id, null, null);
    }
    
    /**
     * Actor constructor.
     * @param id Unique ID of the actor.
     * @param tag Grouping tag of the actor.
     */
    public Actor(int id, ActorTag tag) {
        this(id, tag, null);
    }
    
    /**
     * Actor constructor.
     * @param id Unique ID of the actor.
     * @param tag Grouping tag of the actor.
     * @param parent Parent transform to assign with the actor.
     */
    public Actor(int id, ActorTag tag, Transform parent) {
        super(parent);
        
        this.id = id;
        this.setTag(tag);
    }
    
    /**
     * Setter for the actor's tag.
     * Removes actor reference from the older tag and adds to the new one.
     * @param newTag New actor tag.
     * @return Assigned tag with the actor.
     */
    public final ActorTag setTag(ActorTag newTag) {
        if(this.tag != newTag) {
            // remove actor from old tag
            if(this.tag != null) {
                this.tag.actors.removeValue(this, true);
            }
            
            // assign actor with the new tag
            this.tag = newTag;
            if(this.tag != null) {
                this.tag.actors.add(this);
            }
        }
        
        return this.tag;
    }
    
    /**
     * Retrieve tag assigned with this actor.
     * @return The assigned tag.
     */
    public final ActorTag getTag() {
        return this.tag;
    }
    
    /**
     * Render actor coords.
     * @param gizmos Enabled gizmo.
     *
    @Override
    public void debug(ShapeRenderer gizmos) {
        gizmos.setTransformMatrix(this.world());
        gizmos.begin(ShapeRenderer.ShapeType.Line);
        gizmos.setColor(Color.GRAY);
        gizmos.circle(0.f, 0.f, 6.f);
        gizmos.setColor(Color.RED);
        gizmos.line(0.f, 0.f, 16.f, 0.f);
        gizmos.setColor(Color.BLUE);
        gizmos.line(0.f, 0.f, 0.f, 16.f);
        gizmos.end();
    }
    
    **
     * Dispose actor from the scene.
     * Remove actor from the assigned tag and make orphaned one.
     */
    @Override
    public void dispose() {
        // untag the actor
        this.setTag(null);
    }
}