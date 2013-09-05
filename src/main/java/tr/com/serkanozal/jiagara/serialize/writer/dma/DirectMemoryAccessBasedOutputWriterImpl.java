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

import java.io.OutputStream;

import tr.com.serkanozal.jiagara.domain.builder.buffer.dma.DirectMemoryAccessBasedBufferBuilder;
import tr.com.serkanozal.jiagara.domain.model.buffer.dma.DirectMemoryAccessBasedBuffer;
import tr.com.serkanozal.jiagara.serialize.writer.AbstractBufferedOutputWriter;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import sun.misc.Unsafe;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("restriction")
public class DirectMemoryAccessBasedOutputWriterImpl extends AbstractBufferedOutputWriter<DirectMemoryAccessBasedBuffer> 
		implements DirectMemoryAccessBasedOutputWriter {

	private long startAddress;
	private Unsafe unsafe;
	private int byteArrayBase;
	private int byteArrayScale;
	private int booleanArrayBase;
	private int booleanArrayScale;
	private int charArrayBase;
	private int charArrayScale;
	private int shortArrayBase;
	private int shortArrayScale;
	private int intArrayBase;
	private int intArrayScale;
	private int floatArrayBase;
	private int floatArrayScale;
	private int longArrayBase;
	private int longArrayScale;
	private int doubleArrayBase;
	private int doubleArrayScale;
	@SuppressWarnings("unused")
	private int objectArrayBase;
	@SuppressWarnings("unused")
	private int objectArrayScale;
	
	public DirectMemoryAccessBasedOutputWriterImpl(OutputStream os) {
		super(os, new DirectMemoryAccessBasedBufferBuilder().build());
		//////////////////////////////////////////////////////////////////
		startAddress = JvmUtil.getArrayBaseAddress(bufferArray, byte.class);
		unsafe = JvmUtil.getUnsafe();
		//////////////////////////////////////////////////////////////////
		byteArrayBase = unsafe.arrayBaseOffset(byte[].class);
		byteArrayScale = unsafe.arrayIndexScale(byte[].class);
		//////////////////////////////////////////////////////////////////
		booleanArrayBase = unsafe.arrayBaseOffset(boolean[].class);
		booleanArrayScale = unsafe.arrayIndexScale(boolean[].class);
		//////////////////////////////////////////////////////////////////
		charArrayBase = unsafe.arrayBaseOffset(char[].class);
		charArrayScale = unsafe.arrayIndexScale(char[].class);
		//////////////////////////////////////////////////////////////////
		shortArrayBase = unsafe.arrayBaseOffset(short[].class);
		shortArrayScale = unsafe.arrayIndexScale(short[].class);
		//////////////////////////////////////////////////////////////////
		intArrayBase = unsafe.arrayBaseOffset(int[].class);
		intArrayScale = unsafe.arrayIndexScale(int[].class);
		//////////////////////////////////////////////////////////////////
		floatArrayBase = unsafe.arrayBaseOffset(float[].class);
		floatArrayScale = unsafe.arrayIndexScale(float[].class);
		//////////////////////////////////////////////////////////////////
		longArrayBase = unsafe.arrayBaseOffset(long[].class);
		longArrayScale = unsafe.arrayIndexScale(long[].class);
		//////////////////////////////////////////////////////////////////
		doubleArrayBase = unsafe.arrayBaseOffset(double[].class);
		doubleArrayScale = unsafe.arrayIndexScale(double[].class);
		//////////////////////////////////////////////////////////////////
		objectArrayBase = unsafe.arrayBaseOffset(Object[].class);
		objectArrayScale = unsafe.arrayIndexScale(Object[].class);
		//////////////////////////////////////////////////////////////////
	}
	
	@Override
	public long getAddress() {
		return startAddress;
	}

	@Override
	public void writeByte(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BYTE_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.BYTE_SIZE);
	}

	@Override
	public void writeBoolean(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BOOLEAN_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.BOOLEAN_SIZE);
	}

	@Override
	public void writeCharacter(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.CHAR_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.CHAR_SIZE);
	}

	@Override
	public void writeShort(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.SHORT_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.SHORT_SIZE);
	}

	@Override
	public void writeInteger(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.INT_SIZE);
	}

	@Override
	public void writeFloat(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.FLOAT_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.FLOAT_SIZE);
	}

	@Override
	public void writeLong(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.LONG_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.LONG_SIZE);
	}

	@Override
	public void writeDouble(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.DOUBLE_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, buffer.getIndex(), JvmUtil.DOUBLE_SIZE);
	}

	@Override
	public void writeString(Object obj, long offset) {
		// TODO Implement write string
	}
	
	@Override
	public void writeEnum(Object obj, long offset) {
		// TODO Implement write enum
	}
	
	@Override
	public void writeObject(Object obj, long offset) {
		// TODO Implement write object
	}

	@Override
	public void writeByteArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				byte[] array1 = (byte[])unsafe.getObject(obj, offset);
				long size1 = byteArrayBase + (array1.length * byteArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				byte[] array2 = (byte[])unsafe.getObject(obj, offset);
				long size2 = byteArrayBase + (array2.length * byteArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeBooleanArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				boolean[] array1 = (boolean[])unsafe.getObject(obj, offset);
				long size1 = booleanArrayBase + (array1.length * booleanArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				boolean[] array2 = (boolean[])unsafe.getObject(obj, offset);
				long size2 =  booleanArrayBase + (array2.length * booleanArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeCharacterArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				char[] array1 = (char[])unsafe.getObject(obj, offset);
				long size1 = charArrayBase + (array1.length * charArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				char[] array2 = (char[])unsafe.getObject(obj, offset);
				long size2 = charArrayBase + (array2.length * charArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeShortArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				short[] array1 = (short[])unsafe.getObject(obj, offset);
				long size1 = shortArrayBase + (array1.length * shortArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				short[] array2 = (short[])unsafe.getObject(obj, offset);
				long size2 = shortArrayBase + (array2.length * shortArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeIntegerArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				int[] array1 = (int[])unsafe.getObject(obj, offset);
				long size1 = intArrayBase + (array1.length * intArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				int[] array2 = (int[])unsafe.getObject(obj, offset);
				long size2 = intArrayBase + (array2.length * intArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeFloatArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				float[] array1 = (float[])unsafe.getObject(obj, offset);
				long size1 = floatArrayBase + (array1.length * floatArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				float[] array2 = (float[])unsafe.getObject(obj, offset);
				long size2 =  floatArrayBase + (array2.length * floatArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeLongArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				long[] array1 = (long[])unsafe.getObject(obj, offset);
				long size1 = longArrayBase + (array1.length * longArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				long[] array2 = (long[])unsafe.getObject(obj, offset);
				long size2 = longArrayBase + (array2.length * longArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeDoubleArray(Object obj, long offset) {
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				long arrayAddress1 = JvmUtil.toNativeAddress(unsafe.getInt(obj, offset));
				double[] array1 = (double[])unsafe.getObject(obj, offset);
				long size1 = doubleArrayBase + (array1.length * doubleArrayScale);
				buffer.checkCapacitiyAndHandle(size1);
				unsafe.copyMemory(arrayAddress1, startAddress + buffer.getIndex(), size1);
				break;
			case JvmUtil.ADDRESSING_8_BYTE:
				long arrayAddress2 = JvmUtil.toNativeAddress(unsafe.getLong(obj, offset));
				double[] array2 = (double[])unsafe.getObject(obj, offset);
				long size2 = doubleArrayBase + (array2.length * doubleArrayScale);
				buffer.checkCapacitiyAndHandle(size2);
				unsafe.copyMemory(arrayAddress2, startAddress + buffer.getIndex(), size2);
				break;
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}
	
	@Override
	public void writeObjectArray(Object obj, long offset) {
		// TODO Implement write object array
	}

	@Override
	public void writeByte(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BYTE_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.BYTE_SIZE);
	}

	@Override
	public void writeBoolean(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BOOLEAN_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.BOOLEAN_SIZE);
	}

	@Override
	public void writeCharacter(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.CHAR_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.CHAR_SIZE);
	}

	@Override
	public void writeShort(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.SHORT_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.SHORT_SIZE);
	}

	@Override
	public void writeInteger(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.INT_SIZE);
	}

	@Override
	public void writeFloat(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.FLOAT_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.FLOAT_SIZE);
	}
	
	@Override
	public void writeLong(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.LONG_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.LONG_SIZE);
	}

	@Override
	public void writeDouble(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.DOUBLE_SIZE);
		unsafe.copyMemory(address, startAddress, JvmUtil.DOUBLE_SIZE);
	}

	@Override
	public void writeString(long address) {
		// TODO Implement write string
	}
	
	@Override
	public void writeEnum(long address) {
		// TODO Implement write enum
	}
	
	@Override
	public void writeObject(long address) {
		// TODO Implement write object
	}
	
	private int getArrayLength(long arrayStartAddress, int arrayBaseOffset) {
		long arrayIndexStartAddress = arrayStartAddress + arrayBaseOffset;
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				return unsafe.getInt(arrayIndexStartAddress - JvmUtil.arrayLengthSize());
			case JvmUtil.ADDRESSING_8_BYTE:
				return (int)unsafe.getLong(arrayIndexStartAddress - JvmUtil.arrayLengthSize());
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}

	@Override
	public void writeByteArray(long address) {
		int length = getArrayLength(address, byteArrayBase);
		long size = byteArrayBase + (length * byteArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeBooleanArray(long address) {
		int length = getArrayLength(address, booleanArrayBase);
		long size =  booleanArrayBase + (length * booleanArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeCharacterArray(long address) {
		int length = getArrayLength(address, charArrayBase);
		long size = charArrayBase + (length * charArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeShortArray(long address) {
		int length = getArrayLength(address, shortArrayBase);
		long size = shortArrayBase + (length * shortArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeIntegerArray(long address) {
		int length = getArrayLength(address, intArrayBase);
		long size = intArrayBase + (length * intArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeFloatArray(long address) {
		int length = getArrayLength(address, floatArrayBase);
		long size = floatArrayBase + (length * floatArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeLongArray(long address) {
		int length = getArrayLength(address, longArrayBase);
		long size = longArrayBase + (length * longArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeDoubleArray(long address) {
		int length = getArrayLength(address, doubleArrayBase);
		long size = doubleArrayBase + (length * doubleArrayScale);
		buffer.checkCapacitiyAndHandle(size);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
	}
	
	@Override
	public void writeObjectArray(long address) {
		// TODO Implement write object array
	}

}
