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
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import tr.com.serkanozal.jiagara.serialize.Serializer;
import tr.com.serkanozal.jiagara.serialize.dma.AbstractDirectMemoryAccessBasedSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.service.serialize.SerializerService;
import tr.com.serkanozal.jiagara.service.serialize.SerializerServiceFactory;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public class DmaBasedMapSerializer<T> extends AbstractDirectMemoryAccessBasedSerializer<T, DirectMemoryAccessBasedOutputWriter> 
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
		
	private SerializerService serializerService = SerializerServiceFactory.getSerializerService();
	private MapElementSerializer keyElementSerializer;
	private MapElementSerializer valueElementSerializer;
	private Class<?> keyTypeClass;
	private Class<?> valueTypeClass;
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
			keyTypeClass = (Class<?>)mapType.getActualTypeArguments()[0];
			if (Modifier.isFinal(keyTypeClass.getModifiers())) {
				keySerializer = serializerService.getSerializer(keyTypeClass);
				keyElementSerializer = new KnownAndFinalTypedMapElementSerializer(keySerializer);
				useKeyType = true;
			}
			valueTypeClass = (Class<?>)mapType.getActualTypeArguments()[1];
			if (Modifier.isFinal(valueTypeClass.getModifiers())) {
				valueSerializer = serializerService.getSerializer(valueTypeClass);
				valueElementSerializer = new KnownAndFinalTypedMapElementSerializer(valueSerializer);
				useValueType = true;
			}
		}	
		if (keyElementSerializer == null) {
			keyElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer();
		}
		if (valueElementSerializer == null) {
			valueElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer();
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
			keyElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer();
		}
		if (valueElementSerializer == null) {
			valueElementSerializer = new UnknownOrNonFinalTypedMapElementSerializer();
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
			outputWriter.write(mapType);
			writeClass(mapField.getClass(), outputWriter); 
			outputWriter.write(mapField.size()); 
			for (Map.Entry<?, ?> e : mapField.entrySet()) {
				keyElementSerializer.serialize(e.getKey(), outputWriter);
				valueElementSerializer.serialize(e.getValue(), outputWriter);
			}
		}
	}

	@Override
	public void serializeData(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Map<?, ?> o = (Map<?, ?>)obj;
		if (o == null) {
			outputWriter.writeNull();
		}
		else {
			outputWriter.write(mapType);
			writeClass(o.getClass(), outputWriter); 
			outputWriter.write(o.size()); 
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
			serializer.serialize(element, outputWriter);
		}
		
	}
	
	private class UnknownOrNonFinalTypedMapElementSerializer implements MapElementSerializer {

		@Override
		public void serialize(Object element, DirectMemoryAccessBasedOutputWriter outputWriter) {
			writeClass(element.getClass(), outputWriter); 
			serializerService.serialize(element, outputWriter);
		}
		
	}

}
