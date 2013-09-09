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

package tr.com.serkanozal.jiagara.serialize;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.log4j.Logger;

import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;
import tr.com.serkanozal.jiagara.serialize.field.FieldSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;
import tr.com.serkanozal.jiagara.util.LogUtil;
import tr.com.serkanozal.jiagara.util.ReflectionUtil;

/**
 * @author Serkan ÖZAL
 */
public abstract class AbstractSerializer<T, O extends OutputWriter> implements Serializer<T> {

	protected static final Logger logger = LogUtil.getLogger();

	protected Class<T> clazz;
	protected FieldSerializerFactory<O> fieldSerializerFactory;
	protected FieldSerializer<T, O>[] fieldSerializers;
	
	@SuppressWarnings("unchecked")
	public AbstractSerializer(Class<T> clazz, FieldSerializerFactory<O> fieldSerializerFactory) {
		this.clazz = clazz;
		this.fieldSerializerFactory = fieldSerializerFactory;
		List<Field> fieldsSortedByName = ReflectionUtil.getAllSerializableFieldsSortedByName(clazz);
		if (fieldsSortedByName != null) {
			fieldSerializers = new FieldSerializer[fieldsSortedByName.size()];
			int i = 0;
			for (Field field : fieldsSortedByName) {
				fieldSerializers[i++] = fieldSerializerFactory.createFieldSerializer(field);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void release(OutputStream os) throws SerializationException {
		try {
			O outputWriter = (O) OUTPUT_WRITER_MAP.get(os);
			if (outputWriter != null) {
				outputWriter.release();
				OUTPUT_WRITER_MAP.remove(os);
			}
			os.flush();
		}
		catch (Throwable t) {
			logger.error("Error occured while releasing output stream", t);
			throw new SerializationException("Error occured while releasing output stream", t);
		}
	}
	
}
