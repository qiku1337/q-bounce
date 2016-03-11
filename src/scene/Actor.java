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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Qiku
 */
public abstract class Actor implements Entity {
    /**
     * Actor ID on the scene.
     * Should be unique.
     */
    public final int id;
    
    /**
     * Actor tag.
     * Use sub-groups instead actor tags for actors fetching by one type.
     */
    public final String tag;
    
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
        this(id, "");
    }
    
    /**
     * Actor constructor.
     * @param id Unique ID of the actor.
     * @param tag Tag-up the actor.
     */
    public Actor(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }
    
    /**
     * Reflect the event by name and parameters type.
     * Actor events are just methods with the specified type of parameters.
     * @param name Name of the event (method) to fetch.
     * @param paramTypes Type of the parameters.
     * @return Reflected method.
     */
    public Method event(String name, Class<?>... paramTypes) {
        try {
            return this.getClass().getMethod(name, paramTypes);
        }
        catch(NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Trigger actor event w/o any parameters.
     * @param event Event name to perform.
     * @return Event result.
     */
    public Object trigger(String event) {
        try {
            Method method = this.event(event);
            
            if(method != null) {
                return method.invoke(this);
            }
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Trigger actor event with one parameter.
     * @param event Event name to perform.
     * @param param1 First parameter to pass.
     * @return Event result.
     */
    public Object trigger(String event, Object param1) {
        try {
            Method method = this.event(event, param1.getClass());
            
            if(method != null) {
                return method.invoke(this, param1);
            }
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}