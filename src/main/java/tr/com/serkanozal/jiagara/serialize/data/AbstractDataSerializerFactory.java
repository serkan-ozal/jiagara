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

package tr.com.serkanozal.jiagara.serialize.data;

import org.apache.log4j.Logger;

import tr.com.serkanozal.jiagara.config.ConfigManager;
import tr.com.serkanozal.jiagara.config.ConfigManagerFactory;
import tr.com.serkanozal.jiagara.domain.model.config.DataSerializerConfigBean;
import tr.com.serkanozal.jiagara.serialize.writer.OutputWriter;
import tr.com.serkanozal.jiagara.util.LogUtil;

/**
 * @author Serkan ÖZAL
 */
public abstract class AbstractDataSerializerFactory<O extends OutputWriter> implements DataSerializerFactory<O> {

	protected static final Logger logger = LogUtil.getLogger();
	
	protected ConfigManager configManager = ConfigManagerFactory.getConfigManager();
	
	protected <T> DataSerializer<T, OutputWriter> getConfiguredDataSerializer(Class<T> clazz) {
		DataSerializerConfigBean configBean = configManager.getDataSerializerConfigBean(clazz);
		if (configBean != null) {
			@SuppressWarnings("unchecked")
			Class<? extends DataSerializer<T, OutputWriter>> dataSerializerClass = 
					(Class<? extends DataSerializer<T, OutputWriter>>) configBean.getDataSerializerClass();
			try {
				return dataSerializerClass.newInstance();
			}
			catch (Throwable t) {
				logger.error("Unable to create data serializer from class " + dataSerializerClass, t);
				return null;
			}
		}
		else {
			return null;
		}
	}
	
}
