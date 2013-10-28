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

package tr.com.serkanozal.jiagara.serialize.dma.map;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

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
public class DmaBasedMapSerializer<T> extends AbstractDirectMemoryAccessBasedFieldAndDataSerializer<T, DirectMemoryAccessBasedOutputWriter> 
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
		
	private SerializerService serializerService = SerializerServiceFactory.getSerializerService();
	private MapElementSerializer keyElementSerializer;
	private MapElementSerializer valueElementSerializer;
	private Class<?> keyTypeClass = Object.class;
	private Class<?> valueTypeClass = Object.class;
	@SuppressWarnings("rawtypes")
	private Serializer keySerializer;
	@SuppressWarnings("rawtypes")
	private Serializer valueSerializer;
	private boolean useKeyType = false;
	private boolean useValueType = false;
	private byte mapType = SerDeConstants.MAP_WITHOUT_KEY_TYPE_AND_WITHOUT_VALUE_TYPE;
	
	public DmaBasedMapSerializer(Field field) {
		super(field);
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			ParameterizedType mapType = (ParameterizedType)genericType;
			Type[] types = mapType.getActualTypeArguments();
			if (types != null) {
				if (types.length == 1 || types.length == 2) {
					keyTypeClass = (Class<?>)types[0];
					keySerializer = serializerService.getSerializer(keyTypeClass);
					if (Modifier.isFinal(keyTypeClass.getModifiers())) {
						keyElementSerializer = new KnownAndFinalTypedMapElementSerializer(keySerializer);
						useKeyType = true;
					}
				}	
				if (types.length == 2) {
					valueTypeClass = (Class<?>)types[1];
					valueSerializer = serializerService.getSerializer(valueTypeClass);
					if (Modifier.isFinal(valueTypeClass.getModifiers())) {
						valueElementSerializer = new KnownAndFinalTypedMapElementSerializer(valueSerializer);
						useValueType = true;
					}
				}
			}	
		}	
		if (keyElementSerializer == null) {
			keyElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer(keySerializer, keyTypeClass);
		}
		if (valueElementSerializer == null) {
			valueElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer(valueSerializer, valueTypeClass);
		}
		if (useKeyType == false && useValueType == false) {
			mapType = SerDeConstants.MAP_WITHOUT_KEY_TYPE_AND_WITHOUT_VALUE_TYPE;
		}
		else if (useKeyType == false && useValueType == true) {
			mapType = SerDeConstants.MAP_WITHOUT_KEY_TYPE_AND_WITH_VALUE_TYPE;
		}
		else if (useKeyType == true && useValueType == false) {
			mapType = SerDeConstants.MAP_WITH_KEY_TYPE_AND_WITHOUT_VALUE_TYPE;
		}
		else if (useKeyType == true && useValueType == true) {
			mapType = SerDeConstants.MAP_WITH_KEY_TYPE_AND_WITH_VALUE_TYPE;
		}
	}
	
	public DmaBasedMapSerializer(Class<T> clazz) {
		super(clazz);
		if (keyElementSerializer == null) {
			keyElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer(keySerializer, keyTypeClass);
		}
		if (valueElementSerializer == null) {
			valueElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer(valueSerializer, valueTypeClass);
		}
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Map<?, ?> mapField = (Map<?, ?>)unsafe.getObject(obj, fieldOffset);
		if (mapField == null) {
			outputWriter.writeNull();
		}
		else {
			writeClass(mapField.getClass(), outputWriter); 
			outputWriter.writeVarInteger(mapType, mapField.size()); 
			for (Map.Entry<?, ?> e : mapField.entrySet()) {
				keyElementSerializer.serialize(e.getKey(), outputWriter);
				valueElementSerializer.serialize(e.getValue(), outputWriter);
			}
		}
	}

	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Map<?, ?> o = (Map<?, ?>)obj;
		if (o == null) {
			outputWriter.writeNull();
		}
		else {
			outputWriter.writeVarInteger(mapType, o.size());
			for (Map.Entry<?, ?> e : o.entrySet()) {
				keyElementSerializer.serialize(e.getKey(), outputWriter);
				valueElementSerializer.serialize(e.getValue(), outputWriter);
			}
		}
	}
	
	private interface MapElementSerializer {
		
		void serialize(Object element, DirectMemoryAccessBasedOutputWriter outputWriter);
		
	}
	
	private class KnownAndFinalTypedMapElementSerializer implements MapElementSerializer {

		@SuppressWarnings("rawtypes")
		private Serializer serializer;
		
		@SuppressWarnings("rawtypes")
		private KnownAndFinalTypedMapElementSerializer(Serializer serializer) {
			this.serializer = serializer;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void serialize(Object element, DirectMemoryAccessBasedOutputWriter outputWriter) {
			serializer.serializeContent(element, outputWriter);
		}
		
	}
	
	private class UnknownOrNonFinalTypedMapElementSerializer implements MapElementSerializer {

		@SuppressWarnings("rawtypes")
		private Serializer serializer;
		private Class<?> managedType;
		
		@SuppressWarnings("rawtypes")
		private UnknownOrNonFinalTypedMapElementSerializer(Serializer serializer, Class<?> managedType) {
			this.serializer = serializer;
			this.managedType = managedType;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void serialize(Object element, DirectMemoryAccessBasedOutputWriter outputWriter) {
			if (element.getClass().equals(managedType)) {
				outputWriter.write(SerDeConstants.OBJECT_DATA_WITHOUT_TYPE);
				if (serializer != null) {
					serializer.serializeContent(element, outputWriter);
				}
				else {
					serializerService.serializeContent(element, outputWriter);
				}
			}
			else {
				outputWriter.write(SerDeConstants.OBJECT_DATA);
				serializerService.serialize(element, outputWriter);
			}	
		}
		
	}

}
