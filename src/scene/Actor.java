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
import system.Scene;
/**
 *
 * @author Qiku
 */
public abstract class Actor implements Entity {
	/**
	 * Layer assigned with the actor.
	 */
	private Scene.Layer layer;
	
	/**
	 * Actor unique identification field.
	 */
	public final int id;
	
	/**
	 * Actor type.
	 */
	public final int type;
	
	/**
	 * Actor activity on the scene.
	 * When <b>FALSE</b>, actor not performs updating and drawing methods.
	 */
	public boolean active = true;
	
	/**
	 * Actor visiblity on the scene.
	 * When <b>FALSE</b>, actor not performs drawing method.
	 */
	public boolean visible = true;
	
	/**
	 * Ctor.
	 * @param id Unique actor identifier.
	 */
	public Actor(int id) {
		this(id, 0);
	}
	
	/**
	 * Ctor.
	 * @param id Unique actor identifier.
	 * @param type Actor type.
	 */
	public Actor(int id, int type) {
		this.id = id;
		this.type = type;
	}
	
	/**
	 * Remove actor from the scene.
	 * @see
	 * @return <b>TRUE</b> when the actor is already in the remove queue.
	 */
	public boolean remove() {
		return this.layer.remove(this);
	}
	
	/**
	 * Scene assigned with the actor.
	 * @return Scene instance if actor already assigned with.
	 */
	public Scene getScene() {
		if(this.layer == null) {
			return null;
		}
		
		return this.layer.scene;
	}
	
	/**
	 * Layer assigned with the actor.
	 * @return Layer instance if actor already assigned with.
	 */
	public Scene.Layer getLayer() {
		return this.layer;
	}
	
	/**
	 * Assigned new layer with the actor.
	 * @param layer New layer of the actor.
	 */
	public void setLayer(Scene.Layer layer) {
		if(this.layer == layer) {
			return;
		}
		
		// remove actor from the old scene
		if(this.layer != null) {
			this.layer.actors.removeValue(this, true);
		}
		
		// change the layer
		this.layer = layer;
		if(this.layer != null) {
			this.layer.actors.add(this);
		}
	}
}