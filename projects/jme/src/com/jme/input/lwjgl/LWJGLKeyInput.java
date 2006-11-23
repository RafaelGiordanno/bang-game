/*
 * Copyright (c) 2003-2006 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme.input.lwjgl;

import java.util.logging.Level;

import org.lwjgl.input.Keyboard;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;
import com.jme.util.LoggingSystem;

/**
 * <code>LWJGLKeyInput</code> uses the LWJGL API to access the keyboard.
 * The LWJGL make use of the native interface for the keyboard.
 * @author Mark Powell
 * @version $Id: LWJGLKeyInput.java,v 1.15 2006/07/20 14:21:28 nca Exp $
 */
public class LWJGLKeyInput extends KeyInput {

    /**
     * Constructor instantiates a new <code>LWJGLKeyInput</code> object. During
     * instantiation, the keyboard is created.
     *
     */
    protected LWJGLKeyInput() {
        try {

            Keyboard.create();
        } catch (Exception e) {
            LoggingSystem.getLogger().log( Level.WARNING, "Could not create keyboard.", e );
        }
    }

    /**
     * <code>isKeyDown</code> returns true if the provided key code is pressed,
     * false otherwise.
     * @see com.jme.input.KeyInput#isKeyDown(int)
     */
    public boolean isKeyDown(int key) {
        return Keyboard.isKeyDown(key);
    }

    /**
     * <code>getKeyName</code> returns the string representation of the key
     * code.
     * @see com.jme.input.KeyInput#getKeyName(int)
     */
    public String getKeyName(int key) {
        return Keyboard.getKeyName(key);
    }

    /**
     * <code>getKeyIndex</code> returns the value of the key name
     * @param name the name of the key
     * @return the value of the key
     */
    public int getKeyIndex( String name) {
        return Keyboard.getKeyIndex( name);
    }

    /**
     * <code>update</code> updates the keyboard buffer.
     * @see com.jme.input.KeyInput#update()
     */
    public void update() {
        /**Polling is done in {@link org.lwjgl.opengl.Display#update()} */
        //Keyboard.poll();

        if ( listeners != null && listeners.size() > 0 ) {
            while ( Keyboard.next() ) {
                char c = Keyboard.getEventCharacter();
                int keyCode = Keyboard.getEventKey();
                boolean pressed = Keyboard.getEventKeyState();

                for ( int i = 0; i < listeners.size(); i++ ) {
                    KeyInputListener listener = listeners.get( i );
                    listener.onKey( c, keyCode,  pressed );
                }
            }
        }
        else {
            // clear events - could use a faster method in lwjgl here...
            while ( Keyboard.next() ) {
                //nothing
            }
        }
    }

    /**
     * <code>destroy</code> cleans up the keyboard for use by other programs.
     * @see com.jme.input.KeyInput#destroy()
     */
    public void destroy() {
        Keyboard.destroy();
    }
}