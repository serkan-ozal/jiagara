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

package tr.com.serkanozal.jiagara.serialize.dma.writer;

import java.io.OutputStream;
import java.nio.ByteOrder;

import tr.com.serkanozal.jiagara.domain.builder.buffer.dma.DirectMemoryAccessBasedWritableBufferBuilder;
import tr.com.serkanozal.jiagara.domain.model.buffer.dma.DirectMemoryAccessBasedWritableBuffer;
import tr.com.serkanozal.jiagara.serialize.writer.AbstractBufferedOutputWriter;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import tr.com.serkanozal.jiagara.util.ReflectionUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;
import sun.misc.Unsafe;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("restriction")
public class DirectMemoryAccessBasedOutputWriterImpl extends AbstractBufferedOutputWriter<DirectMemoryAccessBasedWritableBuffer> 
		implements DirectMemoryAccessBasedOutputWriter {

	private static Unsafe unsafe;
	private static int byteArrayBase;
	private static int byteArrayScale;
	private static int booleanArrayBase;
	private static int booleanArrayScale;
	private static int charArrayBase;
	private static int charArrayScale;
	private static int shortArrayBase;
	private static int shortArrayScale;
	private static int intArrayBase;
	private static int intArrayScale;
	private static int floatArrayBase;
	private static int floatArrayScale;
	private static int longArrayBase;
	private static int longArrayScale;
	private static int doubleArrayBase;
	private static int doubleArrayScale;
	private static int objectArrayBase;
	@SuppressWarnings("unused")
	private static int objectArrayScale;
	private static long countOffsetInString;
	private static long valueArrayOffsetInString;
	@SuppressWarnings("unused")
	private static long ordinalOffsetInEnum;
	private static int arrayLengthSize;
	private static long byteArrayCopyStartOffset;
	private static long booleanArrayCopyStartOffset;
	private static long charArrayCopyStartOffset;
	private static long shortArrayCopyStartOffset;
	private static long intArrayCopyStartOffset;
	private static long floatArrayCopyStartOffset;
	private static long longArrayCopyStartOffset;
	private static long doubleArrayCopyStartOffset;
	@SuppressWarnings("unused")
	private static long objectArrayCopyStartOffset;
	private static long nameOffsetInClass;
	
	private long startAddress;

	static {
		init();
	}
	
	private static void init() {
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
		valueArrayOffsetInString = unsafe.objectFieldOffset(ReflectionUtil.getField(String.class, "value"));
		countOffsetInString = unsafe.objectFieldOffset(ReflectionUtil.getField(String.class, "count"));
		//////////////////////////////////////////////////////////////////	
		ordinalOffsetInEnum = unsafe.objectFieldOffset(ReflectionUtil.getField(Enum.class, "ordinal"));
		//////////////////////////////////////////////////////////////////	
		arrayLengthSize = JvmUtil.arrayLengthSize();
		//////////////////////////////////////////////////////////////////		
		byteArrayCopyStartOffset = byteArrayBase - arrayLengthSize;
		booleanArrayCopyStartOffset = booleanArrayBase - arrayLengthSize;
		charArrayCopyStartOffset = charArrayBase - arrayLengthSize;
		shortArrayCopyStartOffset = shortArrayBase - arrayLengthSize;
		intArrayCopyStartOffset = intArrayBase - arrayLengthSize;
		floatArrayCopyStartOffset = floatArrayBase - arrayLengthSize;
		longArrayCopyStartOffset = longArrayBase - arrayLengthSize;
		doubleArrayCopyStartOffset = doubleArrayBase - arrayLengthSize;
		objectArrayCopyStartOffset = objectArrayBase - arrayLengthSize;
		//////////////////////////////////////////////////////////////////		
		nameOffsetInClass = unsafe.objectFieldOffset(ReflectionUtil.getField(Class.class, "name"));
	}
	
	public DirectMemoryAccessBasedOutputWriterImpl(OutputStream os) {
		this(os, new DirectMemoryAccessBasedWritableBufferBuilder().build());
	}
	
	public DirectMemoryAccessBasedOutputWriterImpl(OutputStream os, DirectMemoryAccessBasedWritableBuffer buffer) {
		super(os, buffer);
		startAddress = JvmUtil.getArrayBaseAddress(bufferArray, byte.class);
	}
	
	@Override
	public long getAddress() {
		return startAddress;
	}

	@Override
	public void writeByte(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BYTE_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.BYTE_SIZE);
		buffer.forward(JvmUtil.BYTE_SIZE);
	}

	@Override
	public void writeBoolean(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BOOLEAN_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.BOOLEAN_SIZE);
		buffer.forward(JvmUtil.BOOLEAN_SIZE);
	}

	@Override
	public void writeCharacter(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.CHAR_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.CHAR_SIZE);
		buffer.forward(JvmUtil.CHAR_SIZE);
	}

	@Override
	public void writeShort(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.SHORT_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.SHORT_SIZE);
		buffer.forward(JvmUtil.SHORT_SIZE);
	}

	@Override
	public void writeInteger(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.INT_SIZE);
		buffer.forward(JvmUtil.INT_SIZE);
	}

	@Override
	public void writeFloat(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.FLOAT_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.FLOAT_SIZE);
		buffer.forward(JvmUtil.FLOAT_SIZE);
	}

	@Override
	public void writeLong(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.LONG_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.LONG_SIZE);
		buffer.forward(JvmUtil.LONG_SIZE);
	}

	@Override
	public void writeDouble(Object obj, long offset) {
		buffer.checkCapacitiyAndHandle(JvmUtil.DOUBLE_SIZE);
		unsafe.copyMemory(obj, offset, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.DOUBLE_SIZE);
		buffer.forward(JvmUtil.DOUBLE_SIZE);
	}

	@Override
	public void writeString(Object obj, long offset) {
		write((String)unsafe.getObject(obj, offset));
	}
	
	@Override
	public void writeAsciiString(Object obj, long offset) {
		writeAscii((String)unsafe.getObject(obj, offset));
	}
	
	@Override
	public void write(String value) {
		if (value == null) {
			buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
			write(SerDeConstants.NULL_STRING_LENGTH);
			buffer.forward(JvmUtil.INT_SIZE);
		}
		else {
			int length = value.length();
			int size = (length * JvmUtil.CHAR_SIZE);
			int totalSize = size +  JvmUtil.INT_SIZE;
			buffer.checkCapacitiyAndHandle(totalSize);
			totalSize = size + writeVarInteger(SerDeConstants.STRING_DATA_WITHOUT_OPTIMIZATION, size);
			char[] valueArray = (char[])unsafe.getObject(value, valueArrayOffsetInString);
			unsafe.copyMemory(	valueArray, charArrayBase, 
								bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeAscii(String value) {
		if (value == null) {
			write(SerDeConstants.NULL_STRING_LENGTH);
		}
		else {
			write(SerDeConstants.STRING_DATA_WITH_OPTIMIZATION);
			int length = value.length();
			int size = (length * JvmUtil.BYTE_SIZE);
			long totalSize = size +  JvmUtil.INT_SIZE;
			buffer.checkCapacitiyAndHandle(totalSize);
			totalSize = size + writeVarInteger(SerDeConstants.STRING_DATA_WITH_OPTIMIZATION, size);
			char[] valueArray = (char[])unsafe.getObject(value, valueArrayOffsetInString);
			if (nativeByteOrder == ByteOrder.BIG_ENDIAN) {
				for (int i = 0; i < length; i++) {
					unsafe.putByte( bufferArray, (long)(byteArrayBase + (i * byteArrayScale)),
									unsafe.getByte(valueArray, (long)(charArrayBase + (i << 1) + 1)));
				}
			}
			else {
				for (int i = 0; i < length; i++) {
					unsafe.putByte( bufferArray, (long)(byteArrayBase + (i * byteArrayScale)),
									unsafe.getByte(valueArray, (long)(charArrayBase + (i << 1))));
				}
			}
			buffer.forward(size);
		}	
	}

	@Override
	public void writeEnum(Object obj, long offset) {
		write((Enum<?>)unsafe.getObject(obj, offset));
		/*
		buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
		unsafe.copyMemory(e, ordinalOffsetInEnum, bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), JvmUtil.INT_SIZE);
		buffer.forward(JvmUtil.INT_SIZE);
		*/
	}
	
	@Override
	public void write(Enum<?> value) {
		if (value == null) {
			write(SerDeConstants.NULL_ENUM_ORDINAL);
		}
		else {
			writeVarInteger(value.ordinal());
		}
	}

	@Override
	public void writeByteArray(Object obj, long offset) {
		byte[] array = (byte[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * byteArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, byteArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeBooleanArray(Object obj, long offset) {
		boolean[] array = (boolean[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * booleanArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, booleanArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeCharacterArray(Object obj, long offset) {
		char[] array = (char[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * charArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, charArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeShortArray(Object obj, long offset) {
		short[] array = (short[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * shortArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, shortArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeIntegerArray(Object obj, long offset) {
		int[] array = (int[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * intArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, intArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeFloatArray(Object obj, long offset) {
		float[] array = (float[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * floatArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, floatArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeLongArray(Object obj, long offset) {
		long[] array = (long[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * longArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, longArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeDoubleArray(Object obj, long offset) {
		double[] array = (double[])unsafe.getObject(obj, offset);
		if (array == null) {
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			long size = arrayLengthSize + (array.length * doubleArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(array, doubleArrayCopyStartOffset, 
							  bufferArray, byteArrayBase + (buffer.getIndex() * byteArrayScale), 
							  size);
			buffer.forward(size);
		}	
	}

	@Override
	public void writeByte(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BYTE_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.BYTE_SIZE);
		buffer.forward(JvmUtil.BYTE_SIZE);
	}

	@Override
	public void writeBoolean(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BOOLEAN_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.BOOLEAN_SIZE);
		buffer.forward(JvmUtil.BOOLEAN_SIZE);
	}

	@Override
	public void writeCharacter(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.CHAR_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.CHAR_SIZE);
		buffer.forward(JvmUtil.CHAR_SIZE);
	}

	@Override
	public void writeShort(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.SHORT_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.SHORT_SIZE);
		buffer.forward(JvmUtil.SHORT_SIZE);
	}

	@Override
	public void writeInteger(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.INT_SIZE);
		buffer.forward(JvmUtil.INT_SIZE);
	}

	@Override
	public void writeFloat(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.FLOAT_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.FLOAT_SIZE);
		buffer.forward(JvmUtil.FLOAT_SIZE);
	}
	
	@Override
	public void writeLong(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.LONG_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.LONG_SIZE);
		buffer.forward(JvmUtil.LONG_SIZE);
	}

	@Override
	public void writeDouble(long address) {
		buffer.checkCapacitiyAndHandle(JvmUtil.DOUBLE_SIZE);
		unsafe.copyMemory(address, startAddress + buffer.getIndex(), JvmUtil.DOUBLE_SIZE);
		buffer.forward(JvmUtil.DOUBLE_SIZE);
	}

	@Override
	public void writeString(long address) {
		if (address == 0) { // Null object
			buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
			write(SerDeConstants.NULL_STRING_LENGTH);
			buffer.forward(JvmUtil.INT_SIZE);
		}
		else {
			write(SerDeConstants.STRING_DATA_WITHOUT_OPTIMIZATION);
			int length = unsafe.getInt(address + countOffsetInString); 
			int size = (length * JvmUtil.CHAR_SIZE);
			int totalSize = size +  JvmUtil.INT_SIZE;
			buffer.checkCapacitiyAndHandle(totalSize);
			totalSize = size + writeVarInteger(SerDeConstants.STRING_DATA_WITHOUT_OPTIMIZATION, size);
			long valueArrayAddress = unsafe.getAddress(address + valueArrayOffsetInString);
			unsafe.copyMemory(valueArrayAddress + charArrayBase, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}

	@Override
	public void writeEnum(long address) {
		if (address == 0) { // Null enum
			write(SerDeConstants.NULL_ENUM_ORDINAL);
		}
		else {
			write((Enum<?>)JvmUtil.getObject(address));
			/*
			buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
			unsafe.copyMemory(address + ordinalOffsetInEnum, startAddress + buffer.getIndex(), JvmUtil.INT_SIZE);
			buffer.forward(JvmUtil.INT_SIZE);
			*/
		}
	}

	private int getArrayLength(long arrayStartAddress, int arrayBaseOffset) {
		long arrayIndexStartAddress = arrayStartAddress + arrayBaseOffset;
		switch (JvmUtil.getReferenceSize()) {
			case JvmUtil.ADDRESSING_4_BYTE:
				return unsafe.getInt(arrayIndexStartAddress - arrayLengthSize);
			case JvmUtil.ADDRESSING_8_BYTE:
				return (int)unsafe.getLong(arrayIndexStartAddress - arrayLengthSize);
			default:
				throw new AssertionError("Unsupported reference size: " + JvmUtil.getReferenceSize()); 
		}
	}

	@Override
	public void writeByteArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, byteArrayBase);
			long size = byteArrayBase + (length * byteArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeBooleanArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, booleanArrayBase);
			long size =  booleanArrayBase + (length * booleanArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeCharacterArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, charArrayBase);
			long size = charArrayBase + (length * charArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeShortArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, shortArrayBase);
			long size = shortArrayBase + (length * shortArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeIntegerArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, intArrayBase);
			long size = intArrayBase + (length * intArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeFloatArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, floatArrayBase);
			long size = floatArrayBase + (length * floatArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeLongArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, longArrayBase);
			long size = longArrayBase + (length * longArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeDoubleArray(long address) {
		if (address == 0) { // Null array
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			int length = getArrayLength(address, doubleArrayBase);
			long size = doubleArrayBase + (length * doubleArrayScale);
			buffer.checkCapacitiyAndHandle(size);
			unsafe.copyMemory(address, startAddress + buffer.getIndex(), size);
			buffer.forward(size);
		}	
	}
	
	@Override
	public void writeClassName(Class<?> clazz) {
		writeString(clazz, nameOffsetInClass);
	}

}
