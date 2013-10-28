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

package tr.com.serkanozal.jiagara.config.annotation;

import java.lang.reflect.Field;

import tr.com.serkanozal.jiagara.config.ConfigManager;
import tr.com.serkanozal.jiagara.domain.builder.config.DataSerializerConfigBeanBuilder;
import tr.com.serkanozal.jiagara.domain.builder.config.FieldSerializerConfigBeanBuilder;
import tr.com.serkanozal.jiagara.domain.model.config.DataSerializerConfigBean;
import tr.com.serkanozal.jiagara.domain.model.config.FieldSerializerConfigBean;

/**
 * @author Serkan ÖZAL
 */
public class AnnotationBasedConfigManager implements ConfigManager {

	@Override
	public FieldSerializerConfigBean getFieldSerializerConfigBean(Field field) {
		FieldSerializerConfig fieldSerializerConfig = field.getAnnotation(FieldSerializerConfig.class);
		if (fieldSerializerConfig != null) {
			return 
				new FieldSerializerConfigBeanBuilder().
						field(field).
						fieldSerializerClass(fieldSerializerConfig.fieldSerializerClass()).
					build();
		}
		else {
			return null;
		}
	}

	@Override
	public <T> DataSerializerConfigBean getDataSerializerConfigBean(Class<T> clazz) {
		DataSerializerConfig dataSerializerConfig = clazz.getAnnotation(DataSerializerConfig.class);
		if (dataSerializerConfig != null) {
			return 
				new DataSerializerConfigBeanBuilder().
						clazz(clazz).
						dataSerializerClass(dataSerializerConfig.dataSerializerClass()).
					build();
		}
		else {
			return null;
		}
	}

}
