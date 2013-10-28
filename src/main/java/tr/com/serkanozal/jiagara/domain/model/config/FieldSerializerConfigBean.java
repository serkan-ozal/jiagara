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

package tr.com.serkanozal.jiagara.domain.model.config;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;

/**
 * @author Serkan ÖZAL
 */
public class FieldSerializerConfigBean implements ConfigBean {

	private Class<?> clazz;
	private Field field;
	@SuppressWarnings("rawtypes")
	private Class<? extends FieldSerializer> fieldSerializerClass;
	
	public FieldSerializerConfigBean() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public FieldSerializerConfigBean(Class<?> clazz, Field field, Class<? extends FieldSerializer> fieldSerializerClass) {
		this.clazz = clazz;
		this.field = field;
		this.fieldSerializerClass = fieldSerializerClass;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Field getField() {
		return field;
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	@SuppressWarnings("rawtypes")
	public Class<? extends FieldSerializer> getFieldSerializerClass() {
		return fieldSerializerClass;
	}
	
	@SuppressWarnings("rawtypes")
	public void setFieldSerializerClass(Class<? extends FieldSerializer> fieldSerializerClass) {
		this.fieldSerializerClass = fieldSerializerClass;
	}
	
}
