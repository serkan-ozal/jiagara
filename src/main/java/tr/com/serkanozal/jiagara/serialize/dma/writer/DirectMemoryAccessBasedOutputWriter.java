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

import tr.com.serkanozal.jiagara.domain.model.buffer.dma.DirectMemoryAccessBasedWritableBuffer;
import tr.com.serkanozal.jiagara.serialize.writer.BufferedOutputWriter;

/**
 * @author Serkan ÖZAL
 */
public interface DirectMemoryAccessBasedOutputWriter extends BufferedOutputWriter<DirectMemoryAccessBasedWritableBuffer> {

	long getAddress();
	
	void writeByte(Object obj, long offset);
	void writeBoolean(Object obj, long offset);
	void writeCharacter(Object obj, long offset);
	void writeShort(Object obj, long offset);
	void writeInteger(Object obj, long offset);
	void writeFloat(Object obj, long offset);
	void writeLong(Object obj, long offset);
	void writeDouble(Object obj, long offset);
	void writeString(Object obj, long offset);
	void writeAsciiString(Object obj, long offset);
	void writeEnum(Object obj, long offset);
	
	void writeByteArray(Object obj, long offset);
	void writeBooleanArray(Object obj, long offset);
	void writeCharacterArray(Object obj, long offset);
	void writeShortArray(Object obj, long offset);
	void writeIntegerArray(Object obj, long offset);
	void writeFloatArray(Object obj, long offset);
	void writeLongArray(Object obj, long offset);
	void writeDoubleArray(Object obj, long offset);
	
	void writeByte(long address);
	void writeBoolean(long address);
	void writeCharacter(long address);
	void writeShort(long address);
	void writeInteger(long address);
	void writeFloat(long address);
	void writeLong(long address);
	void writeDouble(long address);
	void writeString(long address);
	void writeEnum(long address);
	
	void writeByteArray(long address);
	void writeBooleanArray(long address);
	void writeCharacterArray(long address);
	void writeShortArray(long address);
	void writeIntegerArray(long address);
	void writeFloatArray(long address);
	void writeLongArray(long address);
	void writeDoubleArray(long address);
	
}
