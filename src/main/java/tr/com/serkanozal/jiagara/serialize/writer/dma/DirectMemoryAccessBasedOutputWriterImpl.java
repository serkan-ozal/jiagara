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

package tr.com.serkanozal.jiagara.serialize.writer.dma;

import tr.com.serkanozal.jiagara.domain.builder.buffer.dma.DirectMemoryAccessBasedBufferBuilder;
import tr.com.serkanozal.jiagara.domain.model.buffer.dma.DirectMemoryAccessBasedBuffer;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import sun.misc.Unsafe;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("restriction")
public class DirectMemoryAccessBasedOutputWriterImpl implements DirectMemoryAccessBasedOutputWriter {

	private DirectMemoryAccessBasedBuffer buffer;
	private long startAddress;
	private byte[] bufferArray;
	private int arrayBaseOffset;
	private Unsafe unsafe;
	
	public DirectMemoryAccessBasedOutputWriterImpl() {
		buffer = new DirectMemoryAccessBasedBufferBuilder().build();
		bufferArray = buffer.getBufferArray();
		startAddress = JvmUtil.getArrayBaseAddress(bufferArray, byte.class);
		arrayBaseOffset = JvmUtil.arrayBaseOffset(byte.class);
		unsafe = JvmUtil.getUnsafe();
	}
	
	@Override
	public long getAddress() {
		return startAddress;
	}
	
	@Override
	public DirectMemoryAccessBasedBuffer getBuffer() {
		return buffer;
	}
	
	@Override
	public void writeByte(byte value) {
		if (buffer.has(JvmUtil.BYTE_SIZE)) {
			buffer.pushByte(value);
		}	
	}

	@Override
	public void writeBoolean(boolean value) {
		if (buffer.has(JvmUtil.BOOLEAN_SIZE)) {
			buffer.pushByte(value ? (byte)0x01 : (byte)0x00);
		}	
	}

	@Override
	public void writeCharacter(char value) {
		if (buffer.has(JvmUtil.CHAR_SIZE)) {
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.forward(position);
		}	
	}

	@Override
	public void writeShort(short value) {
		if (buffer.has(JvmUtil.SHORT_SIZE)) {
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.forward(position);
		}	
	}

	@Override
	public void writeInteger(int value) {
		if (buffer.has(JvmUtil.INT_SIZE)) {
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 24);
			bufferArray[position++] = (byte)(value >> 16);
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.forward(position);
		}	
	}

	@Override
	public void writeFloat(float value) {
		int rawValue = Float.floatToRawIntBits(value);
		if (buffer.has(JvmUtil.FLOAT_SIZE)) {
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(rawValue >> 24);
			bufferArray[position++] = (byte)(rawValue >> 16);
			bufferArray[position++] = (byte)(rawValue >> 8);
			bufferArray[position++] = (byte)rawValue;
			buffer.forward(position);
		}	
	}

	@Override
	public void writeLong(long value) {
		if (buffer.has(JvmUtil.LONG_SIZE)) {
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 56);
			bufferArray[position++] = (byte)(value >> 48);
			bufferArray[position++] = (byte)(value >> 40);
			bufferArray[position++] = (byte)(value >> 32);
			bufferArray[position++] = (byte)(value >> 24);
			bufferArray[position++] = (byte)(value >> 16);
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.forward(position);
		}	
	}

	@Override
	public void writeDouble(double value) {
		long rawValue = Double.doubleToRawLongBits(value);
		if (buffer.has(JvmUtil.DOUBLE_SIZE)) {
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(rawValue >> 56);
			bufferArray[position++] = (byte)(rawValue >> 48);
			bufferArray[position++] = (byte)(rawValue >> 40);
			bufferArray[position++] = (byte)(rawValue >> 32);
			bufferArray[position++] = (byte)(rawValue >> 24);
			bufferArray[position++] = (byte)(rawValue >> 16);
			bufferArray[position++] = (byte)(rawValue >> 8);
			bufferArray[position++] = (byte)value;
			buffer.forward(position);
		}	
	}

	@Override
	public void writeString(String value) {
		
	}

	@Override
	public void writeBytes(byte[] value) {
		
	}
	
	@Override
	public void writeByteFrom(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.BYTE_SIZE);
	}

	@Override
	public void writeBoolean(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.BOOLEAN_SIZE);
	}

	@Override
	public void writeCharacter(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.CHAR_SIZE);
	}

	@Override
	public void writeShort(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.SHORT_SIZE);
	}

	@Override
	public void writeInteger(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.INT_SIZE);
	}

	@Override
	public void writeFloat(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.FLOAT_SIZE);
	}

	@Override
	public void writeLong(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.LONG_SIZE);
	}

	@Override
	public void writeDouble(Object obj, long offset) {
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.DOUBLE_SIZE);
	}

	@Override
	public void writeString(Object obj, long offset) {
		
	}

	@Override
	public void writeBytes(Object obj, long offset) {
		
	}

	@Override
	public void writeByteFrom(long address) {
		unsafe.copyMemory(address, startAddress, JvmUtil.BYTE_SIZE);
	}

	@Override
	public void writeBoolean(long address) {
		unsafe.copyMemory(address, startAddress, JvmUtil.BOOLEAN_SIZE);
	}

	@Override
	public void writeCharacter(long address) {
		unsafe.copyMemory(address, startAddress, JvmUtil.CHAR_SIZE);
	}

	@Override
	public void writeShort(long address) {
		unsafe.copyMemory(address, startAddress, JvmUtil.SHORT_SIZE);
	}

	@Override
	public void writeInteger(long address) {
		unsafe.copyMemory(address, startAddress, JvmUtil.INT_SIZE);
	}

	@Override
	public void writeFloat(long address) {
		unsafe.copyMemory(address, startAddress, JvmUtil.FLOAT_SIZE);
	}

	@Override
	public void writeDouble(long address) {
		unsafe.copyMemory(address, startAddress, JvmUtil.DOUBLE_SIZE);
	}

	@Override
	public void writeString(long address) {
		
	}

	@Override
	public void writeBytes(long address) {
		
	}
	
}
