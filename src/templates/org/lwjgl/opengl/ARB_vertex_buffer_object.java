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

public interface ARB_vertex_buffer_object extends ARB_buffer_object {
	/*
	 * Accepted by the <target> parameters of BindBufferARB, BufferDataARB,
	 * BufferSubDataARB, MapBufferARB, UnmapBufferARB,
	 * GetBufferSubDataARB, GetBufferParameterivARB, and
	 * GetBufferPointervARB:
	*/
	public static final int GL_ARRAY_BUFFER_ARB = 0x8892;
	public static final int GL_ELEMENT_ARRAY_BUFFER_ARB = 0x8893;

	/*
	 * Accepted by the <pname> parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	*/
	public static final int GL_ARRAY_BUFFER_BINDING_ARB = 0x8894;
	public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING_ARB = 0x8895;
	public static final int GL_VERTEX_ARRAY_BUFFER_BINDING_ARB = 0x8896;
	public static final int GL_NORMAL_ARRAY_BUFFER_BINDING_ARB = 0x8897;
	public static final int GL_COLOR_ARRAY_BUFFER_BINDING_ARB = 0x8898;
	public static final int GL_INDEX_ARRAY_BUFFER_BINDING_ARB = 0x8899;
	public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING_ARB = 0x889A;
	public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING_ARB = 0x889B;
	public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING_ARB = 0x889C;
	public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING_ARB = 0x889D;
	public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING_ARB = 0x889E;

	/*
	 * Accepted by the <pname> parameter of GetVertexAttribivARB:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING_ARB = 0x889F;
}