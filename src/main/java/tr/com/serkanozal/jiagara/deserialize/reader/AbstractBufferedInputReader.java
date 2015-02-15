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

package tr.com.serkanozal.jiagara.deserialize.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;

import tr.com.serkanozal.jiagara.domain.model.buffer.ReadableBuffer;
import tr.com.serkanozal.jiagara.domain.model.buffer.WritableBuffer;
import tr.com.serkanozal.jiagara.exception.DeserializationException;
import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public abstract class AbstractBufferedInputReader<B extends ReadableBuffer> implements BufferedInputReader<B> {

	protected static final ByteOrder nativeByteOrder = ByteOrder.nativeOrder();
	
	protected InputStream is;
	protected B buffer;
	protected byte[] bufferArray;
	protected ByteOrderAwareBufferedInputReader byteOrderAwareBufferedInputReader;
	protected int referenceSize = JvmUtil.getReferenceSize();
	protected int loadedIndex = 0;
	
	public AbstractBufferedInputReader(InputStream is, B buffer) {
		this.is = is;
		this.buffer = buffer;
		this.bufferArray = buffer.getBufferArray();
		buffer.setBufferListener(this);
		if (nativeByteOrder == ByteOrder.BIG_ENDIAN) {
			byteOrderAwareBufferedInputReader = new BigEndianBufferedInputReader();
		}
		else if (nativeByteOrder == ByteOrder.LITTLE_ENDIAN) {
			byteOrderAwareBufferedInputReader = new LittleEndianBufferedInputReader();
		}
		else {
			throw new AssertionError("Unsupported byte order: " + nativeByteOrder);
		}
	}
	
	@Override
	public B getBuffer() {
		return buffer;
	}
	
	@Override
	public int index() {
		return loadedIndex + buffer.getIndex();
	}
	
	@Override
	public void release() {
		load();
		buffer.reset();
	}
	
	@Override
	public void onEndOfBuffer() {
		load();
	}
	
	protected void load() {
		try {
			int readData = is.read(bufferArray, 0, bufferArray.length);
			loadedIndex += readData;
		} 
		catch (IOException e) {
			throw new DeserializationException(e);
		}
	}

	@Override
	public byte readByte() {
		return buffer.readByte();
	}

	@Override
	public boolean readBoolean() {
		return buffer.readByte() == 0x00 ? false : true;
	}

	@Override
	public void write(char value) {
		byteOrderAwareBufferedInputReader.write(value);
	}

	@Override
	public void write(short value) {
		byteOrderAwareBufferedInputReader.write(value);
	}

	@Override
	public void write(int value) {
		byteOrderAwareBufferedInputReader.write(value);
	}

	@Override
	public void write(float value) {
		byteOrderAwareBufferedInputReader.write(value);
	}

	@Override
	public void write(long value) {
		byteOrderAwareBufferedInputReader.write(value);
	}

	@Override
	public void write(double value) {
		byteOrderAwareBufferedInputReader.write(value);
	}

	@Override
	public void write(String value) {
		if (value == null) {
			write(SerDeConstants.STRING_NULL);
		}
		else {
			int length = value.length();
			int size = length * JvmUtil.CHAR_SIZE;
			writeVarInteger(SerDeConstants.STRING_DATA_WITHOUT_OPTIMIZATION, size);
			for (int i = 0; i < length; i++) {
				write(value.charAt(i));
			}
		}	
	}
	
	@Override
	public void writeAscii(String value) {
		if (value == null) {
			write(SerDeConstants.STRING_NULL);
		}
		else {
			int length = value.length();
			int size = length * JvmUtil.BYTE_SIZE;
			writeVarInteger(SerDeConstants.STRING_DATA_WITH_OPTIMIZATION, size);
			for (int i = 0; i < length; i++) {
				write((byte)value.charAt(i));
			}
		}	
	}
	
	@Override
	public byte writeVarInteger(byte additionalCode, int value) {
		if ((value & 0xFFFFFF80) == 0) {
			write((byte)(additionalCode | SerDeConstants.SIZE_1_BYTE));
			write((byte)value);
			return JvmUtil.BYTE_SIZE;
		}
		else if ((value & 0xFFFF8000) == 0) {
			write((byte)(additionalCode | SerDeConstants.SIZE_2_BYTE));
			write((short)value);
			return JvmUtil.SHORT_SIZE;
		}
		else {
			write((byte)(additionalCode | SerDeConstants.SIZE_4_BYTE));
			write(value);
			return JvmUtil.INT_SIZE;
		}
	}
	
	@Override
	public byte writeVarInteger(int value) {
		if ((value & 0xFFFFFF80) == 0) {
			write((byte)(SerDeConstants.SIZE_1_BYTE));
			write((byte)value);
			return JvmUtil.BYTE_SIZE;
			
		}
		else if ((value & 0xFFFF8000) == 0) {
			write((byte)(SerDeConstants.SIZE_2_BYTE));
			write((short)value);
			return JvmUtil.SHORT_SIZE;
		}
		else {
			write((byte)(SerDeConstants.SIZE_4_BYTE));
			write(value);
			return JvmUtil.INT_SIZE;
		}
	}
	
	@Override
	public byte writeVarLong(long value) {
		if ((value & 0xFFFFFFFFFFFFFF80L) == 0) {
			write((byte)(SerDeConstants.SIZE_1_BYTE));
			write((byte)value);
			return JvmUtil.BYTE_SIZE;
			
		}
		else if ((value & 0xFFFFFFFFFFFF8000L) == 0) {
			write((byte)(SerDeConstants.SIZE_2_BYTE));
			write((short)value);
			return JvmUtil.SHORT_SIZE;
		}
		else if ((value & 0xFFFFFFFF80000000L) == 0) {
			write((byte)(SerDeConstants.SIZE_4_BYTE));
			write((int)value);
			return JvmUtil.INT_SIZE;
		}
		else {
			write((byte)(SerDeConstants.SIZE_8_BYTE));
			write(value);
			return JvmUtil.LONG_SIZE;
		}
	}
	
	@Override
	public byte writeVarLong(byte additionalCode, long value) {
		if ((value & 0xFFFFFFFFFFFFFF80L) == 0) {
			write((byte)(SerDeConstants.SIZE_1_BYTE));
			write((byte)value);
			return JvmUtil.BYTE_SIZE;
			
		}
		else if ((value & 0xFFFFFFFFFFFF8000L) == 0) {
			write((byte)(SerDeConstants.SIZE_2_BYTE));
			write((short)value);
			return JvmUtil.SHORT_SIZE;
		}
		else if ((value & 0xFFFFFFFF80000000L) == 0) {
			write((byte)(SerDeConstants.SIZE_4_BYTE));
			write((int)value);
			return JvmUtil.INT_SIZE;
		}
		else {
			write((byte)(SerDeConstants.SIZE_8_BYTE));
			write(value);
			return JvmUtil.LONG_SIZE;
		}
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
	public void write(byte[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (byte value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(boolean[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (boolean value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(char[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (char value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(short[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (short value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(int[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (int value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(float[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (float value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(long[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (long value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(double[] array) {
		if (array == null) { 
			write(SerDeConstants.ARRAY_NULL);
		}
		else {
			writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (double value : array) {
				write(value);
			}
		}	
	}
	
	@Override
	public void writeClassName(Class<?> clazz) {
		write(clazz.getName());
	}
	
	private interface ByteOrderAwareBufferedInputReader {
		
		char readCharacter(char value);
		short readShort(short value);
		int readInteger(int value);
		float readFloat(float value);
		long readLong(long value);
		double readDouble(double value);
		
	}
	
	private class LittleEndianBufferedInputReader implements ByteOrderAwareBufferedInputReader {

		@Override
		public void write(char value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.CHAR_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)value;
			bufferArray[position++] = (byte)(value >> 8);
			buffer.move(position);
		}

		@Override
		public void write(short value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.SHORT_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)value;
			bufferArray[position++] = (byte)(value >> 8);
			buffer.move(position);
		}

		@Override
		public void write(int value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)value;
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)(value >> 16);
			bufferArray[position++] = (byte)(value >> 24);
			buffer.move(position);	
		}

		@Override
		public void write(float value) {
			int rawValue = Float.floatToRawIntBits(value);
			buffer.checkCapacitiyAndHandle(JvmUtil.FLOAT_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)rawValue;
			bufferArray[position++] = (byte)(rawValue >> 8);
			bufferArray[position++] = (byte)(rawValue >> 16);
			bufferArray[position++] = (byte)(rawValue >> 24);
			buffer.move(position);
		}

		@Override
		public void write(long value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.LONG_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)value;
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)(value >> 16);
			bufferArray[position++] = (byte)(value >> 24);
			bufferArray[position++] = (byte)(value >> 32);
			bufferArray[position++] = (byte)(value >> 40);
			bufferArray[position++] = (byte)(value >> 48);
			bufferArray[position++] = (byte)(value >> 56);
			buffer.move(position);
		}

		@Override
		public void write(double value) {
			long rawValue = Double.doubleToRawLongBits(value);
			buffer.checkCapacitiyAndHandle(JvmUtil.DOUBLE_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)value;
			bufferArray[position++] = (byte)(rawValue >> 8);
			bufferArray[position++] = (byte)(rawValue >> 16);
			bufferArray[position++] = (byte)(rawValue >> 24);
			bufferArray[position++] = (byte)(rawValue >> 32);
			bufferArray[position++] = (byte)(rawValue >> 40);
			bufferArray[position++] = (byte)(rawValue >> 48);
			bufferArray[position++] = (byte)(rawValue >> 56);
			buffer.move(position);
		}
		
	}
	
	private class BigEndianBufferedInputReader implements ByteOrderAwareBufferedInputReader {

		@Override
		public void write(char value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.CHAR_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.move(position);
		}

		@Override
		public void write(short value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.SHORT_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.move(position);
		}

		@Override
		public void write(int value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 24);
			bufferArray[position++] = (byte)(value >> 16);
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.move(position);	
		}

		@Override
		public void write(float value) {
			int rawValue = Float.floatToRawIntBits(value);
			buffer.checkCapacitiyAndHandle(JvmUtil.FLOAT_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(rawValue >> 24);
			bufferArray[position++] = (byte)(rawValue >> 16);
			bufferArray[position++] = (byte)(rawValue >> 8);
			bufferArray[position++] = (byte)rawValue;
			buffer.move(position);
		}

		@Override
		public void write(long value) {
			buffer.checkCapacitiyAndHandle(JvmUtil.LONG_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(value >> 56);
			bufferArray[position++] = (byte)(value >> 48);
			bufferArray[position++] = (byte)(value >> 40);
			bufferArray[position++] = (byte)(value >> 32);
			bufferArray[position++] = (byte)(value >> 24);
			bufferArray[position++] = (byte)(value >> 16);
			bufferArray[position++] = (byte)(value >> 8);
			bufferArray[position++] = (byte)value;
			buffer.move(position);
		}

		@Override
		public void write(double value) {
			long rawValue = Double.doubleToRawLongBits(value);
			buffer.checkCapacitiyAndHandle(JvmUtil.DOUBLE_SIZE);
			int position = buffer.getIndex();
			bufferArray[position++] = (byte)(rawValue >> 56);
			bufferArray[position++] = (byte)(rawValue >> 48);
			bufferArray[position++] = (byte)(rawValue >> 40);
			bufferArray[position++] = (byte)(rawValue >> 32);
			bufferArray[position++] = (byte)(rawValue >> 24);
			bufferArray[position++] = (byte)(rawValue >> 16);
			bufferArray[position++] = (byte)(rawValue >> 8);
			bufferArray[position++] = (byte)value;
			buffer.move(position);
		}
		
	}

}
