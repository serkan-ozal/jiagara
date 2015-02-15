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

package tr.com.serkanozal.jiagara.serialize.dma;

import java.lang.reflect.Field;
import java.util.Map;

import tr.com.serkanozal.jiagara.domain.model.context.SerializationContext;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.service.serialize.SerializerService;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public abstract class ReferenceAwareDirectMemoryAccessBasedFieldAndDataSerializer<T, O extends DirectMemoryAccessBasedOutputWriter, E>
		extends AbstractDirectMemoryAccessBasedFieldAndDataSerializer<T, O> {

	public ReferenceAwareDirectMemoryAccessBasedFieldAndDataSerializer(Class<T> clazz) {
		super(clazz);
	}
	
	public ReferenceAwareDirectMemoryAccessBasedFieldAndDataSerializer(Field field) {
		super(field);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	protected void writeReference(DirectMemoryAccessBasedOutputWriter outputWriter, Integer identityCode, Integer streamPosition) {
		outputWriter.write(SerDeConstants.REFERENCE);
		outputWriter.writeVarInteger(streamPosition);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings({ "restriction", "unchecked" })
	final public void serializeField(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		E fieldObj = (E)unsafe.getObject(obj, fieldOffset);
		if (fieldObj == null) {
			outputWriter.writeNull();
		}
		else {
			SerializationContext context = SerializerService.SERIALIZATION_CONTEXT.get();
			if (context != null) {
				Map<Integer, Integer> objectAndStreamPositionMap = context.getObjectAndStreamPositionMap();
				Integer identityCode = System.identityHashCode(fieldObj);
				Integer streamPosition = objectAndStreamPositionMap.get(identityCode);
				if (streamPosition == null) {
					objectAndStreamPositionMap.put(identityCode, outputWriter.index());
					doSerializationOfField(obj, fieldObj, outputWriter);
				}
				else {
					writeReference(outputWriter, identityCode, streamPosition);
				}
			}	
			else {
				doSerializationOfField(obj, fieldObj, outputWriter);
			}
		}	
	};
	
	abstract protected void doSerializationOfField(T obj, E fieldObj, DirectMemoryAccessBasedOutputWriter outputWriter);
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	final public void serializeData(T obj, O outputWriter) {
		if (obj == null) {
			outputWriter.writeNull();
		}
		else {
			SerializationContext context = SerializerService.SERIALIZATION_CONTEXT.get();
			if (context != null) {
				Map<Integer, Integer> objectAndStreamPositionMap = context.getObjectAndStreamPositionMap();
				Integer identityCode = System.identityHashCode(obj);
				Integer streamPosition = objectAndStreamPositionMap.get(identityCode);
				if (streamPosition == null) {
					writeClass(obj.getClass(), outputWriter);
					doSerializationOfDataContent((E)obj, outputWriter);
				}
				else {
					writeReference(outputWriter, identityCode, streamPosition);
				}
			}	
			else {
				doSerializationOfDataContent((E)obj, outputWriter);
			}
		}	
	};
	
	@SuppressWarnings("unchecked")
	final public void serializeDataContent(T obj, DirectMemoryAccessBasedOutputWriter outputWriter) {
		if (obj == null) {
			outputWriter.writeNull();
		}
		else {
			SerializationContext context = SerializerService.SERIALIZATION_CONTEXT.get();
			if (context != null) {
				Map<Integer, Integer> objectAndStreamPositionMap = context.getObjectAndStreamPositionMap();
				Integer identityCode = System.identityHashCode(obj);
				Integer streamPosition = objectAndStreamPositionMap.get(identityCode);
				if (streamPosition == null) {
					objectAndStreamPositionMap.put(identityCode, outputWriter.index());
					doSerializationOfDataContent((E)obj, outputWriter);
				}
				else {
					writeReference(outputWriter, identityCode, streamPosition);
				}
			}	
			else {
				doSerializationOfDataContent((E)obj, outputWriter);
			}
		}	
	}
	
	abstract protected void doSerializationOfDataContent(E obj, DirectMemoryAccessBasedOutputWriter outputWriter);
	
}
