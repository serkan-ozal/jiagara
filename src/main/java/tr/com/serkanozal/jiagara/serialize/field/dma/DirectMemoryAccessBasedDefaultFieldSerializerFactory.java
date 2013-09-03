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

package tr.com.serkanozal.jiagara.serialize.field.dma;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;
import tr.com.serkanozal.jiagara.serialize.writer.dma.DirectMemoryAccessBasedOutputWriter;

/**
 * @author Serkan ÖZAL
 */
public class DirectMemoryAccessBasedDefaultFieldSerializerFactory implements DirectMemoryAccessBasedFieldSerializerFactory {

	@Override
	public FieldSerializer<DirectMemoryAccessBasedOutputWriter> createFieldSerializer(Field field) {
		Class<?> fieldClass = field.getClass();
		if (fieldClass.equals(byte.class) || fieldClass.equals(Byte.class)) {
			return new DmaBasedByteFieldSerializer(field);
		}
		else if (fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class)) {
			return new DmaBasedBooleanFieldSerializer(field);
		}
		else if (fieldClass.equals(char.class) || fieldClass.equals(Character.class)) {
			return new DmaBasedCharacterFieldSerializer(field);
		}
		else if (fieldClass.equals(short.class) || fieldClass.equals(Short.class)) {
			return new DmaBasedShortFieldSerializer(field);
		}
		else if (fieldClass.equals(int.class) || fieldClass.equals(Integer.class)) {
			return new DmaBasedIntegerFieldSerializer(field);
		}
		else if (fieldClass.equals(float.class) || fieldClass.equals(Float.class)) {
			return new DmaBasedFloatFieldSerializer(field);
		}
		else if (fieldClass.equals(long.class) || fieldClass.equals(Long.class)) {
			return new DmaBasedLongFieldSerializer(field);
		}
		else if (fieldClass.equals(double.class) || fieldClass.equals(Double.class)) {
			return new DmaBasedDoubleFieldSerializer(field);
		}
		else if (fieldClass.isEnum()) {
			return new DmaBasedEnumFieldSerializer(field);
		}
		else if (fieldClass.isArray()) {
			return new DmaBasedArrayFieldSerializer(field);
		}
		else {
			return new DmaBasedObjectByteFieldSerializer(field);
		}
	}

}
