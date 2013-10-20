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

import tr.com.serkanozal.jiagara.domain.model.context.SerializationContext;
import tr.com.serkanozal.jiagara.exception.SerializationException;
import tr.com.serkanozal.jiagara.serialize.Serializer;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;

/**
 * @author Serkan ÖZAL
 */
public interface SerializerService {

	ThreadLocal<SerializationContext> SERIALIZATION_CONTEXT = new ThreadLocal<SerializationContext>();
	
	<T, O extends OutputWriter> Serializer<T, O> getSerializer(Class<T> clazz);
	<T> void serialize(T obj, OutputStream os) throws SerializationException;
	<T> void serialize(T obj, OutputWriter ow) throws SerializationException;
	<T> void serializeContent(T obj, OutputStream os) throws SerializationException;
	<T> void serializeContent(T obj, OutputWriter ow) throws SerializationException;
	void release(OutputStream os) throws SerializationException;
	
}
