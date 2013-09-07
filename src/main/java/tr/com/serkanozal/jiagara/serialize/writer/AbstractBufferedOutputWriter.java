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

import tr.com.serkanozal.jiagara.domain.model.buffer.Buffer;
import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public abstract class AbstractBufferedOutputWriter<B extends Buffer> implements BufferedOutputWriter<B> {

	protected OutputStream os;
	protected B buffer;
	protected byte[] bufferArray;
	
	public AbstractBufferedOutputWriter(OutputStream os, B buffer) {
		this.os = os;
		this.buffer = buffer;
		this.bufferArray = buffer.getBufferArray();
	}
	
	@Override
	public B getBuffer() {
		return buffer;
	}
	
	@Override
	public void release() {
		doFlush();
		buffer.reset();
	}
	
	@Override
	public void doFlush() {
		try {
			os.write(bufferArray, 0, buffer.getIndex());
		} 
		catch (IOException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void writeNull() {
		int referenceSize = JvmUtil.getReferenceSize();
		buffer.checkCapacitiyAndHandle(referenceSize);
		for (int i = 0; i < referenceSize; i++) {
			buffer.pushByte((byte)0x00);
		}	
	}
	
	@Override
	public void write(byte value) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BYTE_SIZE); 
		buffer.pushByte(value);
	}

	@Override
	public void write(boolean value) {
		buffer.checkCapacitiyAndHandle(JvmUtil.BOOLEAN_SIZE);
		buffer.pushByte(value ? (byte)0x01 : (byte)0x00);
	}

	@Override
	public void write(char value) {
		buffer.checkCapacitiyAndHandle(JvmUtil.CHAR_SIZE);
		int position = buffer.getIndex();
		bufferArray[position++] = (byte)(value >> 8);
		bufferArray[position++] = (byte)value;
		buffer.forward(position);
	}

	@Override
	public void write(short value) {
		buffer.checkCapacitiyAndHandle(JvmUtil.SHORT_SIZE);
		int position = buffer.getIndex();
		bufferArray[position++] = (byte)(value >> 8);
		bufferArray[position++] = (byte)value;
		buffer.forward(position);
	}

	@Override
	public void write(int value) {
		buffer.checkCapacitiyAndHandle(JvmUtil.INT_SIZE);
		int position = buffer.getIndex();
		bufferArray[position++] = (byte)(value >> 24);
		bufferArray[position++] = (byte)(value >> 16);
		bufferArray[position++] = (byte)(value >> 8);
		bufferArray[position++] = (byte)value;
		buffer.forward(position);	
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
		buffer.forward(position);
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
		buffer.forward(position);
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
		buffer.forward(position);
	}

	@Override
	public void write(String value) {
		if (value == null) {
			write(SerDeConstants.NULL_STRING_LENGTH);
		}
		else {
			int length = value.length();
			int size = (length * JvmUtil.CHAR_SIZE + JvmUtil.INT_SIZE);
			buffer.checkCapacitiyAndHandle(size);
			write(length);
			for (int i = 0; i < length; i++) {
				write(value.charAt(i));
			}
		}	
	}
	
	@Override
	public void write(Enum<?> value) {
		if (value == null) {
			write(SerDeConstants.NULL_ENUM_ORDINAL);
		}
		else {
			write(value.ordinal());
		}
	}

	@Override
	public void write(Object value) {
		// TODO Implement write object
	}
	
	@Override
	public void writeTyped(Object value) {
		// TODO Implement write typed object
	}
	
	@Override
	public void write(byte[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.BYTE_SIZE);
			for (byte value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(boolean[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.BOOLEAN_SIZE);
			for (boolean value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(char[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.CHAR_SIZE);
			for (char value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(short[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.SHORT_SIZE);
			for (short value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(int[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.INT_SIZE);
			for (int value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(float[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.FLOAT_SIZE);
			for (float value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(long[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.LONG_SIZE);
			for (long value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(double[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			buffer.checkCapacitiyAndHandle(array.length * JvmUtil.DOUBLE_SIZE);
			for (double value : array) {
				write(value);
			}
		}	
	}

	@Override
	public void write(Object[] array) {
		if (array == null) { 
			write(SerDeConstants.NULL_ARRAY_LENGTH);
		}
		else {
			// TODO Implement write object array
		}
	}
	
}
