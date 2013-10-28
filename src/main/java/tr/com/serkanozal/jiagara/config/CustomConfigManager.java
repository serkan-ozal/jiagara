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

package tr.com.serkanozal.jiagara.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import tr.com.serkanozal.jiagara.domain.builder.config.DataSerializerConfigBeanBuilder;
import tr.com.serkanozal.jiagara.domain.builder.config.FieldSerializerConfigBeanBuilder;
import tr.com.serkanozal.jiagara.domain.model.config.DataSerializerConfigBean;
import tr.com.serkanozal.jiagara.domain.model.config.FieldSerializerConfigBean;
import tr.com.serkanozal.jiagara.serialize.data.DataSerializer;
import tr.com.serkanozal.jiagara.serialize.field.FieldSerializer;

/**
 * @author Serkan ÖZAL
 */
public class CustomConfigManager implements ConfigManager {

	private static final CustomConfigManager customConfigManager = new CustomConfigManager();
	
	private Map<Field, FieldSerializerConfigBean> fieldSerializerConfigMap = 
			new HashMap<Field, FieldSerializerConfigBean>();
	private Map<Class<?>, DataSerializerConfigBean> dataSerializerConfigMap = 
			new HashMap<Class<?>, DataSerializerConfigBean>();

	private CustomConfigManager() {
		
	}
	
	public static CustomConfigManager getInstance() {
		return customConfigManager;
	}
	
	@Override
	public FieldSerializerConfigBean getFieldSerializerConfigBean(Field field) {
		return fieldSerializerConfigMap.get(field);
	}

	@Override
	public <T> DataSerializerConfigBean getDataSerializerConfigBean(Class<T> clazz) {
		return dataSerializerConfigMap.get(clazz);
	}
	
	@SuppressWarnings("rawtypes") 
	public <T> CustomConfigManager registerCustomFieldSerializer(Field field, Class<? extends FieldSerializer> fieldSerializerClass) {
		fieldSerializerConfigMap.
			put(
				field, 
				new FieldSerializerConfigBeanBuilder().
						field(field).
						fieldSerializerClass(fieldSerializerClass).
					build());
		return this;
	}
	
	@SuppressWarnings("rawtypes") 
	public <T> CustomConfigManager registerCustomDataSerializer(Class<?> clazz, Class<? extends DataSerializer> dataSerializerClass) {
		dataSerializerConfigMap.
			put(
				clazz, 
				new DataSerializerConfigBeanBuilder().
						clazz(clazz).
						dataSerializerClass(dataSerializerClass).
					build());
		return this;
	}
	
}
