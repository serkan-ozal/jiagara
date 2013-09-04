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
public class Buffer {

	public static final int DEFAULT_BUFFER_SIZE = 1024;
	
	protected byte[] buffer;
	protected int index;
	
	public Buffer() {
		this.buffer = new byte[DEFAULT_BUFFER_SIZE];
		this.index = 0;
	}
	
	public Buffer(int size) {
		AssertUtil.notNegative(size, "Size cannot be negative: $0");
		this.buffer = new byte[size];
		this.index = 0;
	}
	
	public Buffer(byte[] buffer) {
		AssertUtil.notNull(buffer, "Buffer array cannot be null");
		this.buffer = buffer;
		this.index = 0;
	}
	
	public byte[] getBufferArray() {
		return buffer;
	}

	public int getIndex() {
		return index;
	}

	public int getLength() {
		return buffer.length;
	}
	
	public boolean isEmpty() {
		return index == 0;
	}
	
	public boolean isFull() {
		return index == buffer.length;
	}
	
	public void reset() {
		index = 0;
	}

	public byte getByte(int i) {
		AssertUtil.notNegative(i, "Index cannot be negative number: $0");
		AssertUtil.smaller(i, buffer.length, "Index must be smaller than $1 (length of buffer array): $0");
		return buffer[i];
	}
	
	public void setByte(int i, byte value) {
		AssertUtil.notNegative(i, "Index cannot be negative number: $0");
		AssertUtil.smaller(i, buffer.length, "Index must be smaller than $1 (length of buffer array): $0");
		buffer[i] = value;
	}
	
	public byte popByte() {
		AssertUtil.isFalse(isEmpty(), "Buffer is empty");
		return buffer[--index];
	}
	
	public void pushByte(byte value) {
		AssertUtil.isFalse(isFull(), "Buffer is full");
		buffer[index++] = value;
	}
	
	public void forward(int length) {
		AssertUtil.smaller(index + length, buffer.length, "Index+length must be smaller than $1 (length of buffer array): $0");
		index += length;
	}
	
	public void move(int index) {
		AssertUtil.smaller(index, buffer.length, "Index must be smaller than $1 (length of buffer array): $0");
		this.index = index;
	}
	
	public boolean checkCapacity(int size) {
		return index + size < buffer.length;
	}
	
	public boolean checkCapacity(long size) {
		return index + size < buffer.length;
	}
	
	public boolean checkCapacitiyAndFlushIfNeeded(int size) {
		// TODO Implement flush trigger if needed
		return index + size < buffer.length;
	}
	
	public boolean checkCapacitiyAndFlushIfNeeded(long size) {
		// TODO Implement flush trigger if needed
		return index + size < buffer.length;
	}
	
}
