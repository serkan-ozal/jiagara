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

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author Serkan ÖZAL
 */
@SuppressWarnings("serial")
public class ClassToSerialize implements Serializable {

	private static final Random RANDOM = new SecureRandom();

	private static final int ARRAY_LENGTH = 50;
	private static final int COLLECTION_LENGTH = 10;
	private static final int MAP_LENGTH = 10;
	
	///////////////////////////////////////////////////////////////////////
	
	protected byte byteValue = 1;
	protected boolean booleanValue = true;
	protected char charValue = 'X';
	protected short shortValue = 10;
	protected int intValue = 100;
	protected float floatValue = 200.0F;
	protected long longValue = 1000L;
	protected double doubleValue = 2000.0;
	
	///////////////////////////////////////////////////////////////////////
	
	protected Byte byteObjectValue = 1;
	protected Boolean booleanObjectValue = true;
	protected Character charObjectValue = 'X';
	protected Short shortObjectValue = 10;
	protected Integer intObjectValue = 100;
	protected Float floatObjectValue = 200.0F;
	protected Long longObjectValue = 1000L;
	protected Double doubleObjectValue = 2000.0;
	
	///////////////////////////////////////////////////////////////////////
	
	protected boolean[] booleanArrayValue;
	protected byte[] byteArrayValue;
	protected char[] charArrayValue;
	protected short[] shortArrayValue;
	protected int[] intArrayValue;
	protected float[] floatArrayValue;
	protected long[] longArrayValue;
	protected double[] doubleArrayValue;
	
	///////////////////////////////////////////////////////////////////////
	
	protected Boolean[] booleanObjectArrayValue;
	protected Byte[] byteObjectArrayValue;
	protected Character[] charObjectArrayValue;
	protected Short[] shortObjectArrayValue;
	protected Integer[] intObjectArrayValue;
	protected Float[] floatObjectArrayValue;
	protected Long[] longObjectArrayValue;
	protected Double[] doubleObjectArrayValue;
	
	///////////////////////////////////////////////////////////////////////
	
	protected String stringValue = "str";
	protected Date dateValue = new Date();
	protected EnumToSerialize enumValue;
	protected AggregatedClassToSerialize aggregatedClassValue = new AggregatedClassToSerialize();
	
	///////////////////////////////////////////////////////////////////////
	
	protected Collection<Byte> byteValueCollection = new ArrayList<Byte>();
	protected Collection<Boolean> booleanValueCollection = new ArrayList<Boolean>();
	protected Collection<Character> charValueCollection = new ArrayList<Character>();
	protected Collection<Short> shortValueCollection = new ArrayList<Short>();
	protected Collection<Integer> intValueCollection = new ArrayList<Integer>();
	protected Collection<Float> floatValueCollection = new ArrayList<Float>();
	protected Collection<Long> longValueCollection = new ArrayList<Long>();
	protected Collection<Double> doubleValueCollection = new ArrayList<Double>();
	protected Collection<String> stringValueCollection = new ArrayList<String>();
	protected Collection<EnumToSerialize> enumValueCollection = new ArrayList<EnumToSerialize>();
	protected Collection<AggregatedClassToSerialize> aggregatedClassValueCollection = new ArrayList<AggregatedClassToSerialize>();
	
	///////////////////////////////////////////////////////////////////////
	
	protected Map<String, Byte> byteValueMap = new HashMap<String, Byte>();
	protected Map<String, Boolean> booleanValueMap = new HashMap<String, Boolean>();
	protected Map<String, Character> charValueMap = new HashMap<String, Character>();
	protected Map<String, Short> shortValueMap = new HashMap<String, Short>();
	protected Map<String, Integer> intValueMap = new HashMap<String, Integer>();
	protected Map<String, Float> floatValueMap = new HashMap<String, Float>();
	protected Map<String, Long> longValueMap = new HashMap<String, Long>();
	protected Map<String, Double> doubleValueMap = new HashMap<String, Double>();
	protected Map<String, String> stringValueMap = new HashMap<String, String>();
	protected Map<String, EnumToSerialize> enumValueMap = new HashMap<String, EnumToSerialize>();
	protected Map<String, AggregatedClassToSerialize> aggregatedClassValueMap = new HashMap<String, AggregatedClassToSerialize>();
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public <T extends ClassToSerialize> T randomize() {
		byteValue = (byte) RANDOM.nextInt(256);
		booleanValue = RANDOM.nextBoolean();
		charValue = (char) RANDOM.nextInt(65536);
		shortValue = (short) RANDOM.nextInt(65536);
		intValue = RANDOM.nextInt();
		floatValue = RANDOM.nextFloat();
		longValue = RANDOM.nextLong();
		doubleValue = RANDOM.nextDouble();
		
		///////////////////////////////////////////////////////////////////////
		
		byteObjectValue = (byte) RANDOM.nextInt(256);
		booleanObjectValue = RANDOM.nextBoolean();
		charObjectValue = (char) RANDOM.nextInt(65536);
		shortObjectValue = (short) RANDOM.nextInt(65536);
		intObjectValue = RANDOM.nextInt();
		floatObjectValue = RANDOM.nextFloat();
		longObjectValue = RANDOM.nextLong();
		doubleObjectValue = RANDOM.nextDouble();
		
		///////////////////////////////////////////////////////////////////////
		
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
			intArrayValue[i] = RANDOM.nextInt();
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
		
		///////////////////////////////////////////////////////////////////////
		
		booleanObjectArrayValue = new Boolean[ARRAY_LENGTH];
		for (int i = 0; i < booleanObjectArrayValue.length; i++) {
			booleanObjectArrayValue[i] = RANDOM.nextBoolean();
		}
		byteObjectArrayValue = new Byte[ARRAY_LENGTH];
		for (int i = 0; i < byteObjectArrayValue.length; i++) {
			byteObjectArrayValue[i] = (byte) RANDOM.nextInt(256);
		}
		charObjectArrayValue = new Character[ARRAY_LENGTH];
		for (int i = 0; i < charObjectArrayValue.length; i++) {
			charObjectArrayValue[i] = (char) RANDOM.nextInt(65536);
		}
		shortObjectArrayValue = new Short[ARRAY_LENGTH];
		for (int i = 0; i < shortObjectArrayValue.length; i++) {
			shortObjectArrayValue[i] = (short) RANDOM.nextInt(65536);
		}
		intObjectArrayValue = new Integer[ARRAY_LENGTH];
		for (int i = 0; i < intObjectArrayValue.length; i++) {
			intObjectArrayValue[i] = RANDOM.nextInt();
		}
		floatObjectArrayValue = new Float[ARRAY_LENGTH];
		for (int i = 0; i < floatObjectArrayValue.length; i++) {
			floatObjectArrayValue[i] = RANDOM.nextFloat();
		}
		longObjectArrayValue = new Long[ARRAY_LENGTH];
		for (int i = 0; i < longObjectArrayValue.length; i++) {
			longObjectArrayValue[i] = RANDOM.nextLong();
		}
		doubleObjectArrayValue = new Double[ARRAY_LENGTH];
		for (int i = 0; i < doubleObjectArrayValue.length; i++) {
			doubleObjectArrayValue[i] = RANDOM.nextDouble();
		}
		
		///////////////////////////////////////////////////////////////////////
		
		stringValue = UUID.randomUUID().toString();
		dateValue = new Date(1900 + (byte) RANDOM.nextInt(256), RANDOM.nextInt(12), RANDOM.nextInt(28));
		enumValue = EnumToSerialize.random();
		aggregatedClassValue = new AggregatedClassToSerialize().randomize();
		
		///////////////////////////////////////////////////////////////////////
		
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			byteValueCollection.add((byte) RANDOM.nextInt(256));
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			booleanValueCollection.add(RANDOM.nextBoolean());
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			charValueCollection.add((char) RANDOM.nextInt(65536));
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			shortValueCollection.add((short) RANDOM.nextInt(65536));
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			intValueCollection.add(RANDOM.nextInt());
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			floatValueCollection.add(RANDOM.nextFloat());
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			longValueCollection.add(RANDOM.nextLong());
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			doubleValueCollection.add(RANDOM.nextDouble());
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			stringValueCollection.add(UUID.randomUUID().toString());
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			enumValueCollection.add(EnumToSerialize.random());
		}
		for (int i = 0; i < COLLECTION_LENGTH; i++) {
			aggregatedClassValueCollection.add(new AggregatedClassToSerialize().randomize());
		}
		
		///////////////////////////////////////////////////////////////////////
		
		for (int i = 0; i < MAP_LENGTH; i++) {
			byteValueMap.put(String.valueOf(i), (byte) RANDOM.nextInt(256));
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			booleanValueMap.put(String.valueOf(i), RANDOM.nextBoolean());
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			charValueMap.put(String.valueOf(i), (char) RANDOM.nextInt(65536));
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			shortValueMap.put(String.valueOf(i), (short) RANDOM.nextInt(65536));
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			intValueMap.put(String.valueOf(i), RANDOM.nextInt());
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			floatValueMap.put(String.valueOf(i), RANDOM.nextFloat());
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			longValueMap.put(String.valueOf(i), RANDOM.nextLong());
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			doubleValueMap.put(String.valueOf(i), RANDOM.nextDouble());
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			stringValueMap.put(String.valueOf(i), UUID.randomUUID().toString());
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			enumValueMap.put(String.valueOf(i), EnumToSerialize.random());
		}
		for (int i = 0; i < MAP_LENGTH; i++) {
			aggregatedClassValueMap.put(String.valueOf(i), new AggregatedClassToSerialize().randomize());
		}
		
		///////////////////////////////////////////////////////////////////////
		
		return (T) this;
	}
	
}
