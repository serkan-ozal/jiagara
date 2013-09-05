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

package tr.com.serkanozal.jiagara.serialize.field.dma;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedBooleanArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedByteArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedCharacterArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedDoubleArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedFloatArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedIntegerArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedLongArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedObjectArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.array.DmaBasedShortArrayFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.writer.dma.DirectMemoryAccessBasedOutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DirectMemoryAccessBasedDefaultFieldSerializerFactory implements DirectMemoryAccessBasedFieldSerializerFactory {

	@Override
	public <T> FieldSerializer<T, DirectMemoryAccessBasedOutputWriter> createFieldSerializer(Field field) {
		Class<?> fieldClass = field.getClass();
		if (fieldClass.equals(byte.class)) {
			return new DmaBasedByteFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(boolean.class)) {
			return new DmaBasedBooleanFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(char.class)) {
			return new DmaBasedCharacterFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(short.class)) {
			return new DmaBasedShortFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(int.class)) {
			return new DmaBasedIntegerFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(float.class)) {
			return new DmaBasedFloatFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(long.class)) {
			return new DmaBasedLongFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(double.class)) {
			return new DmaBasedDoubleFieldSerializer<T>(field);
		}
		else if (fieldClass.equals(String.class)) {
			return new DmaBasedStringFieldSerializer<T>(field);
		}
		else if (fieldClass.isEnum()) {
			return new DmaBasedEnumFieldSerializer<T>(field);
		}
		else if (fieldClass.isArray()) {
			Class<?> arrayType = fieldClass.getComponentType();
			if (arrayType.equals(byte.class)) {
				return new DmaBasedByteArrayFieldSerializer<T>(field);
			}
			else if (arrayType.equals(boolean.class)) {
				return new DmaBasedBooleanArrayFieldSerializer<T>(field);
			}
			else if (arrayType.equals(char.class)) {
				return new DmaBasedCharacterArrayFieldSerializer<T>(field);
			}
			else if (arrayType.equals(short.class)) {
				return new DmaBasedShortArrayFieldSerializer<T>(field);
			}
			else if (arrayType.equals(int.class)) {
				return new DmaBasedIntegerArrayFieldSerializer<T>(field);
			}
			else if (arrayType.equals(float.class)) {
				return new DmaBasedFloatArrayFieldSerializer<T>(field);
			}
			else if (arrayType.equals(long.class)) {
				return new DmaBasedLongArrayFieldSerializer<T>(field);
			}
			else if (arrayType.equals(double.class)) {
				return new DmaBasedDoubleArrayFieldSerializer<T>(field);
			}
			else {
				return new DmaBasedObjectArrayFieldSerializer<T>(field);
			}	
		}
		else if (Collection.class.isAssignableFrom(fieldClass)) {
			return new DmaBasedCollectionFieldSerializer<T>(field);
		}
		else if (Map.class.isAssignableFrom(fieldClass)) {
			return new DmaBasedMapFieldSerializer<T>(field);
		}
		else {
			return new DmaBasedObjectFieldSerializer<T>(field);
		}
	}

}
