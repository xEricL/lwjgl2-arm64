/*
 * Copyright (c) 2002-2004 LWJGL Project
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
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
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
package org.lwjgl.opengl;

/**
 * This is the Mac OS X AWT Frame. It contains thread safe
 * methods to manipulateit from non-AWT threads
 * @author elias_naur
 */

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.LWJGLException;

final class MacOSXFrame extends Frame implements WindowListener, ComponentListener {

	private final MacOSXGLCanvas canvas;
	private boolean close_requested;

	/* States */
	private Rectangle bounds;
	private boolean should_update;
	private boolean active;
	private boolean visible;
	private boolean minimized;

	MacOSXFrame(DisplayMode mode, java.awt.DisplayMode requested_mode, boolean fullscreen, int x, int y) throws LWJGLException {
		setResizable(false);
		addWindowListener(this);
		addComponentListener(this);
		canvas = new MacOSXGLCanvas();
		add(canvas, BorderLayout.CENTER);
		setUndecorated(fullscreen);
		if ( fullscreen ) {
			getDevice().setFullScreenWindow(this);
			getDevice().setDisplayMode(requested_mode);
			java.awt.DisplayMode real_mode = getDevice().getDisplayMode();
			/** For some strange reason, the display mode is sometimes silently capped even though the mode is reported as supported */
			if ( requested_mode.getWidth() != real_mode.getWidth() || requested_mode.getHeight() != real_mode.getHeight() ) {
				getDevice().setFullScreenWindow(null);
				syncDispose();
				throw new LWJGLException("AWT capped mode: requested mode = " + requested_mode.getWidth() + "x" + requested_mode.getHeight() +
				                         " but got " + real_mode.getWidth() + " " + real_mode.getHeight());
			}
		}
		pack();
		syncReshape(x, y, mode.getWidth(), mode.getHeight());
		invokeAWT(new Runnable() {
			public void run() {
				setVisible(true);
				requestFocus();
				canvas.requestFocus();
			}
		});
		canvas.waitForCanvasCreated();
	}

	public Rectangle syncGetBounds() {
		synchronized ( this ) {
			return bounds;
		}
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	private void updateBounds() {
		synchronized ( this ) {
			bounds = getBounds();
		}
	}

	public void componentResized(ComponentEvent e) {
		updateBounds();
	}

	public void componentMoved(ComponentEvent e) {
		updateBounds();
	}

	public static GraphicsDevice getDevice() {
		GraphicsEnvironment g_env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = g_env.getDefaultScreenDevice();
		return device;
	}

	public void windowIconified(WindowEvent e) {
		synchronized ( this ) {
			minimized = true;
		}
	}

	public void windowDeiconified(WindowEvent e) {
		synchronized ( this ) {
			minimized = false;
		}
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		synchronized ( this ) {
			close_requested = true;
		}
	}

	public void windowDeactivated(WindowEvent e) {
		synchronized ( this ) {
			active = false;
		}
	}

	public void windowActivated(WindowEvent e) {
		synchronized ( this ) {
			should_update = true;
			active = true;
		}
	}

	public void syncDispose() {
		invokeAWT(new Runnable() {
			public void run() {
				if ( isDisplayable() )
					dispose();
			}
		});
	}

	private class TitleSetter implements Runnable {

		private final String title;

		TitleSetter(String title) {
			this.title = title;
		}

		public void run() {
			setTitle(title);
		}
	}

	public void syncSetTitle(String title) {
		invokeAWT(new TitleSetter(title));
	}

	public boolean syncIsCloseRequested() {
		boolean result;
		synchronized ( this ) {
			result = close_requested;
			close_requested = false;
		}
		return result;
	}

	public boolean syncIsVisible() {
		synchronized ( this ) {
			return !minimized;
		}
	}

	public boolean syncIsActive() {
		synchronized ( this ) {
			return active;
		}
	}

	public MacOSXGLCanvas getCanvas() {
		return canvas;
	}

	public boolean syncShouldUpdateContext() {
		boolean result;
		synchronized ( this ) {
			result = canvas.syncShouldUpdateContext() || should_update;
			should_update = false;
		}
		return result;
	}

	private class Reshaper implements Runnable {

		private final int x;
		private final int y;
		private final int width;
		private final int height;

		Reshaper(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public void run() {
			Insets insets = getInsets();
			setBounds(x, y, width + insets.left + insets.right, height + insets.top + insets.bottom);
		}
	}

	private void invokeAWT(Runnable r) {
		try {
			java.awt.EventQueue.invokeAndWait(r);
		} catch (InterruptedException e) {
			// ignore
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public void syncReshape(int x, int y, int width, int height) {
		invokeAWT(new Reshaper(x, y, width, height));
	}

	private class CursorSetter implements Runnable {

		private final java.awt.Cursor awt_cursor;

		CursorSetter(java.awt.Cursor awt_cursor) {
			this.awt_cursor = awt_cursor;
		}

		public void run() {
			canvas.setCursor(awt_cursor);
		}
	}

	public void syncSetCursor(java.awt.Cursor awt_cursor) {
		invokeAWT(new CursorSetter(awt_cursor));
	}
}