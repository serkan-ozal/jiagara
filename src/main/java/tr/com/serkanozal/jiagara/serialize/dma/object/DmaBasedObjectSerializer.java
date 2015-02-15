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
import java.lang.reflect.Modifier;
import java.util.List;

import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.serialize.Serializer;
import tr.com.serkanozal.jiagara.serialize.dma.ReferenceAwareDirectMemoryAccessBasedFieldAndDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DefaultDirectMemoryAccessBasedFieldSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializerFactory;

import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;
import tr.com.serkanozal.jiagara.service.serialize.SerializerService;
import tr.com.serkanozal.jiagara.service.serialize.SerializerServiceFactory;
import tr.com.serkanozal.jiagara.util.ReflectionUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedObjectSerializer<T> 
		extends ReferenceAwareDirectMemoryAccessBasedFieldAndDataSerializer<T, DirectMemoryAccessBasedOutputWriter, Object> 
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
		
	private SerializerService serializerService = SerializerServiceFactory.getSerializerService();
	private DirectMemoryAccessBasedFieldSerializerFactory fieldSerializerFactory;
	private DirectMemoryAccessBasedFieldSerializer<T>[] fieldSerializers;
	private ObjectSerializer objectSerializer;
	@SuppressWarnings("rawtypes")
	private Serializer serializer;
	
	public DmaBasedObjectSerializer(Field field) {
		super(field);
		if (Modifier.isFinal(fieldType.getModifiers())) {
			objectSerializer = new FinalTypedObjectSerializer();
			serializer = serializerService.getSerializer(fieldType);
		}
		else {
			objectSerializer = new NonFinalTypedObjectSerializer();
		}
	}
	
	public DmaBasedObjectSerializer(Class<T> clazz) {
		this(clazz, new DefaultDirectMemoryAccessBasedFieldSerializerFactory());
	}
	
	@SuppressWarnings("unchecked")
	public DmaBasedObjectSerializer(Class<T> clazz, DirectMemoryAccessBasedFieldSerializerFactory fieldSerializerFactory) {
		super(clazz);
		this.fieldSerializerFactory = fieldSerializerFactory;
		List<Field> fieldsSortedByName = ReflectionUtil.getAllSerializableFieldsSortedByName(clazz);
		if (fieldsSortedByName != null) {
			fieldSerializers = new DirectMemoryAccessBasedFieldSerializer[fieldsSortedByName.size()];
			int i = 0;
			for (Field field : fieldsSortedByName) {
				fieldSerializers[i++] = this.fieldSerializerFactory.createFieldSerializer(field);
			}
		}
		if (Modifier.isFinal(clazz.getModifiers())) {
			objectSerializer = new FinalTypedObjectSerializer();
			serializer = serializerService.getSerializer(clazz);
		}
		else {
			objectSerializer = new NonFinalTypedObjectSerializer();
		}
	}
	
	/*
	@SuppressWarnings("restriction")
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Object fieldObj = unsafe.getObject(obj, fieldOffset);
		if (fieldObj == null) {
			outputWriter.writeNull();
		}
		else {
			objectSerializer.serialize(fieldObj, outputWriter);
		}	
	}
	*/
	
	@Override
	protected void doSerializationOfField(T obj, Object objField, DirectMemoryAccessBasedOutputWriter outputWriter) {
		objectSerializer.serialize(objField, outputWriter);
	}

	/*
	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		try {
			for (FieldSerializer<T, DirectMemoryAccessBasedOutputWriter> fieldSerializer : fieldSerializers) {
				fieldSerializer.serializeField(obj, outputWriter);
			}
		} 
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while serializing data", t);
			throw new SerializationException("Error occured while serializing data", t);
		}
	}
	*/
	
	@SuppressWarnings("unchecked")
	protected void doSerializationOfDataContent(Object obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		try {
			for (FieldSerializer<T, DirectMemoryAccessBasedOutputWriter> fieldSerializer : fieldSerializers) {
				fieldSerializer.serializeField((T)obj, outputWriter);
			}
		} 
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while serializing data", t);
			throw new SerializationException("Error occured while serializing data", t);
		}
	}
	
	private interface ObjectSerializer {
		
		void serialize(Object obj, DirectMemoryAccessBasedOutputWriter outputWriter);
		
	}
	
	private class FinalTypedObjectSerializer implements ObjectSerializer {

		@SuppressWarnings("unchecked")
		@Override
		public void serialize(Object obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
			outputWriter.write(SerDeConstants.OBJECT_DATA_WITHOUT_TYPE);
			serializer.serializeContent(obj, outputWriter);
		}
		
	}
	
	private class NonFinalTypedObjectSerializer implements ObjectSerializer {

		@Override
		public void serialize(Object obj,  DirectMemoryAccessBasedOutputWriter outputWriter) {
			if (obj.getClass().equals(fieldType)) {
				outputWriter.write(SerDeConstants.OBJECT_DATA_WITHOUT_TYPE);
				serializerService.serializeContent(obj, outputWriter);
			}
			else {
				outputWriter.write(SerDeConstants.OBJECT_DATA);
				serializerService.serialize(obj, outputWriter);
			}	
		}
		
	}

}
