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
package vault.q_bounce.editor.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Qiku
 */
public class GUIFieldElement extends GUIInputElement {
	/**
	 * Instance target for field value changing.
	 */
	private final Object target;
	
	/**
	 * Field reflect of the class.
	 */
	private final Field field;
	
	/**
	 * Ctor.
	 * @param position
	 * @param font 
	 * @param field 
	 * @param target 
	 */
	public GUIFieldElement(Field field, Object target, Vector2 position, BitmapFont font) {
		super(null, position, font);
		
		// field modifiers
		this.field = field;
		this.target = target;
		this.width = 350.f;
		getValue();
	}
	
	/**
	 * Input on focused element.
	 * @param input
	 */
	@Override
	public void typed(char input) {
		super.typed(input);
		
		// change field value
		setValue();
	}
	/**
	 * Draw the field input.
	 * @param shape
	 * @param batch 
	 */
	@Override
	public void draw(ShapeRenderer shape, SpriteBatch batch) {
		text = value + (focused && ticks % 20 > 10 ? "|" : "");
		
		if(focused) {
			ticks++;
		}
		
		// calculate label text bounds
		Rectangle bounds = getBounds();
		
		// enable alpha channel usage
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// draw the shape
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setColor(0, 0, 0, .5f);
		shape.rect(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4);
		shape.end();
		
		// draw the label
		batch.begin(); {
			font.setColor(!focused ? (overed ? Color.PINK : Color.WHITE) : Color.RED);
			font.draw(batch, field.getName() + ": " + text, bounds.x, bounds.y + bounds.height);
		}
		batch.end();
	}
	
	/**
	 * Updates the element value from the field.
	 */
	private void getValue() {
		// get field value
		if(target != null && field != null) {
			try {
				switch(field.getType().getTypeName()) {
					case "float":
						value = Float.toString(field.getFloat(target));
						break;
						
					case "int":
						value = Integer.toString(field.getInt(target));
						break;
						
					case "boolean":
						value = Boolean.toString(field.getBoolean(target));
						break;
						
					default:
						value = field.get(target).toString();
				}
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				Logger.getLogger(GUIFieldElement.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	/**
	 * Updates field value from the element.
	 */
	private void setValue() {
		// change field value
		if(target != null && field != null) {
			try {
				switch(field.getType().getTypeName()) {
					case "float":
						field.setFloat(target, value.isEmpty() ? 0 : Float.parseFloat(value));
						break;
						
					case "int":
						field.setInt(target, value.isEmpty() ? 0 : Integer.parseInt(value));
						break;
						
					case "boolean":
						field.setBoolean(target, value.isEmpty() ? false : Boolean.parseBoolean(value));
						break;
						
					default:
						field.set(target, value.isEmpty() ? null : value);
						break;
				}
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				Logger.getLogger(GUIFieldElement.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
