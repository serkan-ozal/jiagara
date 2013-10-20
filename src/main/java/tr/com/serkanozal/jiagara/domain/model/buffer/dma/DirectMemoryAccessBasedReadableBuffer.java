/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tr.com.serkanozal.jiagara.domain.model.buffer.dma;

import tr.com.serkanozal.jiagara.domain.model.buffer.ReadableBuffer;
import tr.com.serkanozal.jiagara.util.JvmUtil;

/**
 * @author Serkan ÖZAL
 */
public class DirectMemoryAccessBasedReadableBuffer extends ReadableBuffer {

	private long startAddress;
	
	public DirectMemoryAccessBasedReadableBuffer(BufferListener bufferListener) {
		super(bufferListener);
		this.startAddress = JvmUtil.getArrayBaseAddress(buffer, byte.class);
	}
	
	public DirectMemoryAccessBasedReadableBuffer(int size, BufferListener bufferListener) {
		super(size, bufferListener);
		this.startAddress = JvmUtil.getArrayBaseAddress(buffer, byte.class);
	}
	
	public DirectMemoryAccessBasedReadableBuffer(byte[] buffer, BufferListener bufferListener) {
		super(buffer, bufferListener);
		this.startAddress = JvmUtil.getArrayBaseAddress(buffer, byte.class);
	}
	
	public long getAddress() {
		return startAddress;
	}
	
}
