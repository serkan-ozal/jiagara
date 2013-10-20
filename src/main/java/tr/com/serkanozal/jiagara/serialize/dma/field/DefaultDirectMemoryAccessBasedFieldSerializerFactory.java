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

package tr.com.serkanozal.jiagara.serialize.dma.field;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedBooleanArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedByteArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedCharacterArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedDoubleArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedFloatArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedIntegerArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedLongArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedObjectArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.DmaBasedShortArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectBooleanSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectByteSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectCharacterSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectDoubleSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectFloatSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectIntegerSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectLongSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.object.DmaBasedObjectShortSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveBooleanSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveByteSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveCharacterSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveDoubleSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveFloatSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveIntegerSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveLongSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.primitive.DmaBasedPrimitiveShortSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedClassSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedCollectionSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedDateSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedEnumSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedMapSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedStringSerializer;

/**
 * @author Serkan ÖZAL
 */
public class DefaultDirectMemoryAccessBasedFieldSerializerFactory implements DirectMemoryAccessBasedFieldSerializerFactory {

	@Override
	public <T> DirectMemoryAccessBasedFieldSerializer<T> createFieldSerializer(Field field) {
		Class<?> fieldClass = field.getType();
		if (fieldClass.equals(byte.class)) {
			return new DmaBasedPrimitiveByteSerializer<T>(field);
		}
		else if (fieldClass.equals(boolean.class)) {
			return new DmaBasedPrimitiveBooleanSerializer<T>(field);
		}
		else if (fieldClass.equals(char.class)) {
			return new DmaBasedPrimitiveCharacterSerializer<T>(field);
		}
		else if (fieldClass.equals(short.class)) {
			return new DmaBasedPrimitiveShortSerializer<T>(field);
		}
		else if (fieldClass.equals(int.class)) {
			return new DmaBasedPrimitiveIntegerSerializer<T>(field);
		}
		else if (fieldClass.equals(float.class)) {
			return new DmaBasedPrimitiveFloatSerializer<T>(field);
		}
		else if (fieldClass.equals(long.class)) {
			return new DmaBasedPrimitiveLongSerializer<T>(field);
		}
		else if (fieldClass.equals(double.class)) {
			return new DmaBasedPrimitiveDoubleSerializer<T>(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		if (fieldClass.equals(Byte.class)) {
			return new DmaBasedObjectByteSerializer<T>(field);
		}
		else if (fieldClass.equals(Boolean.class)) {
			return new DmaBasedObjectBooleanSerializer<T>(field);
		}
		else if (fieldClass.equals(Character.class)) {
			return new DmaBasedObjectCharacterSerializer<T>(field);
		}
		else if (fieldClass.equals(Short.class)) {
			return new DmaBasedObjectShortSerializer<T>(field);
		}
		else if (fieldClass.equals(Integer.class)) {
			return new DmaBasedObjectIntegerSerializer<T>(field);
		}
		else if (fieldClass.equals(Float.class)) {
			return new DmaBasedObjectFloatSerializer<T>(field);
		}
		else if (fieldClass.equals(Long.class)) {
			return new DmaBasedObjectLongSerializer<T>(field);
		}
		else if (fieldClass.equals(Double.class)) {
			return new DmaBasedObjectDoubleSerializer<T>(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		else if (fieldClass.isArray()) {
			Class<?> arrayType = fieldClass.getComponentType();
			if (arrayType.equals(byte.class)) {
				return new DmaBasedByteArraySerializer<T>(field);
			}
			else if (arrayType.equals(boolean.class)) {
				return new DmaBasedBooleanArraySerializer<T>(field);
			}
			else if (arrayType.equals(char.class)) {
				return new DmaBasedCharacterArraySerializer<T>(field);
			}
			else if (arrayType.equals(short.class)) {
				return new DmaBasedShortArraySerializer<T>(field);
			}
			else if (arrayType.equals(int.class)) {
				return new DmaBasedIntegerArraySerializer<T>(field);
			}
			else if (arrayType.equals(float.class)) {
				return new DmaBasedFloatArraySerializer<T>(field);
			}
			else if (arrayType.equals(long.class)) {
				return new DmaBasedLongArraySerializer<T>(field);
			}
			else if (arrayType.equals(double.class)) {
				return new DmaBasedDoubleArraySerializer<T>(field);
			}
			else {
				return new DmaBasedObjectArraySerializer<T>(field);
			}	
		}
		
		//////////////////////////////////////////////////////////////////////////////
		else if (fieldClass.isEnum()) {
			return new DmaBasedEnumSerializer<T>(field);
		}
		else if (fieldClass.equals(String.class)) {
			return new DmaBasedStringSerializer<T>(field);
		}
		else if (Date.class.isAssignableFrom(fieldClass)) {
			return new DmaBasedDateSerializer<T>(field);
		}
		else if (Collection.class.isAssignableFrom(fieldClass)) {
			return new DmaBasedCollectionSerializer<T>(field);
		}
		else if (Map.class.isAssignableFrom(fieldClass)) {
			return new DmaBasedMapSerializer<T>(field);
		}
		else if (Class.class.isAssignableFrom(fieldClass)) {
			return new DmaBasedClassSerializer<T>(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		else {
			return new DmaBasedObjectSerializer<T>(field);
		}
	}

}
