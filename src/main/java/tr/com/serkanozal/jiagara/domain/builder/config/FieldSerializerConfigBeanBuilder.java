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

package tr.com.serkanozal.jiagara.domain.builder.config;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.domain.builder.Builder;
import tr.com.serkanozal.jiagara.domain.model.config.FieldSerializerConfigBean;
import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;

/**
 * @author Serkan ÖZAL
 */
public class FieldSerializerConfigBeanBuilder implements Builder<FieldSerializerConfigBean> {

	private Class<?> clazz;
	private Field field;
	@SuppressWarnings("rawtypes")
	private Class<? extends FieldSerializer> fieldSerializerClass;

	public FieldSerializerConfigBeanBuilder field(Field field) {
		this.field = field;
		this.clazz = field.getDeclaringClass();
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public FieldSerializerConfigBeanBuilder fieldSerializerClass(Class<? extends FieldSerializer> fieldSerializerClass) {
		this.fieldSerializerClass = fieldSerializerClass;
		return this;
	}
	
	@Override
	public FieldSerializerConfigBean build() {
		return new FieldSerializerConfigBean(clazz, field, fieldSerializerClass);
	}
	
}
