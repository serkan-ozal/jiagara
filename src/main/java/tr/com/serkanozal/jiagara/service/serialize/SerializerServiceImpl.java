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


package tr.com.serkanozal.jiagara.service.serialize;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.serialize.Serializer;
import tr.com.serkanozal.jiagara.serialize.SerializerFactory;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import tr.com.serkanozal.jiagara.util.LogUtil;

/**
 * @author Serkan ÖZAL
 */
public class SerializerServiceImpl implements SerializerService {

	private static final Logger logger = LogUtil.getLogger();
	
	private Map<Class<?>, Serializer<?>> cachedSerializers = new HashMap<Class<?>, Serializer<?>>();
	private Map<Long, Serializer<?>> cachedSerializersByAddress = new HashMap<Long, Serializer<?>>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Serializer<T> getSerializer(Class<T> clazz) {
		Serializer<T> serializer = (Serializer<T>) cachedSerializers.get(clazz);
		if (serializer == null) {
			serializer = SerializerFactory.createSerializer(clazz);
			cachedSerializers.put(clazz, serializer);
		}
		return serializer;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private <T> Serializer<T> getSerializer(T obj) {
		long address = JvmUtil.addressOfClass(obj);
		Serializer<T> serializer = (Serializer<T>) cachedSerializersByAddress.get(address);
		if (serializer == null) {
			Class<T> clazz = (Class<T>)obj.getClass();
			serializer = SerializerFactory.createSerializer(clazz);
			cachedSerializersByAddress.put(address, serializer);
			cachedSerializers.put(clazz, serializer);
		}
		return serializer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void serialize(T obj, OutputStream os) throws SerializationException {
		if (obj != null) {
			Serializer<T> serializer = getSerializer((Class<T>)obj.getClass());
			serializer.serialize(obj, os);
		}	
	}
	
	@Override
	public void release(OutputStream os) throws SerializationException {
		try {
			OutputWriter outputWriter = Serializer.OUTPUT_WRITER_MAP.get(os);
			if (outputWriter != null) {
				outputWriter.release();
				Serializer.OUTPUT_WRITER_MAP.remove(os);
			}
			os.flush();
		}
		catch (Throwable t) {
			logger.error("Error occured while releasing output stream", t);
			throw new SerializationException("Error occured while releasing output stream", t);
		}
	}
	
}
