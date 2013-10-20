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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serkan ÖZAL
 */
public class SerializationContext {

	private Map<Class<?>, Integer> classCodeMap = new HashMap<Class<?>, Integer>();
	private Map<Integer, ClassRegistry> registeredClasses = new HashMap<Integer, ClassRegistry>();
	private int lastUsedClassCode = 0;
	
	public Map<Integer, ClassRegistry> getRegisteredClasses() {
		return registeredClasses;
	}
	
	public void setRegisteredClasses(Map<Integer, ClassRegistry> registeredClasses) {
		this.registeredClasses = registeredClasses;
	}
	
	public ClassRegistry getClassRegistry(Class<?> clazz) {
		Integer classCode = classCodeMap.get(clazz);
		if (classCode == null) {
			return null;
		}
		else {
			return registeredClasses.get(classCode);
		}	
	}
	
	public ClassRegistry getClassRegistry(int code) {
		return registeredClasses.get(code);
	}
	
	public ClassRegistry putClassRegistry(Class<?> clazz) {
		ClassRegistry cr = new ClassRegistry(clazz);
		registeredClasses.put(++lastUsedClassCode, cr);
		classCodeMap.put(clazz, lastUsedClassCode);
		return cr;
	}
	
	public ClassRegistry putClassRegistry(int code, Class<?> clazz) {
		ClassRegistry cr = new ClassRegistry(code, clazz);
		registeredClasses.put(code, cr);
		classCodeMap.put(clazz, code);
		return cr;
	}
	
	public Integer getClassCode(Class<?> clazz) {
		return classCodeMap.get(clazz);
	}
	
}
