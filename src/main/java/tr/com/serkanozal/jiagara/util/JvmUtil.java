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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.RuntimeMBeanException;
import javax.management.openmbean.CompositeDataSupport;

import org.apache.log4j.Logger;

import sun.management.VMManagement;
import sun.misc.Unsafe;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * 
 * @author Serkan ÖZAL
 * 
 * @link http://hg.openjdk.java.net/jdk7/hotspot/hotspot/file/9b0ca45cd756/src/share/vm/oops/oop.hpp
 * @link http://hg.openjdk.java.net/jdk7/hotspot/hotspot/file/9b0ca45cd756/src/share/vm/oops/klass.hpp
 * 
 * @link https://blogs.oracle.com/jrockit/entry/understanding_compressed_refer
 * @link https://wikis.oracle.com/display/HotSpotInternals/CompressedOops
 * 
 * Note: Use "-XX:-UseCompressedOops" for 64 bit JVM to disable CompressedOops
 */
@SuppressWarnings("restriction")
public class JvmUtil {
	
	public static final String JAVA_1_6 = "1.6";
	public static final String JAVA_1_7 = "1.7";
	
	public static final String JAVA_VERSION = System.getProperty("java.version");
	public static final String JAVA_SPEC_VERSION = System.getProperty("java.specification.version");
	public static final String JAVA_RUNTIME_VERSION = System.getProperty("java.runtime.version");
	public static final String JAVA_VENDOR = System.getProperty("java.vendor");
	public static final String JVM_VENDOR = System.getProperty("java.vm.vendor");
	public static final String JVM_VERSION = System.getProperty("java.vm.version");
	public static final String JVM_NAME = System.getProperty("java.vm.name");
	public static final String OS_ARCH = System.getProperty("os.arch");
	public static final String OS_NAME = System.getProperty("os.name");
	public static final String OS_VERSION = System.getProperty("os.version");
	
	public static final JavaVersionInfo JAVA_VERSION_INFO = findJavaVersionInfo();
	  
	public static final byte SIZE_32_BIT = 4;
    public static final byte SIZE_64_BIT = 8;
    public static final byte INVALID_ADDRESS = -1;
    
    public static final byte ADDRESSING_4_BYTE = 4;
    public static final byte ADDRESSING_8_BYTE = 8;
    public static final byte ADDRESSING_16_BYTE = 16;

    public static final int NR_BITS = Integer.valueOf(System.getProperty("sun.arch.data.model"));
    public static final int BYTE = 8;
    public static final int WORD = NR_BITS / BYTE;
    public static final int MIN_SIZE = 16; 
    
    public static final int ADDRESS_SHIFT_SIZE_FOR_BETWEEN_32GB_AND_64_GB = 3; 
    public static final int ADDRESS_SHIFT_SIZE_FOR_BIGGER_THAN_64_GB = 4; 
    
    public static final int OBJECT_HEADER_SIZE_32_BIT = 8; 
    public static final int OBJECT_HEADER_SIZE_64_BIT = 12; 
    
    public static final int CLASS_DEF_POINTER_OFFSET_IN_OBJECT_FOR_32_BIT = 4;
    public static final int CLASS_DEF_POINTER_OFFSET_IN_OBJECT_FOR_64_BIT = 8;
    
    public static final int CLASS_DEF_POINTER_OFFSET_IN_CLASS_32_BIT_FOR_JAVA_1_6 = 8; 
    public static final int CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITH_COMPRESSED_REF_FOR_JAVA_1_6 = 12;
    public static final int CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITHOUT_COMPRESSED_REF_FOR_JAVA_1_6 = 16;
    
    public static final int CLASS_DEF_POINTER_OFFSET_IN_CLASS_32_BIT_FOR_JAVA_1_7 = 80; 
    public static final int CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITH_COMPRESSED_REF_FOR_JAVA_1_7 = 84;
    public static final int CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITHOUT_COMPRESSED_REF_FOR_JAVA_1_7 = 160;
    
    public static final int SIZE_FIELD_OFFSET_IN_CLASS_32_BIT = 12;
    public static final int SIZE_FIELD_OFFSET_IN_CLASS_64_BIT = 24;
    
    public static final int BOOLEAN_SIZE = 1;
    public static final int BYTE_SIZE = Byte.SIZE / BYTE;
    public static final int CHAR_SIZE = Character.SIZE / BYTE;
    public static final int SHORT_SIZE = Short.SIZE / BYTE;
    public static final int INT_SIZE = Integer.SIZE / BYTE;
    public static final int FLOAT_SIZE = Float.SIZE / BYTE;
    public static final int LONG_SIZE = Long.SIZE / BYTE;
    public static final int DOUBLE_SIZE = Double.SIZE / BYTE;
    
    private static final Logger logger = Logger.getLogger(JvmUtil.class);
    
    private static VMOptions options;
    private static Unsafe unsafe;
    private static Object[] objArray;
    private static int addressSize;
    private static int headerSize;
    private static int arrayHeaderSize;
    private static long baseOffset;
    private static int indexScale;
	private static int classDefPointerOffsetInObject;
    private static int classDefPointerOffsetInClass;
    private static int sizeFieldOffsetOffsetInClass;
    
    private static JvmAwareUtil jvmAwareUtil;
    
    private static final Map<Class<?>, ClassInfo> classCache = new HashMap<Class<?>, ClassInfo>();
    private static final Map<Class<?>, Map<String, Field>> classFieldCache = new HashMap<Class<?>, Map<String, Field>>();
    private static final Map<Class<?>, Map<Field, Long>> classFieldOffsetCache = new HashMap<Class<?>, Map<Field, Long>>();
    
    static {
    	init();
    }
	
	private static void init() {
        if (isJavaVersionSupported() == false) {
        	throw new AssertionError("Java version is not supported: " + JAVA_SPEC_VERSION); 
        }
		
		try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } 
        catch (NoSuchFieldException e) {
        	throw new RuntimeException("Unable to get unsafe", e);
        } 
        catch (IllegalAccessException e) {
        	throw new RuntimeException("Unable to get unsafe", e);
        }

        objArray = new Object[2];
        
        int headerSize;
        try {
            long off1 = unsafe.objectFieldOffset(HeaderClass.class.getField("b1"));
            headerSize = (int) off1;
        } 
        catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to calculate header size", e);
        }

        JvmUtil.addressSize = unsafe.addressSize();
        JvmUtil.baseOffset = unsafe.arrayBaseOffset(Object[].class);
        JvmUtil.indexScale = unsafe.arrayIndexScale(Object[].class);
        JvmUtil.headerSize = headerSize;
        JvmUtil.arrayHeaderSize = headerSize + indexScale;
        JvmUtil.options = findOptions();

        switch (addressSize) {
            case SIZE_32_BIT:
            	JvmUtil.classDefPointerOffsetInObject = CLASS_DEF_POINTER_OFFSET_IN_OBJECT_FOR_32_BIT;
            	if (isJava_1_6()) {
            		JvmUtil.classDefPointerOffsetInClass = CLASS_DEF_POINTER_OFFSET_IN_CLASS_32_BIT_FOR_JAVA_1_6;
            	}	
            	else if (isJava_1_7()) {
            		JvmUtil.classDefPointerOffsetInClass = CLASS_DEF_POINTER_OFFSET_IN_CLASS_32_BIT_FOR_JAVA_1_7;
            	}
            	JvmUtil.sizeFieldOffsetOffsetInClass = SIZE_FIELD_OFFSET_IN_CLASS_32_BIT;
            	jvmAwareUtil = new Address32BitJvmUtil();
                break;
            case SIZE_64_BIT:
            	JvmUtil.classDefPointerOffsetInObject = CLASS_DEF_POINTER_OFFSET_IN_OBJECT_FOR_64_BIT;
            	if (isJava_1_6()) {
            		if (options.compressedRef) {
            			JvmUtil.classDefPointerOffsetInClass = 
            					CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITH_COMPRESSED_REF_FOR_JAVA_1_6;
            			jvmAwareUtil = new Address64BitWithCompressedOopsJvmUtil();
            		}
            		else {
            			JvmUtil.classDefPointerOffsetInClass = 
            					CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITHOUT_COMPRESSED_REF_FOR_JAVA_1_6;
            			jvmAwareUtil = new Address64BitWithoutCompressedOopsJvmUtil();
            		}
            	}
            	else if (isJava_1_7()) {
            		if (options.compressedRef) {
            			JvmUtil.classDefPointerOffsetInClass = 
            					CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITH_COMPRESSED_REF_FOR_JAVA_1_7;
            			jvmAwareUtil = new Address64BitWithCompressedOopsJvmUtil();
            		}
            		else {
            			JvmUtil.classDefPointerOffsetInClass = 
            					CLASS_DEF_POINTER_OFFSET_IN_CLASS_64_BIT_WITHOUT_COMPRESSED_REF_FOR_JAVA_1_7;
            			jvmAwareUtil = new Address64BitWithoutCompressedOopsJvmUtil();
            		}
            	}
            	else {
            		throw new AssertionError("Java version is not supported: " + JAVA_SPEC_VERSION); 
            	}
            	JvmUtil.sizeFieldOffsetOffsetInClass = SIZE_FIELD_OFFSET_IN_CLASS_64_BIT;
                break;
            default:
            	throw new AssertionError("Unsupported address size: " + addressSize); 
        }        
    }
	
	public static Unsafe getUnsafe() {
		return unsafe;
	}
	
	private static JavaVersionInfo findJavaVersionInfo() {
		if (JAVA_SPEC_VERSION.equals(JAVA_1_6)) {
			return JavaVersionInfo.JAVA_VERSION_1_6;
		}
		else if (JAVA_SPEC_VERSION.equals(JAVA_1_7)) {
			return JavaVersionInfo.JAVA_VERSION_1_7;
		}
		else {
			throw new AssertionError("Java version is not supported: " + JAVA_SPEC_VERSION); 
		}
	}

	public static boolean isJava_1_6() {
		return JAVA_VERSION_INFO == JavaVersionInfo.JAVA_VERSION_1_6;
	}

	public static boolean isJava_1_7() {
		return JAVA_VERSION_INFO == JavaVersionInfo.JAVA_VERSION_1_7;
	}
	
	public static boolean isJavaVersionSupported() {
		return isJava_1_6() || isJava_1_7();
	}
	
	public static VMOptions getOptions() {
		return options;
	}
	
	public static int getAddressSize() {
		return addressSize;
	}
	
	public static boolean isAddressSizeSupported() {
		return addressSize == SIZE_32_BIT || addressSize == SIZE_64_BIT;
	}
	
	public static int getHeaderSize() {
		return headerSize;
	}
	
	public static int getArrayHeaderSize() {
		return arrayHeaderSize;
	}
	
	public static long getBaseOffset() {
		return baseOffset;
	}
	
	public static int getIndexScale() {
		return indexScale;
	}

	public static int getClassDefPointerOffsetInClass() {
		return classDefPointerOffsetInClass;
	}
	
	public static int getClassDefPointerOffsetInObject() {
		return classDefPointerOffsetInObject;
	}
	
	public static int getSizeFieldOffsetOffsetInClass() {
		return sizeFieldOffsetOffsetInClass;
	}
	
	public static boolean isCompressedRef() {
		return options.compressedRef;
	}
	
	public static int getReferenceSize() {
		return options.referenceSize;
	}
	
	public static int getObjectAlignment() {
		return options.objectAlignment;
	}
	
	public static int getCompressedReferenceShift() {
		return options.compressRefShift;
	}
	
	public static String getVmName() {
		return options.name;
	}
	
    public static long normalize(int value) {
        if (value >= 0) {
            return value;
        }    
        else {
            return (~0L >>> 32) & value;
        }    
    }
    
    public static long internalAddressOf(Object obj) {
        return normalize(System.identityHashCode(obj));
    }
    
    private interface JvmAwareUtil {
    	
    	long addressOf(Object obj);
    	long addressOfClass(Object o);
    	long jvmAddressOf(Object obj);
    	long jvmAddressOfClass(Object o);
    	long addressOfClassBase(Class<?> clazz);
    	long addressOfClassInternal(Class<?> clazz);
    	long sizeOfWithUnsafe(Object obj);
    	int getArrayLength(long arrayStartAddress, Class<?> elementType);
    	void setArrayLength(long arrayStartAddress, Class<?> elementType, int length);
    	
    }
    
    private static abstract class BaseJvmAwaretil implements JvmAwareUtil {

    	@Override
		public long sizeOfWithUnsafe(Object obj) {
			if (obj == null) {
				return 0;
			}    
			else {
				long classAddress = JvmUtil.addressOfClassBase(obj.getClass());
				return unsafe.getInt(classAddress + sizeFieldOffsetOffsetInClass);
			}
		}
    	
    }
    
    private static class Address32BitJvmUtil extends BaseJvmAwaretil {

		@Override
		public long addressOf(Object obj) {
			if (obj == null) {
	            return 0;
	        }
	        objArray[0] = obj;
	        return unsafe.getInt(objArray, baseOffset);
		}

		@SuppressWarnings("deprecation")
		@Override
		public long addressOfClass(Object o) {
			return normalize(unsafe.getInt(o, classDefPointerOffsetInObject));
		}
		
		@Override
		public long jvmAddressOf(Object obj) {
			return addressOf(obj);
		}

		@Override
		public long jvmAddressOfClass(Object o) {
			return addressOfClass(o);
		}

		@Override
		public long addressOfClassBase(Class<?> clazz) {
			long addressOfClass = addressOf(clazz);
	    	if (isJava_1_7()) {
	    		return addressOfClass;
	    	}
	    	return normalize(unsafe.getInt(addressOfClass + classDefPointerOffsetInClass));
		}

		@Override
		public long addressOfClassInternal(Class<?> clazz) {
			long addressOfClass = addressOf(clazz);
	    	return normalize(unsafe.getInt(addressOfClass + classDefPointerOffsetInClass));
		}

		@Override
		public int getArrayLength(long arrayStartAddress, Class<?> elementType) {
			long arrayIndexStartAddress = arrayStartAddress + JvmUtil.arrayBaseOffset(elementType);
			return unsafe.getInt(arrayIndexStartAddress - JvmUtil.arrayLengthSize());
		}

		@Override
		public void setArrayLength(long arrayStartAddress, Class<?> elementType, int length) {
			long arrayIndexStartAddress = arrayStartAddress + JvmUtil.arrayBaseOffset(elementType);
			unsafe.putInt(arrayIndexStartAddress - JvmUtil.arrayLengthSize(), length);
		}

    }
    
    private static abstract class Address64BitJvmUtil extends BaseJvmAwaretil {

    	@Override
    	public int getArrayLength(long arrayStartAddress, Class<?> elementType) {
    		long arrayIndexStartAddress = arrayStartAddress + JvmUtil.arrayBaseOffset(elementType);
    		return (int)unsafe.getLong(arrayIndexStartAddress - JvmUtil.arrayLengthSize());
    	}
    	
    	@Override
		public void setArrayLength(long arrayStartAddress, Class<?> elementType, int length) {
			long arrayIndexStartAddress = arrayStartAddress + JvmUtil.arrayBaseOffset(elementType);
			unsafe.putLong(arrayIndexStartAddress - JvmUtil.arrayLengthSize(), length);
		}

    }
    
    private static class Address64BitWithCompressedOopsJvmUtil extends Address64BitJvmUtil {

		@Override
		public long addressOf(Object obj) {
			if (obj == null) {
	            return 0;
	        }
	        objArray[0] = obj;
	        return JvmUtil.toNativeAddress(normalize(unsafe.getInt(objArray, baseOffset)));
		}

		@SuppressWarnings("deprecation")
		@Override
		public long addressOfClass(Object o) {
			return JvmUtil.toNativeAddress(normalize(unsafe.getInt(o, classDefPointerOffsetInObject)));
		}
		
		@Override
		public long jvmAddressOf(Object obj) {
			if (obj == null) {
	            return 0;
	        }
	        objArray[0] = obj;
	        return normalize(unsafe.getInt(objArray, baseOffset));
		}

		@SuppressWarnings("deprecation")
		@Override
		public long jvmAddressOfClass(Object o) {
			return normalize(unsafe.getInt(o, classDefPointerOffsetInObject));
		}

		@Override
		public long addressOfClassBase(Class<?> clazz) {
			long addressOfClass = addressOf(clazz);
	    	if (isJava_1_7()) {
	    		return addressOfClass;
	    	}
	    	return JvmUtil.toNativeAddress(normalize(unsafe.getInt(addressOfClass + classDefPointerOffsetInClass)));
		}

		@Override
		public long addressOfClassInternal(Class<?> clazz) {
			long addressOfClass = addressOf(clazz);
			return JvmUtil.toNativeAddress(normalize(unsafe.getInt(addressOfClass + classDefPointerOffsetInClass)));
		}

    }
    
    private static class Address64BitWithoutCompressedOopsJvmUtil extends Address64BitJvmUtil {

		@Override
		public long addressOf(Object obj) {
			if (obj == null) {
	            return 0;
	        }
	        objArray[0] = obj;
	        return unsafe.getLong(objArray, baseOffset);
		}

		@SuppressWarnings("deprecation")
		@Override
		public long addressOfClass(Object o) {
			return unsafe.getLong(o, classDefPointerOffsetInObject); 
		}
		
		@Override
		public long jvmAddressOf(Object obj) {
			return addressOf(obj);
		}

		@Override
		public long jvmAddressOfClass(Object o) {
			return addressOfClass(o);
		}

		@Override
		public long addressOfClassBase(Class<?> clazz) {
			long addressOfClass = addressOf(clazz);
	    	if (isJava_1_7()) {
	    		return addressOfClass;
	    	}
	    	return unsafe.getLong(addressOfClass + classDefPointerOffsetInClass); 
		}

		@Override
		public long addressOfClassInternal(Class<?> clazz) {
			long addressOfClass = addressOf(clazz);
			return unsafe.getLong(addressOfClass + classDefPointerOffsetInClass); 
		}

    }
    
    public synchronized static long addressOf(Object obj) {
    	return jvmAwareUtil.addressOf(obj);
    }
    
    public synchronized static long jvmAddressOf(Object obj) {
    	return jvmAwareUtil.jvmAddressOf(obj);
    }
  
    public static Field getField(Class<?> clazz, String fieldName) {
    	Map<String, Field> fieldMap = classFieldCache.get(clazz);
    	if (fieldMap == null) {
    		fieldMap = new HashMap<String, Field>();
    		classFieldCache.put(clazz, fieldMap);
    	}
    	Field field = fieldMap.get(fieldName);
    	if (field == null) {
    		field = ReflectionUtil.getField(clazz, fieldName);
    		fieldMap.put(fieldName, field);
    	}
    	return field;
    }
    
    public static long addressOfField(Object obj, String fieldName) {
    	Class<?> clazz = obj.getClass();
    	Field field = getField(clazz, fieldName);
    	if (field == null) {
    		throw new IllegalArgumentException("Field " + fieldName + " couldn't be found at class " + clazz.getName());
    	}
    	long baseAddress = 0; 
        long fieldOffset = 0;
        if (Modifier.isStatic(field.getModifiers())) {
         	baseAddress = JvmUtil.addressOfClassBase(obj.getClass());
         	fieldOffset = unsafe.staticFieldOffset(field);
        }
        else {
         	baseAddress = addressOf(obj);
         	fieldOffset = unsafe.objectFieldOffset(field);
        }
        return baseAddress + fieldOffset; 
    }
    
    @SuppressWarnings("unused")
	private static long findInstanceFieldOffset(Class<?> clazz, Field field) {
    	Map<Field, Long> fieldOffsetMap = classFieldOffsetCache.get(field);
    	if (fieldOffsetMap == null) {
    		fieldOffsetMap = new HashMap<Field, Long>();
    		classFieldOffsetCache.put(clazz, fieldOffsetMap);
    	}
    	Long fieldOffset = fieldOffsetMap.get(field);
    	if (fieldOffset == null) {
    		fieldOffset = JvmUtil.getUnsafe().objectFieldOffset(field);
    		fieldOffsetMap.put(field, fieldOffset);
    	}
    	return fieldOffset;
    }
    
    @SuppressWarnings("unused")
	private static long findClassFieldOffset(Class<?> clazz, Field field) {
    	Map<Field, Long> fieldOffsetMap = classFieldOffsetCache.get(field);
    	if (fieldOffsetMap == null) {
    		fieldOffsetMap = new HashMap<Field, Long>();
    		classFieldOffsetCache.put(clazz, fieldOffsetMap);
    	}
    	Long fieldOffset = fieldOffsetMap.get(field);
    	if (fieldOffset == null) {
    		fieldOffset = JvmUtil.getUnsafe().staticFieldOffset(field);
    		fieldOffsetMap.put(field, fieldOffset);
    	}
    	return fieldOffset;
    }
    
	public static long addressOfClass(Object o) {
    	return jvmAwareUtil.addressOfClass(o);
    }
	
	public static long jvmAddressOfClass(Object o) {
    	return jvmAwareUtil.jvmAddressOfClass(o);
    }
    
    public static long addressOfClass(Class<?> clazz) {
    	return getClassInfo(clazz).classAddress;
    }
    
    private static long addressOfClassBase(Class<?> clazz) {
    	return jvmAwareUtil.addressOfClassBase(clazz);
    }
    
    private static long addressOfClassInternal(Class<?> clazz) {
    	return jvmAwareUtil.addressOfClassInternal(clazz);
    }
    
    public static boolean isPrimitiveType(Class<?> type) {
    	if (type == boolean.class) { 
        	return true; 
        }
    	else if (type == byte.class) { 
        	return true; 
        }
    	else if (type == char.class) { 
        	return true;
        }
        else if (type == short.class) { 
        	return true;
        }
        else if (type == int.class) { 
        	return true;
        }
        else if (type == float.class) { 
        	return true;
        }
        else if (type == long.class) { 
        	return true;
        }
        else if (type == double.class) { 
        	return true;
        }
        else {
        	return false;
        }	
    }
    
    public static boolean isComplexType(Class<?> type) {
    	return !isPrimitiveType(type);
    }
    
    public static Class<?> primitiveTypeOf(Class<?> type) {
    	if (isPrimitiveType(type)) {
    		return type;
    	}
    	
    	if (type == Boolean.class) { 
        	return boolean.class; 
        }
    	else if (type == Byte.class) { 
        	return byte.class; 
        }
    	else if (type == Character.class) { 
        	return char.class;
        }
        else if (type == Short.class) { 
        	return short.class;
        }
        else if (type == Integer.class) { 
        	return int.class;
        }
        else if (type == Float.class) { 
        	return float.class;
        }
        else if (type == Long.class) { 
        	return long.class;
        }
        else if (type == Double.class) { 
        	return double.class;
        }
        else {
        	return null;
        }	
    }
    
    public static Class<?> complexTypeOf(Class<?> type) {
    	if (type == boolean.class) { 
        	return Boolean.class; 
        }
    	else if (type == byte.class) { 
        	return Byte.class; 
        }
    	else if (type == char.class) { 
        	return Character.class;
        }
        else if (type == short.class) { 
        	return Short.class;
        }
        else if (type == int.class) { 
        	return Integer.class;
        }
        else if (type == float.class) { 
        	return Float.class;
        }
        else if (type == long.class) { 
        	return Long.class;
        }
        else if (type == double.class) { 
        	return Double.class;
        }
        else {
        	return type;
        }	
    }

    public static long sizeOfWithUnsafe(Object obj) {
    	return jvmAwareUtil.sizeOfWithUnsafe(obj);
    }  
    
    public static long sizeOfWithReflection(Class<?> objClass) {
    	List<Field> instanceFields = new LinkedList<Field>();
    	
        do {
            if (objClass == Object.class) {
            	return JvmUtil.MIN_SIZE;
            }
            for (Field f : objClass.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    instanceFields.add(f);
                }    
            }
            
            objClass = objClass.getSuperclass();
        } while (instanceFields.isEmpty());

        long maxOffset = 0;
        for (Field f : instanceFields) {
            long offset = unsafe.objectFieldOffset(f);
            if (offset > maxOffset) {
            	maxOffset = offset; 
            }	
        }
        return (((long) maxOffset / JvmUtil.WORD) + 1) * JvmUtil.WORD; 
    }
    
    public static int sizeOfType(Class<?> type) {
    	if (type == boolean.class) { 
        	return BOOLEAN_SIZE; 
        }
    	else if (type == byte.class) { 
        	return BYTE_SIZE; 
        }
    	else if (type == char.class) { 
        	return CHAR_SIZE;
        }
        else if (type == short.class) { 
        	return SHORT_SIZE;
        }
        else if (type == int.class) { 
        	return INT_SIZE;
        }
        else if (type == float.class) { 
        	return FLOAT_SIZE;
        }
        else if (type == long.class) { 
        	return LONG_SIZE;
        }
        else if (type == double.class) { 
        	return DOUBLE_SIZE;
        }
        else {
        	return options.referenceSize;
        }	
    }
    
	public static int sizeOfArray(Object o) {
        int base = unsafe.arrayBaseOffset(o.getClass());
        int scale = unsafe.arrayIndexScale(o.getClass());
        Class<?> type = o.getClass().getComponentType();
        if (type == boolean.class) {
        	return base + ((boolean[]) o).length * scale;
        }
        else if (type == byte.class) {
        	return base + ((byte[]) o).length * scale;
        }
        else if (type == short.class) {
        	return base + ((short[]) o).length * scale;
        }
        else if (type == char.class) {
        	return base + ((char[]) o).length * scale;
        }
        else if (type == int.class) {
        	return base + ((int[]) o).length * scale;
        }
        else if (type == float.class) {
        	return base + ((float[]) o).length * scale;
        }
        else if (type == long.class) {
        	return base + ((long[]) o).length * scale;
        }
        else if (type == double.class) {
        	return base + ((double[]) o).length * scale;
        }
        else {
        	return base + ((Object[]) o).length * scale;
        }	
    }
	
	public static long sizeOfArray(Class<?> elementClass, long elementCount) {
    	return arrayBaseOffset(elementClass) + (elementCount * arrayIndexScale(elementClass));
    }
	
	public static int arrayBaseOffset(Class<?> elementClass) {
		return getClassInfo(elementClass).arrayBaseOffset;
    }
	
	public static int arrayIndexScale(Class<?> elementClass) {
		return getClassInfo(elementClass).arrayIndexScale;
    }
	
	public static int arrayLengthSize() {
		return options.referenceSize;
	}
	
	public static long getArrayBaseAddress(Object array, Class<?> elementType) {
		Class<?> arrayClass = array.getClass();
		if (arrayClass.isArray() == false) {
			return INVALID_ADDRESS;
		}
		long arrayStartAddress = JvmUtil.addressOf(array);
		return arrayStartAddress + JvmUtil.arrayBaseOffset(elementType);
	}
	
	public static int getArrayLength(long arrayStartAddress, Class<?> elementType) {
		return jvmAwareUtil.getArrayLength(arrayStartAddress, elementType);
	}
	
	public static void setArrayLength(long arrayStartAddress, Class<?> elementType, int length) {
		jvmAwareUtil.setArrayLength(arrayStartAddress, elementType, length);
	}
    
    public static long toNativeAddress(long address) {
    	return options.toNativeAddress(address);
    }
    
    public static long toJvmAddress(long address) {
    	return options.toJvmAddress(address);
    }
    
    public static void dump(long address, long size) {
    	dump(System.out, address, size);
    }
    
    public static void dump(PrintStream ps, long address, long size) {
    	for (int i = 0; i < size; i++) {
    		if (i % 16 == 0) {
				ps.print(String.format("[0x%04x]: ", i));
			}
    		ps.print(String.format("%02x ", unsafe.getByte(address + i)));
			if ((i + 1) % 16 == 0) {
				ps.println();
			}
    	}	
    	ps.println();
    }
    
    public static void dump(Object obj, long size) {
    	for (int i = 0; i < size; i++) {
    		if (i % 16 == 0) {
				System.out.print(String.format("[0x%04x]: ", i));
			}
	    	System.out.print(String.format("%02x ", unsafe.getByte(obj, (long)i)));
			if ((i + 1) % 16 == 0) {
				System.out.println();
			}
    	}	
    	System.out.println();
    }
    
    public static void dump(PrintWriter pw, Object root) {
		Node nodeTree = Node.create(root);
	    printTree(new StringBuilder(), new StringBuilder(), pw, nodeTree);
	}

	public static String dump(Object root) {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    dump(pw, root);
	    pw.flush();
	    return sw.toString();
	}
	  
	private static void printTree(StringBuilder prefix, StringBuilder line, PrintWriter pw, Node node) {
	    line.append(node.getName());
	    pw.println(String.format("%,8d %,8d  %s", node.deepSize, node.shallowSize, line.toString()));
	    line.setLength(0);
	    
	    if (node.hasChildren()) {
	    	int pLen = prefix.length();
	    	for (Iterator<Node> i = node.getChildren().iterator(); i.hasNext();) {
	    		Node next = i.next();
	    		line.append(prefix.toString());
	    		line.append("+- ");
	    		prefix.append(i.hasNext() ? "|  " : "   ");
	    		printTree(prefix, line, pw, next);
	    		prefix.setLength(pLen);
	    	}
	    }
	}
    
    public static String objectMemoryAsString(Object o) {
        final ByteOrder byteOrder = ByteOrder.nativeOrder();
        
        StringBuilder b = new StringBuilder();
        final int obSize = (int) shallowSizeOf(o); 
        
        for (int i = 0; i < obSize; i += 2) {
        	if ((i & 0xf) == 0) {
        		if (i > 0) {
        			b.append("\n");
        		}
        		b.append(String.format("%#06x", i));
        	}
      
        	// We go short by short because J9 fails on odd addresses (everything is aligned, including byte fields.
        	int shortValue = unsafe.getShort(o, (long) i);
      
        	if (byteOrder == ByteOrder.BIG_ENDIAN) {
        		b.append(String.format(" %02x", (shortValue >>> 8) & 0xff));
        		b.append(String.format(" %02x", (shortValue & 0xff)));
        	} 
        	else {
        		b.append(String.format(" %02x", (shortValue & 0xff)));
        		b.append(String.format(" %02x", (shortValue >>> 8) & 0xff));
        	}
        }
        return b.toString();
	}
	
	@SuppressWarnings({"unchecked"})
	public static String fieldsLayoutAsString(Class<?> clazz) {
        TreeMap<Long, String> fields = new TreeMap<Long, String>(); 
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
        	for (Field f : c.getDeclaredFields()) {
        		if (Modifier.isStatic(f.getModifiers()) == false) {
        			fields.put(
        					unsafe.objectFieldOffset(f),
        					f.getDeclaringClass().getSimpleName() + "." + f.getName());
        		}	
        	}
        }
        fields.put(shallowSizeOfInstance(clazz), "#shallowSizeOfInstance(" + clazz.getName() + ")");

        StringBuilder b = new StringBuilder();
        Object [] entries = fields.entrySet().toArray();
        for (int i = 0; i < entries.length; i++) {
        	Map.Entry<Long, String> e    = (Map.Entry<Long, String>) entries[i];
        	Map.Entry<Long, String> next = (i + 1 < entries.length ? (Map.Entry<Long, String>) entries[i + 1] : null);
      
          b.append(String.format(
              "@%02d %2s %s\n", 
              e.getKey(),
              next == null ? "" : next.getKey() - e.getKey(),
              e.getValue()));
        }
        return b.toString();
	}
	
	public static long alignObjectSize(long size) {
		size += (long) options.getObjectAlignment() - 1L;
	    return size - (size % options.getObjectAlignment());
	}

	public static long sizeOf(Object obj) {
	    ArrayList<Object> stack = new ArrayList<Object>();
	    stack.add(obj);
	    return measureSizeOf(stack);
	}

	public static long sizeOfAll(Object... objects) {
	    return sizeOfAll(Arrays.asList(objects));
	}

	public static long sizeOfAll(Iterable<Object> objects) {
	    final ArrayList<Object> stack;
	    if (objects instanceof Collection<?>) {
	    	stack = new ArrayList<Object>(((Collection<?>) objects).size());
	    } 
	    else {
	    	stack = new ArrayList<Object>();
	    }

	    for (Object o : objects) {
	    	stack.add(o);
	    }

	    return measureSizeOf(stack);
	}

	public static long shallowSizeOf(Object obj) {
		if (obj == null) {
			return 0;
		}
	    final Class<?> clz = obj.getClass();
	    if (clz.isArray()) {
	    	return shallowSizeOfArray(obj);
	    } 
	    else {
	    	return sizeOf(clz);
	    }
	}
	
	public static long sizeOf(Class<?> clazz) {
		return getClassInfo(clazz).size;
	}

	public static long shallowSizeOfAll(Object... objects) {
		return shallowSizeOfAll(Arrays.asList(objects));
	}

	public static long shallowSizeOfAll(Iterable<Object> objects) {
	    long sum = 0;
	    for (Object o : objects) {
	    	sum += shallowSizeOf(o);
	    }
	    return sum;
	}

	private static long shallowSizeOfInstance(Class<?> clazz) {
	    if (clazz.isArray()) {
	    	throw new IllegalArgumentException("This method does not work with array classes.");
	    }  
	    if (clazz.isPrimitive()) {
	    	return sizeOfType(clazz);
	    }
	    long size = headerSize;
	    
	    for (;clazz != null; clazz = clazz.getSuperclass()) {
	    	final Field[] fields = clazz.getDeclaredFields();
	    	for (Field f : fields) {
	    		if (!Modifier.isStatic(f.getModifiers())) {
	    			size = adjustForField(size, f);
	    		}
	    	}
	    }
	    return alignObjectSize(size);    
	}
	
	private static long shallowSizeOfArray(Object array) {
		long size = arrayHeaderSize;
	    final int len = Array.getLength(array);
	    if (len > 0) {
	    	Class<?> arrayElementClazz = array.getClass().getComponentType();
	    	if (arrayElementClazz.isPrimitive()) {
	    		size += (long) len * sizeOfType(arrayElementClazz);
	    	} 
	    	else {
	    		size += (long) options.referenceSize * len;
	    	}
	    }
	    return alignObjectSize(size);
	}

	private static long measureSizeOf(ArrayList<Object> stack) {
	    final IdentityHashMap<Long, Object> seen = new IdentityHashMap<Long, Object>();
	    final IdentityHashMap<Class<?>, ClassInfo> classCache = new IdentityHashMap<Class<?>, ClassInfo>();

	    long totalSize = 0;
	    while (!stack.isEmpty()) {
	    	final Object obj = stack.remove(stack.size() - 1);
	    	long id = System.identityHashCode(obj);
	    	if (obj == null || seen.containsKey(id)) {
	    		continue;
	    	}
	    	seen.put(id, obj);

	    	final Class<?> obClazz = obj.getClass();
	    	if (obClazz.isArray()) {
	    		/*
	    		 * Consider an array, possibly of primitive types. Push any of its references to
	    		 * the processing stack and accumulate this array's shallow size. 
	    		 */
	    		long size = arrayHeaderSize;
	    		final int len = Array.getLength(obj);
	    		if (len > 0) {
	    			Class<?> componentClazz = obClazz.getComponentType();
	    			if (componentClazz.isPrimitive()) {
	    				size += (long) len * sizeOfType(componentClazz);
	    			} 
	    			else {
	    				size += (long) options.referenceSize * len;

	    				for (int i = len; --i >= 0 ;) {
	    					final Object o = Array.get(obj, i);
	    					if (o != null && !seen.containsKey(id)) {
	    						stack.add(o);
	    					}
	    				}            
	    			}
	    		}
	    		totalSize += alignObjectSize(size);
	    	} 
	    	else {
	    		/*
	    		 * Consider an object. Push any references it has to the processing stack
	    		 * and accumulate this object's shallow size. 
	    		 */
	    		try {
	    			ClassInfo cachedInfo = classCache.get(obClazz);
	    			if (cachedInfo == null) {
	    				classCache.put(obClazz, cachedInfo = createClassInfo(obClazz));
	    			}

	    			for (Field f : cachedInfo.referenceFields) {
	    				// Fast path to eliminate redundancies.
	    				final Object o = f.get(obj);
	    				if (o != null && !seen.containsKey(id)) {
	    					stack.add(o);
	    				}
	    			}

	    			totalSize += cachedInfo.alignedShallowInstanceSize;
	    		} 
	    		catch (IllegalAccessException e) {
	    			// This should never happen as we enabled setAccessible().
	    			throw new RuntimeException("Reflective field access failed?", e);
	    		}
	    	}
	    }

	    // Help the GC.
	    seen.clear();
	    stack.clear();
	    classCache.clear();

	    return totalSize;
	}
	
	private static ClassInfo createClassInfo(final Class<?> clazz) {
	    ClassInfo cachedInfo;
	    long shallowInstanceSize = headerSize;
	    final ArrayList<Field> referenceFields = new ArrayList<Field>(32);
	    for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
	    	final Field[] fields = c.getDeclaredFields();
	    	for (final Field f : fields) {
	    		if (!Modifier.isStatic(f.getModifiers())) {
	    			shallowInstanceSize = adjustForField(shallowInstanceSize, f);

	    			if (!f.getType().isPrimitive()) {
	    				f.setAccessible(true);
	    				referenceFields.add(f);
	    			}
	    		}
	    	}
	    }

	    long size = shallowSizeOfInstance(clazz);
	    Object array = Array.newInstance(clazz, 0);
        int arrayBaseOffset = unsafe.arrayBaseOffset(array.getClass());
        int arrayIndexScale = unsafe.arrayIndexScale(array.getClass());	
        long classAddress = addressOfClassInternal(clazz);
	    cachedInfo = 
	    	new ClassInfo(	alignObjectSize(shallowInstanceSize), 
	    					referenceFields.toArray(new Field[referenceFields.size()]),
	    					size, arrayBaseOffset, arrayIndexScale, classAddress);
	    return cachedInfo;
	}
	
	private static ClassInfo getClassInfo(final Class<?> clazz) {
		ClassInfo cacheEntry = classCache.get(clazz);
		if (cacheEntry == null) {
			cacheEntry = createClassInfo(clazz);
			classCache.put(clazz, cacheEntry);
		}
	    return cacheEntry;
	}

	private static long adjustForField(long sizeSoFar, final Field f) {
		f.setAccessible(true);
	    final Class<?> type = f.getType();
	    final int fsize = sizeOfType(type);
	    long offsetPlusSize = 0;
		if (Modifier.isStatic(f.getModifiers())) {
			offsetPlusSize = unsafe.staticFieldOffset(f) + fsize;
		}
		else {
			offsetPlusSize = unsafe.objectFieldOffset(f) + fsize;
		}			
		return Math.max(sizeSoFar, offsetPlusSize);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T allocateInstance(Class<T> clazz) {
		try {
			return (T) unsafe.allocateInstance(clazz);
		} 
		catch (InstantiationException e) {
			logger.error("Unable to instantiate class: " + clazz.getName(), e);
			return null;
		}
	}

	public static Class<?> defineClass(byte[] classContents) {
		return unsafe.defineClass(null, classContents, 0, classContents.length);
	}
	
	public static void throwException(Throwable t) {
		unsafe.throwException(t);
	}
	 
    public static String toHexAddress(long address) {
        return "0x" + Long.toHexString(address).toUpperCase();
    }
    
    public static String toBinaryStringAddress(long address) {
        return "0x" + Long.toBinaryString(address).toUpperCase();
    }
	
	public static String getProcessId() throws Exception {
        RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();
        Field jvmField = mxbean.getClass().getDeclaredField("jvm");

        jvmField.setAccessible(true);
        VMManagement management = (VMManagement) jvmField.get(mxbean);
        Method method = management.getClass().getDeclaredMethod("getProcessId");
        method.setAccessible(true);
        Integer processId = (Integer) method.invoke(management);

        return processId.toString();
    }
	
	public static void runGC() {
		for (int i = 0; i < 3; i++) {
			System.gc();
		}
	}
    
    public static void info() {
        System.out.println("JVM Name                   : " + JVM_NAME);
        System.out.println("JVM Version                : " + JVM_VERSION);
        System.out.println("JVM Vendor                 : " + JVM_VENDOR);
        System.out.println("Java Version               : " + JAVA_VERSION);
        System.out.println("Java Specification Version : " + JAVA_SPEC_VERSION);
        System.out.println("Java Runtime Version       : " + JAVA_RUNTIME_VERSION);
        System.out.println("Java Vendor                : " + JAVA_VENDOR);
        System.out.println("OS Architecture            : " + OS_ARCH);
        System.out.println("OS Name                    : " + OS_NAME);
        System.out.println("OS Version                 : " + OS_VERSION);
        System.out.println("Word Size                  : " + WORD + " byte");
        
        System.out.println("Running " + (addressSize * BYTE) + "-bit " + options.name + " VM.");
        if (options.compressedRef) {
        	System.out.println("Using compressed references with " + options.compressRefShift + "-bit shift.");
        }
        System.out.println("Objects are " + options.objectAlignment + " bytes aligned.");
        System.out.println();
    }

    private static VMOptions findOptions() {
        // Try Hotspot
        VMOptions hsOpts = getHotspotSpecifics();
        if (hsOpts != null) {
        	return hsOpts;
        }

        // Try JRockit
        VMOptions jrOpts = getJRockitSpecifics();
        if (jrOpts != null) {
        	return jrOpts;
        }
        
        /*
         * When running with CompressedOops on 64-bit platform, the address size
         * reported by Unsafe is still 8, while the real reference fields are 4 bytes long.
         * Try to guess the reference field size with this naive trick.
         */
        int oopSize;
        try {
            long off1 = unsafe.objectFieldOffset(CompressedOopsClass.class.getField("obj1"));
            long off2 = unsafe.objectFieldOffset(CompressedOopsClass.class.getField("obj2"));
            oopSize = (int) Math.abs(off2 - off1);
        } 
        catch (NoSuchFieldException e) {
            oopSize = -1;
        }

        if (oopSize != unsafe.addressSize()) {
        	switch (oopSize) {
	            case ADDRESSING_8_BYTE:
	            	return new VMOptions("Auto-detected", ADDRESS_SHIFT_SIZE_FOR_BETWEEN_32GB_AND_64_GB);
	            case ADDRESSING_16_BYTE:
	            	return new VMOptions("Auto-detected", ADDRESS_SHIFT_SIZE_FOR_BIGGER_THAN_64_GB);
	            default:
	            	throw new AssertionError("Unsupported address size for compressed reference shifting: " + oopSize); 
        	}    	
        }
        else {
            return new VMOptions("Auto-detected");
        }
    }
    
    private static VMOptions getHotspotSpecifics() {
        String name = System.getProperty("java.vm.name");
        if (!name.contains("HotSpot") && !name.contains("OpenJDK")) {
            return null;
        }

        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();

            try {
                ObjectName mbean = new ObjectName("com.sun.management:type=HotSpotDiagnostic");
                CompositeDataSupport compressedOopsValue = 
                		(CompositeDataSupport) server.invoke(mbean, "getVMOption", 
                				new Object[]{"UseCompressedOops"}, new String[]{"java.lang.String"});
                boolean compressedOops = Boolean.valueOf(compressedOopsValue.get("value").toString());
                if (compressedOops) {
                    // If compressed oops are enabled, then this option is also accessible
                    CompositeDataSupport alignmentValue = 
                    		(CompositeDataSupport) server.invoke(mbean, "getVMOption", 
                    				new Object[]{"ObjectAlignmentInBytes"}, new String[]{"java.lang.String"});
                    int align = Integer.valueOf(alignmentValue.get("value").toString());
                    return new VMOptions("HotSpot", log2p(align));
                } 
                else {
                    return new VMOptions("HotSpot");
                }

            } 
            catch (RuntimeMBeanException iae) {
                return new VMOptions("HotSpot");
            }
        } 
        catch (Exception e) {
        	logger.error("Failed to read HotSpot-specific configuration properly", e);
            return null;
        } 
    }

    private static VMOptions getJRockitSpecifics() {
        String name = System.getProperty("java.vm.name");
        if (!name.contains("JRockit")) {
            return null;
        }

        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            String str = (String) server.invoke(new ObjectName("oracle.jrockit.management:type=DiagnosticCommand"), "execute", new Object[]{"print_vm_state"}, new String[]{"java.lang.String"});
            String[] split = str.split("\n");
            for (String s : split) {
                if (s.contains("CompRefs")) {
                    Pattern pattern = Pattern.compile("(.*?)References are compressed, with heap base (.*?) and shift (.*?)\\.");
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.matches()) {
                        return new VMOptions("JRockit", Integer.valueOf(matcher.group(3)));
                    } 
                    else {
                        return new VMOptions("JRockit");
                    }
                }
            }
            return null;
        } 
        catch (Exception e) {
        	logger.error("Failed to read JRockit-specific configuration properly", e);
            return null;
        }
    }
    
    @SuppressWarnings("unused")
	private static int align(int addr) {
        int align = options.objectAlignment;
        if ((addr % align) == 0) {
            return addr;
        } 
        else {
            return ((addr / align) + 1) * align;
        }
    }

    private static int log2p(int x) {
        int r = 0;
        while ((x >>= 1) != 0) {
            r++;
        }    
        return r;
    }
    
    private static int guessAlignment(int oopSize) {
        final int COUNT = 1000 * 1000;
        Object[] array = new Object[COUNT];
        long[] offsets = new long[COUNT];

        for (int c = 0; c < COUNT - 3; c += 3) {
            array[c + 0] = new MyObject1();
            array[c + 1] = new MyObject2();
            array[c + 1] = new MyObject3();
        }

        for (int c = 0; c < COUNT; c++) {
            offsets[c] = addressOfObject(array[c], oopSize);
        }

        Arrays.sort(offsets);

        Multiset<Integer> sizes = HashMultiset.create();
        for (int c = 1; c < COUNT; c++) {
            sizes.add((int) (offsets[c] - offsets[c - 1]));
        }

        int min = -1;
        for (int s : sizes.elementSet()) {
            if (s <= 0) {
            	continue;
            }
            if (min == -1) {
                min = s;
            } 
            else {
                min = gcd(min, s);
            }
        }

        return min;
    }
    
    @SuppressWarnings("unused")
	private static long addressOfObject(Object o) {
    	return addressOfObject(options.referenceSize);
    }
    
    private static long addressOfObject(Object o, int oopSize) {
        Object[] array = new Object[]{o};

        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        long objectAddress;
        switch (oopSize) {
            case SIZE_32_BIT:
                objectAddress = unsafe.getInt(array, baseOffset);
                break;
            case SIZE_64_BIT:
                objectAddress = unsafe.getLong(array, baseOffset);
                break;
            default:
            	throw new AssertionError("Unsupported address size: " + oopSize); 
        }

        return objectAddress;
    }
    
    private static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    private static final class ClassInfo {
    	
        final long alignedShallowInstanceSize;
        final Field[] referenceFields;
        final long size;
        final int arrayBaseOffset;
        final int arrayIndexScale;
        final long classAddress;
        
		ClassInfo(long alignedShallowInstanceSize, Field[] referenceFields, long size, int arrayBaseOffset, 
				int arrayIndexScale, long classAddress) {
			this.alignedShallowInstanceSize = alignedShallowInstanceSize;
			this.referenceFields = referenceFields;
			this.size = size;
			this.arrayBaseOffset = arrayBaseOffset;
			this.arrayIndexScale = arrayIndexScale;
			this.classAddress = classAddress;
		}

	}
    
    public static class VMOptions {
    	
        private final String name;
        private final boolean compressedRef;
        private final int compressRefShift;
        private final int objectAlignment;
        private final int referenceSize;

        public VMOptions(String name) {
            this.name = name;
            this.referenceSize = unsafe.addressSize();
            this.objectAlignment = guessAlignment(this.referenceSize);
            this.compressedRef = false;
            this.compressRefShift = 0;
        }

        public VMOptions(String name, int shift) {
            this.name = name;
            this.referenceSize = SIZE_32_BIT;
            this.objectAlignment = guessAlignment(this.referenceSize) << shift;
            this.compressedRef = true;
            this.compressRefShift = shift;
        }

        public long toNativeAddress(long address) {
            if (compressedRef) {
                return address << compressRefShift;
            } 
            else {
                return address;
            }
        }
        
        public long toJvmAddress(long address) {
            if (compressedRef) {
                return address >> compressRefShift;
            } 
            else {
                return address;
            }
        }

		public String getName() {
			return name;
		}

		public boolean isCompressedRef() {
			return compressedRef;
		}

		public int getCompressRefShift() {
			return compressRefShift;
		}

		public int getObjectAlignment() {
			return objectAlignment;
		}

		public int getReferenceSize() {
			return referenceSize;
		}

    }
    
    @SuppressWarnings("unused")
    private static class CompressedOopsClass {
        
		public Object obj1;
        public Object obj2;
        
    }

    @SuppressWarnings("unused")
    private static class HeaderClass {
    	
        public boolean b1;
        
    }
    
    private static class MyObject1 {

    }

    @SuppressWarnings("unused")
    private static class MyObject2 {
    	
        private boolean b1;
        
    }

    @SuppressWarnings("unused")
    private static class MyObject3 {
    	
        private int i1;
        
    }
  
	private static class Node {
		
	    private String name;
	    private List<Node> children;
	    
	    private long shallowSize;
	    private long deepSize;
	    
	    public Node(String name, Object delegate) {
	    	this.name = name;
	      
	    	if (delegate != null) {
	    		shallowSize = JvmUtil.shallowSizeOf(delegate);
	    		deepSize = shallowSize;
	    	}
	    }
	    
	    private void addChild(Node node) {
	    	if (children == null) {
	    		children = new ArrayList<Node>();
	    	}
	    	children.add(node);
	    	deepSize += node.deepSize;
	    }
	    
	    public static Node create(Object delegate) {
	    	return create("root", delegate, new IdentityHashMap<Object,Integer>());
	    }

	    public static Node create(String prefix, Object delegate, IdentityHashMap<Object,Integer> seen) {
	    	if (delegate == null) {
	    		throw new IllegalArgumentException();
	    	}
	      
	    	if (seen.containsKey(delegate)) {
	    		return new Node("[seen " + uniqueName(delegate, seen) + "]", null);
	    	}
	    	seen.put(delegate, seen.size());
	      
	    	Class<?> clazz = delegate.getClass();
	    	if (clazz.isArray()) {
	    		Node parent = new Node(prefix + " => " + clazz.getSimpleName(), delegate);
	    		if (clazz.getComponentType().isPrimitive()) {
	    			return parent;
	    		} 
	    		else {
	    			final int length = Array.getLength(delegate);
	    			for (int i = 0; i < length; i++) {
	    				Object value = Array.get(delegate, i);
	    				if (value != null) {
	    					parent.addChild(create("[" + i + "]", value, seen));
	    				}
	    			}
	    			return parent;
	    		}
	    	}
	    	else {
	    		List<Field> declaredFields = new ArrayList<Field>();
	    		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
	    			Field[] fields = c.getDeclaredFields();
	    			AccessibleObject.setAccessible(fields, true);
	    			declaredFields.addAll(Arrays.asList(fields));
	    		}
	    		Collections.sort(declaredFields, 
	    			new Comparator<Field>() {
				          @Override
				          public int compare(Field o1, Field o2) {
				        	  return o1.getName().compareTo(o2.getName());
				          }
	    	  		});
	        
	    		Node parent = new Node(prefix + " => " + uniqueName(delegate, seen), delegate);
	    		for (Field f : declaredFields) {
	    			try {
	    				if (!Modifier.isStatic(f.getModifiers()) && !f.getType().isPrimitive()) {
	    					Object fValue = f.get(delegate);
	    					if (fValue != null) {
	    						parent.addChild(create(f.getType().getSimpleName() + " " + f.getName(), fValue, seen));
	    					} 
	    					else {
	    						parent.addChild(new Node(f.getType().getSimpleName() + " " + f.getName() + " => null", null));
	    					}
	    				}
	    			} 
	    			catch (Exception e) {
	    				throw new RuntimeException(e);
	    			}
	    		}
	    		return parent;
	    	}
	    }
	    
	    private static String uniqueName(Object t, IdentityHashMap<Object,Integer> seen) {
	    	return "<" + t.getClass().getSimpleName() + "#" + seen.get(t) + ">";
	    }
	    
	    public String getName() {
	    	return name;
	    }
	    
	    public boolean hasChildren() {
	    	return children != null && !children.isEmpty();
	    }
	    
	    public List<Node> getChildren() {
	    	return children;
	    }
	    
	}
	
	public enum JavaVersionInfo {
		
		JAVA_VERSION_1_6(JAVA_1_6),
		JAVA_VERSION_1_7(JAVA_1_7);
		
		String name;
		
		JavaVersionInfo(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
    @SuppressWarnings("unchecked")
	public static synchronized <T> T getObject(long address) {
    	address = toJvmAddress(address);
    	
    	switch (addressSize) {
            case SIZE_32_BIT:
                putAsIntAddress(objArray, baseOffset, address);
                break;
            case SIZE_64_BIT:
            	int referenceSize = getReferenceSize();
            	switch (referenceSize) {
                 	case ADDRESSING_4_BYTE:
                 		putAsIntAddress(objArray, baseOffset, address);
                 		break;
                 	case ADDRESSING_8_BYTE:
                 		unsafe.putLong(objArray, baseOffset, address);
                 		break;
                 	default:    
                        throw new AssertionError("Unsupported reference size: " + referenceSize);
            	}
            	break;    
            default:
                throw new AssertionError("Unsupported address size: " + JvmUtil.getAddressSize());
        }       

        return (T) objArray[0];
    }
    
    public static synchronized <T> void setObject(long address, T obj) {
        if (obj == null) {
            switch (addressSize) {
                case SIZE_32_BIT:
                    unsafe.putInt(address, 0);
                    break;
                case SIZE_64_BIT:
                	int referenceSize = getReferenceSize();
                	switch (referenceSize) {
                     	case ADDRESSING_4_BYTE:
                     		unsafe.putInt(address, 0);
                     		break;
                     	case ADDRESSING_8_BYTE:
                     		unsafe.putLong(address, 0L);
                     		break;
                     	default:    
                            throw new AssertionError("Unsupported reference size: " + referenceSize);
                	}
                    break;    
                default:
                    throw new AssertionError("Unsupported address size: " + addressSize);
            }
        }
        else {
            long objSize = sizeOf(obj.getClass());
            long objAddress = addressOf(obj);
            unsafe.copyMemory(objAddress, address, headerSize + objSize);   
        }    
    }
    
    public static synchronized <T> T changeObject(T source, T target) {
        if (source == null) {
            throw new IllegalArgumentException("Source object is null !");
        }
        long targetAddress = addressOf(target);
        setObject(targetAddress, source);
        
        return target;
    }
    
    @SuppressWarnings("unchecked")
	public static synchronized <T> T copyObject(T original) {
    	if (original == null) {
            throw new IllegalArgumentException("Original object is null !");
        }
        long originalAddress = addressOf(original);
        Object[] array = new Object[] {null};
        
        switch (addressSize) {
	        case SIZE_32_BIT:
	        	putAsIntAddress(array, baseOffset, originalAddress);
	            break;
	        case SIZE_64_BIT:
	        	int referenceSize = getReferenceSize();
            	switch (referenceSize) {
                 	case ADDRESSING_4_BYTE:
                 		putAsIntAddress(array, baseOffset, originalAddress);
                 		break;
                 	case ADDRESSING_8_BYTE:
                 		unsafe.putLong(array, baseOffset, originalAddress);
                 		break;
                 	default:    
                        throw new AssertionError("Unsupported reference size: " + referenceSize);
            	}
	            break;    
	        default:
	            throw new AssertionError("Unsupported address size: " + addressSize);
        }

        return (T) array[0];
    }
    
    public static long getAsIntAddress(long address) {
    	return normalize(unsafe.getInt(address));
    }
    
    public static long getAsIntAddress(Object obj, long offset) {
    	return normalize(unsafe.getInt(obj, offset));
    }
    
    public static void putAsIntAddress(long address, long intAddress) {
    	long l = unsafe.getLong(address + INT_SIZE);
 		unsafe.putLong(address, intAddress);
 		unsafe.putLong(address + INT_SIZE, l);
    }
    
    public static void putAsIntAddress(Object obj, long offset, long intAddress) {
    	long l = unsafe.getLong(obj, offset + INT_SIZE);
 		unsafe.putLong(obj, offset, intAddress);
 		unsafe.putLong(obj, offset + INT_SIZE, l);
    }
    
    @SuppressWarnings({ "deprecation" })
	public static byte[] createOffHeapBuffer(int length) {
		Unsafe unsafe = JvmUtil.getUnsafe();
		int elementSize = JvmUtil.sizeOfType(byte.class);
		long arraySize = JvmUtil.sizeOfArray(byte.class, length);
		long allocatedAddress = 
				unsafe.allocateMemory(arraySize + (length * elementSize) + 
									  JvmUtil.getAddressSize()); // Extra memory for possible aligning
		byte[] sampleArray = (byte[]) Array.newInstance(byte.class, 0);
		int arrayHeaderSize = JvmUtil.getArrayHeaderSize();

		// Copy sample array header to object pool array header
		for (int i = 0; i < arrayHeaderSize; i++) {
			unsafe.putByte(allocatedAddress + i, unsafe.getByte(sampleArray, i));
		}
		
		// Set length of array object pool array
		JvmUtil.setArrayLength(allocatedAddress, byte.class, length);

		return (byte[])JvmUtil.getObject(allocatedAddress);
	}
	 
}
