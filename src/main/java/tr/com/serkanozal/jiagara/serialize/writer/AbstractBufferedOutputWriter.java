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

package tr.com.serkanozal.jiagara.serialize.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

import tr.com.serkanozal.jiagara.domain.model.buffer.WritableBuffer;
import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public abstract class AbstractBufferedOutputWriter<B extends WritableBuffer> implements BufferedOutputWriter<B> {

	protected static final ByteOrder nativeByteOrder = ByteOrder.nativeOrder();
	
	protected OutputStream os;
	protected B buffer;
	protected byte[] bufferArray;
	protected ByteOrderAwareBufferedOutputWriter byteOrderAwareBufferedOutputWriter;
	protected int referenceSize = JvmUtil.getReferenceSize();
	protected int flushedIndex = 0;
	
	public AbstractBufferedOutputWriter(OutputStream os, B buffer) {
		this.os = os;
		this.buffer = buffer;
		this.bufferArray = buffer.getBufferArray();
		buffer.setBufferListener(this);
		if (nativeByteOrder == ByteOrder.BIG_ENDIAN) {
			byteOrderAwareBufferedOutputWriter = new BigEndianBufferedOutputWriter();
		}
		else if (nativeByteOrder == ByteOrder.LITTLE_ENDIAN) {
			byteOrderAwareBufferedOutputWriter = new LittleEndianBufferedOutputWriter();
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
		return flushedIndex + buffer.getIndex();
	}
	
	@Override
	public void release() {
		flush();
		buffer.reset();
	}
	
	@Override
	public void onEndOfBuffer() {
		flush();
	}
	
	protected void flush() {
		try {
			os.write(bufferArray, 0, buffer.getIndex());
			flushedIndex += buffer.getIndex();
		} 
		catch (IOException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void writeNull() {
		buffer.checkCapacitiyAndHandle(referenceSize);
		for (int i = 0; i < referenceSize; i++) {
			buffer.writeByte(SerDeConstants.NULL);
		}	
	}
	
	@Override
	public void write(byte value) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BYTE_SIZE); 
		buffer.writeByte(value);
	}

	@Override
	public void write(boolean value) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BOOLEAN_SIZE);
		buffer.writeByte(value ? (byte)0x01 : (byte)0x00);
	}

	@Override
	public void write(char value) {
		byteOrderAwareBufferedOutputWriter.write(value);
	}

	@Override
	public void write(short value) {
		byteOrderAwareBufferedOutputWriter.write(value);
	}

	@Override
	public void write(int value) {
		byteOrderAwareBufferedOutputWriter.write(value);
	}

	@Override
	public void write(float value) {
		byteOrderAwareBufferedOutputWriter.write(value);
	}

	@Override
	public void write(long value) {
		byteOrderAwareBufferedOutputWriter.write(value);
	}

	@Override
	public void write(double value) {
		byteOrderAwareBufferedOutputWriter.write(value);
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
	
	private interface ByteOrderAwareBufferedOutputWriter {
		
		void write(char value);
		void write(short value);
		void write(int value);
		void write(float value);
		void write(long value);
		void write(double value);
		
	}
	
	private class LittleEndianBufferedOutputWriter implements ByteOrderAwareBufferedOutputWriter {

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
	
	private class BigEndianBufferedOutputWriter implements ByteOrderAwareBufferedOutputWriter {

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
