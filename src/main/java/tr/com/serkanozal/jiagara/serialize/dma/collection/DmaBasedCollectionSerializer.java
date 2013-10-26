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

package tr.com.serkanozal.jiagara.serialize.dma.collection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

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
public class DmaBasedCollectionSerializer<T> extends AbstractDirectMemoryAccessBasedFieldAndDataSerializer<T, DirectMemoryAccessBasedOutputWriter> 
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {
		
	private static boolean OPTIMIZATION_ENABLED = true;
	
	private SerializerService serializerService = SerializerServiceFactory.getSerializerService();
	private CollectionElementSerializer collectionElementSerializer;
	private Class<?> collectionTypeClass = Object.class;
	@SuppressWarnings("rawtypes")
	private Serializer serializer;
	
	public DmaBasedCollectionSerializer(Field field) {
		super(field);
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			ParameterizedType collectionType = (ParameterizedType)genericType;
			collectionTypeClass = (Class<?>)collectionType.getActualTypeArguments()[0];
			if (Modifier.isFinal(collectionTypeClass.getModifiers())) {
				collectionElementSerializer = new KnownAndFinalTypedCollectionElementSerializer();
			}
			serializer = serializerService.getSerializer(collectionTypeClass);
		}	
		if (collectionElementSerializer == null) {
			collectionElementSerializer = new UnknownOrNonFinalTypedCollectionElementSerializer();
		}
	}
	
	public DmaBasedCollectionSerializer(Class<T> clazz) {
		super(clazz);
		if (collectionElementSerializer == null) {
			collectionElementSerializer = new UnknownOrNonFinalTypedCollectionElementSerializer();
		}
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Collection<?> collectionField = (Collection<?>)unsafe.getObject(obj, fieldOffset);
		if (collectionField == null) {
			outputWriter.writeNull();
		}
		else {
			writeClass(collectionField.getClass(), outputWriter); 
			collectionElementSerializer.serialize(collectionField, outputWriter);
		}
	}

	@Override
	public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		Collection<?> o = (Collection<?>)obj;
		if (o == null) {
			outputWriter.writeNull();
		}
		else {
			collectionElementSerializer.serialize(o, outputWriter);
		}
	}
	
	private interface CollectionElementSerializer {
	
		void serialize(Collection<?> collection, DirectMemoryAccessBasedOutputWriter outputWriter);
		
	}
	
	private class KnownAndFinalTypedCollectionElementSerializer implements CollectionElementSerializer {

		@SuppressWarnings("unchecked")
		@Override
		public void serialize(Collection<?> collection, DirectMemoryAccessBasedOutputWriter outputWriter) {
			outputWriter.writeVarInteger(SerDeConstants.COLLECTION_WITH_TYPE, collection.size()); 
			for (Object collectionObj : collection) {
				serializer.serializeContent(collectionObj, outputWriter);
			}	
		}
		
	}
	
	private class UnknownOrNonFinalTypedCollectionElementSerializer implements CollectionElementSerializer {

		@SuppressWarnings("unchecked")
		@Override
		public void serialize(Collection<?> collection, DirectMemoryAccessBasedOutputWriter outputWriter) {
			boolean allElementsAreSameTypeWithCollection = true;
			if (OPTIMIZATION_ENABLED) {
				for (Object collectionObj : collection) {
					if (collectionObj != null && collectionObj.getClass().equals(collectionTypeClass) == false) {
						allElementsAreSameTypeWithCollection = false;
					}
				}
			}
			else {
				allElementsAreSameTypeWithCollection = false;
			}
			
			if (allElementsAreSameTypeWithCollection) {
				outputWriter.writeVarInteger(SerDeConstants.COLLECTION_WITH_TYPE, collection.size()); 
				for (Object collectionObj : collection) {
					serializer.serializeContent(collectionObj, outputWriter);
				}	
			}
			else {
				outputWriter.writeVarInteger(SerDeConstants.COLLECTION_WITHOUT_TYPE, collection.size()); 
				for (Object collectionObj : collection) {
					if (collectionObj.getClass().equals(collectionTypeClass)) {
						outputWriter.write(SerDeConstants.OBJECT_DATA_WITHOUT_TYPE);
						serializer.serializeContent(collectionObj, outputWriter);
					}
					else {
						outputWriter.write(SerDeConstants.OBJECT_DATA);
						serializerService.serialize(collectionObj, outputWriter);
					}	
				}	
			}	
		}
		
	}

}
