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

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * @author Serkan ÖZAL
 */
public class ClassToSerialize {

	private static final Random RANDOM = new SecureRandom();
	private static final int ARRAY_LENGTH = 50;
	
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
	protected boolean[] booleanArrayValue;
	protected byte[] byteArrayValue;
	protected char[] charArrayValue;
	protected short[] shortArrayValue;
	protected int[] intArrayValue;
	protected float[] floatArrayValue;
	protected long[] longArrayValue;
	protected double[] doubleArrayValue;

	@SuppressWarnings("unchecked")
	public <T extends ClassToSerialize> T randomize() {
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
		booleanArrayValue = new boolean[ARRAY_LENGTH];
		for (int i = 0; i < booleanArrayValue.length; i++) {
			booleanArrayValue[i] = RANDOM.nextBoolean();
		}
		byteArrayValue = new byte[ARRAY_LENGTH];
		for (int i = 0; i < byteArrayValue.length; i++) {
			byteArrayValue[i] = (byte) RANDOM.nextInt(256);
		}
		charArrayValue = new char[ARRAY_LENGTH];
		for (int i = 0; i < charArrayValue.length; i++) {
			charArrayValue[i] = (char) RANDOM.nextInt(65536);
		}
		shortArrayValue = new short[ARRAY_LENGTH];
		for (int i = 0; i < shortArrayValue.length; i++) {
			shortArrayValue[i] = (short) RANDOM.nextInt(65536);
		}
		intArrayValue = new int[ARRAY_LENGTH];
		for (int i = 0; i < intArrayValue.length; i++) {
			intArrayValue[i] = (short) RANDOM.nextInt();
		}
		floatArrayValue = new float[ARRAY_LENGTH];
		for (int i = 0; i < floatArrayValue.length; i++) {
			floatArrayValue[i] = RANDOM.nextFloat();
		}
		longArrayValue = new long[ARRAY_LENGTH];
		for (int i = 0; i < longArrayValue.length; i++) {
			longArrayValue[i] = RANDOM.nextLong();
		}
		doubleArrayValue = new double[ARRAY_LENGTH];
		for (int i = 0; i < doubleArrayValue.length; i++) {
			doubleArrayValue[i] = RANDOM.nextDouble();
		}
		return (T) this;
	}
	
}
