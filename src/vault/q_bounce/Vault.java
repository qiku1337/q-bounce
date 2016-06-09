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
package vault.q_bounce;

import com.badlogic.gdx.utils.Array;

import vault.q_bounce.editor.PropSerialized;
import vault.q_bounce.editor.props.BounceProp;
import vault.q_bounce.editor.props.SpikesProp;
import vault.q_bounce.editor.props.TileProp;

/**
 *
 * @author Qiku
 */
public abstract class Vault {

	

	
	
	/**
	 * Klasy propów widoczne dla edytora.
	 */
	static public final Array<Class<? extends PropSerialized>> PROP_CLASSES = new Array<>(
		new Class[] {
			TileProp.class,
                        BounceProp.class,
                        SpikesProp.class
		}
	);
	
	/**
	 * Preload vault assets.
	 */
	static public void preload() {	
		
	}	
	/**
	 * Unload vault assets.
	 */
	static public void unload() {
		
	}
}
