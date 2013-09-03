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

package tr.com.serkanozal.jiagara.dma;

import org.apache.log4j.Logger;

import sun.misc.Unsafe;
import tr.com.serkanozal.jiagara.util.JvmUtil;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings( { "unchecked", "restriction" } )
public class DirectMemoryServiceImpl implements DirectMemoryService {
	
	private final Logger logger = Logger.getLogger(getClass());

    private Unsafe unsafe;
    private Object[] objArray;
    
    public DirectMemoryServiceImpl() {
    	init();
    }
    
    private void init() {
        initUnsafe();
    }
    
    private void initUnsafe() {
        unsafe = JvmUtil.getUnsafe();
        objArray = new Object[1];
    }

    @Override
    public long allocateMemory(long size) {
    	return unsafe.allocateMemory(size);
    }
    
    @Override
    public void freeMemory(long address) {
    	unsafe.freeMemory(address);
    }
    
    @Override
    public Object allocateInstance(Class<?> clazz) {
    	try {
			return unsafe.allocateInstance(clazz);
		} 
    	catch (InstantiationException e) {
    		logger.error("Error at UnsafeBasedOffHeapMemoryServiceImpl.allocateInstance()", e);
    		return null;
		}
    }
    
    @Override
    public void copyMemory(long sourceAddress, long destinationAddress, long size) {
    	unsafe.copyMemory(sourceAddress, destinationAddress, size);
    }
  
    @Override
    public synchronized <T> T getObject(long address) {
    	address = JvmUtil.toJvmAddress(address);
    	
    	switch (JvmUtil.getAddressSize()) {
            case JvmUtil.SIZE_32_BIT:
                unsafe.putInt(objArray, JvmUtil.getBaseOffset(), (int)address);
                break;
            case JvmUtil.SIZE_64_BIT:
                unsafe.putLong(objArray, JvmUtil.getBaseOffset(), address);
                break;    
            default:
                throw new AssertionError("Unsupported index size: " + JvmUtil.getAddressSize());
        }       
    	
        return (T) objArray[0];
    }
    
    @Override
    public synchronized <T> void setObject(long address, T obj) {
        if (obj == null) {
            switch (JvmUtil.getAddressSize()) {
                case JvmUtil.SIZE_32_BIT:
                    unsafe.putInt(address, 0);
                    break;
                case JvmUtil.SIZE_64_BIT:
                    unsafe.putLong(address, 0L);
                    break;    
                default:
                    throw new AssertionError("Unsupported address size: " + JvmUtil.getAddressSize());
            }
        }
        else {
            long objSize = JvmUtil.sizeOf(obj.getClass());
            long objAddress = JvmUtil.addressOf(obj);
            unsafe.copyMemory(objAddress, address, JvmUtil.getHeaderSize() + objSize);   
        }    
    }
    
    @Override
    public synchronized <T> T setObject(T source, T target) {
        if (source == null) {
            throw new IllegalArgumentException("Source object is null !");
        }
        long targetAddress = JvmUtil.addressOf(target);
        setObject(targetAddress, source);
        
        return target;
    }
    
    @Override
    public synchronized <T> T copyObject(T original) {
    	if (original == null) {
            throw new IllegalArgumentException("Original object is null !");
        }
        long originalAddress = JvmUtil.addressOf(original);
        Object[] array = new Object[] {null};
        
        switch (JvmUtil.getAddressSize()) {
	        case JvmUtil.SIZE_32_BIT:
	        	unsafe.putInt(array, JvmUtil.getBaseOffset(), (int)originalAddress);
	            break;
	        case JvmUtil.SIZE_64_BIT:
	        	unsafe.putLong(array, JvmUtil.getBaseOffset(), originalAddress);
	            break;    
	        default:
	            throw new AssertionError("Unsupported address size: " + JvmUtil.getAddressSize());
        }

        return (T) array[0];
    }
    
    @Override
    public byte getByte(long address) {
    	return unsafe.getByte(address);
    }

    @Override
    public void putByte(long address, byte x) {
    	unsafe.putByte(address, x);
    }
    
    @Override
    public char getChar(long address) {
    	return unsafe.getChar(address);
    }

    @Override
    public void putChar(long address, char x) {
    	unsafe.putChar(address, x);
    }

    @Override
    public short getShort(long address) {
    	return unsafe.getShort(address);
    }

    @Override
    public void putShort(long address, short x) {
    	unsafe.putShort(address, x);
    }
   
    @Override
    public int getInt(long address) {
    	return unsafe.getInt(address);
    }

    @Override
    public void putInt(long address, int x) {
    	unsafe.putInt(address, x);
    }
    
    @Override
    public float getFloat(long address) {
    	return unsafe.getFloat(address);
    }

    @Override
    public void putFloat(long address, float x) {
    	unsafe.putFloat(address, x);
    }

    @Override
    public long getLong(long address) {
    	return unsafe.getLong(address);
    }

    @Override
    public void putLong(long address, long x) {
    	unsafe.putLong(address, x);
    }

    @Override
    public double getDouble(long address) {
    	return unsafe.getDouble(address);
    }

    @Override
    public void putDouble(long address, double x) {
    	unsafe.putDouble(address, x);
    }

    @Override
    public long getAddress(long address) {
    	return unsafe.getAddress(address);
    }

    @Override
    public void putAddress(long address, long x) {
    	unsafe.putAddress(address, x);
    }
    
    @Override
    public boolean getBoolean(Object o, long offset) {
    	return unsafe.getBoolean(o, offset);
    }
    
    @Override
    public void putBoolean(Object o, long offset, boolean x) {
    	unsafe.putBoolean(o, offset, x);
    }
    
    @Override
    public byte getByte(Object o, long offset) {
    	return unsafe.getByte(o, offset);
    }
   
    @Override
    public void putByte(Object o, long offset, byte x) {
    	unsafe.putByte(o, offset, x);
    }
    
    @Override
    public char getChar(Object o, long offset) {
    	return unsafe.getChar(o, offset);
    }
    
    @Override
    public void putChar(Object o, long offset, char x) {
    	unsafe.putChar(o, offset, x);
    }
   
    @Override
    public short getShort(Object o, long offset) {
    	return unsafe.getShort(o, offset);
    }
    
    @Override
    public void putShort(Object o, long offset, short x) {
    	unsafe.putShort(o, offset, x);
    }
    
    @Override
    public int getInt(Object o, long offset) {
    	return unsafe.getInt(o, offset);
    }

    @Override
    public void putInt(Object o, long offset, int x) {
    	unsafe.putInt(o, offset, x);
    }

    @Override
    public float getFloat(Object o, long offset) {
    	return unsafe.getFloat(o, offset);
    }
    
    @Override
    public void putFloat(Object o, long offset, float x) {
    	unsafe.putFloat(o, offset, x);
    }
    
    @Override
    public long getLong(Object o, long offset) {
    	return unsafe.getLong(o, offset);
    }
    
    @Override
    public void putLong(Object o, long offset, long x) {
    	unsafe.putLong(o, offset, x);
    }
   
    @Override
    public double getDouble(Object o, long offset) {
    	return unsafe.getDouble(o, offset);
    }
    
    @Override
    public void putDouble(Object o, long offset, double x) {
    	unsafe.putDouble(o, offset, x);
    }
    
    @Override
    public Object getObject(Object o, long offset) {
    	return unsafe.getInt(o, offset);
    }

    @Override
    public void putObject(Object o, long offset, Object x) {
    	unsafe.putObject(o, offset, x);
    }

}
