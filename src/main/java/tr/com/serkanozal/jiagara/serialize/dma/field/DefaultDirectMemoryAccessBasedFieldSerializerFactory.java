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
import tr.com.serkanozal.jiagara.serialize.field.AbstractFieldSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DefaultDirectMemoryAccessBasedFieldSerializerFactory 
		extends AbstractFieldSerializerFactory<DirectMemoryAccessBasedOutputWriter> implements DirectMemoryAccessBasedFieldSerializerFactory {

	@Override
	public <T> DirectMemoryAccessBasedFieldSerializer<T> createFieldSerializer(Field field) {
		FieldSerializer<T, OutputWriter> configuredFieldSerializer = super.getConfiguredFieldSerializer(field);
		if (configuredFieldSerializer != null) {
			return new DirectMemoryAccessBasedFieldSerializerDispatcher<T>(configuredFieldSerializer);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		DirectMemoryAccessBasedFieldSerializer<T> fieldSerializer = null;
		Class<?> fieldClass = field.getType();
		
		//////////////////////////////////////////////////////////////////////////////
		
		if (fieldClass.equals(byte.class)) {
			fieldSerializer = new DmaBasedPrimitiveByteSerializer<T>(field);
		}
		else if (fieldClass.equals(boolean.class)) {
			fieldSerializer = new DmaBasedPrimitiveBooleanSerializer<T>(field);
		}
		else if (fieldClass.equals(char.class)) {
			fieldSerializer = new DmaBasedPrimitiveCharacterSerializer<T>(field);
		}
		else if (fieldClass.equals(short.class)) {
			fieldSerializer = new DmaBasedPrimitiveShortSerializer<T>(field);
		}
		else if (fieldClass.equals(int.class)) {
			fieldSerializer = new DmaBasedPrimitiveIntegerSerializer<T>(field);
		}
		else if (fieldClass.equals(float.class)) {
			fieldSerializer = new DmaBasedPrimitiveFloatSerializer<T>(field);
		}
		else if (fieldClass.equals(long.class)) {
			fieldSerializer = new DmaBasedPrimitiveLongSerializer<T>(field);
		}
		else if (fieldClass.equals(double.class)) {
			fieldSerializer = new DmaBasedPrimitiveDoubleSerializer<T>(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		else if (fieldClass.equals(Byte.class)) {
			fieldSerializer = new DmaBasedObjectByteSerializer<T>(field);
		}
		else if (fieldClass.equals(Boolean.class)) {
			fieldSerializer = new DmaBasedObjectBooleanSerializer<T>(field);
		}
		else if (fieldClass.equals(Character.class)) {
			fieldSerializer = new DmaBasedObjectCharacterSerializer<T>(field);
		}
		else if (fieldClass.equals(Short.class)) {
			fieldSerializer = new DmaBasedObjectShortSerializer<T>(field);
		}
		else if (fieldClass.equals(Integer.class)) {
			fieldSerializer = new DmaBasedObjectIntegerSerializer<T>(field);
		}
		else if (fieldClass.equals(Float.class)) {
			fieldSerializer = new DmaBasedObjectFloatSerializer<T>(field);
		}
		else if (fieldClass.equals(Long.class)) {
			fieldSerializer = new DmaBasedObjectLongSerializer<T>(field);
		}
		else if (fieldClass.equals(Double.class)) {
			fieldSerializer = new DmaBasedObjectDoubleSerializer<T>(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		else if (fieldClass.isArray()) {
			Class<?> arrayType = fieldClass.getComponentType();
			if (arrayType.equals(byte.class)) {
				fieldSerializer = new DmaBasedPrimitiveByteArraySerializer<T>(field);
			}
			else if (arrayType.equals(boolean.class)) {
				fieldSerializer = new DmaBasedPrimitiveBooleanArraySerializer<T>(field);
			}
			else if (arrayType.equals(char.class)) {
				fieldSerializer = new DmaBasedPrimitiveCharacterArraySerializer<T>(field);
			}
			else if (arrayType.equals(short.class)) {
				fieldSerializer = new DmaBasedPrimitiveShortArraySerializer<T>(field);
			}
			else if (arrayType.equals(int.class)) {
				fieldSerializer = new DmaBasedPrimitiveIntegerArraySerializer<T>(field);
			}
			else if (arrayType.equals(float.class)) {
				fieldSerializer = new DmaBasedPrimitiveFloatArraySerializer<T>(field);
			}
			else if (arrayType.equals(long.class)) {
				fieldSerializer = new DmaBasedPrimitiveLongArraySerializer<T>(field);
			}
			else if (arrayType.equals(double.class)) {
				fieldSerializer = new DmaBasedPrimitiveDoubleArraySerializer<T>(field);
			}
			else if (arrayType.equals(Byte.class)) {
				fieldSerializer = new DmaBasedObjectByteArraySerializer<T>(field);
			}
			else if (arrayType.equals(Boolean.class)) {
				fieldSerializer = new DmaBasedObjectBooleanArraySerializer<T>(field);
			}
			else if (arrayType.equals(Character.class)) {
				fieldSerializer = new DmaBasedObjectCharacterArraySerializer<T>(field);
			}
			else if (arrayType.equals(Short.class)) {
				fieldSerializer = new DmaBasedObjectShortArraySerializer<T>(field);
			}
			else if (arrayType.equals(Integer.class)) {
				fieldSerializer = new DmaBasedObjectIntegerArraySerializer<T>(field);
			}
			else if (arrayType.equals(Float.class)) {
				fieldSerializer = new DmaBasedObjectFloatArraySerializer<T>(field);
			}
			else if (arrayType.equals(Long.class)) {
				fieldSerializer = new DmaBasedObjectLongArraySerializer<T>(field);
			}
			else if (arrayType.equals(Double.class)) {
				fieldSerializer = new DmaBasedObjectDoubleArraySerializer<T>(field);
			}
			else if (arrayType.equals(String.class)) {
				fieldSerializer = new DmaBasedStringArraySerializer<T>(field);
			}
			else {
				fieldSerializer = new DmaBasedObjectArraySerializer<T>(field);
			}	
		}
		
		//////////////////////////////////////////////////////////////////////////////
		else if (fieldClass.isEnum()) {
			fieldSerializer = new DmaBasedEnumSerializer<T>(field);
		}
		else if (fieldClass.equals(String.class)) {
			fieldSerializer = new DmaBasedStringSerializer<T>(field);
		}
		else if (Date.class.isAssignableFrom(fieldClass)) {
			fieldSerializer = new DmaBasedDateSerializer<T>(field);
		}
		else if (Collection.class.isAssignableFrom(fieldClass)) {
			fieldSerializer = new DmaBasedCollectionSerializer<T>(field);
		}
		else if (Map.class.isAssignableFrom(fieldClass)) {
			fieldSerializer = new DmaBasedMapSerializer<T>(field);
		}
		else if (Class.class.isAssignableFrom(fieldClass)) {
			fieldSerializer = new DmaBasedClassSerializer<T>(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		else {
			fieldSerializer = new DmaBasedObjectSerializer<T>(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		if (fieldSerializer != null) {
			fieldSerializer.useField(field);
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
		return fieldSerializer;
	}

}
