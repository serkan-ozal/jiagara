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

import org.apache.log4j.Logger;

import tr.com.serkanozal.jiagara.domain.model.context.ClassRegistry;
import tr.com.serkanozal.jiagara.domain.model.context.SerializationContext;
import tr.com.serkanozal.jiagara.serialize.dma.data.DirectMemoryAccessBasedDataSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.field.DirectMemoryAccessBasedFieldSerializer;
import tr.com.serkanozal.jiagara.serialize.dma.writer.DirectMemoryAccessBasedOutputWriter;
import tr.com.serkanozal.jiagara.service.serialize.SerializerService;
import tr.com.serkanozal.jiagara.util.JvmUtil;
import tr.com.serkanozal.jiagara.util.LogUtil;
import tr.com.serkanozal.jiagara.util.ReflectionUtil;
import tr.com.serkanozal.jiagara.util.SerDeConstants;
import sun.misc.Unsafe;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("restriction")
public abstract class AbstractDirectMemoryAccessBasedSerializer<T, O extends DirectMemoryAccessBasedOutputWriter>
		implements DirectMemoryAccessBasedFieldSerializer<T>, DirectMemoryAccessBasedDataSerializer<T> {

	protected static final Logger logger = LogUtil.getLogger();
	
	protected Field field;
	protected Class<?> fieldType;
	protected Unsafe unsafe;
	protected long fieldOffset;
	protected long codeOffsetInClassRegistry;
	protected Class<T> clazz;

	public AbstractDirectMemoryAccessBasedSerializer(Class<T> clazz) {
		this.clazz = clazz;
		unsafe = JvmUtil.getUnsafe();
		codeOffsetInClassRegistry =  unsafe.objectFieldOffset(ReflectionUtil.getField(ClassRegistry.class, "code")); 
	}
	
	public AbstractDirectMemoryAccessBasedSerializer(Field field) {
		this.field = field;
		fieldType = field.getType();
		unsafe = JvmUtil.getUnsafe();
		fieldOffset = unsafe.objectFieldOffset(field);
		codeOffsetInClassRegistry =  unsafe.objectFieldOffset(ReflectionUtil.getField(ClassRegistry.class, "code")); 
	}
	
	protected void writeClass(Class<?> clazz, O ow) {
		SerializationContext context = SerializerService.SERIALIZATION_CONTEXT.get();
		if (context != null) {
			ClassRegistry cr = context.getClassRegistry(clazz);
			if (cr != null) {
				ow.write(SerDeConstants.CLASS_CODE);
				ow.writeInteger(cr, codeOffsetInClassRegistry);
			}
			else {
				cr = context.putClassRegistry(clazz);
				ow.write(SerDeConstants.CLASS_NAME_WIT_CODE);
				ow.writeClassName(clazz);
				ow.writeInteger(cr, codeOffsetInClassRegistry);
			}
		}
		else {
			ow.write(SerDeConstants.CLASS_NAME_WITHOUT_CODE);
			ow.writeClassName(clazz);
		}
	}
	
}
