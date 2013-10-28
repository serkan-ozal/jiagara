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
import java.util.Date;
import java.util.Map;

import tr.com.serkanozal.jiagara.serialize.data.AbstractDataSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.data.DataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectBooleanArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectByteArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectCharacterArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectDoubleArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectFloatArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectIntegerArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectLongArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedObjectShortArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.object.DmaBasedStringArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveBooleanArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveByteArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveCharacterArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveDoubleArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveFloatArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveIntegerArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveLongArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.array.primitive.DmaBasedPrimitiveShortArraySerializer;
import tr.com.serkanozal.jiagara.serialize.dma.collection.DmaBasedCollectionSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.map.DmaBasedMapSerializer;
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
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedDateSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedEnumSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.specific.DmaBasedStringSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DefaultDirectMemoryAccessBasedDataSerializerFactory 
		extends AbstractDataSerializerFactory<DirectMemoryAccessBasedOutputWriter> implements DirectMemoryAccessBasedDataSerializerFactory {

	@Override
	public <T> DirectMemoryAccessBasedDataSerializer<T> createDataSerializer(Class<T> clazz) {
		DataSerializer<T, OutputWriter> configuredDataSerializer = super.getConfiguredDataSerializer(clazz);
		if (configuredDataSerializer != null) {
			return new DirectMemoryAccessBasedDataSerializerDispatcher<T>(configuredDataSerializer);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
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
				return new DmaBasedPrimitiveByteArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(boolean.class)) {
				return new DmaBasedPrimitiveBooleanArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(char.class)) {
				return new DmaBasedPrimitiveCharacterArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(short.class)) {
				return new DmaBasedPrimitiveShortArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(int.class)) {
				return new DmaBasedPrimitiveIntegerArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(float.class)) {
				return new DmaBasedPrimitiveFloatArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(long.class)) {
				return new DmaBasedPrimitiveLongArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(double.class)) {
				return new DmaBasedPrimitiveDoubleArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Byte.class)) {
				return new DmaBasedObjectByteArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Boolean.class)) {
				return new DmaBasedObjectBooleanArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Character.class)) {
				return new DmaBasedObjectCharacterArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Short.class)) {
				return new DmaBasedObjectShortArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Integer.class)) {
				return new DmaBasedObjectIntegerArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Float.class)) {
				return new DmaBasedObjectFloatArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Long.class)) {
				return new DmaBasedObjectLongArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(Double.class)) {
				return new DmaBasedObjectDoubleArraySerializer<T>(clazz);
			}
			else if (arrayType.equals(String.class)) {
				return new DmaBasedStringArraySerializer<T>(clazz);
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
		else if (Date.class.isAssignableFrom(clazz)) {
			return new DmaBasedDateSerializer<T>(clazz);
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
