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

package tr.com.serkanozal.jiagara.domain.model.buffer;

import tr.com.serkanozal.jiagara.util.AssertUtil;

/**
 * @author Serkan ÖZAL
 */
public class WritableBuffer extends Buffer {

	public WritableBuffer(BufferListener bufferListener) {
		super(bufferListener);
	}
	
	public WritableBuffer(int size, BufferListener bufferListener) {
		super(size, bufferListener);
	}
	
	public WritableBuffer(byte[] buffer, BufferListener bufferListener) {
		super(buffer, bufferListener);
	}
	
	public boolean isEmpty() {
		return index == 0;
	}
	
	public boolean isFull() {
		return index == buffer.length;
	}
	
	public void writeByte(byte value) {
		AssertUtil.isFalse(isFull(), "Buffer is full");
		buffer[index++] = value;
	}

}
