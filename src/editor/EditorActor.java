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
package editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import controllers.EditorController;
import scene.Actor;
/**
 *
 * @author Qiku
 */
public class EditorActor extends Actor {
	private final EditorController editor;
	
	/**
	 * Ctor.
	 * @param id Unique actor identifier.
	 * @param editor Editor controller.
	 */
	public EditorActor(int id, EditorController editor) {
		super(id, TYPE_EDITOR);
		this.editor = editor;
	}
	
	/**
	 * {@inheritDoc} 
	 * @param batch
	 */
	@Override
	public void draw(SpriteBatch batch) {
		if(editor != null) {
			editor.draw(batch);
		}
	}
}
