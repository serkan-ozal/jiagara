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

package tr.com.serkanozal.jiagara.benchmark;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map.Entry;

/**
 * @author Serkan ÖZAL
 */
public class ExternalizableClassToSerialize extends ClassToSerialize implements Externalizable {
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeByte(byteValue);
		out.writeBoolean(booleanValue);
		out.writeChar(charValue);
		out.writeShort(shortValue);
		out.writeInt(intValue);
		out.writeFloat(floatValue);
		out.writeLong(longValue);
		out.writeDouble(doubleValue);
		out.writeBytes(stringValue);
		out.writeInt(enumValue.ordinal());
		
		///////////////////////////////////////////////////////////////////////
		
		out.writeInt(byteArrayValue.length);
		for (byte b : byteArrayValue) {
			out.writeByte(b);
		}
		out.writeInt(booleanArrayValue.length);
		for (boolean b : booleanArrayValue) {
			out.writeBoolean(b);
		}
		out.writeInt(charArrayValue.length);
		for (char c : charArrayValue) {
			out.writeChar(c);
		}
		out.writeInt(shortArrayValue.length);
		for (short s : shortArrayValue) {
			out.writeShort(s);
		}
		out.writeInt(intArrayValue.length);
		for (int i : intArrayValue) {
			out.writeInt(i);
		}
		out.writeInt(floatArrayValue.length);
		for (float f : floatArrayValue) {
			out.writeFloat(f);
		}
		out.writeInt(longArrayValue.length);
		for (long l : longArrayValue) {
			out.writeLong(l);
		}
		out.writeInt(doubleArrayValue.length);
		for (double d : doubleArrayValue) {
			out.writeDouble(d);
		}
		
		///////////////////////////////////////////////////////////////////////
		
		out.writeInt(byteObjectArrayValue.length);
		for (byte b : byteObjectArrayValue) {
			out.writeByte(b);
		}
		out.writeInt(booleanObjectArrayValue.length);
		for (boolean b : booleanObjectArrayValue) {
			out.writeBoolean(b);
		}
		out.writeInt(charObjectArrayValue.length);
		for (char c : charObjectArrayValue) {
			out.writeChar(c);
		}
		out.writeInt(shortObjectArrayValue.length);
		for (short s : shortObjectArrayValue) {
			out.writeShort(s);
		}
		out.writeInt(intObjectArrayValue.length);
		for (int i : intObjectArrayValue) {
			out.writeInt(i);
		}
		out.writeInt(floatObjectArrayValue.length);
		for (float f : floatObjectArrayValue) {
			out.writeFloat(f);
		}
		out.writeInt(longObjectArrayValue.length);
		for (long l : longObjectArrayValue) {
			out.writeLong(l);
		}
		out.writeInt(doubleObjectArrayValue.length);
		for (double d : doubleObjectArrayValue) {
			out.writeDouble(d);
		}
		
		///////////////////////////////////////////////////////////////////////
		
		out.writeBytes(stringValue);
		out.writeLong(dateValue.getTime());
		out.writeInt(enumValue.ordinal());
		aggregatedClassValue.serialize(out);
		
		///////////////////////////////////////////////////////////////////////
	
		out.writeInt(byteValueCollection.size());
		for (byte b : byteValueCollection) {
			out.writeByte(b);
		}
		out.writeInt(booleanValueCollection.size());
		for (boolean b : booleanValueCollection) {
			out.writeBoolean(b);
		}
		out.writeInt(charValueCollection.size());
		for (char c : charValueCollection) {
			out.writeChar(c);
		}
		out.writeInt(shortValueCollection.size());
		for (short s : shortValueCollection) {
			out.writeShort(s);
		}
		out.writeInt(intValueCollection.size());
		for (int i : intValueCollection) {
			out.writeInt(i);
		}
		out.writeInt(floatValueCollection.size());
		for (float f : floatValueCollection) {
			out.writeFloat(f);
		}
		out.writeInt(longValueCollection.size());
		for (long l : longValueCollection) {
			out.writeLong(l);
		}
		out.writeInt(doubleValueCollection.size());
		for (double d : doubleValueCollection) {
			out.writeDouble(d);
		}
		out.writeInt(stringValueCollection.size());
		for (String str : stringValueCollection) {
			out.writeBytes(str);
		}
		out.writeInt(enumValueCollection.size());
		for (EnumToSerialize e : enumValueCollection) {
			out.writeInt(e.ordinal());
		}
		out.writeInt(aggregatedClassValueCollection.size());
		for (AggregatedClassToSerialize a : aggregatedClassValueCollection) {
			a.serialize(out);
		}
		
		///////////////////////////////////////////////////////////////////////
		
		out.writeInt(byteValueMap.size());
		for (Entry<String, Byte> ent : byteValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeByte(ent.getValue());
		}
		out.writeInt(booleanValueMap.size());
		for (Entry<String, Boolean> ent : booleanValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeBoolean(ent.getValue());
		}
		out.writeInt(charValueMap.size());
		for (Entry<String, Character> ent : charValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeChar(ent.getValue());
		}
		out.writeInt(shortValueMap.size());
		for (Entry<String, Short> ent : shortValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeShort(ent.getValue());
		}
		out.writeInt(intValueMap.size());
		for (Entry<String, Integer> ent : intValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeInt(ent.getValue());
		}
		out.writeInt(floatValueMap.size());
		for (Entry<String, Float> ent : floatValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeFloat(ent.getValue());
		}
		out.writeInt(longValueMap.size());
		for (Entry<String, Long> ent : longValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeLong(ent.getValue());
		}
		out.writeInt(doubleValueMap.size());
		for (Entry<String, Double> ent : doubleValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeDouble(ent.getValue());
		}
		out.writeInt(stringValueMap.size());
		for (Entry<String, String> ent : stringValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeBytes(ent.getValue());
		}
		out.writeInt(enumValueMap.size());
		for (Entry<String, EnumToSerialize> ent : enumValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			out.writeInt(ent.getValue().ordinal());
		}
		out.writeInt(aggregatedClassValueMap.size());
		for (Entry<String, AggregatedClassToSerialize> ent : aggregatedClassValueMap.entrySet()) {
			out.writeBytes(ent.getKey());
			ent.getValue().serialize(out);
		}
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		
	}
	
}
