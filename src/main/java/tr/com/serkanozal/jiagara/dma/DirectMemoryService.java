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

/**
 * @author Serkan ÖZAL
 */
public interface DirectMemoryService {

	long allocateMemory(long size);
	void freeMemory(long address);
	Object allocateInstance(Class<?> clazz);
	void copyMemory(long sourceAddress, long destinationAddress, long size);

    <T> T getObject(long address);
    <T> void setObject(long address, T obj);
	<T> T setObject(T source, T target);
	<T> T copyObject(T original);
	
	byte getByte(long address);
	void putByte(long address, byte x);
	char getChar(long address);
	void putChar(long address, char x);
	short getShort(long address);
	void putShort(long address, short x);
	int getInt(long address);
	void putInt(long address, int x);
	float getFloat(long address);
	void putFloat(long address, float x);
	long getLong(long address);
	void putLong(long address, long x);
	double getDouble(long address);
	void putDouble(long address, double x);
	long getAddress(long address);
	void putAddress(long address, long x);
	
	boolean getBoolean(Object o, long offset);
	void putBoolean(Object o, long offset, boolean x);
	byte getByte(Object o, long offset);
	void putByte(Object o, long offset, byte x);
	char getChar(Object o, long offset);
	void putChar(Object o, long offset, char x);
	short getShort(Object o, long offset);
	void putShort(Object o, long offset, short x);
	int getInt(Object o, long offset);
	void putInt(Object o, long offset, int x);
	float getFloat(Object o, long offset);
	void putFloat(Object o, long offset, float x);
	long getLong(Object o, long offset);
	void putLong(Object o, long offset, long x);
	double getDouble(Object o, long offset);
	void putDouble(Object o, long offset, double x);
	Object getObject(Object o, long offset);
	void putObject(Object o, long offset, Object x);

}
