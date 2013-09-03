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

	void writeByte(byte value);
	void writeBoolean(boolean value);
	void writeCharacter(char value);
	void writeShort(short value);
	void writeInteger(int value);
	void writeFloat(float value);
	void writeLong(long value);
	void writeDouble(double value);
	void writeString(String value);
	void writeBytes(byte[] value);
	
}
