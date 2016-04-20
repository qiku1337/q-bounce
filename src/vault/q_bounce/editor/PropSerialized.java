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
package vault.q_bounce.editor;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Qiku
 */
public abstract class PropSerialized {
	/**
	 * Identity of the actor.
	 */
	public int id = 0;
	
	/**
	 * Entity layer.
	 */
	public int layer = 0;
	
	/**
	 * Position in level editor.
	 * Can be used for positioning the actors on the scene.
	 */
	public Vector2 position = new Vector2();
	
	/**
	 * Draw-up the collision or bounding shape etc.
	 * @param gizmo
	 */
	public abstract void draw(ShapeRenderer gizmo);
	
	/**
	 * Returns the actor class assigned with the serializable prop.
	 * @return Actor class.
	 */
	public abstract Class<? extends PropActor> getActorClass();
	
	/**
	 * Instance the prop actor.
	 * @return 
	 */
	public PropActor instance() {
		try {
			// create the new instance
			return getActorClass().getConstructor(PropSerialized.class).newInstance(this);
		} catch (InstantiationException ex) {
			Logger.getLogger(PropSerialized.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(PropSerialized.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(PropSerialized.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(PropSerialized.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(PropSerialized.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(PropSerialized.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return null;
	}
}
