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

package tr.com.serkanozal.jiagara.serialize.field.dma.array;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.serialize.field.dma.AbstractDmaBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.writer.dma.DirectMemoryAccessBasedOutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedBooleanArrayFieldSerializer<T> extends AbstractDmaBasedFieldSerializer<T, DirectMemoryAccessBasedOutputWriter> 
		implements DirectMemoryAccessBasedFieldSerializer<T> {
	
	public DmaBasedBooleanArrayFieldSerializer(Field field) {
		super(field);
	}
	
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		outputWriter.writeBooleanArray(obj, fieldOffset);
	}

}
