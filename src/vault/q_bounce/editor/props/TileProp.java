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
package vault.q_bounce.editor.props;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import vault.q_bounce.actors.TileActor;
import vault.q_bounce.editor.PropActor;
import vault.q_bounce.editor.PropSerialized;

/**
 *
 * @author Qiku
 */
public class TileProp extends PropSerialized {
	/**
	 * Editable field.
	 */
	public float radius = 32.f;
        /**
	 * Multiplikator odleglosci kamery.
	 */
	public float zoomed = 1.f;
	
	/**
	 * Ctor.
	 */
        	/**
	 * Przesuniecie tla.
	 */
	public float offset_x = 0.f, offset_y = 0.f;
	public TileProp() {
		this.layer = 2;
	}
        
	
	/**
	 * Draw the turret radius as bounds.
	 * @param gizmo 
	 */
	@Override
	public void draw(ShapeRenderer gizmo) {
		gizmo.setColor(Color.YELLOW);
		gizmo.circle(position.x, position.y, radius);
	}

	/**
	 * TurretActor class.
	 * @return 
	 */
	@Override
	public Class<? extends PropActor> getActorClass() {
		return TileActor.class;
	}
}
