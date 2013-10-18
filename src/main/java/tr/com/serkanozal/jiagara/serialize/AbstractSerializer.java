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

import org.apache.log4j.Logger;

import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.serialize.data.DataSerializer;
import tr.com.serkanozal.jiagara.serialize.data.DataSerializerFactory;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;
import tr.com.serkanozal.jiagara.util.LogUtil;

/**
 * @author Serkan ÖZAL
 */
public abstract class AbstractSerializer<T, O extends OutputWriter> implements Serializer<T, O> {

	protected static final Logger logger = LogUtil.getLogger();

	protected Class<T> clazz;
	protected DataSerializerFactory<O> dataSerializerFactory;
	protected DataSerializer<T, O> dataSerializer;
	
	public AbstractSerializer(Class<T> clazz, DataSerializerFactory<O> dataSerializerFactory) {
		this.clazz = clazz;
		this.dataSerializerFactory = dataSerializerFactory;
		this.dataSerializer = dataSerializerFactory.createDataSerializer(clazz);
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
