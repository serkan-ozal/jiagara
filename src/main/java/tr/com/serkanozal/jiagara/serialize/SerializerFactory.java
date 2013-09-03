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

import tr.com.serkanozal.jiagara.domain.model.serialize.SerializationType;
import tr.com.serkanozal.jiagara.serialize.dma.DirectMemoryAccessBasedSerializer;

/**
 * @author Serkan ÖZAL
 */
public class SerializerFactory {

	public static <T> Serializer<T> createSerializer(Class<T> clazz) {
		return createSerializer(clazz, SerializationType.getDefault());
	}
	
	public static <T> Serializer<T> createSerializer(Class<T> clazz, SerializationType serializationType) {
		switch (serializationType) {
			case DIRECT_MEMORY_ACCESS_BASED:
				return new DirectMemoryAccessBasedSerializer<T>(clazz);
			default:	
				return new DirectMemoryAccessBasedSerializer<T>(clazz);
		}
	}
	
}
