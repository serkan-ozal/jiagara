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

/**
 * @author Serkan ÖZAL
 */
public interface OutputWriter {

	void release();
	
	void writeNull();
	
	void write(byte value);
	void write(boolean value);
	void write(char value);
	void write(short value);
	void write(int value);
	void write(float value);
	void write(long value);
	void write(double value);
	void write(String value);
	void write(Enum<?> value);
	void write(Object value);
	void writeTyped(Object value);
	
	void write(byte[] array);
	void write(boolean[] array);
	void write(char[] array);
	void write(short[] array);
	void write(int[] array);
	void write(float[] array);
	void write(long[] array);
	void write(double[] array);
	void write(Object[] array);
	
}
