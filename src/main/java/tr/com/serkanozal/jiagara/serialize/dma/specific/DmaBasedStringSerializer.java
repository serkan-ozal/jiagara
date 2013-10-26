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

package tr.com.serkanozal.jiagara.serialize.dma.specific;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.serialize.dma.AbstractDirectMemoryAccessBasedFieldAndDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedStringSerializer<T> extends AbstractDirectMemoryAccessBasedFieldAndDataSerializer<T, DirectMemoryAccessBasedOutputWriter>  
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
		
	private static boolean OPTIMIZATION_ENABLED = true;
	
	private StringSerializer stringSerializer;
	
	public DmaBasedStringSerializer(Field field) {
		super(field);
		if (OPTIMIZATION_ENABLED) {
			stringSerializer = new OptimizedStringSerializer();
		}
		else {
			stringSerializer = new NonOptimizedStringSerializer();
		}
	}
	
	public DmaBasedStringSerializer(Class<T> clazz) {
		super(clazz);
		if (OPTIMIZATION_ENABLED) {
			stringSerializer = new OptimizedStringSerializer();
		}
		else {
			stringSerializer = new NonOptimizedStringSerializer();
		}
	}
	
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		stringSerializer.serializeField(obj, fieldOffset, outputWriter);
	}

	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		stringSerializer.serializeData((String)obj, outputWriter);
	}
	
	private boolean isAscii(String s) {
		if (s == null) {
			return false;
		}
		int charCount = s.length();
		for (int i = 0; i < charCount; i++) {
            int c = s.charAt(i);
            if (c > 127) {
            	return false;
            }
		}
		return true;
	}
	
	private interface StringSerializer {
		
		void serializeField(Object obj, long fieldOffset, DirectMemoryAccessBasedOutputWriter outputWriter);
		void serializeData(String str, DirectMemoryAccessBasedOutputWriter outputWriter);
		
	}
	
	private class NonOptimizedStringSerializer implements StringSerializer {

		@Override
		public void serializeField(Object obj, long fieldOffset, DirectMemoryAccessBasedOutputWriter outputWriter) {
			outputWriter.writeString(obj, fieldOffset);
		}
		
		@Override
		public void serializeData(String str, DirectMemoryAccessBasedOutputWriter outputWriter) {
			outputWriter.write(str);
		}
		
	}
	
	private class OptimizedStringSerializer implements StringSerializer {

		@SuppressWarnings("restriction")
		@Override
		public void serializeField(Object obj, long fieldOffset, DirectMemoryAccessBasedOutputWriter outputWriter) {
			serializeData((String)unsafe.getObject(obj, fieldOffset), outputWriter);
		}
		
		@Override
		public void serializeData(String str, DirectMemoryAccessBasedOutputWriter outputWriter) {
			boolean isAscii = isAscii(str);
			if (isAscii) {
				outputWriter.writeAscii(str);
			}
			else {
				outputWriter.write(str);
			}
		}
		
	}
	
}
