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

package tr.com.serkanozal.jiagara.serialize.field;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import tr.com.serkanozal.jiagara.config.ConfigManager;
import tr.com.serkanozal.jiagara.config.ConfigManagerFactory;
import tr.com.serkanozal.jiagara.domain.model.config.FieldSerializerConfigBean;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;
import tr.com.serkanozal.jiagara.util.LogUtil;

/**
 * @author Serkan ÖZAL
 */
public abstract class AbstractFieldSerializerFactory<O extends OutputWriter> implements FieldSerializerFactory<O> {

	protected static final Logger logger = LogUtil.getLogger();
	
	protected ConfigManager configManager = ConfigManagerFactory.getConfigManager();
	
	protected <T> FieldSerializer<T, OutputWriter> getConfiguredFieldSerializer(Field field) {
		FieldSerializerConfigBean configBean = configManager.getFieldSerializerConfigBean(field);
		if (configBean != null) {
			@SuppressWarnings("unchecked")
			Class<? extends FieldSerializer<T, OutputWriter>> fieldSerializerClass = 
					(Class<? extends FieldSerializer<T, OutputWriter>>) configBean.getFieldSerializerClass();
			try {
				return fieldSerializerClass.newInstance();
			}
			catch (Throwable t) {
				logger.error("Unable to create field serializer from class " + fieldSerializerClass, t);
				return null;
			}
		}
		else {
			return null;
		}
	}
	
}
