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

import tr.com.serkanozal.jiagara.domain.model.context.SerializationContext;
import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.serialize.Serializer;
import tr.com.serkanozal.jiagara.serialize.SerializerFactory;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import tr.com.serkanozal.jiagara.util.LogUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;

/**
 * @author Serkan ÖZAL
 */
public class SerializerServiceImpl implements SerializerService {

	private static final Logger logger = LogUtil.getLogger();
	
	private Map<Class<?>, Serializer<?, ? extends OutputWriter>> cachedSerializers = 
			new HashMap<Class<?>, Serializer<?, ? extends OutputWriter>>();
	private Map<Long, Serializer<?, ? extends OutputWriter>> cachedSerializersByAddress = 
			new HashMap<Long, Serializer<?, ? extends OutputWriter>>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T, O extends OutputWriter> Serializer<T, O> getSerializer(Class<T> clazz) {
		Serializer<T, O> serializer = (Serializer<T, O>) cachedSerializers.get(clazz);
		if (serializer == null) {
			serializer = SerializerFactory.createSerializer(clazz);
			cachedSerializers.put(clazz, serializer);
		}
		return serializer;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private <T, O extends OutputWriter> Serializer<T, O> getSerializer(T obj) {
		long address = JvmUtil.addressOfClass(obj);
		Serializer<T, O> serializer = (Serializer<T, O>) cachedSerializersByAddress.get(address);
		if (serializer == null) {
			Class<T> clazz = (Class<T>)obj.getClass();
			serializer = SerializerFactory.createSerializer(clazz);
			cachedSerializers.put(clazz, serializer);
			cachedSerializersByAddress.put(address, serializer);
		}
		return serializer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void serialize(T obj, OutputStream os) throws SerializationException {
		boolean serializationContextAdded = false;
		try {
			if (SERIALIZATION_CONTEXT.get() == null) {
				SERIALIZATION_CONTEXT.set(new SerializationContext());
				serializationContextAdded = true;
			}
			if (obj != null) {
				Serializer<T, OutputWriter> serializer = getSerializer((Class<T>)obj.getClass());
				serializer.serialize(obj, os);
			}
			else {
				os.write(SerDeConstants.NULL);
			}
		}
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while releasing output stream", t);
			throw new SerializationException("Error occured while releasing output stream", t);
		}
		finally {
			if (serializationContextAdded) {
				SERIALIZATION_CONTEXT.remove();
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void serialize(T obj, OutputWriter ow) throws SerializationException {
		boolean serializationContextAdded = false;
		try {
			if (SERIALIZATION_CONTEXT.get() == null) {
				SERIALIZATION_CONTEXT.set(new SerializationContext());
				serializationContextAdded = true;
			}
			if (obj != null) {
				Serializer<T, OutputWriter> serializer = getSerializer((Class<T>)obj.getClass());
				serializer.serialize(obj, ow);
			}	
			else {
				ow.write(SerDeConstants.NULL);
			}
		}
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while releasing output stream", t);
			throw new SerializationException("Error occured while releasing output stream", t);
		}
		finally {
			if (serializationContextAdded) {
				SERIALIZATION_CONTEXT.remove();
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void serializeContent(T obj, OutputStream os) throws SerializationException {
		boolean serializationContextAdded = false;
		try {
			if (SERIALIZATION_CONTEXT.get() == null) {
				SERIALIZATION_CONTEXT.set(new SerializationContext());
				serializationContextAdded = true;
			}
			if (obj != null) {
				Serializer<T, OutputWriter> serializer = getSerializer((Class<T>)obj.getClass());
				serializer.serializeContent(obj, os);
			}
			else {
				os.write(SerDeConstants.NULL);
			}
		}
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while releasing output stream", t);
			throw new SerializationException("Error occured while releasing output stream", t);
		}
		finally {
			if (serializationContextAdded) {
				SERIALIZATION_CONTEXT.remove();
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void serializeContent(T obj, OutputWriter ow) throws SerializationException {
		boolean serializationContextAdded = false;
		try {
			if (SERIALIZATION_CONTEXT.get() == null) {
				SERIALIZATION_CONTEXT.set(new SerializationContext());
				serializationContextAdded = true;
			}
			if (obj != null) {
				Serializer<T, OutputWriter> serializer = getSerializer((Class<T>)obj.getClass());
				serializer.serializeContent(obj, ow);
			}	
			else {
				ow.write(SerDeConstants.NULL);
			}
		}
		catch (SerializationException e) {
			throw e;
		}
		catch (Throwable t) {
			logger.error("Error occured while releasing output stream", t);
			throw new SerializationException("Error occured while releasing output stream", t);
		}
		finally {
			if (serializationContextAdded) {
				SERIALIZATION_CONTEXT.remove();
			}	
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
