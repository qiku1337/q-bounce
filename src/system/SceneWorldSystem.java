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
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import scene.Actor;
import scene.ActorTag;
import scene.Camera;
import scene.Entity;
import scene.Transform;

/**
 *
 * @author Qiku
 */
public final class SceneWorldSystem implements Iterable<Transform>, Disposable {
    /**
     * Root transform object of the scene.
     */
    public final Transform root;
    
    /**
     * List of the transformable objects to remove.
     * Remove process were enforced after scene update or in flush method.
     */
    public final Array<Transform> remove = new Array<>(false, 16);
    
    /**
     * Scene actors tag.
     */
    public final Map<String, ActorTag> tags = new HashMap<>();
    
    /**
     * Static action.
     * Performed before any main program execution.
     * This one performs the initialization of Box2D physics engine.
     */
    static {
        Box2D.init();
    }

    /**
     * Scene world system constructor.
     * Initializes scene with the camera as the root transformable object.
     */
    public SceneWorldSystem() {
        this.root = new Camera();
        if(this.root instanceof Entity) {
            ((Entity)this.root).create();
        }
    }
    
    /**
     * Assign transformable object with the scene root.
     * @param object Transformable object to assign with the parent.
     * @return Added object to the scene.
     */
    public Transform add(Transform object) {
        return this.add(object, this.root);
    }
    
    /**
     * Assign transformable object with the another one.
     * @param object Transformable object to assign with the parent.
     * @param parent Parent transformable object.
     * @return Added object to the scene.
     */
    public Transform add(Transform object, Transform parent) {
        if(object != null) {
            object.setParent(parent);
            if(object instanceof Entity) {
                ((Entity)object).create();
            }
        }
        return object;
    }
    
    /**
     * Assign scene actor with the root transformable object.
     * @param actor Scene actor to assign with the root transformable object.
     * @return Added actor to the scene.
     */
    public Actor add(Actor actor) {
        return this.add(actor, (ActorTag)null, root);
    }
    
    /**
     * Assign scene actor with the root transformable object and named tag.
     * If the tag name does not exists, the new one is created.
     * @param actor Scene actor to assign with the root transformable object.
     * @param tagName Tag name to assign with the scene actor.
     * @return Added actor to the scene.
     */
    public Actor add(Actor actor, String tagName) {
        return this.add(actor, tagName, root);
    }
    
    /**
     * Assign scene actor with the root transformable object and tag.
     * @param actor Scene actor to assign with the root transformable object.
     * @param tag Actor tag to assign with the scene actor.
     * @return Added actor to the scene.
     */
    public Actor add(Actor actor, ActorTag tag) {
        return this.add(actor, tag, root);
    }
    
    /**
     * Assign scene actor with the transformable object and named tag.
     * If the tag name does not exists, the new one is created.
     * @param actor Scene actor to assign with the transformable object.
     * @param tagName Tag name to assign with the scene actor.
     * @param parent Parent transformable object.
     * @return Added actor to the scene.
     */
    public Actor add(Actor actor, String tagName, Transform parent) {
        ActorTag tag = null;
        if(tagName != null) {
            tag = this.tags.get(tagName);
            
            if(tag == null) {
                tag = new ActorTag(tagName);
                this.tags.put(tagName, tag);
            }
        }
        
        return this.add(actor, tag, parent);
    }
    
    /**
     * Assign scene actor with the transformable object and tag.
     * @param actor Scene actor to assign with the transformable object.
     * @param tag Actor tag to assign with the scene actor.
     * @param parent Parent transformable object.
     * @return Added actor to the scene.
     */
    public Actor add(Actor actor, ActorTag tag, Transform parent) {
        if(actor != null) {
            actor.setParent(parent);
            actor.setTag(tag);
            actor.create();
        }
        return actor;
    }
    
    /**
     * Add transformable object to removal stack.
     * The object were removed in update process or by calling flush method.
     * @param object Transform object to remove.
     */
    public void remove(Transform object) {
        this.remove.add(root);
    }
    
    /**
     * Remove immediate transformable object from the scene.
     * Removes the transformable object from the scene immediately.
     * @param object Transform object to remove.
     */
    public void removeImmediate(Transform object) {
        if(object instanceof Entity) {
            Entity entity = (Entity)object;
            entity.destroy();
            entity.dispose();
        }
        
        object.setParent(null);
        
        // remove instance from remove list
        int removeIndexExists = this.remove.indexOf(object, true);
        if(removeIndexExists != -1) {
            this.remove.removeIndex(removeIndexExists);
        }
        
        // remove transform children
        for(Transform child : object) {
            this.removeImmediate(child);
        }
    }
    
    /**
     * Flush removed transformable objects from the scene.
     * Performs immediate remove of the removed objects.
     */
    public void flush() {
        for(Transform removed : this.remove) {
            this.removeImmediate(removed);
        }
    }
    
    /**
     * Update scene.
     * @param delta Delta time to update the entities.
     */
    public void update(float delta) {
        this.update(root, true, delta);
    }
    
    /**
     * Update transformable object.
     * @param root Update perform target.
     * @param childrenToo Update children if <b>TRUE</b>.
     * @param delta Delta time to update the entities.
     */
    public void update(Transform root, boolean childrenToo, float delta) {
        if(root instanceof Entity) {
            ((Entity)root).update(delta);
        }
        
        if(childrenToo) {
            for(Transform child : root) {
                this.update(child, childrenToo, delta);
            }
        }
    }
    
    /**
     * Clean-up the scene from transformable objects.
     * Remove all transform objects from the scene.
     * Uses immediate removal method.
     */
    public void clear() {
        this.removeImmediate(root);
    }
    
    /**
     * Dispose scene.
     */
    @Override
    public void dispose() {
        this.clear();
    }

    /**
     * Allow iteration by transform objects through the scene world.
     * @return 
     */
    @Override
    public Iterator<Transform> iterator() {
        return this.root.iterator();
    }
}