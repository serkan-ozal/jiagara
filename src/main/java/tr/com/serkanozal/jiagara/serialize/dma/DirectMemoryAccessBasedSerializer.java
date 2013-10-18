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

import java.io.OutputStream;

import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.serialize.AbstractSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDefaultDataSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriterFactory;

/**
 * @author Serkan ÖZAL
 */
public class DirectMemoryAccessBasedSerializer<T> extends AbstractSerializer<T, DirectMemoryAccessBasedOutputWriter> {

	public DirectMemoryAccessBasedSerializer(Class<T> clazz) {
		super(clazz, new DirectMemoryAccessBasedDefaultDataSerializerFactory());
	}
	
	public DirectMemoryAccessBasedSerializer(Class<T> clazz, DirectMemoryAccessBasedDataSerializerFactory dataSerializerFactory) {
		super(clazz, dataSerializerFactory);
	}
	
	@Override
	public void serialize(T obj, OutputStream os) throws SerializationException {
		DirectMemoryAccessBasedOutputWriter outputWriter = null;
		try {
			outputWriter = (DirectMemoryAccessBasedOutputWriter) OUTPUT_WRITER_MAP.get(os);
			if (outputWriter == null) {
				outputWriter = 
					DirectMemoryAccessBasedOutputWriterFactory.createDirectMemoryAccessBasedOutputWriter(os);
				OUTPUT_WRITER_MAP.put(os, outputWriter);
			}	
			serialize(obj, outputWriter);
		}
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while serialization", t);
			throw new SerializationException("Error occured while serialization", t);
		}
	}
	
	@Override
	public void serialize(T obj, DirectMemoryAccessBasedOutputWriter ow) throws SerializationException {
		try {
			dataSerializer.serializeData(obj, ow);
		} 
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while serialization", t);
			throw new SerializationException("Error occured while serialization", t);
		}
	}

}
