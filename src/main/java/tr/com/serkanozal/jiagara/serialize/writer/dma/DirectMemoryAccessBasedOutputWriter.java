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

import tr.com.serkanozal.jiagara.domain.model.buffer.dma.DirectMemoryAccessBasedBuffer;
import tr.com.serkanozal.jiagara.serialize.writer.BufferedOutputWriter;

/**
 * @author Serkan ÖZAL
 */
public interface DirectMemoryAccessBasedOutputWriter extends BufferedOutputWriter<DirectMemoryAccessBasedBuffer> {

	long getAddress();
	
	void writeByteFrom(Object obj, long offset);
	void writeBoolean(Object obj, long offset);
	void writeCharacter(Object obj, long offset);
	void writeShort(Object obj, long offset);
	void writeInteger(Object obj, long offset);
	void writeFloat(Object obj, long offset);
	void writeLong(Object obj, long offset);
	void writeDouble(Object obj, long offset);
	void writeString(Object obj, long offset);
	void writeBytes(Object obj, long offset);
	
	void writeByteFrom(long address);
	void writeBoolean(long address);
	void writeCharacter(long address);
	void writeShort(long address);
	void writeInteger(long address);
	void writeFloat(long address);
	void writeLong(long address);
	void writeDouble(long address);
	void writeString(long address);
	void writeBytes(long address);
	
}
