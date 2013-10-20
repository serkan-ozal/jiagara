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

package tr.com.serkanozal.jiagara.domain.model.context;

/**
 * @author Serkan ÖZAL
 */
public class ClassRegistry {

	private int code;
	private Class<?> clazz;
	
	public ClassRegistry(Class<?> clazz) {
		this.code = clazz.hashCode();
		this.clazz = clazz;
	}
	
	public ClassRegistry(int code, Class<?> clazz) {
		this.code = code;
		this.clazz = clazz;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public int hashCode() {
		return code;
	}
	
}
