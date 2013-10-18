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

package tr.com.serkanozal.jiagara.serialize.dma.data;

import java.util.Collection;
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
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedEnumSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedMapSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedStringSerializer;

/**
 * @author Serkan ÖZAL
 */
public class DirectMemoryAccessBasedDefaultDataSerializerFactory implements DirectMemoryAccessBasedDataSerializerFactory {

	@Override
	public <T> DirectMemoryAccessBasedDataSerializer<T> createDataSerializer(Class<T> clazz) {
		if (clazz.equals(byte.class)) {
			return new DmaBasedPrimitiveByteSerializer<T>(clazz);
		}
		else if (clazz.equals(boolean.class)) {
			return new DmaBasedPrimitiveBooleanSerializer<T>(clazz);
		}
		else if (clazz.equals(char.class)) {
			return new DmaBasedPrimitiveCharacterSerializer<T>(clazz);
		}
		else if (clazz.equals(short.class)) {
			return new DmaBasedPrimitiveShortSerializer<T>(clazz);
		}
		else if (clazz.equals(int.class)) {
			return new DmaBasedPrimitiveIntegerSerializer<T>(clazz);
		}
		else if (clazz.equals(float.class)) {
			return new DmaBasedPrimitiveFloatSerializer<T>(clazz);
		}
		else if (clazz.equals(long.class)) {
			return new DmaBasedPrimitiveLongSerializer<T>(clazz);
		}
		else if (clazz.equals(double.class)) {
			return new DmaBasedPrimitiveDoubleSerializer<T>(clazz);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		if (clazz.equals(Byte.class)) {
			return new DmaBasedObjectByteSerializer<T>(clazz);
		}
		else if (clazz.equals(Boolean.class)) {
			return new DmaBasedObjectBooleanSerializer<T>(clazz);
		}
		else if (clazz.equals(Character.class)) {
			return new DmaBasedObjectCharacterSerializer<T>(clazz);
		}
		else if (clazz.equals(Short.class)) {
			return new DmaBasedObjectShortSerializer<T>(clazz);
		}
		else if (clazz.equals(Integer.class)) {
			return new DmaBasedObjectIntegerSerializer<T>(clazz);
		}
		else if (clazz.equals(Float.class)) {
			return new DmaBasedObjectFloatSerializer<T>(clazz);
		}
		else if (clazz.equals(Long.class)) {
			return new DmaBasedObjectLongSerializer<T>(clazz);
		}
		else if (clazz.equals(Double.class)) {
			return new DmaBasedObjectDoubleSerializer<T>(clazz);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		else if (clazz.isArray()) {
			Class<?> arrayType = clazz.getComponentType();
			if (arrayType.equals(byte.class)) {
				return new DmaBasedByteArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(boolean.class)) {
				return new DmaBasedBooleanArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(char.class)) {
				return new DmaBasedCharacterArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(short.class)) {
				return new DmaBasedShortArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(int.class)) {
				return new DmaBasedIntegerArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(float.class)) {
				return new DmaBasedFloatArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(long.class)) {
				return new DmaBasedLongArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(double.class)) {
				return new DmaBasedDoubleArraySerializer<T>(clazz);
			}
			else {
				return new DmaBasedObjectArraySerializer<T>(clazz);
			}	
		}
		
		//////////////////////////////////////////////////////////////////////////////
		else if (clazz.isEnum()) {
			return new DmaBasedEnumSerializer<T>(clazz);
		}
		else if (clazz.equals(String.class)) {
			return new DmaBasedStringSerializer<T>(clazz);
		}
		else if (Collection.class.isAssignableFrom(clazz)) {
			return new DmaBasedCollectionSerializer<T>(clazz);
		}
		else if (Map.class.isAssignableFrom(clazz)) {
			return new DmaBasedMapSerializer<T>(clazz);
		}
		else if (Class.class.isAssignableFrom(clazz)) {
			return new DmaBasedClassSerializer<T>(clazz);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		else {
			return new DmaBasedObjectSerializer<T>(clazz);
		}
	}

}
