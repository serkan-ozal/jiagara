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

package tr.com.serkanozal.jiagara.util;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Serkan OZAL
 */
public class PropertiesUtil {

	private static final Logger logger = LogUtil.getLogger();
	
	public static final String DEFAULT_PROPERTIES_DIRECTORY_PATH = "properties";
	public static String PROPERTIES_DIRECTORY_PATH = DEFAULT_PROPERTIES_DIRECTORY_PATH;
	private static final String PROPERTIES_FILE_EXTENSION = ".properties";
	
	private static Map<String, Map<String, String>> cachedProperties = new HashMap<String, Map<String, String>>();
	
	private PropertiesUtil() {
		
	}
	
	private static String preProcessPropertiesFileName(String configFileName) {
		if (configFileName.endsWith(PROPERTIES_FILE_EXTENSION) == false) {
			configFileName += PROPERTIES_FILE_EXTENSION;
		}
		return configFileName;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, String> loadProperties(String propertiesFileName) {
		try {
			propertiesFileName = preProcessPropertiesFileName(propertiesFileName);
			Properties prop = new Properties();
			prop.load(IoUtil.getResourceAsStream(
								PROPERTIES_DIRECTORY_PATH +  File.separator + propertiesFileName));
			Map<String, String> configs = new HashMap<String, String>();
			Enumeration keys = prop.keys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String value = prop.getProperty(key);
				configs.put(key, value);
			}
			return configs;
		} 
		catch (Throwable t) {
			logger.error("Unable to load configs from " + propertiesFileName, t);
			return null;
		}
	}
	
	public static Map<String, String> getProperties(String propertiesFileName) {
		propertiesFileName = preProcessPropertiesFileName(propertiesFileName);
		Map<String, String> configs = cachedProperties.get(propertiesFileName);
		if (configs == null) {
			configs = loadProperties(propertiesFileName);
			cachedProperties.put(propertiesFileName, configs);
		}
		return configs;
	}
	
	public static String getProperty(String propertiesFileName, String key) {
		return getProperties(propertiesFileName).get(key);
	}
	
}
