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

package tr.com.serkanozal.jiagara.serialize.dma.object;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.serialize.dma.AbstractDirectMemoryAccessBasedSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.util.ReflectionUtil;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedObjectIntegerSerializer<T> extends AbstractDirectMemoryAccessBasedSerializer<T, DirectMemoryAccessBasedOutputWriter> 
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
	
	private long valueFieldOffset;
	
	@SuppressWarnings("restriction")
	public DmaBasedObjectIntegerSerializer(Field field) {
		super(field);
		valueFieldOffset = unsafe.objectFieldOffset(ReflectionUtil.getField(Integer.class, "value"));
	}
	
	public DmaBasedObjectIntegerSerializer(Class<T> clazz) {
		super(clazz);
	}

	@SuppressWarnings("restriction")
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Integer intField = unsafe.getInt(obj, fieldOffset);
		if (intField == null) {
			outputWriter.writeNull();
		}
		else {
			outputWriter.writeInteger(intField, valueFieldOffset);
		}	
	}

	@Override
	public void serializeData(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		outputWriter.writeInteger((Integer)obj, valueFieldOffset);
	}

}