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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import java.util.Iterator;
import main.Game;
import scene.Actor;
/**
 *
 * @author Qiku
 */
public class Scene implements System {
	/**
	 * Scene layer class.
	 */
	public class Layer implements Disposable, Iterable<Actor> {
		/**
		 * Scene assigned with the layer.
		 * Allows only the single scene assignement.
		 */
		public final Scene scene;
		
		/**
		 * Actors on the layer.
		 */
		public final Array<Actor> actors = new Array<>();
		
		/**
		 * Actors to perform adding to the layer.
		 */
		private final Array<Actor> toAppend = new Array<>();
		
		/**
		 * Actors to remove from the layer.
		 */
		private final Array<Actor> toRemove = new Array<>();
		
		/**
		 * Ctor.
		 * Adding layer to the given scene.
		 * @param scene Assign layer with the scene.
		 */
		public Layer(Scene scene) {
			this.scene = scene;
			
			// put layer onto scene layers stack
			this.scene.layers.add(this);
		}
		
		/**
		 * Add actor to the layer appending queue.
		 * @param actor Actor to add.
		 * @return Chaining actor.
		 */
		public Actor add(Actor actor) {
			this.toAppend.add(actor);
			return actor;
		}
		
		/**
		 * Append actor to the remove queue.
		 * @param actor Actor to remove.
		 * @return <b>TRUE</b> when actor added to the remove queue,
		 *			otherwise actor already exists in the queue.
		 */
		public boolean remove(Actor actor) {
			Layer realLayer = actor.getLayer();
			
			// remove actor from assigned layer, nor current
			if(realLayer != null) {
				if(!realLayer.toRemove.contains(actor, true)) {
					realLayer.toRemove.add(actor);
					return true;
				}
			}
			
			return false;
		}
		
		/**
		 * Find actor by id.
		 * @param <T> Cast type.
		 * @param id Id of the actor to find.
		 * @param as Actor class to cast.
		 * @return <b>NULL</b> when actor by given id does not exists.
		 */
		public <T extends Actor> T getById(int id, Class<T> as) {
			for(Actor actor : this.actors) {
				if(actor.id == id) {
					return as.cast(actor);
				}
			}
			return null;
		}

		/**
		 * Act the update of the layer.
		 * Performs update on all actors.
		 * @param delta Delta time to perform the update.
		 */
		public void update(float delta) {
			// update active actors
			for(Actor actor : actors) {
				if(actor.active) {
					actor.update(delta);
				}
			}
			
			// perform scene flush
			this.flush();
		}
		
		/**
		 * Draw the layer.
		 * Performs draw on all actors.
		 * @param batch Sprite batch as rendering target.
		 */
		public void draw(SpriteBatch batch) {
			for(Actor actor : actors) {
				if(actor.active && actor.visible) {
					actor.draw(batch);
				}
			}
		}
		
		/**
		 * Draw debug information of the layer.
		 * Performs debug information drawing on all actors.
		 * @param gizmo
		 */
		public void debug(ShapeRenderer gizmo) {
			for(Actor actor : this.actors) {
				actor.debug(gizmo);
			}
		}
		
		/**
		 * Flush actors from the remove queue.
		 * Append new actors to the scene, or change layer.
		 */
		public void flush() {
			// remove actors from the layer
			if(toRemove.size > 0) {
				for(Actor actor : toRemove) {
					if(actor.destroy()) {
						this.actors.removeValue(actor, true);
						actor.dispose();
					}
				}
				toRemove.clear();
			}
			
			// append avaiting actors
			if(toAppend.size > 0) {
				for(Actor actor : toAppend) {
					if(actor.getLayer() == null) {
						actor.setLayer(this);
						actor.create();
					} else {
						actor.setLayer(this);
					}
				}
				toAppend.clear();
			}
		}
		
		/**
		 * Dispose layer by disposing all assigned actors.
		 * @see Disposable#dispose() 
		 */
		@Override
		public void dispose() {
			for(Actor actor : actors) {
				actor.dispose();
			}
			
			// clear actors set
			actors.clear();
		}
		
		/**
		 * @see Iterable#iterator() 
		 */
		@Override
		public Iterator<Actor> iterator() {
			return this.actors.iterator();
		}
	}
	
	/**
	 * Generic scene layers.
	 */
	public final Layer
		BACKGROUND,
		ACTION_1,
		ACTION_2, 
		ACTION_3,
		FOREGROUND,
		GUI,
		DEBUG;
	
	/**
	 * Scene layers.
	 */
	public final Array<Layer> layers = new Array<>();
	
	/**
	 * Sprite batching instance.
	 */
	public final SpriteBatch batch = new SpriteBatch();
	
	/**
	 * Shape renderer for gizmos.
	 */
	public final ShapeRenderer gizmo = new ShapeRenderer();
	
	/**
	 * Scene orthographic camera.
	 */
	public final OrthographicCamera camera = new OrthographicCamera();
	
	/**
	 * Ctor.
	 */
	public Scene() {
		// init orthographic camera
		this.camera.setToOrtho(true);
		this.camera.update();
		
		// create generic layers
		this.BACKGROUND = new Layer(this);
		this.ACTION_1 = new Layer(this);
		this.ACTION_2 = new Layer(this);
		this.ACTION_3 = new Layer(this);
		this.FOREGROUND = new Layer(this);
		this.GUI = new Layer(this);
		this.DEBUG = new Layer(this);
	}
	
	/**
	 * Update the whole scene.
	 */
	@Override
	public void perform() {
		// act actors update
		for(Layer layer : this.layers) {
			layer.update(Gdx.graphics.getDeltaTime());
		}
		
		// assign camera projection matrix
		batch.setProjectionMatrix(camera.combined);
		gizmo.setProjectionMatrix(camera.combined);
		
		// draw actors' sprites
		for(Layer layer : this.layers) {
			layer.draw(batch);
		}
		
		// draw scene debug information
		if(Game.DEBUG) {
			for(Layer layer : this.layers) {
				layer.debug(gizmo);
			}
		}
	}
	
	/**
	 * Clear-up the scene with the generic layers create.
	 */
	public void clear() {
		// clear up the layers
		for(Layer layer : this.layers) {
			layer.dispose();
		}
	}

	/**
	 * @see Disposable#dispose() 
	 */
	@Override
	public void dispose() {
		// clearup the scene
		this.clear();
		
		// dispose the scene
		this.gizmo.dispose();
		this.batch.dispose();
	}
}
