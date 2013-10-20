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

package tr.com.serkanozal.jiagara.serialize.dma.array;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import tr.com.serkanozal.jiagara.serialize.Serializer;
import tr.com.serkanozal.jiagara.serialize.dma.AbstractDirectMemoryAccessBasedFieldAndDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.service.serialize.SerializerService;
import tr.com.serkanozal.jiagara.service.serialize.SerializerServiceFactory;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedObjectArraySerializer<T> extends AbstractDirectMemoryAccessBasedFieldAndDataSerializer<T, DirectMemoryAccessBasedOutputWriter> 
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
	
	private SerializerService serializerService = SerializerServiceFactory.getSerializerService();
	private ObjectElementSerializer objectElementSerializer;
	@SuppressWarnings("rawtypes")
	private Serializer serializer;
	
	public DmaBasedObjectArraySerializer(Field field) {
		super(field);
		if (Modifier.isFinal(fieldType.getComponentType().getModifiers())) {
			objectElementSerializer = new FinalTypedObjectElementSerializer();
			serializer = serializerService.getSerializer(fieldType.getComponentType());
		}
		else {
			objectElementSerializer = new NonFinalTypedObjectElementSerializer();
			serializer = serializerService.getSerializer(fieldType.getComponentType());
		}
	}
	
	public DmaBasedObjectArraySerializer(Class<T> clazz) {
		super(clazz);
		if (Modifier.isFinal(clazz.getComponentType().getModifiers())) {
			objectElementSerializer = new FinalTypedObjectElementSerializer();
		}
		else {
			objectElementSerializer = new NonFinalTypedObjectElementSerializer();
		}
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Object[] arrayField = (Object[])unsafe.getObject(obj, fieldOffset);
		if (arrayField == null) {
			outputWriter.writeNull();
		}
		else {
			writeClass(arrayField.getClass(), outputWriter); 
			objectElementSerializer.serialize(arrayField, outputWriter);
		}
	}

	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Object[] o = (Object[])obj;
		if (o == null) {
			outputWriter.writeNull();
		}
		else {
			objectElementSerializer.serialize(o, outputWriter);
		}
	}
	
	private interface ObjectElementSerializer {
		
		void serialize(Object[] array, DirectMemoryAccessBasedOutputWriter outputWriter);
		
	}
	
	private class FinalTypedObjectElementSerializer implements ObjectElementSerializer {

		@SuppressWarnings("unchecked")
		@Override
		public void serialize(Object[] array, DirectMemoryAccessBasedOutputWriter outputWriter) {
			outputWriter.writeVarInteger(SerDeConstants.ARRAY_DATA_WITHOUT_TYPE, array.length); 
			for (Object o : array) {
				serializer.serializeContent(o, outputWriter);
			}
		}
		
	}
	
	private class NonFinalTypedObjectElementSerializer implements ObjectElementSerializer {

		@SuppressWarnings("unchecked")
		@Override
		public void serialize(Object[] array, DirectMemoryAccessBasedOutputWriter outputWriter) {
			Class<?> arrayComponentType = array.getClass().getComponentType();
			boolean allElementsAreSameTypeWithArray = true;
			for (Object o : array) {
				if (o != null && o.getClass().equals(arrayComponentType) == false) {
					allElementsAreSameTypeWithArray = false;
				}
			}	
			if (allElementsAreSameTypeWithArray) {
				outputWriter.writeVarInteger(SerDeConstants.ARRAY_DATA_WITHOUT_TYPE, array.length); 
				for (Object o : array) {
					serializer.serializeContent(o, outputWriter);
				}
			}
			else {
				outputWriter.writeVarInteger(SerDeConstants.ARRAY_DATA, array.length); 
				for (Object o : array) {
					if (o.getClass().equals(arrayComponentType)) {
						outputWriter.write(SerDeConstants.ARRAY_DATA_WITHOUT_TYPE);
						serializer.serializeContent(o, outputWriter);
					}
					else {
						outputWriter.write(SerDeConstants.ARRAY_DATA);
						serializerService.serialize(o, outputWriter);
					}
				}
			}	
		}
		
	}

}
