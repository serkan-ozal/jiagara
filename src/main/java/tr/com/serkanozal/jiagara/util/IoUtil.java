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
import java.io.FileDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * @author Serkan OZAL
 */
public class IoUtil {

	private static final Logger logger = LogUtil.getLogger();
	
	private static Method FILE_DISPATCHER_WRITE_METHOD;
	
	static {
		init();
	}
	
	private IoUtil() {
		
	}
	
	private static void init() {
		try {
			Class<?> cls = Class.forName("sun.nio.ch.FileDispatcher");
			FILE_DISPATCHER_WRITE_METHOD = cls.getDeclaredMethod("write", new Class[] {FileDescriptor.class, long.class, int.class});
			FILE_DISPATCHER_WRITE_METHOD.setAccessible(true);
		}
		catch (Throwable t) {
			logger.error("Error occured while initializing IoUtil", t);
		}
	}
	
	public static InputStream getResourceAsStream(String resourcePath) {
		try {
			if (resourcePath.startsWith("/") == false) {
				resourcePath = "/" + resourcePath;
			}
			InputStream is = IoUtil.class.getResourceAsStream(resourcePath);
			if  (is == null) {
				is = getCallerClass().getResourceAsStream(resourcePath);
			}
			return is;
		}
		catch (Throwable t) {
			logger.error("Unable to get resource " + "(" + resourcePath + ")" + " as stream", t);
			return null;
		}
	}
	
	public static File getResourceAsFile(String resourcePath) {
		try {
			if (resourcePath.startsWith("/") == false) {
				resourcePath = "/" + resourcePath;
			}
			URL url = IoUtil.class.getResource(resourcePath);
			if (url == null) {
				url = getCallerClass().getResource(resourcePath);
			}
			return new File(url.toURI());
		} 
		catch (Throwable t) {
			logger.error("Unable to get resource " + "(" + resourcePath + ")" + " as file", t);
			return null;
		}
	}
	
	private static Class<?> getCallerClass() {
		try {
			return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
		} 
		catch (ClassNotFoundException e) {
			logger.error("Unable to get caller class", e);
			return null;
		}
	}
	
	public static void writeDirectly(FileDescriptor fd, long startAddress, int length) {
		try {
			FILE_DISPATCHER_WRITE_METHOD.invoke(null, new Object[] { fd, startAddress, length });
		} 
		catch (Throwable t) {
			logger.error("Error occured while writing to file descriptor " + fd, t);
		} 
	}
	
}
