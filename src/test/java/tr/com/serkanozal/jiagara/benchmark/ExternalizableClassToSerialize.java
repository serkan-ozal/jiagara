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
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		
	}
	
}
