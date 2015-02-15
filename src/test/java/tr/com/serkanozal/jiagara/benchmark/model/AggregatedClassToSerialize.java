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

package tr.com.serkanozal.jiagara.benchmark.model;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("serial")
public class AggregatedClassToSerialize implements Serializable {

	private static final Random RANDOM = new SecureRandom();

	protected byte byteValue = 1;
	protected boolean booleanValue = true;
	protected char charValue = 'X';
	protected short shortValue = 10;
	protected int intValue = 100;
	protected float floatValue = 200.0F;
	protected long longValue = 1000;
	protected double doubleValue = 2000.0;
	protected String stringValue = "str";
	protected EnumToSerialize enumValue;
	transient protected AggregatedClassToSerialize reference = this;

	@SuppressWarnings("unchecked")
	public <T extends AggregatedClassToSerialize> T randomize() {
		byteValue = (byte) RANDOM.nextInt(256);
		booleanValue = RANDOM.nextBoolean();
		charValue = (char) RANDOM.nextInt(65536);
		shortValue = (short) RANDOM.nextInt(65536);
		intValue = RANDOM.nextInt();
		floatValue = RANDOM.nextFloat();
		longValue = RANDOM.nextLong();
		doubleValue = RANDOM.nextDouble();
		stringValue = UUID.randomUUID().toString();
		enumValue = EnumToSerialize.random();
		
		return (T) this;
	}
	
	public void serialize(ObjectOutput out) throws IOException {
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
		
		out.writeBytes(stringValue);
		out.writeInt(enumValue.ordinal());
	}
	
}
