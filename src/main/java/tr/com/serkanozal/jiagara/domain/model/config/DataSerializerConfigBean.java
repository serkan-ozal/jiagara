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

import tr.com.serkanozal.jiagara.serialize.data.DataSerializer;

/**
 * @author Serkan ÖZAL
 */
public class DataSerializerConfigBean implements ConfigBean {

	private Class<?> clazz;
	@SuppressWarnings("rawtypes")
	private Class<? extends DataSerializer> dataSerializerClass;
	
	public DataSerializerConfigBean() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public DataSerializerConfigBean(Class<?> clazz, Class<? extends DataSerializer> dataSerializerClass) {
		this.clazz = clazz;
		this.dataSerializerClass = dataSerializerClass;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends DataSerializer> getDataSerializerClass() {
		return dataSerializerClass;
	}
	
	@SuppressWarnings("rawtypes")
	public void setDataSerializerClass(Class<? extends DataSerializer> dataSerializerClass) {
		this.dataSerializerClass = dataSerializerClass;
	}
	
}
