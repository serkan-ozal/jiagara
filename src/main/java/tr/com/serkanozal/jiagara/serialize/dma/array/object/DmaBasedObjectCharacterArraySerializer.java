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

package tr.com.serkanozal.jiagara.serialize.dma.array.object;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.serialize.dma.AbstractDirectMemoryAccessBasedFieldAndDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.util.ReflectionUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedObjectCharacterArraySerializer<T> extends AbstractDirectMemoryAccessBasedFieldAndDataSerializer<T, DirectMemoryAccessBasedOutputWriter> 
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
	
	protected long valueFieldOffset;
	
	@SuppressWarnings("restriction")
	public DmaBasedObjectCharacterArraySerializer(Field field) {
		super(field);
		valueFieldOffset = unsafe.objectFieldOffset(ReflectionUtil.getField(Character.class, "value"));
	}
	
	@SuppressWarnings("restriction")
	public DmaBasedObjectCharacterArraySerializer(Class<T> clazz) {
		super(clazz);
		valueFieldOffset = unsafe.objectFieldOffset(ReflectionUtil.getField(Character.class, "value"));
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		writeArray((Character[])unsafe.getObject(obj, fieldOffset), outputWriter);
	}

	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		writeArray((Character[])obj, outputWriter);
	}
	
	protected void writeArray(Character[] array, DirectMemoryAccessBasedOutputWriter outputWriter) {
		if (array == null) {
			outputWriter.write(SerDeConstants.ARRAY_NULL);
		}
		else {
			outputWriter.writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
			for (Character o : array) {
				outputWriter.write(o);
			}
		}
	}

}
