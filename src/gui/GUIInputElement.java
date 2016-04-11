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
package gui;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 *
 * @author Qiku
 */
public class GUIInputElement extends GUILabelElement {
	/**
	 * Input field width.
	 */
	public float width = 200.f;
	
	/**
	 * Input value
	 */
	public String value;
	
	/**
	 * Field focus ticks.
	 */
	protected int ticks = 0;
	
	/**
	 * Ctor.
	 * @param value
	 * @param position
	 * @param font 
	 */
	public GUIInputElement(String value, Vector2 position, BitmapFont font) {
		super(value, position, font);
		
		// input value
		this.value = value;
	}

	/**
	 * Input on focused element.
	 * @param input
	 */
	@Override
	public void typed(char input) {
		if(input > 0) {
			if(input == 8) {
				if(value.length() > 0) {
					value = value.substring(0, value.length() - 1);
				}
			} else if(input == 13) {
			} else if(input != 96) {
				value += input;
			}
		}
	}
	
	/**
	 * Draw the input field.
	 * @param shape
	 * @param batch 
	 */
	@Override
	public void draw(ShapeRenderer shape, SpriteBatch batch) {
		text = value + (focused && ticks % 20 > 10 ? "|" : "");
		super.draw(shape, batch);
		
		if(focused) {
			ticks++;
		}
	}
	
	/**
	 * {@inheritDoc} 
	 * @return 
	 */
	@Override
	public Rectangle getBounds() {
		Rectangle inputFiled = super.getBounds();
		
		// calculate input element bounds
		inputFiled.width = width;
		return inputFiled;
	}
}
