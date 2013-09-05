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
		// TODO Implement write string
	}
	
	@Override
	public void write(Enum<?> value) {
		// TODO Implement write enum
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
		for (byte value : array) {
			write(value);
		}
	}

	@Override
	public void write(boolean[] array) {
		for (boolean value : array) {
			write(value);
		}
	}

	@Override
	public void write(char[] array) {
		for (char value : array) {
			write(value);
		}
	}

	@Override
	public void write(short[] array) {
		for (short value : array) {
			write(value);
		}
	}

	@Override
	public void write(int[] array) {
		for (int value : array) {
			write(value);
		}
	}

	@Override
	public void write(float[] array) {
		for (float value : array) {
			write(value);
		}
	}

	@Override
	public void write(long[] array) {
		for (long value : array) {
			write(value);
		}
	}

	@Override
	public void write(double[] array) {
		for (double value : array) {
			write(value);
		}
	}

	@Override
	public void write(Object[] array) {
		for (Object value : array) {
			write(value);
		}
	}
	
}
