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
import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.dma.DirectMemoryAccessBasedDefaultFieldSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.field.dma.DirectMemoryAccessBasedFieldSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.writer.dma.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.serialize.writer.dma.DirectMemoryAccessBasedOutputWriterFactory;

/**
 * @author Serkan ÖZAL
 */
public class DirectMemoryAccessBasedSerializer<T> extends AbstractSerializer<T, DirectMemoryAccessBasedOutputWriter> {

	public DirectMemoryAccessBasedSerializer(Class<T> clazz) {
		super(clazz, new DirectMemoryAccessBasedDefaultFieldSerializerFactory());
	}
	
	public DirectMemoryAccessBasedSerializer(Class<T> clazz, DirectMemoryAccessBasedFieldSerializerFactory fieldSerializerFactory) {
		super(clazz, fieldSerializerFactory);
	}
	
	@Override
	public void serialize(T obj, OutputStream os) throws SerializationException {
		try {
			DirectMemoryAccessBasedOutputWriter outputWriter = 
					DirectMemoryAccessBasedOutputWriterFactory.createDirectMemoryAccessBasedOutputWriter(os);
			for (FieldSerializer<T, DirectMemoryAccessBasedOutputWriter> fieldSerializer : fieldSerializers) {
				fieldSerializer.serializeField(obj, outputWriter);
			}
			outputWriter.release();
			os.flush();
		} 
		catch (Throwable t) {
			logger.error("Error occured while serialization", t);
			throw new SerializationException("Error occured while serialization", t);
		}
	}

}
